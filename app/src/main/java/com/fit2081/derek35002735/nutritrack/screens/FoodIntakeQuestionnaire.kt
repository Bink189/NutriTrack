package com.fit2081.derek35002735.nutritrack.screens
import android.app.AlertDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import android.app.TimePickerDialog
import androidx.compose.foundation.Image
import java.util.Calendar
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.ui.res.painterResource
import com.fit2081.derek35002735.nutritrack.R
import com.fit2081.derek35002735.nutritrack.data.viewmodels.FoodIntakeQuestionnaireViewModel

@Composable
fun FoodIntakeQuestionnaire(navController: NavController, userId: String, viewModel: FoodIntakeQuestionnaireViewModel) {
    //State variables to hold user input
    val selectedCategories by viewModel.selectedCategories.collectAsState()
    val mealTime by viewModel.mealTime.collectAsState()
    val sleepTime by viewModel.sleepTime.collectAsState()
    val wakeUpTime by viewModel.wakeUpTime.collectAsState()
    val selectedPersona by viewModel.selectedPersona.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var showModal by remember {mutableStateOf(false)}
    var modalDesc by remember { mutableStateOf("") }
    var modalImageRes by remember { mutableStateOf(0) }
    val isSaveEnabled by viewModel.isSaveEnabled.collectAsState()  //Flag to check if user has made selections before being able to click save
    var errorMessage by remember { mutableStateOf("") }

    //List for different food categories
    val foodCategories = listOf("Fruits", "Vegetables", "Grains", "Red Meat", "Seafood", "Poultry", "Fish", "Eggs", "Nuts/Seeds")
    //List of health personas
    val personas = listOf(
        "Health Devotee",
        "Mindful Eater",
        "Wellness Striver",
        "Balance Seeker",
        "Health Procrastrinator",
        "Food Carefree"
    )

    //Map for key-value pairs, each persona (key) is given a description (value)
    val personaDesc = mapOf(
        "Health Devotee" to "I’m passionate about healthy eating & health plays a big part in my life. I use social media to follow active lifestyle personalities or get new recipes/exercise ideas. I may even buy superfoods or follow a particular type of diet. I like to think I am super healthy.\n",
        "Mindful Eater" to "I’m health-conscious and being healthy and eating healthy is important to me. Although health means different things to different people, I make conscious lifestyle decisions about eating based on what I believe healthy means. I look for new recipes and healthy eating information on social media.\n",
        "Wellness Striver" to "I aspire to be healthy (but struggle sometimes). Healthy eating is hard work! I’ve tried to improve my diet, but always find things that make it difficult to stick with the changes. Sometimes I notice recipe ideas or healthy eating hacks, and if it seems easy enough, I’ll give it a go.\n",
        "Balance Seeker" to "I try and live a balanced lifestyle, and I think that all foods are okay in moderation. I shouldn’t have to feel guilty about eating a piece of cake now and again. I get all sorts of inspiration from social media like finding out about new restaurants, fun recipes and sometimes healthy eating tips.\n",
        "Health Procrastrinator" to "I’m contemplating healthy eating but it’s not a priority for me right now. I know the basics about what it means to be healthy, but it doesn’t seem relevant to me right now. I have taken a few steps to be healthier but I am not motivated to make it a high priority because I have too many other things going on in my life.\n",
        "Food Carefree" to "I’m not bothered about healthy eating. I don’t really see the point and I don’t think about it. I don’t really notice healthy eating tips or recipes and I don’t care what I eat."
    )

    //Map each persona to an image
    val personaImages = mapOf(
        "Health Devotee" to R.drawable.persona_1,
        "Mindful Eater" to R.drawable.persona_2,
        "Wellness Striver" to R.drawable.persona_3,
        "Balance Seeker" to R.drawable.persona_4,
        "Health Procrastrinator" to R.drawable.persona_5,
        "Food Carefree" to R.drawable.persona_6
    )

    //Get current context
    val context = LocalContext.current



    //Enable save button if user has filled in all fields
    LaunchedEffect(selectedPersona, mealTime, sleepTime, wakeUpTime, selectedCategories) {
        viewModel.updateSaveEnabled()
    }

    //Validation to ensure the three times are not the same
    LaunchedEffect(mealTime, sleepTime, wakeUpTime) {
        errorMessage = when {
            mealTime.isNotEmpty() && sleepTime.isNotEmpty() && mealTime == sleepTime -> "Meal time and sleep time cannot be the same."
            mealTime.isNotEmpty() && wakeUpTime.isNotEmpty() && mealTime == wakeUpTime -> "Meal time and wake-up time cannot be the same."
            sleepTime.isNotEmpty() && wakeUpTime.isNotEmpty() && sleepTime == wakeUpTime -> "Sleep time and wake-up time cannot be the same."
            else -> ""
        }
        viewModel.updateSaveEnabled()
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Food Intake Questionnaire", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(20.dp))
            //Prompt to select food categories
            Text("Select the food categories you consume regularly")
            //LazyVerticalGrid to display items in a vertically scrollable container
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp)
            ) {
                //Iterate over food categories list
                items(foodCategories.size) { index ->
                    val category = foodCategories[index]
                   Row(verticalAlignment = Alignment.CenterVertically) {
                       //Checkbox to allow user to select a category
                        Checkbox(
                            //checked parameter is set as true if selectedCategories list contains the current category
                            checked = selectedCategories.contains(category),
                            //Lambda function to update the list based on if the checkbox is checked
                            onCheckedChange = {
                                if (it) {
                                    viewModel.updateSelectedCategories(selectedCategories + category)
                                } else {
                                    viewModel.updateSelectedCategories(selectedCategories - category)
                                }
                            }
                        )
                       //Text composable to display the name of the category
                        Text(category)
                    }

                }

            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp), thickness = 2.dp

            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Select a persona to view its description")
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp)

            ) {
                //Iterate over persona list and create a composable button for each persona
                items(personas.size) { index ->
                    val persona = personas[index]
                    //Description and image are displayed for a persona if the user clicks a button
                    Button(onClick =  {
                        modalDesc = personaDesc[persona] ?: ""
                        modalImageRes = personaImages[persona] ?: 0
                        showModal = true //Flag set to true to display modal dialog
                    }) {
                        Text(persona)
                    }
                }

            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Which persona best fits you?")
            Box {
                TextButton(onClick = { expanded = true }) {
                    Text(if (selectedPersona.isEmpty()) "Select Persona" else selectedPersona)
                }
                //Dropdown menu for persona selection
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    //Iterate over each persona and create a dropdown menu item for each
                    personas.forEach { persona ->
                        DropdownMenuItem(
                            text = { Text(persona) },
                            onClick = {
                                viewModel.updateSelectedPersona(persona) //Update state variable for selected persona
                                expanded = false
                            }
                        )
                    }
                }

            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp), thickness = 2.dp

            )
            Spacer(modifier = Modifier.height(16.dp))
            //Time Picker prompt to enter meal, sleep and wake up times
            Text("Timings")
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()){
                Text("What time of the day do you eat your biggest meal?", modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                TimePickerField(label = "Meal Time", time = mealTime) {viewModel.updateMealTime(it)}

            }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()){
                Text("When do you sleep at night?", modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                TimePickerField(label = "Sleep Time", time = sleepTime) {viewModel.updateSleepTime(it) }

            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()){
                Text("What time do you wake up?", modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                TimePickerField(label = "Wake Up Time", time = wakeUpTime) {viewModel.updateWakeUpTime(it) }


            }
            Spacer(modifier = Modifier.height(16.dp))

            //Display error message if any of the validation checks fail
            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }
            //Save button to save the user's selected options to shared preferences
            Button(onClick = {
                viewModel.saveFoodIntake(
                    context = context,
                    userId = userId,
                    onSuccess = { navController.navigate("home/$userId") },
                    onError = { message ->
                        AlertDialog.Builder(context)
                            .setTitle("Error")
                            .setMessage(message)
                            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                            .show()
                    }
                )
            }, enabled = isSaveEnabled) {
                Text("Save")
            }

            }
        }
    //Modal dialog to display persona description
    if (showModal) {
        AlertDialog (
            onDismissRequest = {showModal = false},
            title = { Text("Persona Description") },
            text = {
                Column {
                    Image(
                        painter = painterResource(id = modalImageRes),
                        contentDescription = "Persona Image",
                        modifier = Modifier.size(100.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(modalDesc)
                }
            },
            confirmButton = {
                Button(onClick = {showModal = false}) {
                    Text("Close")
                }
            }
        )
    }
    }

//Function to display a time picker dialog
@Composable
fun TimePickerField(label: String, time: String, onTimeSelected: (String) -> Unit) {
    val context = LocalContext.current //Get current context
    //Get the current hour and minute
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    //Button to display the time picker
    Button(onClick = {
        TimePickerDialog(context, { _, selectedHour, selectedMinute ->
            onTimeSelected(String.format("%02d:%02d", selectedHour, selectedMinute)) //onTimeSelected callback is invoked wiith formatted time string
        }, hour, minute, true).show()
    }) {
        Text(if (time.isEmpty()) label else time)
    }
}
