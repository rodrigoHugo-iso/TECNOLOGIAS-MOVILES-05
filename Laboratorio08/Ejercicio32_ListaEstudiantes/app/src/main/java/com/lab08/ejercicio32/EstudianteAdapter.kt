package com.lab08.ejercicio32

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter para mostrar la lista de estudiantes en un RecyclerView.
 */
class EstudianteAdapter(private val lista: MutableList<Estudiante>) :
    RecyclerView.Adapter<EstudianteAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView  = view.findViewById(R.id.tvNombre)
        val tvCarrera: TextView = view.findViewById(R.id.tvCarrera)
        val tvCurso: TextView   = view.findViewById(R.id.tvCurso)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_estudiante, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val e = lista[position]
        holder.tvNombre.text  = e.nombre
        holder.tvCarrera.text = "Carrera: ${e.carrera}"
        holder.tvCurso.text   = "Curso: ${e.curso}"
    }

    override fun getItemCount(): Int = lista.size

    fun agregarEstudiante(estudiante: Estudiante) {
        lista.add(estudiante)
        notifyItemInserted(lista.size - 1)
    }
}
