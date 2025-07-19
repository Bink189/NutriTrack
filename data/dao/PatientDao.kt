@Dao
interface PatientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(patient: Patient)

    @Query("SELECT * FROM patient WHERE userId = :userId AND password = :password")
    suspend fun validateLogin(userId: String, password: String): Patient?

    @Query("SELECT * FROM patient WHERE userId = :userId AND phoneNumber = :phoneNumber")
    suspend fun findByUserIdAndPhone(userId: String, phoneNumber: String): Patient?
}
