package com.example.conversormulti

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editTextCantidad: EditText
    private lateinit var spinnerOrigen: Spinner
    private lateinit var spinnerDestino: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextCantidad = findViewById(R.id.editTextText)
        spinnerOrigen = findViewById(R.id.spinnerOrigen)
        spinnerDestino = findViewById(R.id.spinnerDestino)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.monedas,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerOrigen.adapter = adapter
        spinnerDestino.adapter = adapter
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

        val cantidad = texto.toDoubleOrNull()
        if (cantidad == null || cantidad <= 0) {
            Toast.makeText(this, "Número inválido", Toast.LENGTH_SHORT).show()
            return
        }

        val origen = spinnerOrigen.selectedItem.toString()
        val destino = spinnerDestino.selectedItem.toString()

        if (origen == destino) {
            Toast.makeText(this, "Selecciona monedas diferentes", Toast.LENGTH_SHORT).show()
            return
        }

        val resultado = convertir(cantidad, origen, destino)

        Toast.makeText(
            this,
            "%.2f $origen = %.2f $destino".format(cantidad, resultado),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun convertir(cantidad: Double, origen: String, destino: String): Double {

        val tasas = mapOf(
            "Dólar" to 1.0,
            "Sol" to 0.27,
            "Euro" to 1.08,
            "Libra" to 1.25,
            "Rupia" to 0.012,
            "Real" to 0.20,
            "Peso" to 0.059,
            "Yuan" to 0.14,
            "Yen" to 0.0067
        )

        val enDolares = cantidad / tasas[origen]!!
        return enDolares * tasas[destino]!!
    }
}