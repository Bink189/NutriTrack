@Dao
interface FoodIntakeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(foodIntake: FoodIntake)

    @Query("SELECT * FROM food_intake WHERE patientId = :patientId")
    suspend fun getFoodIntakeByPatientId(patientId: String): List<FoodIntake>
}
