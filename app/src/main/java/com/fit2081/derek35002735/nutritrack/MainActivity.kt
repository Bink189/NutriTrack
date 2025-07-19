package com.fit2081.derek35002735.nutritrack

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fit2081.derek35002735.nutritrack.data.viewmodels.FoodIntakeQuestionnaireViewModel
import com.fit2081.derek35002735.nutritrack.data.viewmodels.HomeViewModel
import com.fit2081.derek35002735.nutritrack.data.viewmodels.HomeViewModelFactory
import com.fit2081.derek35002735.nutritrack.data.viewmodels.InsightsViewModel
import com.fit2081.derek35002735.nutritrack.data.viewmodels.InsightsViewModelFactory
import com.fit2081.derek35002735.nutritrack.data.viewmodels.LoginViewModel
import com.fit2081.derek35002735.nutritrack.data.viewmodels.LoginViewModelFactory
import com.fit2081.derek35002735.nutritrack.data.viewmodels.NutriCoachViewModel
import com.fit2081.derek35002735.nutritrack.data.NutriTrackDatabase
import com.fit2081.derek35002735.nutritrack.data.NutriTrackRepository
import com.fit2081.derek35002735.nutritrack.data.viewmodels.ClinicianViewModel
import com.fit2081.derek35002735.nutritrack.data.viewmodels.ClinicianViewModelFactory
import com.fit2081.derek35002735.nutritrack.data.viewmodels.NutriCoachViewModelFactory
import com.fit2081.derek35002735.nutritrack.screens.ClinicianLogin
import com.fit2081.derek35002735.nutritrack.screens.ClinicianScreen
import com.fit2081.derek35002735.nutritrack.screens.FoodIntakeQuestionnaire
import com.fit2081.derek35002735.nutritrack.screens.HomeScreen
import com.fit2081.derek35002735.nutritrack.screens.Insights
import com.fit2081.derek35002735.nutritrack.screens.Login
import com.fit2081.derek35002735.nutritrack.screens.NutriCoachScreen
import com.fit2081.derek35002735.nutritrack.screens.Register
import com.fit2081.derek35002735.nutritrack.screens.SettingsScreen
import com.fit2081.derek35002735.nutritrack.screens.WelcomeScreen
import com.fit2081.derek35002735.nutritrack.ui.theme.NutriTrackTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialize the database and repository
        val database = NutriTrackDatabase.getInstance(this)
        val repository = NutriTrackRepository(database.patientDao(), database.foodIntakeDao(), database)
        val sharedPreferences = getSharedPreferences("NutriTrackPrefs", MODE_PRIVATE)
        val loginViewModel = LoginViewModel(repository, sharedPreferences)

        //Retrieve the user session from the userId and lastScreen from the ViewModel
        val (userId, lastScreen) = loginViewModel.getSession()


        //Initialize the database in a coroutine
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("Database Initialization", "initializeDatabase called")
            repository.initializeDatabase(this@MainActivity)
        }

        setContent {

            val navController = rememberNavController() //Create a NavController to manage navigation

            NutriTrackTheme {
                //Set the start destination
                NavHost(navController, startDestination = "welcome") {
                    //Define the navigation graph
                    composable("welcome") { WelcomeScreen(navController) }
                    composable("register") {
                        val viewModel: RegisterViewModel = viewModel(factory = RegisterViewModelFactory(repository))
                        Register(navController, viewModel) }
                    composable("login") {
                        val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(repository, sharedPreferences))
                        Login(navController, viewModel)
                    }
                    composable("home/{userId}") { backStackEntry ->
                        //Retrieve the userId from the backStackEntry arguments || Code was taken from https://developer.android.com/guide/navigation/backstack
                        val userId = backStackEntry.arguments?.getString("userId") ?: ""
                        val viewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(repository))
                        HomeScreen(navController, viewModel, userId)
                    }
                    composable("insights/{userId}") { backStackEntry ->
                        //Retrieve the userId from the backStackEntry arguments
                        val userId = backStackEntry.arguments?.getString("userId") ?: ""
                        val viewModel: InsightsViewModel = viewModel(factory = InsightsViewModelFactory(repository))
                        Insights(navController, viewModel, userId)
                    }
                    composable("settings/{userId}") { backStackEntry ->
                        //Retrieve the userId from the backStackEntry arguments
                        val userId = backStackEntry.arguments?.getString("userId") ?: ""
                        val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(repository, sharedPreferences))
                        SettingsScreen(navController, viewModel, userId)
                    }
                    composable("foodIntake/{userId}") { backStackEntry ->
                        //Retrieve the userId from the backStackEntry arguments
                        val userId = backStackEntry.arguments?.getString("userId") ?: ""
                        val viewModel: FoodIntakeQuestionnaireViewModel = viewModel()

                        FoodIntakeQuestionnaire(navController, userId, viewModel)
                    }

                    composable("nutricoach/{userId}") { backStackEntry ->
                        //Retrieve the userId from the backStackEntry arguments
                        val userId = backStackEntry.arguments?.getString("userId") ?: ""
                        val viewModel: NutriCoachViewModel = viewModel(factory = NutriCoachViewModelFactory(repository))
                        NutriCoachScreen(navController, viewModel, userId)
                    }

                    composable("clinicianLogin/{userId}") { backStackEntry ->
                        //Retrieve the userId from the backStackEntry arguments
                        val userId = backStackEntry.arguments?.getString("userId") ?: ""
                        ClinicianLogin(navController, userId)
                    }

                    composable("clinician/{userId}") { backStackEntry ->
                        //Retrieve the userId from the backStackEntry arguments
                        val userId = backStackEntry.arguments?.getString("userId") ?: ""
                        val viewModel: ClinicianViewModel = viewModel(factory = ClinicianViewModelFactory(repository))
                        ClinicianScreen(navController, viewModel, userId)
                    }
                }
                //Handle the user session and navigation
                LaunchedEffect(Unit) {
                    if (userId != null && lastScreen != null) {
                        //Navigate to the last screen the user was on
                        if (lastScreen == "welcome" || lastScreen == "login" || lastScreen == "register") {
                            navController.navigate(lastScreen)
                        } else {
                            //Assuming the last screen is a user-specific screen
                            navController.navigate("$lastScreen/$userId")
                        }
                    } else {
                        navController.navigate("welcome")
                    }
                }

                //Code was taken from https://developer.android.com/reference/androidx/navigation/NavController.OnDestinationChangedListener
                //Add a listener to the NavController to update the last screen in shared preferences
                navController.addOnDestinationChangedListener { _, destination, arguments ->
                    val route = destination.route?.substringBefore("/")
                    if (route != null) {
                        loginViewModel.updateLastScreen(route)
                    }
                }

            }
        }
    }
}