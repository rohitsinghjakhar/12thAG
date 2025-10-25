package com.roni.class12thagjetnotes.study

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.databinding.ActivityPrintNotesBinding

class PrintNotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrintNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrintNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        startAnimations()
    }

    private fun setupUI() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnContactUs.setOnClickListener {
            openWhatsApp()
        }

        binding.btnCallNow.setOnClickListener {
            makePhoneCall()
        }

        binding.btnEmailUs.setOnClickListener {
            sendEmail()
        }
    }

    private fun openWhatsApp() {
        val phoneNumber = "+919876543210" // Replace with actual number
        val message = "Hi, I want to order printed notes for Class 12."
        val url = "https://wa.me/$phoneNumber?text=${Uri.encode(message)}"

        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun makePhoneCall() {
        val phoneNumber = "tel:+919876543210" // Replace with actual number
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber))
        startActivity(intent)
    }

    private fun sendEmail() {
        val email = "jakhar301021@gmail.com" // Replace with actual email
        val subject = "Inquiry about Printed Notes"
        val body = "Hi, I would like to know more about ordering printed notes."

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }

        try {
            startActivity(Intent.createChooser(intent, "Send Email"))
        } catch (e: Exception) {
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startAnimations() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)

        binding.headerLayout.startAnimation(fadeIn)
        binding.contentLayout.startAnimation(slideUp)
    }
}