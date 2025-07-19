 package com.fit2081.derek35002735.nutritrack.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.Image
import com.fit2081.derek35002735.nutritrack.R


 @Composable
fun WelcomeScreen(navController: NavController) {
    //Scaffold provides basic structure for welcome screen
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding->
        //Column is used to arrange the elements vertically
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Title and logo
            Text("NutriTrack", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.nutritracklogo),
                contentDescription = "NutriTrack Logo",
                modifier = Modifier.size(200.dp)
            )
            //App description and disclaimer
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "This app provides general health and nutrition information for educational purposes only. It is not intended as medical advice, diagnosis, or treatment.\n"+ "" +
                        "Always consult a qualified healthcare professional before making any changes to your diet, exercise, or health regimen.\n" +
                        "Use this app at your own risk.If you’d like to an Accredited Practicing Dietitian (APD), please visit the Monash Nutrition/Dietetics Clinic (discounted rates for students):\n" +
                        "https://www.monash.edu/medicine/scs/nutrition/clinics/nutrition"
            )
            Spacer(modifier = Modifier.height(16.dp))
            //Login button
            Button(onClick = {
                Log.d("WelcomeScreen", "Navigating to login")
                //Navigate to login screen
                navController.navigate("login") }) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Designed by Derek Chew (35002735)", textAlign = TextAlign.Center)
        }
    }
}