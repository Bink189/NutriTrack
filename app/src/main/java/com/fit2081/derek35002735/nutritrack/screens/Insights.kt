package com.fit2081.derek35002735.nutritrack.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fit2081.derek35002735.nutritrack.data.viewmodels.InsightsViewModel


@Composable
fun Insights(navController: NavController, viewModel: InsightsViewModel, userId: String) {
    val context = LocalContext.current
    ///Variable to hold the user data from the view model
    val userData by viewModel.userData.collectAsState()

    //Fetch the user data from the view model
    LaunchedEffect(userId) {
        viewModel.fetchUserData(userId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavigationBar(navController, userId) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Breakdown of Food Quality Score", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(16.dp))

            //Display the food quality score breakdown using progress bars
            userData?.let { data ->
                ProgressBarWithLabel(label = "Vegetables", progress = data.vegetableScore.toInt())
                ProgressBarWithLabel(label = "Fruits", progress = data.fruitScore.toInt())
                ProgressBarWithLabel(label = "Grains & Cereals", progress = data.grainsScore.toInt())
                ProgressBarWithLabel(label = "Whole Grains", progress = data.wholeGrainScore.toInt())
                ProgressBarWithLabel(label = "Meat and Alternatives", progress = data.meatScore.toInt())
                ProgressBarWithLabel(label = "Water", progress = data.waterScore.toInt())
                ProgressBarWithLabel(label = "Dairy", progress = data.dairyScore.toInt())
                ProgressBarWithLabel(label = "Unsaturated Fats", progress = data.unsaturatedFatScore.toInt())
                ProgressBarWithLabel(label = "Saturated Fats", progress = data.saturatedFatScore.toInt())
                ProgressBarWithLabel(label = "Sodium", progress = data.sodiumScore.toInt())
                ProgressBarWithLabel(label = "Sugar", progress = data.sugarScore.toInt())
                ProgressBarWithLabel(label = "Alcohol", progress = data.alcoholScore.toInt())
                ProgressBarWithLabel(label = "Discretionary Foods", progress = data.discretionaryScore.toInt())

                Spacer(modifier = Modifier.height(16.dp))
                Text("Total Score: ${data.heifaTotalScore}/100", style = MaterialTheme.typography.headlineMedium)
                LinearProgressIndicator(
                    progress = (data.heifaTotalScore / 100).toFloat().coerceIn(0f, 1f),
                    modifier = Modifier.fillMaxWidth()
                )
            } ?: run {
                Text("No data available")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                //Create an intent to share the food quality score
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "Check out my food quality score: ${userData?.heifaTotalScore}/100")
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share your score with"))
            }) {
                Text("Share with someone")
            }
            //Button that unlocks the nutricoach screen
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("nutricoach/$userId") }) {
                Text("Improve my diet")
            }
        }
    }
}

@Composable
//Function to display a progress bar with a label
fun ProgressBarWithLabel(label: String, progress: Int) {
    //Create a column to arrange the label and progress bar vertically
    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        //Row to arrange the label and progress value horizontally
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =  Arrangement.SpaceBetween
        ) {
            Text(label, style = MaterialTheme.typography.bodyMedium)
            //Display the progress as a percentage
            Text("$progress/10", style = MaterialTheme.typography.bodyMedium)
        }
        //Display the progress bar
        LinearProgressIndicator(progress = (progress / 10.0).toFloat().coerceIn(0f, 1f), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
    }
}