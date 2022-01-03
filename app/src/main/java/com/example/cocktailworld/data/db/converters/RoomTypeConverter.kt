package com.example.cocktailworld.data.db.converters
import androidx.room.TypeConverter
import com.example.cocktailworld.model.Drink
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RoomTypeConverter {
    companion object{
        @TypeConverter
        @JvmStatic
        fun fromList(drinks: List<Drink>): String{
            val gson = Gson()
            return  gson.toJson(drinks)
        }
        @TypeConverter
        @JvmStatic
        fun fromString(value: String): List<Drink>{
            val typeList = object : TypeToken<List<Drink>>() {}.type
            return Gson().fromJson(value,typeList)
        }
    }
}