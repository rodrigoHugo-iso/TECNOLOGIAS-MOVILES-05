package com.example.myapplication

import java.io.Serializable

data class Evento(
    val tipo: String,
    val categoria: Int,
    val nombre: String,
    val personas: String,
    val fecha: String
) : Serializable