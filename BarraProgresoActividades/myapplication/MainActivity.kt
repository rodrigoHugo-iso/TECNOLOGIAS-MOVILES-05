package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val TAG = "Ciclo de Vida"

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

        binding.btnMostrar.setOnClickListener {
            val intent = Intent(this, SegundaActividad::class.java)
            startActivity(intent)
        }
    }

    override fun onStart()   { super.onStart();   Log.d(TAG, "En el evento onStart()") }
    override fun onRestart(){ super.onRestart(); Log.d(TAG, "En el evento onRestart()") }
    override fun onResume() { super.onResume();  Log.d(TAG, "En el evento onResume()") }
    override fun onPause()  { super.onPause();   Log.d(TAG, "En el evento onPause()") }
    override fun onStop()   { super.onStop();    Log.d(TAG, "En el evento onStop()") }
    override fun onDestroy(){ super.onDestroy(); Log.d(TAG, "En el evento onDestroy()") }
}