package com.lab08.ejercicio32

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Ejercicio 32 — Registro + Lista en tiempo real con RecyclerView.
 *
 * Usa addChildEventListener para escuchar cambios en el nodo "Estudiantes"
 * y actualizar el RecyclerView automáticamente sin recargar la pantalla.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etCarrera: EditText
    private lateinit var etCurso: EditText
    private lateinit var btnGuardar: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var estudiantesRef: DatabaseReference
    private lateinit var adapter: EstudianteAdapter
    private val listaEstudiantes = mutableListOf<Estudiante>()

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

        etNombre   = findViewById(R.id.etNombre)
        etCarrera  = findViewById(R.id.etCarrera)
        etCurso    = findViewById(R.id.etCurso)
        btnGuardar = findViewById(R.id.btnGuardar)
        recyclerView = findViewById(R.id.recyclerView)

        // Configurar RecyclerView
        adapter = EstudianteAdapter(listaEstudiantes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnGuardar.setOnClickListener { guardarEstudiante() }

        // Escuchar cambios en tiempo real con ChildEventListener
        escucharEstudiantes()
    }

    private fun guardarEstudiante() {
        val nombre  = etNombre.text.toString().trim()
        val carrera = etCarrera.text.toString().trim()
        val curso   = etCurso.text.toString().trim()

        if (nombre.isEmpty() || carrera.isEmpty() || curso.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val id = estudiantesRef.push().key ?: return
        val estudiante = Estudiante(estudianteId = id, nombre = nombre, carrera = carrera, curso = curso)

        estudiantesRef.child(id).setValue(estudiante)
            .addOnSuccessListener {
                Toast.makeText(this, "Estudiante Registrado", Toast.LENGTH_SHORT).show()
                etNombre.text.clear()
                etCarrera.text.clear()
                etCurso.text.clear()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun escucharEstudiantes() {
        // addChildEventListener notifica solo los cambios individuales,
        // lo que es más eficiente que recargar todo el nodo cada vez.
        estudiantesRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val e = snapshot.getValue(Estudiante::class.java) ?: return
                adapter.agregarEstudiante(e)
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) { /* Ejercicio 33 */ }
            override fun onChildRemoved(snapshot: DataSnapshot) { /* Ejercicio 33 */ }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
