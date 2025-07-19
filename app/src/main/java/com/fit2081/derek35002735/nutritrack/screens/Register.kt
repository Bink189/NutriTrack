package com.fit2081.derek35002735.nutritrack.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fit2081.derek35002735.nutritrack.RegisterViewModel

@Composable
fun Register(navController: NavController, viewModel: RegisterViewModel) {

    //Variable to fetch all users from the view model
    val userList = viewModel.userList.collectAsState()
    //Variables to hold the user input from the view model
    val selectedUserId by viewModel.selectedUserId.collectAsState()
    val phoneNumber by viewModel.phoneNumber.collectAsState()
    val password by viewModel.password.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                TextButton(onClick = { expanded = true }) {
                    Text(if (selectedUserId.isEmpty()) "Select User ID" else selectedUserId)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    //Dropdown menu to select user ID
                    userList.value.forEach { user ->
                        DropdownMenuItem(
                            text = { Text(user.userId) },
                            onClick = {
                                viewModel.updateSelectedUserId(user.userId)
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = phoneNumber,
                onValueChange = { input ->
                    //Restrict input to digits only
                    if(input.all {it.isDigit()}) {
                        viewModel.updatePhoneNumber(input)
                    }
                                },
                label = { Text("Phone Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = userName,
                onValueChange = { viewModel.updateUserName(it) },
                label = { Text("User Name") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = password,
                onValueChange = { viewModel.updatePassword(it)},
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.height(16.dp))
            //Button to register the user
            Button(onClick = {
                //Minimum password length check
                if(password.length >= 4) {
                    viewModel.registerUser()
                } else {
                    viewModel.updateErrorMessage("Password must be at least 4 characters long")
                }
            }) {
                Text("Register")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("login") }) {
                Text("Back to Login")
            }
            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }
            if (successMessage.isNotEmpty()) {
                Text(successMessage, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}