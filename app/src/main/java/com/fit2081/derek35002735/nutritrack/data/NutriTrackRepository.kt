package com.fit2081.derek35002735.nutritrack.data

import android.content.Context
import android.util.Log
import com.fit2081.derek35002735.nutritrack.BuildConfig
import com.fit2081.derek35002735.nutritrack.CsvUtils
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

//This part of the code was taken from the week 5 lab
class NutriTrackRepository(
    private val patientDao: PatientDao,
    private val foodIntakeDao: FoodIntakeDao,
    private val database: NutriTrackDatabase

) {

    //Patient operations
    suspend fun insertPatient(patient: Patient) {
        patientDao.insertPatient(patient)
    }

    suspend fun updatePatient(patient: Patient) {
        patientDao.updatePatient(patient)
    }

    suspend fun getAllPatients(): List<Patient> {
        return patientDao.getAllPatients()
    }

    suspend fun getPatientById(userId: String): Patient? {
        return patientDao.getPatientById(userId)
    }

    suspend fun getFruitScore(userId: String): Double {
        return patientDao.getFruitScore(userId)
    }

    suspend fun getHeifaScoresByGender(gender: String): List<Double> {
        return patientDao.getHeifaTotalScore(gender)
    }
    //FoodIntake operations
    suspend fun insertFoodIntake(foodIntake: FoodIntake) {
        foodIntakeDao.insertFoodIntake(foodIntake)
    }

    suspend fun getFoodIntakeByPatientId(patientId: String): Flow<List<FoodIntake>> {
        return foodIntakeDao.getFoodIntakeByPatientId(patientId)
    }
    suspend fun hasFoodIntake(userId: String): Boolean {
        return database.foodIntakeDao().hasFoodIntake(userId)
    }

    //Test patient data
    suspend fun insertTestPatient() {
        val testPatient = Patient(
            userId = "123",
            phoneNumber = "1234567890",
            name = "Test User",
            gender = "Male",
            password = "password",
            heifaTotalScore = 50.0,
            discretionaryScore = 10.0,
            vegetableScore = 10.0,
            fruitScore = 10.0,
            grainsScore = 2.5,
            wholeGrainScore = 2.4,
            meatScore = 3.0,
            dairyScore = 5.0,
            sodiumScore = 5.0,
            alcoholScore = 0.0,
            waterScore = 8.0,
            sugarScore = 4.0,
            saturatedFatScore = 7.0,
            unsaturatedFatScore = 1.23
        )
        patientDao.insertPatient(testPatient)
        Log.d("Test Insert", "Inserted test patient: $testPatient")
    }

    //Function to get all patients and their food intakes, pass the data as a dataset
    suspend fun getDataset(): String {
        val patients = getAllPatients()
        val foodIntakes = foodIntakeDao.getAllFoodIntakes()
        return "Patients: $patients\nFood Intakes: $foodIntakes"
    }

    //Function to initialize the database with the CSV data
    suspend fun initializeDatabase(context: Context) {
        withContext(Dispatchers.IO) {
            val existingPatients = patientDao.getAllPatients()
            if (existingPatients.isNotEmpty()) {
                Log.d("Database Initialization", "Database already initialized with ${existingPatients.size} patients.")
                return@withContext
            }
            Log.d("Database Initialization", "Calling readCsvData")
            val userList = CsvUtils.readCsvData(context)
            Log.d("Database Initialization", "Patients parsed from CSV: ${userList.size}")
            if (userList.isEmpty()) {
                Log.e("Database Initialization", "No data found in CSV file.")
                return@withContext
            }

            //Insert each user into the database and populate the patient table with their information
            userList.forEach { userData ->
                try {
                    val patient = Patient(
                        userId = userData.userId,
                        phoneNumber = userData.phoneNumber,
                        name = userData.name,
                        gender = userData.gender,
                        password = userData.password,
                        heifaTotalScore = userData.heifaTotalScore,
                        discretionaryScore = userData.discretionaryScore,
                        vegetableScore = userData.vegetableScore,
                        fruitScore = userData.fruitScore,
                        grainsScore = userData.grainsScore,
                        wholeGrainScore = userData.wholeGrainScore,
                        meatScore = userData.meatScore,
                        dairyScore = userData.dairyScore,
                        sodiumScore = userData.sodiumScore,
                        alcoholScore = userData.alcoholScore,
                        waterScore = userData.waterScore,
                        sugarScore = userData.sugarScore,
                        saturatedFatScore = userData.saturatedFatScore,
                        unsaturatedFatScore = userData.unsaturatedFatScore
                    )
                    insertPatient(patient)
                    Log.d("Database Insert", "Inserted patient: $patient")
                } catch (e: Exception) {
                    Log.e("Database Insert", "Failed to insert patient: ${e.message}")
                }
            }
            Log.d("Database Initialization", "Database initialized with ${userList.size} patients.")
        }
    }

    //This part of the code was taken from the Week 7 Lab
    //Retrofit instance to make network calls
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.fruityvice.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //Service instance to define the API endpoints
    private val service = retrofit.create(FruityViceService::class.java)

    //Function to get fruit facts by fruit name
    suspend fun getFruitFacts(fruitName: String): FruitResponse? {
        return try {
            service.getFruitInfo(fruitName)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    //Interface to define the API endpoints
    interface FruityViceService {
        @GET("fruit/{name}")
        suspend fun getFruitInfo(@retrofit2.http.Path("name") name: String): FruitResponse
    }

    //Data class to represent the response from the FruityVice API
    data class FruitResponse(
        val id: Int,
        val name: String,
        val family: String,
        val genus: String,
        val order: String,
        val nutritions: Nutritions
    ) {
        //Sub data class to represent the nutrition information for each fruit
        data class Nutritions(
            val carbohydrates: Double,
            val protein: Double,
            val fat: Double,
            val calories: Double,
            val sugar: Double
        )
    }

    //This code was taken from the week 7 lab
    //Declare an instance of the generative model
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.apiKey
    )

    //Function to generate a message using the generative model
    suspend fun generateMessage(prompt: String): String {
        return withContext(Dispatchers.IO) {
            try {
                //Send the prompt to the generative model and get the response
                val response = generativeModel.generateContent(prompt)
                response.text ?: "No response from Gemini AI"
            } catch (e: Exception) {
                Log.e("GenAIService", "Exception: ${e.message}", e)
                "Error connecting to Gemini AI: ${e.message}"
            }
        }
    }

    //Function to find patterns in the dataset using the generative model
    suspend fun findPatterns(dataset: String): List<String> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("GenerativeAIService", "Dataset sent to AI: $dataset")
                val response = generativeModel.generateContent("Analyze the HEIFA scores and Food Intake entries from this dataset and return 3 data patterns: $dataset")
                val result = response.text?.split("\n") ?: emptyList()
                Log.d("GenerativeAIService", "AI Response: $result")
                if (result.isEmpty()) {
                    listOf("No patterns identified. Please check the dataset.")
                } else {
                    result
                }
            } catch (e: Exception) {
                Log.e("GenerativeAIService", "Error: ${e.message}", e)
                listOf("Error: Unable to process the dataset. Please try again later.")
            }
        }
    }
}