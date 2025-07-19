package com.fit2081.derek35002735.nutritrack.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController


@Composable
//Function to create a bottom navigation bar
fun BottomNavigationBar(navController: NavController, userId: String) {
    BottomAppBar {
        //Navigation bar with items
        NavigationBar {
            NavigationBarItem(
                //Icon for the home screen
                icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                label = { Text("Home") },
                selected = false,
                onClick = { navController.navigate("home/$userId") } //Navigate to home screen
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Star, contentDescription = "Insights") },
                label = { Text("Insights") },
                selected = false,
                onClick = { navController.navigate("insights/$userId") } //Navigate to insights screen
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Settings, contentDescription = "Settngs") },
                label = { Text("Settings") },
                selected = false,
                onClick = {navController.navigate("settings/$userId")  } //Navigate to settings
            )

            NavigationBarItem(
                icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "NutriCoach") },
                label = { Text("NutriCoach") },
                selected = false,
                onClick = {navController.navigate("nutricoach/$userId")  } //Navigate to NutriCoach
            )

        }
    }
}