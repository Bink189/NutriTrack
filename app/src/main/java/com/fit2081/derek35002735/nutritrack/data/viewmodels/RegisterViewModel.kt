package com.fit2081.derek35002735.nutritrack

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

class RegisterViewModel(private val repository: NutriTrackRepository) : ViewModel() {

    //Define the state variables for user data
    private val _userList = MutableStateFlow<List<Patient>>(emptyList())
    val userList: StateFlow<List<Patient>> = _userList.asStateFlow()

    private val _selectedUserId = MutableStateFlow("")
    val selectedUserId: StateFlow<String> = _selectedUserId.asStateFlow()

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow("")
    val successMessage: StateFlow<String> = _successMessage.asStateFlow()

    //Initialize the ViewModel and fetch users
    init {
        fetchUsers()
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

    //Function to update the selected user ID for the UI
    fun updateSelectedUserId(userId: String) {
        _selectedUserId.value = userId
    }

    //Function to update the user's entered phone number for the UI
    fun updatePhoneNumber(phone: String) {
        _phoneNumber.value = phone
    }

    //Function to update the user's entered password for the UI
    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    //Function to update the user's entered username for the UI
    fun updateUserName(name: String) {
        _userName.value = name
    }

    //Function to update the displayed error message
    fun updateErrorMessage(message: String) {
        _errorMessage.value = message
    }

    //Function to register a new user
    fun registerUser() {
        viewModelScope.launch {
            try {
                //Check if the selected user ID and phone number match
                val user = repository.getPatientById(_selectedUserId.value)
                if (user != null && user.phoneNumber == _phoneNumber.value) {
                    //Update the user details with their name and password
                    user.password = _password.value
                    user.name = _userName.value
                    repository.updatePatient(user)
                    _successMessage.value = "Registration successful!"
                    _errorMessage.value = ""
                } else {
                    _errorMessage.value = "Invalid User ID or Phone Number"
                    _successMessage.value = ""
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"
                _successMessage.value = ""
            }
        }
    }
}
//Code was taken from https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-factories
//ViewModelFactory to create instances of the viewmodel
class RegisterViewModelFactory(private val repository: NutriTrackRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}