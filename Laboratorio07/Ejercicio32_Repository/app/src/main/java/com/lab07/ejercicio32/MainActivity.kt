package com.lab07.ejercicio32

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lab07.ejercicio32.data.AppDatabase
import com.lab07.ejercicio32.data.Articulo
import com.lab07.ejercicio32.databinding.ActivityMainBinding
import com.lab07.ejercicio32.repository.ArticuloRepository
import kotlinx.coroutines.launch

/**
 * Activity limpia que solo conoce el Repository, nunca el DAO directamente.
 * Esto implementa el principio de separación de responsabilidades:
 *   - MainActivity: maneja la UI y eventos del usuario
 *   - ArticuloRepository: encapsula toda la lógica de acceso a datos
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    // La Activity solo conoce el Repository, NO el DAO
    private lateinit var repository: ArticuloRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Crear el Repository inyectando el DAO (en proyectos reales se usa ViewModel + DI)
        val dao = AppDatabase.getInstance(this).articuloDao()
        repository = ArticuloRepository(dao)

        binding.btnRegistrar.setOnClickListener { registrar() }
        binding.btnBuscar.setOnClickListener { buscar() }
        binding.btnEliminar.setOnClickListener { eliminar() }
        binding.btnListar.setOnClickListener { listarTodos() }
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    private fun registrar() {
        val codigo = binding.etCodigo.text.toString()
        val desc = binding.etDescripcion.text.toString()
        val precio = binding.etPrecio.text.toString()

        if (codigo.isEmpty() || desc.isEmpty() || precio.isEmpty()) {
            toast("Complete todos los campos"); return
        }

        lifecycleScope.launch {
            // La Activity usa el Repository, no el DAO
            val resultado = repository.registrar(
                Articulo(codigo.toInt(), desc, precio.toDouble())
            )
            resultado.fold(
                onSuccess = {
                    binding.tvResultados.text = "✅ Artículo registrado con ID: $it"
                    limpiar()
                    toast("Registro exitoso")
                },
                onFailure = {
                    binding.tvResultados.text = "❌ Error: ${it.message}"
                    toast("Error al registrar")
                }
            )
        }
    }

    private fun buscar() {
        val codigo = binding.etCodigo.text.toString()
        if (codigo.isEmpty()) { toast("Ingrese el código"); return }
        lifecycleScope.launch {
            val articulo = repository.buscar(codigo.toInt())
            if (articulo != null) {
                binding.etDescripcion.setText(articulo.descripcion)
                binding.etPrecio.setText(articulo.precio.toString())
                binding.tvResultados.text = "✅ Encontrado:\n$articulo"
            } else {
                binding.tvResultados.text = "❌ Artículo no encontrado"
                toast("No existe el artículo")
            }
        }
    }

    private fun eliminar() {
        val codigo = binding.etCodigo.text.toString()
        if (codigo.isEmpty()) { toast("Ingrese el código"); return }
        lifecycleScope.launch {
            val exito = repository.eliminar(codigo.toInt())
            limpiar()
            if (exito) {
                binding.tvResultados.text = "✅ Artículo eliminado"
                toast("Eliminado")
            } else {
                binding.tvResultados.text = "❌ Artículo no encontrado"
            }
        }
    }

    private fun listarTodos() {
        lifecycleScope.launch {
            val lista = repository.listarTodos()
            if (lista.isEmpty()) {
                binding.tvResultados.text = "(sin artículos)"
            } else {
                val sb = StringBuilder("📋 Lista de artículos:\n\n")
                lista.forEach { a ->
                    sb.appendLine("Cód: ${a.codigo} | ${a.descripcion} | S/. ${"%.2f".format(a.precio)}")
                }
                binding.tvResultados.text = sb.toString()
            }
        }
    }

    private fun limpiar() {
        binding.etCodigo.setText("")
        binding.etDescripcion.setText("")
        binding.etPrecio.setText("")
    }
}
