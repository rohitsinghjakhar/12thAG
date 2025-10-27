package com.roni.class12thagjetnotes.students.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roni.class12thagjetnotes.students.models.*
import com.roni.class12thagjetnotes.students.repo.FirebaseRepository
import kotlinx.coroutines.launch

class StudentDeskViewModel : ViewModel() {

    private val repository = FirebaseRepository()

    private val _classes = MutableLiveData<List<ClassModel>>()
    val classes: LiveData<List<ClassModel>> = _classes

    private val _subjects = MutableLiveData<List<Subject>>()
    val subjects: LiveData<List<Subject>> = _subjects

    private val _mediums = MutableLiveData<List<Medium>>()
    val mediums: LiveData<List<Medium>> = _mediums

    private val _chapters = MutableLiveData<List<Chapter>>()
    val chapters: LiveData<List<Chapter>> = _chapters

    private val _content = MutableLiveData<List<Content>>()
    val content: LiveData<List<Content>> = _content

    private val _quizzes = MutableLiveData<List<Quiz>>()
    val quizzes: LiveData<List<Quiz>> = _quizzes

    private val _questions = MutableLiveData<List<Question>>()
    val questions: LiveData<List<Question>> = _questions

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    companion object {
        private const val TAG = "StudentDeskViewModel"
    }

    fun loadClasses() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                val classesList = repository.getClasses()
                _classes.value = classesList
            } catch (e: Exception) {
                Log.e(TAG, "Error loading classes", e)
                _error.value = "Failed to load classes: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadSubjects(classId: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                val subjectsList = repository.getSubjects(classId)
                _subjects.value = subjectsList
            } catch (e: Exception) {
                Log.e(TAG, "Error loading subjects", e)
                _error.value = "Failed to load subjects: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadMediums(subjectId: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                val mediumsList = repository.getMediums(subjectId)
                _mediums.value = mediumsList
            } catch (e: Exception) {
                Log.e(TAG, "Error loading mediums", e)
                _error.value = "Failed to load mediums: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadChapters(mediumId: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                val chaptersList = repository.getChapters(mediumId)
                _chapters.value = chaptersList
            } catch (e: Exception) {
                Log.e(TAG, "Error loading chapters", e)
                _error.value = "Failed to load chapters: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadContent(chapterId: String, type: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                val contentList = repository.getContent(chapterId, type)
                _content.value = contentList
            } catch (e: Exception) {
                Log.e(TAG, "Error loading content", e)
                _error.value = "Failed to load content: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadQuizzes(chapterId: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                val quizzesList = repository.getQuizzes(chapterId)
                _quizzes.value = quizzesList
            } catch (e: Exception) {
                Log.e(TAG, "Error loading quizzes", e)
                _error.value = "Failed to load quizzes: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadQuestions(quizId: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                val questionsList = repository.getQuestions(quizId)
                _questions.value = questionsList
            } catch (e: Exception) {
                Log.e(TAG, "Error loading questions", e)
                _error.value = "Failed to load questions: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    suspend fun saveQuizResult(result: QuizResult): Boolean {
        return repository.saveQuizResult(result)
    }
}