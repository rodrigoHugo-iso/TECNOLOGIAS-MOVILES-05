package com.lab07.ejercicio29

import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lab07.ejercicio29.databinding.ActivityMainBinding
import java.io.File

/**
 * Ejercicio 29 - Almacenamiento Externo con Scoped Storage (Android 10+)
 *
 * getExternalFilesDir() devuelve un directorio exclusivo de la app en el
 * almacenamiento externo. No requiere permisos READ/WRITE_EXTERNAL_STORAGE
 * en Android 10+ (API 29+). El directorio se elimina al desinstalar la app.
 *
 * Ruta típica: /sdcard/Android/data/<paquete>/files/
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val FILE_NAME = "archivo_externo.txt"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mostrar la ruta del directorio externo de la app
        val externalDir = getExternalFilesDir(null)
        binding.tvRuta.text = "Ruta: ${externalDir?.absolutePath ?: "No disponible"}"

        binding.btnVerificar.setOnClickListener { verificarDisponibilidad() }
        binding.btnGuardar.setOnClickListener { guardarArchivo() }
        binding.btnCargar.setOnClickListener { cargarArchivo() }
    }

    /**
     * Verifica si el almacenamiento externo está disponible para lectura/escritura.
     * Environment.getExternalStorageState() retorna el estado del volumen externo.
     */
    private fun verificarDisponibilidad() {
        val estado = Environment.getExternalStorageState()
        val mensaje = when (estado) {
            Environment.MEDIA_MOUNTED ->
                "✅ Almacenamiento externo disponible (lectura y escritura)"
            Environment.MEDIA_MOUNTED_READ_ONLY ->
                "⚠️ Almacenamiento externo solo lectura"
            else ->
                "❌ Almacenamiento externo no disponible (estado: $estado)"
        }
        binding.tvStatus.text = mensaje
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    /**
     * Guarda texto en el almacenamiento externo privado de la app.
     * Usa getExternalFilesDir(null) - no requiere permisos en Android 10+.
     * El parámetro null indica el directorio raíz; se puede pasar
     * Environment.DIRECTORY_DOCUMENTS, DIRECTORY_PICTURES, etc.
     */
    private fun guardarArchivo() {
        val texto = binding.editText.text.toString()
        if (texto.isEmpty()) {
            Toast.makeText(this, "Escribe algo primero", Toast.LENGTH_SHORT).show()
            return
        }

        // Verificar disponibilidad antes de escribir
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            Toast.makeText(this, "Almacenamiento externo no disponible", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            // getExternalFilesDir(type) - directorio privado de la app en externo
            // No requiere WRITE_EXTERNAL_STORAGE en Android 10+
            val externalDir = getExternalFilesDir(null)
                ?: throw Exception("No se pudo obtener el directorio externo")

            val archivo = File(externalDir, FILE_NAME)
            // Extensión Kotlin writeText() - escribe y cierra automáticamente
            archivo.writeText(texto, Charsets.UTF_8)

            val msg = "Guardado en: ${archivo.absolutePath}"
            binding.tvStatus.text = msg
            binding.editText.setText("")
            Toast.makeText(this, "Archivo guardado exitosamente", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Carga el archivo desde el directorio externo privado de la app.
     * readText() es la extensión Kotlin equivalente a bufferedReader().use { readText() }
     */
    private fun cargarArchivo() {
        try {
            val externalDir = getExternalFilesDir(null)
                ?: throw Exception("No se pudo obtener el directorio externo")

            val archivo = File(externalDir, FILE_NAME)

            if (!archivo.exists()) {
                Toast.makeText(this, "El archivo no existe aún", Toast.LENGTH_SHORT).show()
                return
            }

            // Extensión Kotlin readText() - lee todo el contenido como String
            val contenido = archivo.readText(Charsets.UTF_8)
            binding.editText.setText(contenido)
            binding.tvStatus.text = "Cargado desde: ${archivo.absolutePath}"
            Toast.makeText(this, "Archivo cargado exitosamente", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al cargar: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
