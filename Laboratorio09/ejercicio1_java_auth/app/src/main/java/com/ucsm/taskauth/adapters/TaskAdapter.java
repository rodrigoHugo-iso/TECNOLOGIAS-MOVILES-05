package com.ucsm.taskauth.adapters;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ucsm.taskauth.R;
import com.ucsm.taskauth.models.Task;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    public interface OnTaskActionListener {
        void onToggleComplete(Task task, int position);
        void onDeleteTask(Task task, int position);
    }

    private final List<Task> tasks;
    private final OnTaskActionListener listener;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public TaskAdapter(List<Task> tasks, OnTaskActionListener listener) {
        this.tasks    = tasks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);

        holder.tvTitle.setText(task.getTitle());
        holder.tvDescription.setText(task.getDescription());
        holder.cbCompleted.setChecked(task.isCompleted());

        // Tachado si está completado
        if (task.isCompleted()) {
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        // Mostrar fecha si existe
        if (task.getDueDate() != null) {
            holder.tvDate.setVisibility(View.VISIBLE);
            holder.tvDate.setText("📅 " + sdf.format(task.getDueDate().toDate()));
        } else {
            holder.tvDate.setVisibility(View.GONE);
        }

        holder.cbCompleted.setOnClickListener(v -> listener.onToggleComplete(task, position));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteTask(task, position));
    }

    @Override
    public int getItemCount() { return tasks.size(); }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox   cbCompleted;
        TextView   tvTitle, tvDescription, tvDate;
        ImageButton btnDelete;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            cbCompleted    = itemView.findViewById(R.id.cbCompleted);
            tvTitle        = itemView.findViewById(R.id.tvTitle);
            tvDescription  = itemView.findViewById(R.id.tvDescription);
            tvDate         = itemView.findViewById(R.id.tvDate);
            btnDelete      = itemView.findViewById(R.id.btnDelete);
        }
    }
}
