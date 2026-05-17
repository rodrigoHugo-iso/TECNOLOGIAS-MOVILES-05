package com.lab07.ejercicio30

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lab07.ejercicio30.data.AppDatabase
import com.lab07.ejercicio30.data.Estudiante
import com.lab07.ejercicio30.data.EstudianteDao
import com.lab07.ejercicio30.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dao: EstudianteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = AppDatabase.getInstance(this).estudianteDao()

        binding.btnRegistrar.setOnClickListener { registrar() }
        binding.btnBuscar.setOnClickListener { buscar() }
        binding.btnModificar.setOnClickListener { modificar() }
        binding.btnEliminar.setOnClickListener { eliminar() }
        binding.btnListar.setOnClickListener { listarTodos() }
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    private fun limpiar() {
        binding.etId.setText("")
        binding.etNombre.setText("")
        binding.etCarrera.setText("")
        binding.etCiclo.setText("")
    }

    private fun registrar() {
        val id = binding.etId.text.toString()
        val nombre = binding.etNombre.text.toString()
        val carrera = binding.etCarrera.text.toString()
        val ciclo = binding.etCiclo.text.toString()

        if (id.isEmpty() || nombre.isEmpty() || carrera.isEmpty() || ciclo.isEmpty()) {
            toast("Complete todos los campos")
            return
        }
        lifecycleScope.launch {
            try {
                dao.insertar(Estudiante(id.toInt(), nombre, carrera, ciclo.toInt()))
                limpiar()
                toast("Estudiante registrado exitosamente")
            } catch (e: Exception) {
                toast("Error: El ID ya existe o datos inválidos")
            }
        }
    }

    private fun buscar() {
        val id = binding.etId.text.toString()
        if (id.isEmpty()) { toast("Ingrese el ID"); return }
        lifecycleScope.launch {
            val est = dao.buscarPorId(id.toInt())
            if (est != null) {
                binding.etNombre.setText(est.nombre)
                binding.etCarrera.setText(est.carrera)
                binding.etCiclo.setText(est.ciclo.toString())
                binding.tvResultados.text = "Encontrado: $est"
            } else {
                toast("Estudiante no encontrado")
            }
        }
    }

    private fun modificar() {
        val id = binding.etId.text.toString()
        val nombre = binding.etNombre.text.toString()
        val carrera = binding.etCarrera.text.toString()
        val ciclo = binding.etCiclo.text.toString()
        if (id.isEmpty() || nombre.isEmpty() || carrera.isEmpty() || ciclo.isEmpty()) {
            toast("Complete todos los campos"); return
        }
        lifecycleScope.launch {
            val rows = dao.actualizar(Estudiante(id.toInt(), nombre, carrera, ciclo.toInt()))
            if (rows > 0) toast("Estudiante modificado") else toast("ID no encontrado")
        }
    }

    private fun eliminar() {
        val id = binding.etId.text.toString()
        if (id.isEmpty()) { toast("Ingrese el ID"); return }
        lifecycleScope.launch {
            val rows = dao.eliminarPorId(id.toInt())
            limpiar()
            if (rows > 0) toast("Eliminado exitosamente") else toast("ID no encontrado")
        }
    }

    private fun listarTodos() {
        lifecycleScope.launch {
            val lista = dao.listarTodos()
            if (lista.isEmpty()) {
                binding.tvResultados.text = "No hay estudiantes registrados"
            } else {
                val sb = StringBuilder()
                lista.forEach { e ->
                    sb.appendLine("ID: ${e.id} | ${e.nombre}")
                    sb.appendLine("   Carrera: ${e.carrera} | Ciclo: ${e.ciclo}")
                    sb.appendLine("---")
                }
                binding.tvResultados.text = sb.toString()
            }
        }
    }
}
