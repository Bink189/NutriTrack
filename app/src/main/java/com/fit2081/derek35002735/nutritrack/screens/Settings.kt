package com.fit2081.derek35002735.nutritrack.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fit2081.derek35002735.nutritrack.data.viewmodels.LoginViewModel


@Composable
fun SettingsScreen(navController: NavController, viewModel: LoginViewModel, userId: String) {
    //Variable to hold the user data from the view model
    val userData by viewModel.userData.collectAsState()

    //Fetch the user data from the view model
    LaunchedEffect(userId) {
        viewModel.fetchUserData(userId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(navController, userId)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            //Display user data
            userData?.let { user ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Account Details", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Name: ${user.name}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Phone: ${user.phoneNumber}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "ID: ${user.userId}", style = MaterialTheme.typography.bodyMedium)
                }
            } ?: Text("Loading user data...")

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                //Logout button to direct back to the login screen
                Button(
                    onClick = {
                        viewModel.clearSession()
                        navController.navigate("login")
                              },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Logout")
                }

                //Button to navigate to the clinician login screen
                Button(
                    onClick = { navController.navigate("clinicianLogin/$userId") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Clinician Login")
                }
            }
        }
    }
}