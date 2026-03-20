package com.example.conversor

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editTextCantidad: EditText
    private lateinit var radioDolares: RadioButton
    private lateinit var radioSoles: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextCantidad = findViewById(R.id.editTextText)
        radioDolares = findViewById(R.id.radio0)
        radioSoles = findViewById(R.id.radio1)
    }
    fun miClicManejador(view: View) {
        if (view.id == R.id.bntConvertir) {
            convertirMoneda()
        }
    }
    private fun convertirMoneda() {
        val texto = editTextCantidad.text.toString().trim()

        if (texto.isEmpty()) {
            Toast.makeText(this, "Ingresa una cantidad", Toast.LENGTH_SHORT).show()
            return
        }
        val cantidad = texto.toFloatOrNull()
        if (cantidad == null || cantidad <= 0) {
            Toast.makeText(this, "Número inválido", Toast.LENGTH_SHORT).show()
            return
        }
        val tipoCambio = 3.41f

        if (radioDolares.isChecked) {
            val resultado = cantidad * tipoCambio
            Toast.makeText(
                this,
                "%.2f dólares = %.2f soles".format(cantidad, resultado),
                Toast.LENGTH_LONG
            ).show()

        } else if (radioSoles.isChecked) {
            val resultado = cantidad / tipoCambio
            Toast.makeText(
                this,
                "%.2f soles = %.2f dólares".format(cantidad, resultado),
                Toast.LENGTH_LONG
            ).show()

        } else {
            Toast.makeText(this, "Selecciona una opción", Toast.LENGTH_SHORT).show()
        }
    }
}
