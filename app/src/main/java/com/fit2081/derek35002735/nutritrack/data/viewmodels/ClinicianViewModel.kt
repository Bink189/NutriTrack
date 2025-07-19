package com.fit2081.derek35002735.nutritrack.data.viewmodels
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fit2081.derek35002735.nutritrack.data.NutriTrackRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClinicianViewModel(private val repository: NutriTrackRepository) : ViewModel() {

    //Define the state flows for average scores
    private val _averageMaleScore = MutableStateFlow(0.0)
    val averageMaleScore: StateFlow<Double> = _averageMaleScore

    private val _averageFemaleScore = MutableStateFlow(0.0)
    val averageFemaleScore: StateFlow<Double> = _averageFemaleScore

    //Initialise the ViewModel and calculate average scores
    init {
        calculateAverageScores()
    }

    //Function to calculate average scores depending on user gender
    private fun calculateAverageScores() {
        viewModelScope.launch {
            val maleScores = repository.getHeifaScoresByGender("Male")
            val femaleScores = repository.getHeifaScoresByGender("Female")

            _averageMaleScore.value = maleScores.averageOrNull() ?: 0.0
            _averageFemaleScore.value = femaleScores.averageOrNull() ?: 0.0
        }
    }

    //Extension function to calculate average or return null if the list is empty
    private fun List<Double>.averageOrNull(): Double? = if (isNotEmpty()) average() else null

    //Function to fetch data patterns using GenAIService
    fun fetchDataPatterns(onResult: (List<String>) -> Unit) {
        viewModelScope.launch {
            try {
                val dataset = repository.getDataset()
                val patterns = repository.findPatterns(dataset)
                onResult(patterns)
            } catch (e: Exception) {
                Log.e("ClinicianViewModel", "Error fetching data patterns: ${e.message}")
                onResult(emptyList())
            }
        }
    }
}

//Code was taken from https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-factories
//Factory class to create instances of ClinicianViewModel
class ClinicianViewModelFactory(
    private val repository: NutriTrackRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClinicianViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ClinicianViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}