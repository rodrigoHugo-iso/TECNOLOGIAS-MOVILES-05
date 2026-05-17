package com.lab07.ejercicio31

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.lab07.ejercicio31.data.AppDatabase
import com.lab07.ejercicio31.data.Articulo
import com.lab07.ejercicio31.data.ArticuloDao
import com.lab07.ejercicio31.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

/**
 * Ejercicio 31 - Flow<List<Articulo>> con repeatOnLifecycle.
 *
 * repeatOnLifecycle(Lifecycle.State.STARTED) garantiza que la colección
 * del Flow solo ocurra cuando la Activity está visible (STARTED o superior).
 * Se cancela automáticamente cuando pasa a STOPPED y se reinicia al volver
 * a STARTED. Esto previene fugas de memoria y actualizaciones innecesarias
 * en segundo plano.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dao: ArticuloDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = AppDatabase.getInstance(this).articuloDao()

        binding.btnAgregar.setOnClickListener { agregar() }
        binding.btnEliminar.setOnClickListener { eliminar() }

        // Observar el Flow con repeatOnLifecycle
        observarArticulos()
    }

    /**
     * Recolecta el Flow<List<Articulo>> respetando el ciclo de vida.
     * repeatOnLifecycle crea un nuevo bloque de coroutine cada vez que
     * la Activity llega a STARTED y lo cancela en STOPPED.
     */
    private fun observarArticulos() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                dao.listarTodos().collect { lista ->
                    // Este bloque se ejecuta AUTOMÁTICAMENTE cada vez que
                    // la base de datos cambia, sin necesidad de llamarlo manualmente
                    actualizarUI(lista)
                }
            }
        }
    }

    private fun actualizarUI(lista: List<Articulo>) {
        if (lista.isEmpty()) {
            binding.tvArticulos.text = "(sin artículos)"
        } else {
            val sb = StringBuilder()
            lista.forEach { a ->
                sb.appendLine("Cód: ${a.codigo} | ${a.descripcion} | S/. ${"%.2f".format(a.precio)}")
            }
            binding.tvArticulos.text = sb.toString().trimEnd()
        }
    }

    private fun agregar() {
        val codigo = binding.etCodigo.text.toString()
        val desc = binding.etDescripcion.text.toString()
        val precio = binding.etPrecio.text.toString()

        if (codigo.isEmpty() || desc.isEmpty() || precio.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            dao.insertar(Articulo(codigo.toInt(), desc, precio.toDouble()))
            // No es necesario recargar manualmente: el Flow emite automáticamente
            binding.etCodigo.setText("")
            binding.etDescripcion.setText("")
            binding.etPrecio.setText("")
            Toast.makeText(this@MainActivity, "Artículo agregado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun eliminar() {
        val codigo = binding.etCodigo.text.toString()
        if (codigo.isEmpty()) {
            Toast.makeText(this, "Ingrese el código", Toast.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch {
            val rows = dao.eliminarPorCodigo(codigo.toInt())
            // El Flow actualiza la lista automáticamente al eliminar
            if (rows > 0) Toast.makeText(this@MainActivity, "Eliminado", Toast.LENGTH_SHORT).show()
            else Toast.makeText(this@MainActivity, "Código no encontrado", Toast.LENGTH_SHORT).show()
        }
    }
}
