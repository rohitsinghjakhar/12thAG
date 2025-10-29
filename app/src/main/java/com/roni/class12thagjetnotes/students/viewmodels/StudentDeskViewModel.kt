package com.roni.class12thagjetnotes.students.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
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

    // ✅ FIX: Use a Map to cache content by (chapterId + type) to prevent conflicts
    private val contentCache = mutableMapOf<String, MutableLiveData<List<Content>>>()

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
        if (!_classes.value.isNullOrEmpty()) return
        viewModelScope.launch {
            safeLoad {
                _classes.value = repository.getClasses()
            }
        }
    }

    fun loadSubjects(classId: String, appContext: android.content.Context) {
        if (!_subjects.value.isNullOrEmpty()) return
        viewModelScope.launch {
            safeLoad {
                val subjectsList = repository.getSubjects(classId)
                _subjects.value = subjectsList

                // ✅ Preload all subject icons into Glide cache
                subjectsList.forEach { subject ->
                    Glide.with(appContext)
                        .load(subject.icon)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .preload()
                }
            }
        }
    }

    fun loadMediums(subjectId: String) {
        if (!_mediums.value.isNullOrEmpty()) return
        viewModelScope.launch {
            safeLoad { _mediums.value = repository.getMediums(subjectId) }
        }
    }

    fun loadChapters(mediumId: String) {
        if (!_chapters.value.isNullOrEmpty()) return
        viewModelScope.launch {
            safeLoad { _chapters.value = repository.getChapters(mediumId) }
        }
    }

    // ✅ FIX: Return LiveData specific to this chapter and content type
    fun getContentLiveData(chapterId: String, contentType: String): LiveData<List<Content>> {
        val cacheKey = "$chapterId:$contentType"

        // Return existing LiveData if already created
        if (contentCache.containsKey(cacheKey)) {
            return contentCache[cacheKey]!!
        }

        // Create new LiveData for this chapter+type combination
        val liveData = MutableLiveData<List<Content>>()
        contentCache[cacheKey] = liveData

        // Load content
        viewModelScope.launch {
            try {
                _loading.postValue(true)
                _error.postValue(null)
                val contentList = repository.getContent(chapterId, contentType)
                liveData.postValue(contentList)
                Log.d(TAG, "Loaded ${contentList.size} items for $cacheKey")
            } catch (e: Exception) {
                Log.e(TAG, "Error loading content for $cacheKey", e)
                _error.postValue("Error: ${e.message}")
                liveData.postValue(emptyList())
            } finally {
                _loading.postValue(false)
            }
        }

        return liveData
    }

    fun loadQuizzes(chapterId: String) {
        if (!_quizzes.value.isNullOrEmpty()) return
        viewModelScope.launch {
            safeLoad { _quizzes.value = repository.getQuizzes(chapterId) }
        }
    }

    fun loadQuestions(quizId: String) {
        viewModelScope.launch {
            safeLoad { _questions.value = repository.getQuestions(quizId) }
        }
    }

    suspend fun saveQuizResult(result: QuizResult): Boolean {
        return repository.saveQuizResult(result)
    }

    // ✅ Common function for error handling and loading state
    private suspend fun safeLoad(block: suspend () -> Unit) {
        try {
            _loading.postValue(true)
            _error.postValue(null)
            block()
        } catch (e: Exception) {
            Log.e(TAG, "Data load error", e)
            _error.postValue("Error: ${e.message}")
        } finally {
            _loading.postValue(false)
        }
    }

    // ✅ Optional: Clear content cache when needed (e.g., when leaving chapter)
    fun clearContentCache() {
        contentCache.clear()
    }
}