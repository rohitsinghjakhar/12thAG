package com.roni.class12thagjetnotes.students

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.databinding.ActivityStudentDeskBinding
import com.roni.class12thagjetnotes.students.fragments.ClassesListFragment


class StudentDeskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentDeskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDeskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load the initial fragment - Classes List
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ClassesListFragment())
                .commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}