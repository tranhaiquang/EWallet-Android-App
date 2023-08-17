package com.example.ewallet

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import com.example.ewallet.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        getSupportActionBar()?.hide()
        setContentView(binding.root)

        auth = Firebase.auth

        // Set status bar to transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            controller?.hide(WindowInsets.Type.statusBars())
            controller?.systemBarsBehavior = WindowInsetsController.BEHAVIOR_DEFAULT
        }

        binding.btnLogin.setOnClickListener {
            if (binding.etEmail.text.toString() == "" || binding.etPassword.text.toString() == "") {
                Toast.makeText(
                    this,
                    "Please enter email or password",
                    Toast.LENGTH_SHORT,
                ).show()
            } else {
                auth.signInWithEmailAndPassword(binding.etEmail.text.toString(), binding.etPassword.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("EMAIL", binding.etEmail.text.toString())
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this,
                                "Incorrect email or password",
                                Toast.LENGTH_SHORT,
                            ).show()

                        }
                    }
            }

        }


    }
}