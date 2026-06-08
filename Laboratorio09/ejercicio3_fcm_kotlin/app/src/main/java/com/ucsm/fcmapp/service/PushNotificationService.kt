package com.ucsm.fcmapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ucsm.fcmapp.service.R
import com.ucsm.fcmapp.ui.MainActivity

/**
 * Servicio FCM.
 *
 * PASOS PARA CONECTAR:
 *  1. Agrega google-services.json al módulo app.
 *  2. Sube el archivo de clave de servicio en Firebase Console > Cloud Messaging > configuración.
 *  3. Ejecuta la app y copia el device token desde Logcat (tag: FCM_TOKEN).
 *  4. Usa ese token en Firebase Console > Messaging > "Enviar mensaje de prueba".
 */
class PushNotificationService : FirebaseMessagingService() {

    companion object {
        private const val TAG             = "FCM_SERVICE"
        const val CHANNEL_ID              = "fcm_default_channel"
        const val CHANNEL_NAME            = "Notificaciones"
        private var notificationIdCounter = 0
    }

    // ─── Token nuevo (se llama cuando Firebase renueva el token) ──────────────
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "FCM Token renovado: $token")

        // TODO: Envía el token a tu servidor o guárdalo en Firestore/Realtime DB
        // Ejemplo: FirebaseFirestore.getInstance()
        //             .collection("users").document(uid)
        //             .update("fcmToken", token)

        // Guardarlo localmente (SharedPreferences) para fácil acceso
        getSharedPreferences("fcm_prefs", Context.MODE_PRIVATE)
            .edit()
            .putString("token", token)
            .apply()
    }

    // ─── Mensaje recibido ──────────────────────────────────────────────────────
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "Mensaje recibido desde: ${remoteMessage.from}")

        // Datos del mensaje
        val title = remoteMessage.notification?.title
            ?: remoteMessage.data["title"]
            ?: "Nueva notificación"

        val body = remoteMessage.notification?.body
            ?: remoteMessage.data["body"]
            ?: ""

        // Datos personalizados (payload data)
        val extraData = remoteMessage.data["extra"] ?: ""

        Log.d(TAG, "Título: $title | Cuerpo: $body | Extra: $extraData")

        showNotification(title, body, extraData)
    }

    // ─── Construir y mostrar la notificación ───────────────────────────────────
    private fun showNotification(title: String, body: String, extraData: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Crear canal (requerido en Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description    = "Canal principal de notificaciones FCM"
                enableLights(true)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Intent al abrir la notificación → abre MainActivity
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra("extra_data", extraData)
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setAutoCancel(true)
            .setSound(soundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationIdCounter++, notification)
    }
}
