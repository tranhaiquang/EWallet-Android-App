package com.example.ewallet

import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.example.ewallet.databinding.ActivityLoginBinding
import com.example.ewallet.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        getSupportActionBar()?.hide()
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            window.statusBarColor = Color.TRANSPARENT
        }

        //val email = intent.getStringExtra("EMAIL")
        val email = "haiquang1412@gmail.com"
        var userID = ""

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.getString("email").toString() == email) {
                        userID = document.id.toString()
                        binding.etUsername.setText(document.getString("email"))
                        binding.etBalance.setText(document.get("balance").toString()
                            ?.let { addCommasToNumber(it.toDouble()) } + " VND")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Error", "Error getting documents.", exception)
            }

        binding.cardViewDeposit.setOnClickListener {
            val docRef = db.collection("users").document("AaDTFVluzjNHwumt4X0o")
            val amount: Double = 4204.0
            docRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val userBalance = documentSnapshot.getDouble("balance") ?: 0.0
                        updateBalance(userBalance, amount, "AaDTFVluzjNHwumt4X0o")
                        updateUI(userBalance + amount)

                    } else {
                        Log.d(TAG, "Document does not exist")
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error getting document", e)
                }




        }
    }

    fun updateUI(userBalance: Double) {
        binding.etBalance.text = userBalance.toString()
            ?.let { addCommasToNumber(it.toDouble()) } + " VND"
    }

    fun updateBalance(balance: Double, amount: Double, id: String) {
        val docRef = db.collection("users").document(id)

        val updates = hashMapOf<String, Any>(
            "balance" to balance + amount
        )

        docRef.update(updates as Map<String, Any>)
            .addOnSuccessListener {
                Toast.makeText(this, "Balance is updated", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update balance", Toast.LENGTH_SHORT).show()
            }
    }

    fun addCommasToNumber(number: Double): String {
        val formatter: NumberFormat = DecimalFormat("#,###.##")
        return formatter.format(number)
    }
}