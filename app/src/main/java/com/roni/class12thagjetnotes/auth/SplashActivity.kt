package com.roni.class12thagjetnotes.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.roni.class12thagjetnotes.MainActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Check if user is already logged in after a delay
        Handler(Looper.getMainLooper()).postDelayed({
            checkUserAndNavigate()
        }, 2000) // 2 second splash screen
    }

    private fun checkUserAndNavigate() {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            // User is signed in, navigate to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // No user is signed in, navigate to LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}