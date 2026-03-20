package com.example.pizzas

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var gridView: GridView

    // Nombres de pizzas
    private val nombres = arrayOf(
        "Pizza Pepperoni",
        "Pizza Hawaiana",
        "Pizza Vegetariana",
        "Pizza BBQ"
    )

    // Imágenes (usa tus drawables)
    private val imagenes = intArrayOf(
        R.drawable.pizza1,
        R.drawable.pizza2,
        R.drawable.pizza3,
        R.drawable.pizza4
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridView = findViewById(R.id.gridView)

        val adapter = object : BaseAdapter() {
            override fun getCount(): Int = imagenes.size

            override fun getItem(position: Int): Any = imagenes[position]

            override fun getItemId(position: Int): Long = position.toLong()

            override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup?): android.view.View {
                val imageView = ImageView(applicationContext)
                imageView.setImageResource(imagenes[position])
                imageView.layoutParams = AbsListView.LayoutParams(300, 300)
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                return imageView
            }
        }

        gridView.adapter = adapter

        // CLICK
        gridView.setOnItemClickListener { _, _, position, _ ->
            Toast.makeText(
                this,
                nombres[position],
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}