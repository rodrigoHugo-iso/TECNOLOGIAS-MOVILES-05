package com.example.myapplication

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class SegundaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_segunda)

        val listView = findViewById<ListView>(R.id.listView)

        val eventos = intent.getSerializableExtra("eventos") as ArrayList<Evento>

        // Ordenar por categoría (importancia)
        eventos.sortByDescending { it.categoria }

        val listaTexto = eventos.map {
            "${it.nombre} - ${it.tipo} - Prioridad: ${it.categoria}\n${it.fecha} | ${it.personas}"
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaTexto)
        listView.adapter = adapter
    }
}