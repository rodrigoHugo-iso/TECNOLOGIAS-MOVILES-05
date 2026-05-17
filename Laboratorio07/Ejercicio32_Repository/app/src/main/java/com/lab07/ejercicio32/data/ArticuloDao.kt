package com.lab07.ejercicio32.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ArticuloDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(articulo: Articulo): Long

    @Update
    suspend fun actualizar(articulo: Articulo): Int

    @Query("DELETE FROM articulos WHERE codigo = :codigo")
    suspend fun eliminarPorCodigo(codigo: Int): Int

    @Query("SELECT * FROM articulos WHERE codigo = :codigo LIMIT 1")
    suspend fun buscarPorCodigo(codigo: Int): Articulo?

    @Query("SELECT * FROM articulos ORDER BY codigo ASC")
    suspend fun listarTodos(): List<Articulo>
}
