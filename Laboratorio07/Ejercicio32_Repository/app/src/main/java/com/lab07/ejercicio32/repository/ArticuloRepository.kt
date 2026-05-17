package com.lab07.ejercicio32.repository

import com.lab07.ejercicio32.data.Articulo
import com.lab07.ejercicio32.data.ArticuloDao

/**
 * Ejercicio 32 - Patrón Repository.
 *
 * El Repository actúa como intermediario entre la Activity y el DAO.
 * Beneficios:
 *  - La Activity no conoce la fuente de datos (Room, API, caché, etc.)
 *  - Si en el futuro se agrega una API remota, solo se modifica el Repository
 *  - Facilita las pruebas unitarias (se puede sustituir el DAO por un mock)
 *  - Separa las responsabilidades: UI ← Repository ← DAO ← Room
 *
 * Arquitectura:
 *   MainActivity → ArticuloRepository → ArticuloDao → Room (SQLite)
 */
class ArticuloRepository(private val dao: ArticuloDao) {

    /**
     * Intenta registrar un artículo.
     * Retorna Result<Long> para manejo de errores sin excepciones en la Activity.
     */
    suspend fun registrar(articulo: Articulo): Result<Long> = runCatching {
        dao.insertar(articulo)
    }

    /**
     * Busca un artículo por código.
     * La Activity no sabe si viene de Room, caché o red.
     */
    suspend fun buscar(codigo: Int): Articulo? = dao.buscarPorCodigo(codigo)

    /**
     * Elimina un artículo por código.
     * Retorna true si la eliminación fue exitosa.
     */
    suspend fun eliminar(codigo: Int): Boolean = dao.eliminarPorCodigo(codigo) > 0

    /**
     * Lista todos los artículos ordenados por código.
     */
    suspend fun listarTodos(): List<Articulo> = dao.listarTodos()
}
