package com.roni.class12thagjetnotes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.roni.class12thagjetnotes.study.*
import com.roni.class12thagjetnotes.adapter.BannerAdapter
import com.roni.class12thagjetnotes.adapter.OtherAppAdapter
import com.roni.class12thagjetnotes.adapter.ReviewAdapter
import com.roni.class12thagjetnotes.adapter.SocialMediaAdapter
import com.roni.class12thagjetnotes.auth.ProfileActivity
import com.roni.class12thagjetnotes.databinding.ActivityMainBinding
import com.roni.class12thagjetnotes.jet.JetTaiyariActivity
import com.roni.class12thagjetnotes.models.Banner
import com.roni.class12thagjetnotes.models.OtherApp
import com.roni.class12thagjetnotes.models.Review
import com.roni.class12thagjetnotes.models.SocialMedia
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var socialMediaAdapter: SocialMediaAdapter
    private lateinit var otherAppAdapter: OtherAppAdapter

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    // Store developer profile data
    private var developerName = "Rohit Choudhary"
    private var developerTitle = "Full Stack Developer"
    private var developerTagline = "Empowering students to achieve their dreams"
    private var developerBio = ""
    private var developerEmail = ""
    private var developerPhone = ""
    private var developerLocation = ""
    private var developerExperience = ""
    private var developerGithub = ""
    private var developerLinkedin = ""
    private var developerPortfolio = ""
    private var developerEducation = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupGreeting()
        setupProfilePhoto()
        setupBanners()

        // Load data from Firebase
        loadDeveloperProfile()
        loadReviewsFromFirebase()
        loadSocialMediaFromFirebase()
        loadOtherAppsFromFirebase()

        setupClickListeners()
        startAnimations()
    }

    private fun setupGreeting() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val greeting = when (hour) {
            in 0..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..20 -> "Good Evening"
            else -> "Good Night"
        }

        binding.tvGreeting.text = greeting
        binding.tvUserName.text = "Student"
    }

    private fun setupProfilePhoto() {
        // Set default profile icon
    }

    private fun setupBanners() {
        val banners = listOf(
            Banner("1", "🎯 JET 2025 Preparation", "Complete study materials for JET entrance exam", "#667eea"),
            Banner("2", "📚 Class 12th Notes", "Subject-wise comprehensive notes", "#764ba2"),
            Banner("3", "📝 Previous Year Papers", "Solve past papers and practice", "#f093fb"),
            Banner("4", "🎬 Video Lectures", "Watch and learn from expert teachers", "#4facfe")
        )

        bannerAdapter = BannerAdapter(banners)
        binding.bannerViewPager.adapter = bannerAdapter
        TabLayoutMediator(binding.bannerIndicator, binding.bannerViewPager) { _, _ -> }.attach()
        setupAutoScroll()
    }

    private fun setupAutoScroll() {
        val handler = android.os.Handler(mainLooper)
        val runnable = object : Runnable {
            override fun run() {
                val currentItem = binding.bannerViewPager.currentItem
                val itemCount = bannerAdapter.itemCount
                if (currentItem < itemCount - 1) {
                    binding.bannerViewPager.currentItem = currentItem + 1
                } else {
                    binding.bannerViewPager.currentItem = 0
                }
                handler.postDelayed(this, 3000)
            }
        }
        handler.postDelayed(runnable, 3000)
    }

    private fun loadDeveloperProfile() {
        binding.progressDeveloper.visibility = View.VISIBLE

        firestore.collection("developerProfile")
            .document("main")
            .get()
            .addOnSuccessListener { document ->
                binding.progressDeveloper.visibility = View.GONE

                if (document.exists()) {
                    // Get data from Firebase
                    developerName = document.getString("name") ?: "Rohit Choudhary"
                    developerTitle = document.getString("title") ?: "Full Stack Developer"
                    developerTagline = document.getString("tagline") ?: "Empowering students"
                    val photoUrl = document.getString("photoUrl") ?: ""
                    developerBio = document.getString("bio") ?: ""
                    developerEmail = document.getString("email") ?: ""
                    developerPhone = document.getString("phone") ?: ""
                    developerLocation = document.getString("location") ?: ""
                    developerExperience = document.getString("experience") ?: ""
                    developerGithub = document.getString("github") ?: ""
                    developerLinkedin = document.getString("linkedin") ?: ""
                    developerPortfolio = document.getString("portfolio") ?: ""
                    developerEducation = document.getString("education") ?: ""

                    // Update UI
                    binding.tvDeveloperName.text = developerName
                    binding.tvDeveloperTitle.text = developerTitle
                    binding.tvDeveloperTagline.text = developerTagline

                    // Load photo if available
                    if (photoUrl.isNotEmpty()) {
                        loadDeveloperPhoto(photoUrl)
                    }

                    Log.d("MainActivity", "Developer profile loaded successfully")
                }
            }
            .addOnFailureListener { e ->
                binding.progressDeveloper.visibility = View.GONE
                Log.e("MainActivity", "Error loading developer profile", e)
                showToast("Using default developer info")
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
                        .placeholder(android.R.drawable.btn_star_big_on)
                        .into(binding.ivDeveloperPhoto)
                }
                .addOnFailureListener { e ->
                    Log.e("MainActivity", "Error loading photo", e)
                }
        } else if (photoUrl.startsWith("http")) {
            Glide.with(this)
                .load(photoUrl)
                .circleCrop()
                .placeholder(android.R.drawable.btn_star_big_on)
                .into(binding.ivDeveloperPhoto)
        }
    }

    private fun loadReviewsFromFirebase() {
        firestore.collection("reviews")
            .orderBy("rating", Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener { documents ->
                val reviews = mutableListOf<Review>()

                for (document in documents) {
                    val id = document.id
                    val userName = document.getString("userName") ?: ""
                    val userImage = document.getString("userImage") ?: ""
                    val rating = document.getDouble("rating") ?: 0.0
                    val reviewText = document.getString("reviewText") ?: ""
                    val date = document.getString("date") ?: ""

                    reviews.add(Review(id, userName, userImage, rating.toFloat(), reviewText, date))
                }

                if (reviews.isNotEmpty()) {
                    reviewAdapter = ReviewAdapter(reviews)
                    binding.reviewsRecyclerView.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                        adapter = reviewAdapter
                        setHasFixedSize(true)
                    }
                    Log.d("MainActivity", "Loaded ${reviews.size} reviews")
                } else {
                    loadDefaultReviews()
                }
            }
            .addOnFailureListener { e ->
                Log.e("MainActivity", "Error loading reviews", e)
                loadDefaultReviews()
            }
    }

    private fun loadDefaultReviews() {
        val reviews = listOf(
            Review("1", "Priya Sharma", "", 5.0f, "Excellent notes! Helped me score 95% in board exams.", "2 days ago"),
            Review("2", "Rahul Kumar", "", 4.5f, "Great content and easy to understand. Highly recommended!", "1 week ago"),
            Review("3", "Neha Patel", "", 5.0f, "Best app for Class 12th preparation. Love the video lectures!", "3 days ago")
        )

        reviewAdapter = ReviewAdapter(reviews)
        binding.reviewsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = reviewAdapter
            setHasFixedSize(true)
        }
    }

    private fun loadSocialMediaFromFirebase() {
        firestore.collection("socialMedia")
            .orderBy("order")
            .get()
            .addOnSuccessListener { documents ->
                val socialMediaList = mutableListOf<SocialMedia>()

                for (document in documents) {
                    val id = document.id
                    val platform = document.getString("platform") ?: ""
                    val url = document.getString("url") ?: ""
                    val iconName = document.getString("icon") ?: "ic_menu_share"

                    val iconResId = when (iconName.lowercase()) {
                        "facebook" -> android.R.drawable.ic_menu_share
                        "twitter" -> android.R.drawable.ic_menu_send
                        "instagram" -> android.R.drawable.ic_menu_gallery
                        "youtube" -> android.R.drawable.ic_menu_slideshow
                        "linkedin" -> android.R.drawable.ic_menu_view
                        else -> android.R.drawable.ic_menu_share
                    }

                    socialMediaList.add(SocialMedia(id, platform, url, iconResId))
                }

                if (socialMediaList.isNotEmpty()) {
                    socialMediaAdapter = SocialMediaAdapter(socialMediaList)
                    binding.socialMediaRecyclerView.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                        adapter = socialMediaAdapter
                        setHasFixedSize(true)
                    }
                    Log.d("MainActivity", "Loaded ${socialMediaList.size} social media links")
                } else {
                    loadDefaultSocialMedia()
                }
            }
            .addOnFailureListener { e ->
                Log.e("MainActivity", "Error loading social media", e)
                loadDefaultSocialMedia()
            }
    }

    private fun loadDefaultSocialMedia() {
        val socialMediaList = listOf(
            SocialMedia("1", "Facebook", "https://facebook.com", android.R.drawable.ic_menu_share),
            SocialMedia("2", "Instagram", "https://instagram.com", android.R.drawable.ic_menu_gallery),
            SocialMedia("3", "YouTube", "https://youtube.com", android.R.drawable.ic_menu_slideshow)
        )

        socialMediaAdapter = SocialMediaAdapter(socialMediaList)
        binding.socialMediaRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = socialMediaAdapter
            setHasFixedSize(true)
        }
    }

    private fun loadOtherAppsFromFirebase() {
        firestore.collection("otherApps")
            .orderBy("order")
            .get()
            .addOnSuccessListener { documents ->
                val otherApps = mutableListOf<OtherApp>()

                for (document in documents) {
                    val id = document.id
                    val name = document.getString("name") ?: ""
                    val description = document.getString("description") ?: ""
                    val rating = document.getDouble("rating") ?: 0.0
                    val downloads = document.getString("downloads") ?: "0"
                    val packageName = document.getString("packageName") ?: ""
                    val playStoreUrl = document.getString("playStoreUrl") ?: ""
                    val iconName = document.getString("icon") ?: "ic_menu_gallery"

                    val iconResId = when (iconName.lowercase()) {
                        "edit" -> android.R.drawable.ic_menu_edit
                        "compass" -> android.R.drawable.ic_menu_compass
                        "info" -> android.R.drawable.ic_menu_info_details
                        "agenda" -> android.R.drawable.ic_menu_agenda
                        "slideshow" -> android.R.drawable.ic_menu_slideshow
                        else -> android.R.drawable.ic_menu_gallery
                    }

                    otherApps.add(OtherApp(id, name, description, iconResId, rating, downloads, packageName, playStoreUrl))
                }

                if (otherApps.isNotEmpty()) {
                    otherAppAdapter = OtherAppAdapter(otherApps)
                    binding.otherAppsRecyclerView.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                        adapter = otherAppAdapter
                        setHasFixedSize(true)
                    }
                    Log.d("MainActivity", "Loaded ${otherApps.size} apps")
                } else {
                    loadDefaultOtherApps()
                }
            }
            .addOnFailureListener { e ->
                Log.e("MainActivity", "Error loading other apps", e)
                loadDefaultOtherApps()
            }
    }

    private fun loadDefaultOtherApps() {
        val otherApps = listOf(
            OtherApp("1", "English Grammar", "Master English grammar", android.R.drawable.ic_menu_edit, 4.6, "50K+",
                "com.example.englishgrammar", "https://play.google.com/store/apps/details?id=com.example.englishgrammar"),
            OtherApp("2", "Math Solver Pro", "Solve complex math problems", android.R.drawable.ic_menu_compass, 4.7, "100K+",
                "com.example.mathsolver", "https://play.google.com/store/apps/details?id=com.example.mathsolver")
        )

        otherAppAdapter = OtherAppAdapter(otherApps)
        binding.otherAppsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = otherAppAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupClickListeners() {
        // Header Icons
        binding.ivProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.ivNotification.setOnClickListener {
            showToast("Notifications coming soon!")
        }

        // View Developer Profile Button
        binding.btnViewDeveloperProfile.setOnClickListener {
            try {
                val intent = Intent(this, DeveloperProfileActivity::class.java)
                intent.putExtra("developer_name", developerName)
                intent.putExtra("developer_title", developerTitle)
                intent.putExtra("developer_tagline", developerTagline)
                intent.putExtra("developer_bio", developerBio)
                intent.putExtra("developer_email", developerEmail)
                intent.putExtra("developer_phone", developerPhone)
                intent.putExtra("developer_location", developerLocation)
                intent.putExtra("developer_experience", developerExperience)
                intent.putExtra("developer_github", developerGithub)
                intent.putExtra("developer_linkedin", developerLinkedin)
                intent.putExtra("developer_portfolio", developerPortfolio)
                intent.putExtra("developer_education", developerEducation)
                startActivity(intent)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error opening developer profile", e)
                showToast("Developer profile feature coming soon!")
            }
        }

        // Feature Cards - Now routing through SubjectsActivity
        binding.cardPdfNotes.setOnClickListener {
            openSubjectsActivity("pdfnotes")
        }

        binding.cardBooks.setOnClickListener {
            openSubjectsActivity("books")
        }

        binding.cardVideos.setOnClickListener {
            openSubjectsActivity("videos")
        }

        binding.cardPreviousPapers.setOnClickListener {
            openSubjectsActivity("pyqs")
        }

        binding.cardSyllabus.setOnClickListener {
            openSubjectsActivity("syllabus")
        }

        binding.cardQuiz.setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
        }

        binding.cardJetTaiyari.setOnClickListener {
            startActivity(Intent(this, JetTaiyariActivity::class.java))
        }

        binding.cardPrintNotes.setOnClickListener {
            showToast("Print feature coming soon!")
        }

        binding.cardShareApp.setOnClickListener {
            shareApp()
        }

        binding.btnLogout.setOnClickListener {
            showLogoutDialog()
        }
    }

    // NEW METHOD: Opens SubjectsActivity with content type
    private fun openSubjectsActivity(contentType: String) {
        val intent = Intent(this, SubjectsActivity::class.java)
        intent.putExtra("content_type", contentType)
        startActivity(intent)
    }

    private fun shareApp() {
        val shareText = """
            📚 Check out AGJet Notes App! 
            
            Complete study materials for Class 12th and JET preparation.
            
            Download now!
        """.trimIndent()

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share App via"))
    }

    private fun showLogoutDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                showToast("Logged out successfully")
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun startAnimations() {
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        binding.headerSection.startAnimation(fadeIn)

        binding.bannerCardContainer.postDelayed({
            val slideUp = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
            binding.bannerCardContainer.startAnimation(slideUp)
        }, 200)

        val cards = listOf(
            binding.cardPdfNotes, binding.cardBooks, binding.cardVideos,
            binding.cardPreviousPapers, binding.cardQuiz, binding.cardSyllabus,
            binding.cardJetTaiyari, binding.cardPrintNotes, binding.cardShareApp
        )

        cards.forEachIndexed { index, card ->
            card.postDelayed({
                card.alpha = 0f
                card.translationY = 50f
                card.animate().alpha(1f).translationY(0f).setDuration(400).start()
            }, (index * 80).toLong())
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}