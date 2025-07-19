package com.fit2081.derek35002735.nutritrack.data.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fit2081.derek35002735.nutritrack.data.NutriTrackRepository
import com.fit2081.derek35002735.nutritrack.data.Patient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: NutriTrackRepository) : ViewModel() {
    //Define the state variable for user data
    private val _userData = MutableStateFlow<Patient?>(null)
    val userData: StateFlow<Patient?> = _userData.asStateFlow()

    //Function to fetch user data by ID
    fun fetchUserData(userId: String) {
        viewModelScope.launch {
            try {
                _userData.value = repository.getPatientById(userId)
            } catch (e: Exception) {
                _userData.value = null
            }
        }
    }
}
//Code was taken from https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-factories
//ViewModelFactory to create instances of the viewmodel
class HomeViewModelFactory(private val repository: NutriTrackRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}