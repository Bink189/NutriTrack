package com.fit2081.derek35002735.nutritrack.data.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.fit2081.derek35002735.nutritrack.data.FoodIntake
import com.fit2081.derek35002735.nutritrack.data.NutriTrackDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FoodIntakeQuestionnaireViewModel : ViewModel() {

    //Define the state variables for the questionnaire
    private val _selectedCategories = MutableStateFlow<List<String>>(emptyList())
    val selectedCategories: StateFlow<List<String>> = _selectedCategories.asStateFlow()

    private val _mealTime = MutableStateFlow("")
    val mealTime: StateFlow<String> = _mealTime.asStateFlow()

    private val _sleepTime = MutableStateFlow("")
    val sleepTime: StateFlow<String> = _sleepTime.asStateFlow()

    private val _wakeUpTime = MutableStateFlow("")
    val wakeUpTime: StateFlow<String> = _wakeUpTime.asStateFlow()

    private val _selectedPersona = MutableStateFlow("")
    val selectedPersona: StateFlow<String> = _selectedPersona.asStateFlow()

    private val _isSaveEnabled = MutableStateFlow(false)
    val isSaveEnabled: StateFlow<Boolean> = _isSaveEnabled.asStateFlow()

    //Function to update the selected categories in the questionnaire
    fun updateSaveEnabled() {
        _isSaveEnabled.value =
            _selectedPersona.value.isNotEmpty() &&
                _mealTime.value.isNotEmpty() &&
                _sleepTime.value.isNotEmpty() &&
                _wakeUpTime.value.isNotEmpty() &&
                _selectedCategories.value.isNotEmpty() &&
                _mealTime.value != _sleepTime.value &&
                _mealTime.value != _wakeUpTime.value &&
                _sleepTime.value != _wakeUpTime.value
    }

    //Function to update the user selected categories for the UI
    fun updateSelectedCategories(categories: List<String>) {
        _selectedCategories.value = categories
        updateSaveEnabled()
    }

    //Function to update the user selected meal time for the UI
    fun updateMealTime(time: String) {
        _mealTime.value = time
        updateSaveEnabled()
    }
    //Function to update the user selected sleep time for the UI
    fun updateSleepTime(time: String) {
        _sleepTime.value = time
        updateSaveEnabled()
    }

    //Function to update the user selected wake up time for the UI
    fun updateWakeUpTime(time: String) {
        _wakeUpTime.value = time
        updateSaveEnabled()
    }

    //Function to update the user selected persona for the UI
    fun updateSelectedPersona(persona: String) {
        _selectedPersona.value = persona
        updateSaveEnabled()
    }

    //Function to save the food intake data to the database
    fun saveFoodIntake(
        context: Context,
        userId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val db = Room.databaseBuilder(
                context,
                NutriTrackDatabase::class.java,
                "nutritrack_db"
            ).build()

            //Check if the patient exists in the database
            val patientExists = db.patientDao().getPatientById(userId) != null
            if (patientExists) {
                val foodIntake = FoodIntake(
                    patientId = userId,
                    selectedCategories = _selectedCategories.value,
                    selectedPersona = _selectedPersona.value,
                    mealTime = _mealTime.value,
                    sleepTime = _sleepTime.value,
                    wakeUpTime = _wakeUpTime.value
                )
                //Insert the food intake data into the database if the patient exists
                db.foodIntakeDao().insertFoodIntake(foodIntake)
                withContext(Dispatchers.Main) { onSuccess() }
            } else {
                withContext(Dispatchers.Main) {
                    onError("Patient ID does not exist. Please register first.")
                }
            }
        }
    }
}