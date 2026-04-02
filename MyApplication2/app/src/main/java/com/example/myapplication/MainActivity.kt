package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val listaEventos = ArrayList<Evento>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spTipo = findViewById<Spinner>(R.id.spTipo)
        val spCategoria = findViewById<Spinner>(R.id.spCategoria)
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etPersonas = findViewById<EditText>(R.id.etPersonas)
        val etFecha = findViewById<EditText>(R.id.etFecha)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val btnVer = findViewById<Button>(R.id.btnVer)

        // Spinner tipo
        val tipos = arrayOf("Reunión", "Cumpleaños", "Clase")
        spTipo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipos)

        // Spinner categoría
        val categorias = arrayOf(1, 2, 3, 4, 5)
        spCategoria.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categorias)

        btnRegistrar.setOnClickListener {

            val evento = Evento(
                tipo = spTipo.selectedItem.toString(),
                categoria = spCategoria.selectedItem.toString().toInt(),
                nombre = etNombre.text.toString(),
                personas = etPersonas.text.toString(),
                fecha = etFecha.text.toString()
            )

            listaEventos.add(evento)

            Toast.makeText(this, "Evento registrado", Toast.LENGTH_SHORT).show()

            etNombre.text.clear()
            etPersonas.text.clear()
            etFecha.text.clear()
        }

        btnVer.setOnClickListener {
            val intent = Intent(this, SegundaActivity::class.java)
            intent.putExtra("eventos", listaEventos)
            startActivity(intent)
        }
    }
}