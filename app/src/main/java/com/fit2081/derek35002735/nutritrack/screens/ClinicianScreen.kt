package com.fit2081.derek35002735.nutritrack.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import com.fit2081.derek35002735.nutritrack.data.viewmodels.ClinicianViewModel

@Composable
fun ClinicianScreen (navController: NavController, viewModel: ClinicianViewModel, userId: String){

    //Fetch the average HEIFA scores for both males and females
    val averageMaleScore by viewModel.averageMaleScore.collectAsState()
    val averageFemaleScore by viewModel.averageFemaleScore.collectAsState()
    //Fetch the insights for the dataset from the view model
    var insights by remember {mutableStateOf<List<String>>(emptyList())}
    var isLoading by remember { mutableStateOf(false)}

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Admin View") },
                navigationIcon = {
                    //Back button to navigate back to the settings screen
                    IconButton(onClick = { navController.navigate("settings/$userId") }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Display the average HEIFA scores for males and females
            Text("Average HEIFA Scores", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Male: ${"%.2f".format(averageMaleScore)}", style = MaterialTheme.typography.bodyLarge)
            Text("Female: ${"%.2f".format(averageFemaleScore)}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                //Fetch the data patterns from the view model
                isLoading = true
                viewModel.fetchDataPatterns {result ->
                    insights = result
                    isLoading = false
                }

            }) {
                Text("Find Data Patterns")
            }
            Spacer(modifier = Modifier.height(16.dp))

            //Display circular progress indicator while loading
            if(isLoading) {
                CircularProgressIndicator()
            } else if (insights.isNotEmpty()){
                Text("Key Data Patterns", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(insights){ pattern ->
                        Text("- $pattern", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }

}
