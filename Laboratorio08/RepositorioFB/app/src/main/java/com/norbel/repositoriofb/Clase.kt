package com.norbel.repositoriofb

/**
 * Modelo de datos para un registro de clase académica.
 * Firebase requiere un constructor sin argumentos para deserializar;
 * esto se logra asignando valores por defecto a todas las propiedades.
 */
data class Clase(
    var claseid: String = "",
    var seccion: String = "",
    var area: String = "",
    var tema: String = ""
)
