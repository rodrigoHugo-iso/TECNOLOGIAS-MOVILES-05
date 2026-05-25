package com.lab08.ejercicio33

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

/**
 * Ejercicio 33 — CRUD completo con Firebase Realtime Database.
 *
 * Operaciones implementadas:
 *   CREATE → push() + setValue()
 *   READ   → addValueEventListener() (carga inicial) + addChildEventListener() (actualizaciones)
 *   UPDATE → updateChildren() en EditarEstudianteActivity
 *   DELETE → removeValue() con confirmación
 */
class MainActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etCarrera: EditText
    private lateinit var etCurso: EditText
    private lateinit var btnGuardar: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var estudiantesRef: DatabaseReference
    private lateinit var adapter: EstudianteAdapter
    private val lista = mutableListOf<Estudiante>()

    // Launcher para recibir el resultado de EditarEstudianteActivity
    private val editLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val e = Estudiante(
                estudianteId = result.data?.getStringExtra("id") ?: return@registerForActivityResult,
                nombre       = result.data?.getStringExtra("nombre") ?: "",
                carrera      = result.data?.getStringExtra("carrera") ?: "",
                curso        = result.data?.getStringExtra("curso") ?: ""
            )
            // UPDATE: actualizar solo los campos modificados
            estudiantesRef.child(e.estudianteId).updateChildren(
                mapOf("nombre" to e.nombre, "carrera" to e.carrera, "curso" to e.curso)
            ).addOnSuccessListener {
                adapter.actualizarEstudiante(e)
                Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { ex ->
                Toast.makeText(this, "Error: ${ex.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        estudiantesRef = FirebaseDatabase.getInstance().getReference("Estudiantes")

        etNombre     = findViewById(R.id.etNombre)
        etCarrera    = findViewById(R.id.etCarrera)
        etCurso      = findViewById(R.id.etCurso)
        btnGuardar   = findViewById(R.id.btnGuardar)
        recyclerView = findViewById(R.id.recyclerView)

        adapter = EstudianteAdapter(
            lista,
            onEdit   = { e -> abrirEditar(e) },
            onDelete = { e -> confirmarEliminar(e) }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnGuardar.setOnClickListener { crearEstudiante() }

        // READ: cargar datos iniciales y escuchar cambios en tiempo real
        leerEstudiantes()
    }

    /** CREATE */
    private fun crearEstudiante() {
        val nombre  = etNombre.text.toString().trim()
        val carrera = etCarrera.text.toString().trim()
        val curso   = etCurso.text.toString().trim()

        if (nombre.isEmpty() || carrera.isEmpty() || curso.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val id = estudiantesRef.push().key ?: return
        val e  = Estudiante(estudianteId = id, nombre = nombre, carrera = carrera, curso = curso)

        estudiantesRef.child(id).setValue(e)
            .addOnSuccessListener {
                Toast.makeText(this, "Estudiante registrado", Toast.LENGTH_SHORT).show()
                etNombre.text.clear(); etCarrera.text.clear(); etCurso.text.clear()
            }
            .addOnFailureListener { ex ->
                Toast.makeText(this, "Error: ${ex.message}", Toast.LENGTH_SHORT).show()
            }
    }

    /** READ — escucha cambios en tiempo real */
    private fun leerEstudiantes() {
        estudiantesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lista.clear()
                for (child in snapshot.children) {
                    child.getValue(Estudiante::class.java)?.let { lista.add(it) }
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error al leer: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /** UPDATE — abrir pantalla de edición */
    private fun abrirEditar(e: Estudiante) {
        val intent = Intent(this, EditarEstudianteActivity::class.java).apply {
            putExtra("id", e.estudianteId)
            putExtra("nombre", e.nombre)
            putExtra("carrera", e.carrera)
            putExtra("curso", e.curso)
        }
        editLauncher.launch(intent)
    }

    /** DELETE — confirmar antes de eliminar */
    private fun confirmarEliminar(e: Estudiante) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar estudiante")
            .setMessage("¿Seguro que deseas eliminar a ${e.nombre}?")
            .setPositiveButton("Eliminar") { _, _ ->
                estudiantesRef.child(e.estudianteId).removeValue()
                    .addOnSuccessListener {
                        adapter.eliminarEstudiante(e.estudianteId)
                        Toast.makeText(this, "Eliminado", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { ex ->
                        Toast.makeText(this, "Error: ${ex.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
