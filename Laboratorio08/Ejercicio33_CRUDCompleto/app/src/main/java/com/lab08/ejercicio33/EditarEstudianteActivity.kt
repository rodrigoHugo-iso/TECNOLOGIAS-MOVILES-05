package com.lab08.ejercicio33

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Pantalla de edición para el flujo UPDATE del CRUD.
 * Recibe los datos del estudiante por Intent, permite modificarlos
 * y devuelve el resultado al MainActivity.
 */
class EditarEstudianteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_editar_estudiante)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val id      = intent.getStringExtra("id") ?: ""
        val nombre  = intent.getStringExtra("nombre") ?: ""
        val carrera = intent.getStringExtra("carrera") ?: ""
        val curso   = intent.getStringExtra("curso") ?: ""

        val etNombre  = findViewById<EditText>(R.id.etNombreEdit)
        val etCarrera = findViewById<EditText>(R.id.etCarreraEdit)
        val etCurso   = findViewById<EditText>(R.id.etCursoEdit)

        // Pre-cargar los valores actuales
        etNombre.setText(nombre)
        etCarrera.setText(carrera)
        etCurso.setText(curso)

        findViewById<Button>(R.id.btnActualizar).setOnClickListener {
            val nuevoNombre  = etNombre.text.toString().trim()
            val nuevoCarrera = etCarrera.text.toString().trim()
            val nuevoCurso   = etCurso.text.toString().trim()

            if (nuevoNombre.isEmpty() || nuevoCarrera.isEmpty() || nuevoCurso.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val result = Intent().apply {
                putExtra("id", id)
                putExtra("nombre", nuevoNombre)
                putExtra("carrera", nuevoCarrera)
                putExtra("curso", nuevoCurso)
            }
            setResult(RESULT_OK, result)
            finish()
        }

        findViewById<Button>(R.id.btnCancelar).setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}
