package com.fit2081.derek35002735.nutritrack.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import com.fit2081.derek35002735.nutritrack.R
import com.fit2081.derek35002735.nutritrack.data.viewmodels.HomeViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel, userId: String) {
    //Variables to hold the user data from the view model
    val userData by viewModel.userData.collectAsState()
    val userName = userData?.name ?: "User"
    val foodQualityScore = userData?.heifaTotalScore ?: 0.0

    //Fetch the user data from the view model when the screen is launched
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
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Hello,")
            Text(userName, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.balanceddiet),
                contentDescription = "Balanced Diet",
                modifier = Modifier.size(200.dp)
            )
            //Display the food quality score
            Spacer(modifier = Modifier.height(35.dp))
            Text("My Score", style = MaterialTheme.typography.headlineSmall)
            Text("Your food quality score: ${foodQualityScore}/100")
            Spacer(modifier = Modifier.height(40.dp))
            Text("What is the Food Quality Score?", style = MaterialTheme.typography.headlineSmall)
            Text("The Food Quality Score is a measure of how well your eating patterns align with established food guidelines, helping you identify both strengths and opportunities for improvements in your diet")
            Spacer(modifier = Modifier.height(16.dp))
            Text("This personalised measurement considers various food groups including vegetables, fruits, grains and dairy to give you practical insights for making healthier food choices")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("foodIntake/$userId") }) {
                Text("Edit")
            }
        }
    }
}


