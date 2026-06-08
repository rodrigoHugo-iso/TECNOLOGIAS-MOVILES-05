package com.example.bitacorafcm

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private lateinit var txtToken: TextView
    private lateinit var btnObtenerToken: Button
    private lateinit var btnCopiarToken: Button

    private var tokenFCM = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtToken = findViewById(R.id.txtToken)
        btnObtenerToken = findViewById(R.id.btnObtenerToken)
        btnCopiarToken = findViewById(R.id.btnCopiarToken)

        btnObtenerToken.setOnClickListener {

            FirebaseMessaging.getInstance().token
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        tokenFCM = task.result

                        txtToken.text = tokenFCM

                        Toast.makeText(
                            this,
                            "Token obtenido correctamente",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        Toast.makeText(
                            this,
                            "Error al obtener token",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        btnCopiarToken.setOnClickListener {

            if (tokenFCM.isNotEmpty()) {

                val clipboard =
                    getSystemService(Context.CLIPBOARD_SERVICE)
                            as ClipboardManager

                val clip =
                    ClipData.newPlainText(
                        "FCM Token",
                        tokenFCM
                    )

                clipboard.setPrimaryClip(clip)

                Toast.makeText(
                    this,
                    "Token copiado",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                Toast.makeText(
                    this,
                    "Obtén el token primero",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}