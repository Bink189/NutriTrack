package com.fit2081.derek35002735.nutritrack.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
//DAO interface for FoodIntake entity
interface FoodIntakeDao {
    @Insert
    suspend fun insertFoodIntake(foodIntake: FoodIntake)

    @Query("SELECT * FROM FoodIntake WHERE patientId = :patientId")
    fun getFoodIntakeByPatientId(patientId: String): Flow<List<FoodIntake>>

    @Query("SELECT COUNT(*) > 0 FROM FoodIntake WHERE patientId = :userId")
    suspend fun hasFoodIntake(userId: String): Boolean

    @Query("SELECT * FROM FoodIntake")
    suspend fun getAllFoodIntakes(): List<FoodIntake>

    @Update
    suspend fun updateFoodIntake(foodIntake: FoodIntake)
}