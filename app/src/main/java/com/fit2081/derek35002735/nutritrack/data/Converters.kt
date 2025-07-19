package com.fit2081.derek35002735.nutritrack.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

//Code was taken from https://developer.android.com/training/data-storage/room/referencing-data
//Class to convert complex data types to and from strings for the Room database
class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromMap(value: Map<String, Double>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toMap(value: String): Map<String, Double> {
        val type = object : TypeToken<Map<String, Double>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toList(json: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(json, type)
    }

}