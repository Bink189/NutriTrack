package com.fit2081.derek35002735.nutritrack.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fit2081.derek35002735.nutritrack.R
import com.fit2081.derek35002735.nutritrack.data.viewmodels.NutriCoachViewModel

@Composable
fun NutriCoachScreen(navController: NavController, viewModel: NutriCoachViewModel = viewModel(), userId: String) {

    //Fetch the fruit score for the user
    LaunchedEffect(userId) {
        viewModel.fetchFruitScore(userId)
    }

    //Variables to hold the fruit facts and motivational message from the view model
    var fruitName by remember { mutableStateOf("") }
    val fruitFacts by viewModel.fruitFacts.collectAsState()
    val motivationalMessage by viewModel.motivationalMessage.collectAsState()
    val fruitScore by viewModel.fruitScore.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(navController, userId) //Add a bottom navigation bar
        }
    ) { innerPadding ->
        Column(modifier= Modifier.fillMaxSize()) {
            Box (
                modifier =Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Column (modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally)
                {
                    Text("NutriCoach", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(8.dp) )

                    //If the fruit score is not optimal, display the fruit facts and input field
                    if(fruitScore < 10.0) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)

                        ) {
                            Text("Fruit Name", style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, MaterialTheme.colorScheme.primary)
                                    .padding(8.dp)
                            ) {
                                BasicTextField(
                                    value = fruitName,
                                    onValueChange = { fruitName = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )

                            }

                        }
                        //Button to fetch fruit facts from the API
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {viewModel.fetchFruitFacts((fruitName))}) {
                            Text("Get Fruit Facts")
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)

                        ) {
                            //Click to scroll through fruit info when displayed
                            items(fruitFacts.size) { index ->
                                Text(
                                    fruitFacts[index],
                                    style = MaterialTheme.typography.bodyMedium

                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }

                    } else {
                        //Display an image of a fruit if the fruit score is optimal
                        Image(
                            painter = painterResource(id = R.drawable.fruits),
                            contentDescription = "Fruits",
                            modifier = Modifier.size(200.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Good Job, your fruit score is optimal! Keep it up!",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                }
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp), thickness = 2.dp

            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Motivational Message", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    //Button to generate a motivational message by sending a prompt to the API
                    Button(onClick = {viewModel.generateMotivationalMessage(userId)}) {
                        Text("Get Motivational Message")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(motivationalMessage, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }


}