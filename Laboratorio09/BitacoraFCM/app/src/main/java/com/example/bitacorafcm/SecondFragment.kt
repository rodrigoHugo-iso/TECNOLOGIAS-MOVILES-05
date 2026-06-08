package com.example.bitacorafcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotification : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title =
            remoteMessage.notification?.title
                ?: "Firebase"

        val body =
            remoteMessage.notification?.body
                ?: "Nuevo mensaje"

        Log.d("FCM", "Título: $title")
        Log.d("FCM", "Mensaje: $body")

        mostrarNotificacion(title, body)
    }

    private fun mostrarNotificacion(
        titulo: String,
        mensaje: String
    ) {

        val channelId = "firebase_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                channelId,
                "Canal Firebase",
                NotificationManager.IMPORTANCE_HIGH
            )

            val manager =
                getSystemService(NotificationManager::class.java)

            manager.createNotificationChannel(channel)
        }

        val notification =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(titulo)
                .setContentText(mensaje)
                .setAutoCancel(true)
                .build()

        val manager =
            getSystemService(NOTIFICATION_SERVICE)
                    as NotificationManager

        manager.notify(1, notification)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("FCM", "TOKEN NUEVO: $token")
    }
}