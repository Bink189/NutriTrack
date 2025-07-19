package com.fit2081.derek35002735.nutritrack.data.viewmodels


import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
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

class LoginViewModel(private val repository: NutriTrackRepository, private val sharedPreferences: SharedPreferences) : ViewModel() {

    //Define state variables for user data
    private val _userList = MutableStateFlow<List<Patient>>(emptyList())
    val userList: StateFlow<List<Patient>> = _userList.asStateFlow()

    private val _selectedUserId = MutableStateFlow("")
    val selectedUserId: StateFlow<String> = _selectedUserId.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    private val _userData = MutableStateFlow<Patient?>(null)
    val userData: StateFlow<Patient?> = _userData.asStateFlow()

    //Initialize the ViewModel and fetch users
    init {
        fetchUsers()
    }

    //Function to save the user's session using shared preferences
    fun saveSession(userId: String, lastScreen: String) {
        viewModelScope.launch {
            sharedPreferences.edit().apply {
                putString("loggedInUserId", userId)
                putString("lastScreen", lastScreen)
                apply()
            }
        }
    }

    //Function to update the last screen in shared preferences
    fun updateLastScreen(lastScreen: String) {
        sharedPreferences.edit().apply() {
            putString("lastScreen", lastScreen)
            apply()
        }
    }

    //Function to clear the user's session
    fun clearSession() {
        sharedPreferences.edit().clear().apply()
    }

    //Function to retrieve the user's session from shared preferences
    fun getSession(): Pair<String?, String?> {
        val userId = sharedPreferences.getString("loggedInUserId", null)
        val lastScreen = sharedPreferences.getString("lastScreen", null)
        return Pair(userId, lastScreen)
    }

    //Function to fetch all users from the repository
    private fun fetchUsers() {
        viewModelScope.launch {
            try {
                val users = repository.getAllPatients()
                _userList.value = users
            } catch (e: Exception) {
                _errorMessage.value = "Failed to fetch users: ${e.message}"
            }
        }
    }

    //Function to fetch individual user data by ID
    fun fetchUserData(userId: String) {
        viewModelScope.launch {
            try {
                _userData.value = repository.getPatientById(userId)
            } catch (e: Exception) {
                _userData.value = null
            }
        }
    }

    //Function to update the selected user ID in the UI
    fun updateSelectedUserId(userId: String) {
        _selectedUserId.value = userId
    }

    //Function to update the user's password for the UI
    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    //Function to log in the user if their credentials are valid
    fun loginUser(onLoginSuccess: (String, Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val user = repository.getPatientById(selectedUserId.value)
                if (user != null && user.password == password.value) {
                    //Check if the user has food intake data
                    val hasFoodIntake = repository.hasFoodIntake(selectedUserId.value)
                    onLoginSuccess(selectedUserId.value, hasFoodIntake)
                } else {
                    _errorMessage.value = "Invalid User ID or Password"
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }
}
//Code was taken from https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-factories
//Factory class to create instances of the viewmodel
class LoginViewModelFactory(private val repository: NutriTrackRepository, private val sharedPreferences: SharedPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository, sharedPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}