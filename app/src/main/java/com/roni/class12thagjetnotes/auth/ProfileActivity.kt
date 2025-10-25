package com.roni.class12thagjetnotes.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.roni.class12thagjetnotes.databinding.ActivityProfileBinding
import com.roni.class12thagjetnotes.models.UserProfile

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private var selectedImageUri: Uri? = null
    private var currentPhotoUrl: String = ""

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            selectedImageUri = result.data?.data
            selectedImageUri?.let { uri ->
                Glide.with(this)
                    .load(uri)
                    .circleCrop()
                    .into(binding.ivProfilePicture)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        setupClickListeners()
        loadUserProfile()
    }

    private fun setupClickListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnEditPhoto.setOnClickListener {
            openImagePicker()
        }

        binding.btnSaveProfile.setOnClickListener {
            saveProfile()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    private fun loadUserProfile() {
        val user = auth.currentUser ?: return

        showLoading(true)

        // Set email (read-only)
        binding.etEmail.setText(user.email)

        // Load profile from Firestore
        firestore.collection("users")
            .document(user.uid)
            .get()
            .addOnSuccessListener { document ->
                showLoading(false)

                if (document.exists()) {
                    val profile = document.toObject(UserProfile::class.java)
                    profile?.let {
                        binding.etFullName.setText(it.fullName)
                        binding.etPhone.setText(it.phone)
                        binding.etStudentId.setText(it.studentId)
                        binding.etDepartment.setText(it.department)
                        if (it.yearOfStudy > 0) {
                            binding.etYear.setText(it.yearOfStudy.toString())
                        }
                        binding.etAddress.setText(it.address)

                        // Load profile photo
                        if (it.photoUrl.isNotEmpty()) {
                            currentPhotoUrl = it.photoUrl
                            loadProfilePhoto(it.photoUrl)
                        }
                    }
                } else {
                    // New user - pre-fill with auth data
                    binding.etFullName.setText(user.displayName ?: "")
                }
            }
            .addOnFailureListener { exception ->
                showLoading(false)
                Toast.makeText(
                    this,
                    "Failed to load profile: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun loadProfilePhoto(photoUrl: String) {
        if (photoUrl.startsWith("gs://") || photoUrl.startsWith("users/")) {
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
                        .into(binding.ivProfilePicture)
                }
                .addOnFailureListener {
                    // Keep default icon - no action needed
                }
        } else if (photoUrl.startsWith("http")) {
            Glide.with(this)
                .load(photoUrl)
                .circleCrop()
                .into(binding.ivProfilePicture)
        }
    }

    private fun saveProfile() {
        val user = auth.currentUser ?: return

        // Get form data
        val fullName = binding.etFullName.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val studentId = binding.etStudentId.text.toString().trim()
        val department = binding.etDepartment.text.toString().trim()
        val yearStr = binding.etYear.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()

        // Validation
        if (fullName.isEmpty()) {
            binding.nameInputLayout.error = "Please enter your name"
            binding.etFullName.requestFocus()
            return
        }

        binding.nameInputLayout.error = null

        showLoading(true)

        // Upload photo first if selected
        if (selectedImageUri != null) {
            uploadProfilePhoto(user.uid) { photoUrl ->
                saveUserData(user.uid, fullName, phone, studentId, department, yearStr, address, photoUrl)
            }
        } else {
            saveUserData(user.uid, fullName, phone, studentId, department, yearStr, address, currentPhotoUrl)
        }
    }

    private fun uploadProfilePhoto(userId: String, onSuccess: (String) -> Unit) {
        selectedImageUri?.let { uri ->
            val photoRef = storage.reference
                .child("users/$userId/profile.jpg")

            photoRef.putFile(uri)
                .addOnSuccessListener {
                    val photoPath = "users/$userId/profile.jpg"
                    onSuccess(photoPath)
                }
                .addOnFailureListener { exception ->
                    showLoading(false)
                    Toast.makeText(
                        this,
                        "Failed to upload photo: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun saveUserData(
        userId: String,
        fullName: String,
        phone: String,
        studentId: String,
        department: String,
        yearStr: String,
        address: String,
        photoUrl: String
    ) {
        val year = yearStr.toIntOrNull() ?: 0

        // Create user profile object using FieldValue.serverTimestamp()
        val userProfile = hashMapOf(
            "uid" to userId,
            "fullName" to fullName,
            "email" to (auth.currentUser?.email ?: ""),
            "phone" to phone,
            "studentId" to studentId,
            "department" to department,
            "yearOfStudy" to year,
            "address" to address,
            "photoUrl" to photoUrl,
            "updatedAt" to FieldValue.serverTimestamp()
        )

        // Save to Firestore
        firestore.collection("users")
            .document(userId)
            .set(userProfile)
            .addOnSuccessListener {
                // Update Firebase Auth display name
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(fullName)
                    .build()

                auth.currentUser?.updateProfile(profileUpdates)
                    ?.addOnCompleteListener { task ->
                        showLoading(false)
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Profile saved successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Profile saved, but display name update failed",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    }
            }
            .addOnFailureListener { exception ->
                showLoading(false)
                Toast.makeText(
                    this,
                    "Failed to save profile: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun showLoading(show: Boolean) {
        binding.progressOverlay.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnSaveProfile.isEnabled = !show
    }
}