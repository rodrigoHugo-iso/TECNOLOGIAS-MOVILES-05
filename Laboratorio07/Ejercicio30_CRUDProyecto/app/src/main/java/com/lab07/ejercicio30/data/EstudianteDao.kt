package com.lab07.ejercicio30.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface EstudianteDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(estudiante: Estudiante): Long

    @Update
    suspend fun actualizar(estudiante: Estudiante): Int

    @Query("DELETE FROM estudiantes WHERE id = :id")
    suspend fun eliminarPorId(id: Int): Int

    @Query("SELECT * FROM estudiantes WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): Estudiante?

    @Query("SELECT * FROM estudiantes ORDER BY nombre ASC")
    suspend fun listarTodos(): List<Estudiante>
}
