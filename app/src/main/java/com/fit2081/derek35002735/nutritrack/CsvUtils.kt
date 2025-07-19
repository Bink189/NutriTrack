package com.fit2081.derek35002735.nutritrack

import android.content.Context
import android.util.Log
import com.fit2081.derek35002735.nutritrack.data.Patient


object CsvUtils {
    //Function to read and parse CSV data from assets
     fun readCsvData(context: Context): List<Patient> {
         Log.d("CSV Debug", "readCsvData function called")
        // Open the CSV file from assets
        val userList = mutableListOf<Patient>()
        val inputStream = context.assets.open("data.csv")

        inputStream.bufferedReader().use { reader ->
            val lines = reader.readLines()
            if (lines.isEmpty()) {
                Log.e("CSV Parsing", "CSV file is empty")
                return userList
            }
            //Skip the header line
            lines.drop(1).forEach { line ->
                val tokens = line.split(",")
                Log.d("CSV Debug", "Processing line with ${tokens.size} tokens")
                //Check if the line has the expected number of tokens
                if (tokens.size >= 63) {
                    //Extract the relevant fields from the tokens
                    val phoneNumber = tokens[0]
                    val id = tokens[1]
                    val gender = tokens[2]
                    val heifaTotalScore = if (gender == "Male") tokens[3].toDoubleOrNull() ?: 0.0
                    else tokens[4].toDoubleOrNull() ?: 0.0

                    Log.d("CSV Parsing", "Total Score: $heifaTotalScore")

                    val discretionaryScore = if (gender == "Male") tokens[5].toDoubleOrNull() ?: 0.0
                    else tokens[6].toDoubleOrNull() ?: 0.0

                    val vegetableScore = if (gender == "Male") tokens[8].toDoubleOrNull() ?: 0.0
                    else tokens[9].toDoubleOrNull() ?: 0.0

                    Log.d("CSV Parsing", "Discretionary Score: $discretionaryScore, Vegetable Score: $vegetableScore")

                    var fruitScore = if (gender == "Male") tokens[19].toDoubleOrNull() ?: 0.0
                    else tokens[20].toDoubleOrNull() ?: 0.0

                    val grainsScore = if (gender == "Male") tokens[29].toDoubleOrNull() ?: 0.0
                    else tokens[30].toDoubleOrNull() ?: 0.0

                    val wholeGrainScore = if(gender =="Male") tokens[33].toDoubleOrNull() ?: 0.0
                    else tokens[34].toDoubleOrNull() ?: 0.0

                    val meatScore = if(gender == "Male") tokens[36].toDoubleOrNull() ?: 0.0
                    else tokens[37].toDoubleOrNull() ?: 0.0

                    val dairyScore = if (gender == "Male") tokens[40].toDoubleOrNull() ?: 0.0
                    else tokens[41].toDoubleOrNull() ?: 0.0

                    val sodiumScore = if (gender == "Male") tokens[43].toDoubleOrNull() ?: 0.0
                    else tokens[44].toDoubleOrNull() ?: 0.0

                    val alcoholScore = if (gender == "Male") tokens[46].toDoubleOrNull() ?: 0.0
                    else tokens[47].toDoubleOrNull() ?: 0.0

                    val waterScore = if (gender == "Male") tokens[49].toDoubleOrNull() ?: 0.0
                    else tokens[50].toDoubleOrNull() ?: 0.0

                    val sugarScore = if (gender == "Male") tokens[54].toDoubleOrNull() ?: 0.0
                    else tokens[55].toDoubleOrNull() ?: 0.0

                    val saturatedFatScore = if (gender == "Male") tokens[57].toDoubleOrNull() ?: 0.0
                    else tokens[58].toDoubleOrNull() ?: 0.0

                    val unsaturatedFatScore = if (gender == "Male") tokens[60].toDoubleOrNull() ?: 0.0
                    else tokens[61].toDoubleOrNull() ?: 0.0

                    //Add the parsed data to the list of users
                    userList.add(
                        Patient(
                            id,
                            phoneNumber,
                            name = "",
                            gender = gender,
                            password = "",
                            heifaTotalScore = heifaTotalScore,
                            discretionaryScore = discretionaryScore,
                            vegetableScore = vegetableScore,
                            fruitScore = fruitScore,
                            grainsScore = grainsScore,
                            wholeGrainScore = wholeGrainScore,
                            meatScore = meatScore,
                            dairyScore = dairyScore,
                            sodiumScore = sodiumScore,
                            alcoholScore = alcoholScore,
                            waterScore = waterScore,
                            sugarScore = sugarScore,
                            saturatedFatScore = saturatedFatScore,
                            unsaturatedFatScore = unsaturatedFatScore
                        )
                    )
                } else {
                    Log.e("CSV Parsing", "Malformed line: $line")
                }
            }
        }
        return userList
    }
}