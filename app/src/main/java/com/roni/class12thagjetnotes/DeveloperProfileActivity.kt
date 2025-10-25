package com.roni.class12thagjetnotes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.firebase.storage.FirebaseStorage
import com.roni.class12thagjetnotes.R

class DeveloperProfileActivity : AppCompatActivity() {

    private val storage = FirebaseStorage.getInstance()

    // Views
    private lateinit var ivBack: ImageView
    private lateinit var ivDeveloperPhoto: ImageView
    private lateinit var tvDeveloperName: TextView
    private lateinit var tvDeveloperTitle: TextView
    private lateinit var tvDeveloperTagline: TextView
    private lateinit var tvBio: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvExperience: TextView
    private lateinit var tvEducation: TextView
    private lateinit var btnGithub: MaterialButton
    private lateinit var btnLinkedin: MaterialButton
    private lateinit var btnPortfolio: MaterialButton
    private lateinit var layoutEmail: LinearLayout
    private lateinit var layoutPhone: LinearLayout
    private lateinit var sectionBio: LinearLayout
    private lateinit var sectionEducation: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developer_profile)

        initViews()
        setupBackButton()
        loadProfileData()
        setupClickListeners()
    }

    private fun initViews() {
        // Find all views by ID
        ivBack = findViewById(R.id.ivBack)
        ivDeveloperPhoto = findViewById(R.id.ivDeveloperPhoto)
        tvDeveloperName = findViewById(R.id.tvDeveloperName)
        tvDeveloperTitle = findViewById(R.id.tvDeveloperTitle)
        tvDeveloperTagline = findViewById(R.id.tvDeveloperTagline)
        tvBio = findViewById(R.id.tvBio)
        tvEmail = findViewById(R.id.tvEmail)
        tvPhone = findViewById(R.id.tvPhone)
        tvLocation = findViewById(R.id.tvLocation)
        tvExperience = findViewById(R.id.tvExperience)
        tvEducation = findViewById(R.id.tvEducation)
        btnGithub = findViewById(R.id.btnGithub)
        btnLinkedin = findViewById(R.id.btnLinkedin)
        btnPortfolio = findViewById(R.id.btnPortfolio)
        layoutEmail = findViewById(R.id.layoutEmail)
        layoutPhone = findViewById(R.id.layoutPhone)
        sectionBio = findViewById(R.id.sectionBio)
        sectionEducation = findViewById(R.id.sectionEducation)
    }

    private fun setupBackButton() {
        ivBack.setOnClickListener {
            finish()
        }
    }

    private fun loadProfileData() {
        val name = intent.getStringExtra("developer_name") ?: "Rohit Choudhary"
        val title = intent.getStringExtra("developer_title") ?: "Full Stack Developer"
        val tagline = intent.getStringExtra("developer_tagline") ?: "Empowering students"
        val photoUrl = intent.getStringExtra("developer_photo") ?: ""
        val bio = intent.getStringExtra("developer_bio") ?: ""
        val email = intent.getStringExtra("developer_email") ?: ""
        val phone = intent.getStringExtra("developer_phone") ?: ""
        val location = intent.getStringExtra("developer_location") ?: ""
        val experience = intent.getStringExtra("developer_experience") ?: ""
        val github = intent.getStringExtra("developer_github") ?: ""
        val linkedin = intent.getStringExtra("developer_linkedin") ?: ""
        val portfolio = intent.getStringExtra("developer_portfolio") ?: ""
        val education = intent.getStringExtra("developer_education") ?: ""

        // Set basic info
        tvDeveloperName.text = name
        tvDeveloperTitle.text = title
        tvDeveloperTagline.text = tagline

        // Load photo
        if (photoUrl.isNotEmpty()) {
            loadDeveloperPhoto(photoUrl)
        }

        // Set detailed info
        if (bio.isNotEmpty()) {
            tvBio.text = bio
        } else {
            sectionBio.visibility = View.GONE
        }

        if (email.isNotEmpty()) {
            tvEmail.text = email
        } else {
            layoutEmail.visibility = View.GONE
        }

        if (phone.isNotEmpty()) {
            tvPhone.text = phone
        } else {
            layoutPhone.visibility = View.GONE
        }

        if (location.isNotEmpty()) {
            tvLocation.text = location
        } else {
            findViewById<LinearLayout>(R.id.layoutLocation).visibility = View.GONE
        }

        if (experience.isNotEmpty()) {
            tvExperience.text = experience
        } else {
            findViewById<LinearLayout>(R.id.layoutExperience).visibility = View.GONE
        }

        if (education.isNotEmpty()) {
            tvEducation.text = education
        } else {
            sectionEducation.visibility = View.GONE
        }

        // Social links
        if (github.isEmpty()) btnGithub.visibility = View.GONE
        if (linkedin.isEmpty()) btnLinkedin.visibility = View.GONE
        if (portfolio.isEmpty()) btnPortfolio.visibility = View.GONE

        // Setup social link clicks
        btnGithub.setOnClickListener {
            if (github.isNotEmpty()) openUrl(github)
        }

        btnLinkedin.setOnClickListener {
            if (linkedin.isNotEmpty()) openUrl(linkedin)
        }

        btnPortfolio.setOnClickListener {
            if (portfolio.isNotEmpty()) openUrl(portfolio)
        }
    }

    private fun loadDeveloperPhoto(photoUrl: String) {
        if (photoUrl.startsWith("gs://") || photoUrl.startsWith("images/")) {
            val storageRef = if (photoUrl.startsWith("gs://")) {
                storage.getReferenceFromUrl(photoUrl)
            } else {
                storage.reference.child(photoUrl)
            }

            storageRef.downloadUrl
                .addOnSuccessListener { uri ->
                    Glide.with(this)
                        .load(uri)
                        .circleCrop()
                        .placeholder(android.R.drawable.ic_menu_myplaces)
                        .into(ivDeveloperPhoto)
                }
                .addOnFailureListener {
                    // Keep default
                }
        } else if (photoUrl.startsWith("http")) {
            Glide.with(this)
                .load(photoUrl)
                .circleCrop()
                .placeholder(android.R.drawable.ic_menu_myplaces)
                .into(ivDeveloperPhoto)
        }
    }

    private fun setupClickListeners() {
        // Email click
        layoutEmail.setOnClickListener {
            val email = tvEmail.text.toString()
            if (email.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:$email")
                }
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            }
        }

        // Phone click
        layoutPhone.setOnClickListener {
            val phone = tvPhone.text.toString()
            if (phone.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phone")
                }
                startActivity(intent)
            }
        }
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}