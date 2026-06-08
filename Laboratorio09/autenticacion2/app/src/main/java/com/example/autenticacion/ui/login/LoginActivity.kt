package com.example.autenticacion.ui.login

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.autenticacion.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth

    private var semail: String = ""
    private var spassword: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        binding.login.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            performLoginOrRegistration()
        }
    }

    private fun performLoginOrRegistration() {

        semail = binding.username.text.toString().trim()
        spassword = binding.password.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(semail).matches()) {

            binding.username.error = "Formato inválido de email"
            binding.loading.visibility = View.GONE

        } else if (TextUtils.isEmpty(spassword) || spassword.length < 6) {

            binding.password.error = "El password debe tener al menos 6 caracteres"
            binding.loading.visibility = View.GONE

        } else {

            registerUser()
        }
    }

    private fun registerUser() {

        mAuth.createUserWithEmailAndPassword(semail, spassword)
            .addOnCompleteListener(this) { task ->

                binding.loading.visibility = View.GONE

                if (task.isSuccessful) {

                    Toast.makeText(
                        this,
                        "Registro exitoso",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()

                } else {

                    Toast.makeText(
                        this,
                        "Error: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}