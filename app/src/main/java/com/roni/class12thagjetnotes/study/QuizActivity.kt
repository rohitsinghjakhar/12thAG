package com.roni.class12thagjetnotes.study

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.adapter.QuizAdapter
import com.roni.class12thagjetnotes.databinding.ActivityQuizBinding
import com.roni.class12thagjetnotes.models.quiz.Quiz

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private lateinit var firestore: FirebaseFirestore
    private val quizList = mutableListOf<Quiz>()
    private lateinit var adapter: QuizAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        setupUI()
        setupRecyclerView()
        loadQuizzes()
        startAnimations()
    }

    private fun setupUI() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        adapter = QuizAdapter(quizList) { quiz ->
            startQuiz(quiz)
        }

        binding.rvQuizzes.layoutManager = LinearLayoutManager(this)
        binding.rvQuizzes.adapter = adapter
    }

    private fun loadQuizzes() {
        binding.progressBar.visibility = View.VISIBLE

        firestore.collection("quizzes")
            .get()
            .addOnSuccessListener { documents ->
                quizList.clear()
                for (document in documents) {
                    val quiz = document.toObject(Quiz::class.java)
                    quizList.add(quiz)
                }

                if (quizList.isEmpty()) {
                    quizList.addAll(getSampleQuizzes())
                }

                adapter.notifyDataSetChanged()
                binding.progressBar.visibility = View.GONE
                binding.tvTotalQuizzes.text = "${quizList.size} Quizzes Available"
            }
            .addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                quizList.addAll(getSampleQuizzes())
                adapter.notifyDataSetChanged()
                binding.tvTotalQuizzes.text = "${quizList.size} Quizzes Available"
            }
    }

    private fun getSampleQuizzes(): List<Quiz> {
        return listOf(
            Quiz(
                id = "1",
                title = "Mathematics - Full Syllabus Test",
                subject = "Mathematics",
                description = "Complete mathematics test for JET preparation",
                totalQuestions = 100,
                duration = 180,
                maxMarks = 400,
                difficulty = "Hard",
                attempts = 1250
            ),
            Quiz(
                id = "2",
                title = "Physics - Quick Test",
                subject = "Physics",
                description = "30 minutes quick test on important topics",
                totalQuestions = 30,
                duration = 30,
                maxMarks = 120,
                difficulty = "Medium",
                attempts = 890
            ),
            Quiz(
                id = "3",
                title = "Chemistry - Organic Chemistry",
                subject = "Chemistry",
                description = "Test your organic chemistry knowledge",
                totalQuestions = 50,
                duration = 60,
                maxMarks = 200,
                difficulty = "Medium",
                attempts = 1100
            ),
            Quiz(
                id = "4",
                title = "JET Mock Test - Full Paper",
                subject = "All Subjects",
                description = "Complete mock test as per JET pattern",
                totalQuestions = 200,
                duration = 180,
                maxMarks = 800,
                difficulty = "Hard",
                attempts = 2300
            )
        )
    }

    private fun startQuiz(quiz: Quiz) {
        // TODO: Navigate to quiz screen
        Toast.makeText(this, "Starting: ${quiz.title}", Toast.LENGTH_SHORT).show()
    }

    private fun startAnimations() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)

        binding.headerLayout.startAnimation(fadeIn)
        binding.rvQuizzes.startAnimation(slideUp)
    }
}