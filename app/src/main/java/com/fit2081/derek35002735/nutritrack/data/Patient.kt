package com.fit2081.derek35002735.nutritrack.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
//Data class to represent a patient in the database
data class Patient(
    @PrimaryKey
    val userId: String,
    val phoneNumber: String,
    var name: String,
    val gender: String,
    var password: String,
    var heifaTotalScore: Double,
    var discretionaryScore: Double,
    var vegetableScore: Double,
    var fruitScore: Double,
    var grainsScore: Double,
    var dairyScore: Double,
    var sodiumScore: Double,
    var alcoholScore: Double,
    var waterScore: Double,
    var meatScore: Double,
    var wholeGrainScore: Double,
    var sugarScore: Double,
    var saturatedFatScore: Double,
    var unsaturatedFatScore: Double
)