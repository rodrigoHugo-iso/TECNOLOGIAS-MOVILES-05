package com.ucsm.fcmapp.ui

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.messaging.FirebaseMessaging
import com.ucsm.fcmapp.service.databinding.ActivityMainBinding

/**
 * Pantalla principal del Ejercicio 3.
 *
 * Funcionalidades:
 *  • Muestra el FCM device token
 *  • Botón para copiarlo al portapapeles
 *  • Botón para suscribirse/desuscribirse de un topic
 *  • Lista de notificaciones recibidas en esta sesión
 *
 * CÓMO USAR:
 *  1. Instala la app → copia el token que aparece en pantalla.
 *  2. Ve a Firebase Console > Messaging > Nueva campaña.
 *  3. Pega el token en "Enviar mensaje de prueba" → presiona Probar.
 *  4. Para topics: subscribete al topic "general" y envía desde la consola
 *     eligiendo el topic como destino.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val receivedNotifications = mutableListOf<String>()
    private lateinit var notifAdapter: NotificationAdapter

    // Solicitar permiso de notificaciones en Android 13+
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                Toast.makeText(this, "Permiso de notificaciones concedido", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Sin permiso: las notificaciones no aparecerán", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // RecyclerView de notificaciones recibidas
        notifAdapter = NotificationAdapter(receivedNotifications)
        binding.recyclerNotifications.layoutManager = LinearLayoutManager(this)
        binding.recyclerNotifications.adapter        = notifAdapter

        requestNotificationPermission()
        loadToken()
        setupButtons()

        // Si la actividad fue abierta desde una notificación
        intent.getStringExtra("extra_data")?.let { extra ->
            if (extra.isNotEmpty()) showExtraData(extra)
        }
    }

    // ─── Permiso Android 13+ ───────────────────────────────────────────────────
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> { /* Ya tiene permiso */ }
                else -> requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // ─── Obtener el FCM token ──────────────────────────────────────────────────
    private fun loadToken() {
        // Primero intenta desde SharedPreferences (guardado por el Service)
        val cached = getSharedPreferences("fcm_prefs", Context.MODE_PRIVATE)
            .getString("token", null)

        if (cached != null) {
            displayToken(cached)
        } else {
            binding.tvToken.text = "Obteniendo token..."
        }

        // Solicita el token a Firebase (actualiza si hay uno nuevo)
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM_TOKEN", token)
                displayToken(token)
                getSharedPreferences("fcm_prefs", Context.MODE_PRIVATE)
                    .edit().putString("token", token).apply()
            } else {
                binding.tvToken.text = "Error al obtener token"
                Log.e("FCM_TOKEN", "Error", task.exception)
            }
        }
    }

    private fun displayToken(token: String) {
        binding.tvToken.text = token
    }

    // ─── Botones ───────────────────────────────────────────────────────────────
    private fun setupButtons() {
        // Copiar token
        binding.btnCopyToken.setOnClickListener {
            val token = binding.tvToken.text.toString()
            if (token.isNotEmpty() && token != "Obteniendo token...") {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("FCM Token", token))
                Toast.makeText(this, "Token copiado al portapapeles", Toast.LENGTH_SHORT).show()
            }
        }

        // Suscribirse al topic "general"
        binding.btnSubscribeTopic.setOnClickListener {
            FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener { task ->
                    val msg = if (task.isSuccessful) "Suscrito al topic 'general'"
                              else "Error al suscribirse"
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                    binding.tvTopicStatus.text = "Topic: general ✓"
                }
        }

        // Desuscribirse del topic
        binding.btnUnsubscribeTopic.setOnClickListener {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("general")
                .addOnCompleteListener { task ->
                    val msg = if (task.isSuccessful) "Desuscrito del topic 'general'"
                              else "Error al desuscribirse"
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                    binding.tvTopicStatus.text = "Topic: sin suscripción"
                }
        }
    }

    // ─── Agrega notificación a la lista visual ────────────────────────────────
    fun addNotificationToList(title: String, body: String) {
        val entry = "[$title] $body"
        receivedNotifications.add(0, entry)
        notifAdapter.notifyItemInserted(0)
        binding.recyclerNotifications.scrollToPosition(0)
    }

    private fun showExtraData(data: String) {
        Toast.makeText(this, "Dato extra de notificación: $data", Toast.LENGTH_LONG).show()
    }
}
