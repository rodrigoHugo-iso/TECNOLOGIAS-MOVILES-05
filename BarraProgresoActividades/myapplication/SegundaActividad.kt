package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class SegundaActividad : AppCompatActivity() {

    private val TIMER_RUNTIME = 10000
    private var nbActivo = false
    private lateinit var nProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_segunda)

        nProgressBar = findViewById(R.id.progressBar)

        val timerThread = Thread {
            nbActivo = true
            try {
                var espera = 0
                while (nbActivo && espera < TIMER_RUNTIME) {
                    Thread.sleep(200)
                    if (nbActivo) {
                        espera += 200
                        actualizarProgress(espera)
                    }
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                onContinuar()
            }
        }

        timerThread.start()
    }

    private fun actualizarProgress(timePassed: Int) {
        if (::nProgressBar.isInitialized) {
            val progress = nProgressBar.max * timePassed / TIMER_RUNTIME
            runOnUiThread {
                nProgressBar.progress = progress
            }
        }
    }

    private fun onContinuar() {
        Log.d("mensajeFinal", "Su barra de progreso acaba de cargar")
    }
}