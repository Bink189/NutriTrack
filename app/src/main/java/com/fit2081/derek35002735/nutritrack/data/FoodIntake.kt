package com.fit2081.derek35002735.nutritrack.data
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

//Data class to represent a food intake entry for a user
//Code taken from https://medium.com/@vontonnie/connecting-room-tables-using-foreign-keys-c19450361603
@Entity(
    foreignKeys =  [ForeignKey(
        entity = Patient::class,
        parentColumns = ["userId"],
        childColumns = ["patientId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class FoodIntake(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val patientId: String, //User ID to associate with the data
    val selectedCategories: List<String>,
    val mealTime: String,
    val sleepTime: String,
    val wakeUpTime: String,
    val selectedPersona: String

)