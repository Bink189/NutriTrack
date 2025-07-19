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

@Composable
fun ClinicianLogin(navController: NavController, userId: String) {
    //Place holder for clinician key input
    var clinicianKeyField by remember { mutableStateOf("") }
    val clinicianKey = "dollar-entry-apples" //Clinician Key
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Clinician Login") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("settings/$userId") }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()) {innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement =  Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = clinicianKeyField,
                onValueChange = { clinicianKeyField = it },
                label = { Text("Clinician Key") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions =  KeyboardOptions(
                    keyboardType = KeyboardType.Password
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                //Check if the clinician key is correct
                if (clinicianKeyField == clinicianKey) {
                    navController.navigate("clinician/$userId")
                } else {
                    errorMessage = "Invalid Clinician Key"
                }
            }) {
                Text("Login")
            }
            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,

                )
            }
        }
    }

}
