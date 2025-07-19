package com.fit2081.derek35002735.nutritrack.data.viewmodels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fit2081.derek35002735.nutritrack.data.NutriTrackRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class NutriCoachViewModel(private val repository: NutriTrackRepository): ViewModel() {

    //Define the state flows for fruit facts, score, and motivational message
    private val _fruitFacts = MutableStateFlow<List<String>>(emptyList())
    val fruitFacts: StateFlow<List<String>> get() = _fruitFacts

    private val _fruitScore = MutableStateFlow(0.0)
    val fruitScore: StateFlow<Double> get() = _fruitScore

    private val _motivationalMessage = MutableStateFlow("")
    val motivationalMessage: StateFlow<String> get() = _motivationalMessage

    //Function to fetch the fruit score based on user ID
    fun fetchFruitScore(userId: String) {
        viewModelScope.launch {
            try {
                val score = repository.getFruitScore(userId)
                _fruitScore.value = score
            } catch (e: Exception) {
                _fruitScore.value = 0.0
                e.printStackTrace()
            }
        }
    }

    //Function to fetch fruit facts using FruityViceApi
    fun fetchFruitFacts(fruitName: String) {
        viewModelScope.launch {
            _fruitFacts.value = listOf("Fetching fruit facts...")
            try {
                val response = repository.getFruitFacts(fruitName)
                //Check if the response is null or empty
                _fruitFacts.value = listOf(
                    "Name: ${response?.name ?: "N/A"}",
                    "Family: ${response?.family ?: "N/A"}",
                    "Genus: ${response?.genus ?: "N/A"}",
                    "Order: ${response?.order ?: "N/A"}",
                    "Calories: ${response?.nutritions?.calories ?: "N/A"}",
                    "Carbohydrates: ${response?.nutritions?.carbohydrates ?: "N/A"}",
                    "Protein: ${response?.nutritions?.protein ?: "N/A"}",
                    "Fat: ${response?.nutritions?.fat ?: "N/A"}",
                    "Sugar: ${response?.nutritions?.sugar ?: "N/A"}"
                )
            } catch (e: Exception) {
                _fruitFacts.value = listOf("Error fetching fruit facts")
                e.printStackTrace()

            }
        }
    }

    //Function to generate a motivational message using GenAIService
    fun generateMotivationalMessage(userId: String) {
        viewModelScope.launch {
            _motivationalMessage.value = "Generating motivational message..."
                try {
                    //Fetch the patient data and food intake data
                    val patient = repository.getPatientById(userId )
                    val foodIntake = repository.getFoodIntakeByPatientId(userId).firstOrNull()

                    val prompt =  """Generate a short motivational message based on the following user data:$patient Food Intake:$foodIntake""".trimIndent()
                    //Call the GenAIService to generate a message by passing the prompt
                    val message = repository.generateMessage(
                        prompt
                    )
                    _motivationalMessage.value = message
                } catch (e: Exception) {
                    _motivationalMessage.value = "Error generating message"
                }

        }
    }
}
//Code was taken from https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-factories
//ViewModelFactory to create instances of the viewmodel
class NutriCoachViewModelFactory(
    private val repository: NutriTrackRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NutriCoachViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NutriCoachViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}