package com.lab08.ejercicio33

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter para el RecyclerView del CRUD completo.
 * Expone callbacks para click corto (editar) y click largo (eliminar).
 */
class EstudianteAdapter(
    private val lista: MutableList<Estudiante>,
    private val onEdit: (Estudiante) -> Unit,
    private val onDelete: (Estudiante) -> Unit
) : RecyclerView.Adapter<EstudianteAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView  = view.findViewById(R.id.tvNombre)
        val tvCarrera: TextView = view.findViewById(R.id.tvCarrera)
        val tvCurso: TextView   = view.findViewById(R.id.tvCurso)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_estudiante, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val e = lista[position]
        holder.tvNombre.text  = e.nombre
        holder.tvCarrera.text = "Carrera: ${e.carrera}"
        holder.tvCurso.text   = "Curso: ${e.curso}"

        // Click corto → editar
        holder.itemView.setOnClickListener { onEdit(e) }
        // Click largo → confirmar eliminación
        holder.itemView.setOnLongClickListener { onDelete(e); true }
    }

    override fun getItemCount(): Int = lista.size

    fun agregarEstudiante(e: Estudiante) {
        lista.add(e)
        notifyItemInserted(lista.size - 1)
    }

    fun actualizarEstudiante(e: Estudiante) {
        val idx = lista.indexOfFirst { it.estudianteId == e.estudianteId }
        if (idx >= 0) { lista[idx] = e; notifyItemChanged(idx) }
    }

    fun eliminarEstudiante(estudianteId: String) {
        val idx = lista.indexOfFirst { it.estudianteId == estudianteId }
        if (idx >= 0) { lista.removeAt(idx); notifyItemRemoved(idx) }
    }
}
