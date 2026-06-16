package com.noah.smartreply

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
// Importación de la clase autogenerada por View Binding para tu layout activity_main.xml
import com.noah.smartreply.databinding.ActivityMainBinding
import com.google.mlkit.nl.smartreply.SmartReply
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult
import com.google.mlkit.nl.smartreply.TextMessage

class MainActivity : AppCompatActivity() {

    // Instancia global para acceder a los componentes visuales de manera segura y eficiente
    private lateinit var binding: ActivityMainBinding

    // Historial que almacena la conversación en la memoria local del dispositivo
    private var conversation = ArrayList<TextMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sendButton.setOnClickListener {
            addMessage(binding.messageText.text.toString())
        }
        binding.hintsButton.setOnClickListener {
            getHints()
        }
        binding.clearButton.setOnClickListener {
            conversation = ArrayList()
            binding.hint0Button.visibility = View.GONE
            binding.hint1Button.visibility = View.GONE
            binding.hint2Button.visibility = View.GONE
            binding.messageText.setText("")
            binding.nameText.setText("")
            binding.errorText.text = ""
        }
        binding.hint0Button.setOnClickListener {
            addMessage(binding.hint0Button.text.toString())
        }
        binding.hint1Button.setOnClickListener {
            addMessage(binding.hint1Button.text.toString())
        }
        binding.hint2Button.setOnClickListener {
            addMessage(binding.hint2Button.text.toString())
        }
    }

    /**
     * Agrega el mensaje ingresado al historial de la conversación.
     * se registra como un usuario remoto para que el
     * modelo entienda el contexto y pueda generar respuestas para ti.
     */
    private fun addMessage(text: String) {
        if (text.isNotEmpty()) {
            conversation.add(
                TextMessage.createForRemoteUser(
                    text,
                    System.currentTimeMillis(),
                    binding.nameText.text.toString()
                )
            )
            binding.messageText.setText("") // Limpia el campo de entrada
            Toast.makeText(this, "Mensaje enviado", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Procesa la lista de mensajes acumulados y solicita sugerencias
     * automáticas al SDK local de Smart Reply.
     */
    private fun getHints() {
        if (conversation.isEmpty()) {
            Toast.makeText(this, "Escribe y envía un mensaje primero", Toast.LENGTH_SHORT).show()
            return
        }

        val smartReply = SmartReply.getClient()

        smartReply.suggestReplies(conversation)
            .addOnSuccessListener { result ->
                // Validación del idioma (ML Kit actualmente solo procesa conversaciones en inglés)
                if (result.status == SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE) {
                    Toast.makeText(
                        applicationContext,
                        "Lenguaje no soportado (usa inglés)",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                // Si no hay suficientes sugerencias
                else if (result.status == SmartReplySuggestionResult.STATUS_NO_REPLY) {
                    Toast.makeText(
                        applicationContext,
                        "No hay sugerencias disponibles",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                // Si la generación fue exitosa, asigna los textos y hace visibles los botones
                else if (result.status == SmartReplySuggestionResult.STATUS_SUCCESS) {
                    binding.hint0Button.text = result.suggestions[0].text
                    binding.hint1Button.text = result.suggestions[1].text
                    binding.hint2Button.text = result.suggestions[2].text

                    binding.hint0Button.visibility = View.VISIBLE
                    binding.hint1Button.visibility = View.VISIBLE
                    binding.hint2Button.visibility = View.VISIBLE
                }
            }
            .addOnFailureListener { exception ->
                // Despliega el error en el TextView en caso de fallo crítico de inicialización
                binding.errorText.text = exception.toString()
            }
    }
}