package com.lab07.ejercicio31.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Ejercicio 31: listarTodos() retorna Flow<List<Articulo>>.
 *
 * Room emite automáticamente un nuevo valor cada vez que la tabla
 * "articulos" cambia (insert, update, delete). La Activity observa
 * este Flow con repeatOnLifecycle para actualizaciones en tiempo real.
 */
@Dao
interface ArticuloDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(articulo: Articulo)

    @Query("DELETE FROM articulos WHERE codigo = :codigo")
    suspend fun eliminarPorCodigo(codigo: Int): Int

    // CLAVE del Ejercicio 31: retorna Flow en lugar de List
    // Room observa la tabla y emite la lista actualizada en cada cambio
    @Query("SELECT * FROM articulos ORDER BY codigo ASC")
    fun listarTodos(): Flow<List<Articulo>>
}
