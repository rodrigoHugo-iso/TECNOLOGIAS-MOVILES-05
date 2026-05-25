package com.lab08.ejercicio31

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Ejercicio 31 — Registro de Estudiante en Firebase Realtime Database.
 *
 * Al presionar "Guardar", los datos ingresados (Nombre, Carrera, Curso)
 * se almacenan en el nodo "Estudiantes" de la base de datos.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etCarrera: EditText
    private lateinit var etCurso: EditText
    private lateinit var btnGuardar: Button
    private lateinit var estudiantesRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // Referencia al nodo "Estudiantes" en Realtime Database
        estudiantesRef = FirebaseDatabase.getInstance().getReference("Estudiantes")

        etNombre   = findViewById(R.id.etNombre)
        etCarrera  = findViewById(R.id.etCarrera)
        etCurso    = findViewById(R.id.etCurso)
        btnGuardar = findViewById(R.id.btnGuardar)

        btnGuardar.setOnClickListener { guardarEstudiante() }
    }

    private fun guardarEstudiante() {
        val nombre  = etNombre.text.toString().trim()
        val carrera = etCarrera.text.toString().trim()
        val curso   = etCurso.text.toString().trim()

        // Validación: todos los campos son obligatorios
        if (nombre.isEmpty() || carrera.isEmpty() || curso.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // push() genera una clave única para cada nuevo estudiante
        val id = estudiantesRef.push().key ?: return
        val estudiante = Estudiante(
            estudianteId = id,
            nombre       = nombre,
            carrera      = carrera,
            curso        = curso
        )

        estudiantesRef.child(id).setValue(estudiante)
            .addOnSuccessListener {
                Toast.makeText(this, "Estudiante Registrado", Toast.LENGTH_LONG).show()
                // Limpiar campos tras el registro exitoso
                etNombre.text.clear()
                etCarrera.text.clear()
                etCurso.text.clear()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
