package com.norbel.recyclerview

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private lateinit var lista: MutableList<Producto>
    private lateinit var adapter: ProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val txtNombre = findViewById<EditText>(R.id.txtNombre)
        val txtCantidad = findViewById<EditText>(R.id.txtCantidad)
        val txtPrecio = findViewById<EditText>(R.id.txtPrecio)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val recycler = findViewById<RecyclerView>(R.id.recyclerProductos)

        lista = cargarProductos()

        adapter = ProductoAdapter(lista)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        btnAgregar.setOnClickListener {
            val nombre = txtNombre.text.toString()
            val cantidad = txtCantidad.text.toString().toIntOrNull() ?: 0
            val precio = txtPrecio.text.toString().toDoubleOrNull() ?: 0.0

            val producto = Producto(nombre, cantidad, precio)
            lista.add(producto)

            guardarProductos(lista)

            adapter.notifyDataSetChanged()

            txtNombre.text.clear()
            txtCantidad.text.clear()
            txtPrecio.text.clear()
        }
    }

    private fun guardarProductos(lista: List<Producto>) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val json = Gson().toJson(lista)
        prefs.edit().putString("productos", json).apply()
    }

    private fun cargarProductos(): MutableList<Producto> {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val json = prefs.getString("productos", null)

        return if (json != null) {
            val type = object : TypeToken<MutableList<Producto>>() {}.type
            Gson().fromJson(json, type)
        } else {
            mutableListOf()
        }
    }
}