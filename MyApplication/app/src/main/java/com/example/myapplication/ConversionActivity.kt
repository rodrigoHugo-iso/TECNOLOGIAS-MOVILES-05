package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ConversionActivity : AppCompatActivity() {

    private val TIMER_RUNTIME = 3000
    private var nbActivo = false
    private lateinit var nProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversion)

        nProgressBar = findViewById(R.id.progressBar)

        val etMonto = findViewById<EditText>(R.id.etMonto)
        val btn = findViewById<Button>(R.id.btnConvertir)
        val tv = findViewById<TextView>(R.id.tvResultado)

        btn.setOnClickListener {
            val monto = etMonto.text.toString().toDoubleOrNull()

            if (monto != null) {
                val resultado = monto * 0.27
                tv.text = "USD: $resultado"
            } else {
                tv.text = "Ingrese un valor válido"
            }
        }

        val timerThread = Thread {
            nbActivo = true
            var espera = 0

            while (nbActivo && espera < TIMER_RUNTIME) {
                Thread.sleep(100)
                espera += 100
                actualizarProgress(espera)
            }

            onContinuar()
        }

        timerThread.start()
    }

    private fun actualizarProgress(timePassed: Int) {
        val progress = nProgressBar.max * timePassed / TIMER_RUNTIME
        runOnUiThread {
            nProgressBar.progress = progress
        }
    }

    private fun onContinuar() {
        Log.d("mensajeFinal", "Carga completa - Conversion")
    }
}