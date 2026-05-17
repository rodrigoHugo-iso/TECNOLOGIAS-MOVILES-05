package com.lab07.ejercicio30.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room que representa la tabla "estudiantes".
 * Ejercicio 30: CRUD completo para el sistema de proyecto.
 */
@Entity(tableName = "estudiantes")
data class Estudiante(
    @PrimaryKey
    val id: Int,
    val nombre: String,
    val carrera: String,
    val ciclo: Int
)
