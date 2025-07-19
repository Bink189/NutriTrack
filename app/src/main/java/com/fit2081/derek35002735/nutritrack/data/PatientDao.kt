package com.fit2081.derek35002735.nutritrack.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
//Dao interface for Patient entity
interface PatientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatient(patient: Patient)

    @Query("SELECT * FROM Patient")
    suspend fun getAllPatients(): List<Patient>

    @Query("SELECT userId FROM Patient")
    suspend fun getAllUserIds(): List<String>

    @Query("SELECT * FROM Patient WHERE userId = :userId LIMIT 1")
    suspend fun getPatientById(userId: String): Patient?

    @Query("SELECT fruitScore FROM Patient WHERE userId = :userId")
    suspend fun getFruitScore(userId: String): Double

    @Query("SELECT heifaTotalScore FROM Patient WHERE gender =:gender")
    suspend fun getHeifaTotalScore(gender: String): List<Double>

    @Update
    suspend fun updatePatient(patient: Patient)

    @Query("DELETE FROM Patient")
    suspend fun deleteAllPatients()

    //Replace the existing patient data with the new data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(patients: List<Patient>)

    @Query("SELECT * FROM Patient WHERE userId = :userId AND phoneNumber = :phoneNumber")
    suspend fun getPatientByIdAndPhone(userId: String, phoneNumber: String): Patient?

    @Query("SELECT * FROM Patient WHERE userId = :userId AND password = :password")
    suspend fun getPatientByIdAndPassword(userId: String, password: String): Patient?
}