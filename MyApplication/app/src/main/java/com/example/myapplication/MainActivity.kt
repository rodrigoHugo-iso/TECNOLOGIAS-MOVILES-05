package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val TAG = "Ciclo de Vida"

    // 🔥 ProgressBar variables
    private val TIMER_RUNTIME = 2000
    private var nbActivo = false
    private lateinit var nProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "En el evento onCreate()")

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 🔥 ProgressBar
        nProgressBar = findViewById(R.id.progressBar)

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

        // 🔥 Navegación
        binding.btnMostrar.setOnClickListener {
            startActivity(Intent(this, SegundaActividad::class.java))
        }

        binding.btnConversion.setOnClickListener {
            startActivity(Intent(this, ConversionActivity::class.java))
        }

        binding.btnDrawer.setOnClickListener {
            startActivity(Intent(this, DrawerActivity::class.java))
        }
    }

    private fun actualizarProgress(timePassed: Int) {
        val progress = nProgressBar.max * timePassed / TIMER_RUNTIME
        runOnUiThread {
            nProgressBar.progress = progress
        }
    }

    private fun onContinuar() {
        Log.d("mensajeFinal", "Carga completa - Main")
    }

    override fun onStart()   { super.onStart(); Log.d(TAG, "onStart()") }
    override fun onRestart(){ super.onRestart(); Log.d(TAG, "onRestart()") }
    override fun onResume() { super.onResume(); Log.d(TAG, "onResume()") }
    override fun onPause()  { super.onPause(); Log.d(TAG, "onPause()") }
    override fun onStop()   { super.onStop(); Log.d(TAG, "onStop()") }
    override fun onDestroy(){ super.onDestroy(); Log.d(TAG, "onDestroy()") }
}