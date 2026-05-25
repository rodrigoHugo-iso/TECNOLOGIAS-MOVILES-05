package com.lab08.ejercicio31

/**
 * Modelo de datos para un estudiante.
 * Los valores por defecto son necesarios para que Firebase pueda
 * deserializar los objetos con su constructor vacío.
 */
data class Estudiante(
    var estudianteId: String = "",
    var nombre: String = "",
    var carrera: String = "",
    var curso: String = ""
)
