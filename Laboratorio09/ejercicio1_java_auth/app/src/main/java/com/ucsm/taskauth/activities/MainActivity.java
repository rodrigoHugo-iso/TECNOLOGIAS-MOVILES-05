package com.ucsm.taskauth.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ucsm.taskauth.R;
import com.ucsm.taskauth.adapters.TaskAdapter;
import com.ucsm.taskauth.databinding.ActivityMainBinding;
import com.ucsm.taskauth.models.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskActionListener {

    private ActivityMainBinding binding;
    private FirebaseAuth        mAuth;
    private FirebaseFirestore   db;
    private FirebaseUser        currentUser;
    private TaskAdapter         adapter;
    private List<Task>          taskList = new ArrayList<>();

    // Fecha seleccionada en el DatePicker del diálogo
    private Calendar selectedDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth       = FirebaseAuth.getInstance();
        db          = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setSubtitle(currentUser.getEmail());

        // RecyclerView
        adapter = new TaskAdapter(taskList, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        binding.fabAddTask.setOnClickListener(v -> showAddTaskDialog());

        loadTasks();
    }

    // ─── Cargar tareas del usuario desde Firestore ─────────────────────────────
    private void loadTasks() {
        db.collection("tasks")
                .whereEqualTo("userId", currentUser.getUid())
                .orderBy("dueDate", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null || snapshots == null) return;
                    taskList.clear();
                    for (var doc : snapshots) {
                        Task t = doc.toObject(Task.class);
                        taskList.add(t);
                    }
                    adapter.notifyDataSetChanged();
                    binding.tvEmpty.setVisibility(taskList.isEmpty() ? View.VISIBLE : View.GONE);
                });
    }

    // ─── Diálogo para agregar tarea ────────────────────────────────────────────
    private void showAddTaskDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_task, null);
        EditText etTitle       = dialogView.findViewById(R.id.etTaskTitle);
        EditText etDescription = dialogView.findViewById(R.id.etTaskDescription);
        View     btnPickDate   = dialogView.findViewById(R.id.btnPickDate);
        android.widget.TextView tvSelectedDate = dialogView.findViewById(R.id.tvSelectedDate);

        selectedDate = null;

        btnPickDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, day) -> {
                selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, day);
                tvSelectedDate.setText(String.format("%02d/%02d/%04d", day, month + 1, year));
                tvSelectedDate.setVisibility(View.VISIBLE);
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        new AlertDialog.Builder(this)
                .setTitle("Nueva tarea")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String title = etTitle.getText().toString().trim();
                    String desc  = etDescription.getText().toString().trim();
                    if (title.isEmpty()) {
                        Toast.makeText(this, "El título es obligatorio", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Timestamp dueDate = selectedDate != null
                            ? new Timestamp(new Date(selectedDate.getTimeInMillis()))
                            : null;
                    saveTask(new Task(title, desc, dueDate, currentUser.getUid()));
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // ─── Guardar tarea en Firestore ────────────────────────────────────────────
    private void saveTask(Task task) {
        db.collection("tasks")
                .add(task)
                .addOnSuccessListener(ref -> Toast.makeText(this, "Tarea guardada", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    // ─── Listener del adapter ──────────────────────────────────────────────────
    @Override
    public void onToggleComplete(Task task, int position) {
        db.collection("tasks").document(task.getId())
                .update("completed", !task.isCompleted());
    }

    @Override
    public void onDeleteTask(Task task, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar tarea")
                .setMessage("¿Deseas eliminar \"" + task.getTitle() + "\"?")
                .setPositiveButton("Eliminar", (d, w) ->
                        db.collection("tasks").document(task.getId()).delete())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // ─── Menú con opción de logout ─────────────────────────────────────────────
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
