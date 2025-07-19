package com.fit2081.derek35002735.nutritrack.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Patient::class, FoodIntake::class], version = 8, exportSchema = false )
@TypeConverters(Converters::class)
abstract class NutriTrackDatabase: RoomDatabase(){
    //Abstract methods to get DAO instances
    abstract fun patientDao(): PatientDao
    abstract fun foodIntakeDao(): FoodIntakeDao

    companion object {
        @Volatile
        private var INSTANCE: NutriTrackDatabase? = null
        //Migration object to handle database version changes || Code was taken from https://developer.android.com/training/data-storage/room/migrating-db-versions
        private val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //Drop the old Patient table if it exists
                database.execSQL("DROP TABLE IF EXISTS Patient")

                //Create the new Patient table with the updated schema
                database.execSQL("""
            CREATE TABLE IF NOT EXISTS Patient (
                userId TEXT PRIMARY KEY NOT NULL,
                phoneNumber TEXT NOT NULL,
                name TEXT NOT NULL,
                gender TEXT NOT NULL,
                password TEXT NOT NULL,
                heifaTotalScore REAL NOT NULL,
                discretionaryScore REAL NOT NULL,
                vegetableScore REAL NOT NULL,
                fruitScore REAL NOT NULL,
                grainsScore REAL NOT NULL,
                dairyScore REAL NOT NULL,
                sodiumScore REAL NOT NULL,
                alcoholScore REAL NOT NULL,
                waterScore REAL NOT NULL,
                meatScore REAL NOT NULL,
                wholeGrainScore REAL NOT NULL,
                sugarScore REAL NOT NULL,
                saturatedFatScore REAL NOT NULL,
                unsaturatedFatScore REAL NOT NULL
            )
        """.trimIndent())


            }
        }

        //Function to get the singleton instance of the database
        fun getInstance(context: Context): NutriTrackDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NutriTrackDatabase::class.java,
                    "nutritrack_db"
                )
                    .addMigrations(MIGRATION_7_8)
                    .build()

                INSTANCE = instance
                println("NutriTrackDatabase instance created")
                instance
            }.also {
                println("NutriTrackDatabase getinstance called")
            }
        }
    }
}