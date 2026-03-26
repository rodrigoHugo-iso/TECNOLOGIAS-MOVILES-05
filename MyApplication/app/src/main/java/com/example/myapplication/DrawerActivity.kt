package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.widget.Toolbar
import com.google.android.material.navigation.NavigationView

class DrawerActivity : AppCompatActivity() {

    private val TIMER_RUNTIME = 3000
    private var nbActivo = false
    private lateinit var nProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        nProgressBar = findViewById(R.id.progressBar)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open,
            R.string.close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.nav_home -> {
                    // te quedas en la misma pantalla
                }

                R.id.nav_conversion -> {
                    startActivity(Intent(this, ConversionActivity::class.java))
                }
            }
            drawerLayout.closeDrawers()
            true
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
        Log.d("mensajeFinal", "Carga completa - Drawer")
    }
}