package com.fit2081.derek35002735.nutritrack.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.fit2081.derek35002735.nutritrack.data.viewmodels.LoginViewModel


@Composable
fun Login(navController: NavController, viewModel: LoginViewModel) {
    //State variables to hold the user input and error messages
    val userList = viewModel.userList.collectAsState()
    val selectedUserId by viewModel.selectedUserId.collectAsState()
    val password by viewModel.password.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    var expanded by remember { mutableStateOf(false) }



    Scaffold(
        //Top bar with back button to navigate back to the welcome screen
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Login") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("welcome") }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                //Dropdown menu to select user ID
                TextButton(onClick = { expanded = true }) {
                    Text(if (selectedUserId.isEmpty()) "Select User ID" else selectedUserId)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    userList.value.forEach { user ->
                        DropdownMenuItem(
                            text = { Text(user.userId) },
                            onClick = {
                                viewModel.updateSelectedUserId (user.userId)
                                expanded = false
                            }
                        )
                    }
                }
            }
            //Fields for user input
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = password,
                onValueChange = { viewModel.updatePassword(it)},
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                //Check if the user has completed the food intake
                viewModel.loginUser { userId, hasFoodIntake ->
                    //Navigate to the appropriate screen based on the food intake status
                    viewModel.saveSession(userId,if (hasFoodIntake) "home/$userId" else "foodIntake/$userId") //Save the user's session
                    if (hasFoodIntake) {
                        navController.navigate("home/$userId")
                    } else {

                        navController.navigate("foodIntake/$userId")
                    }
                }
            }) {
                Text("Login")
            }
            //Button to direct the user to the register screen
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("register") }) {
                Text("Register")
            }
            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}





