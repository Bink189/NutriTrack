@Entity(tableName = "patient")
data class Patient(
    @PrimaryKey val userId: String,
    val phoneNumber: String,
    val name: String,
    val sex: String,
    val scores: String, // JSON or delimited string for scores
    val password: String
)
