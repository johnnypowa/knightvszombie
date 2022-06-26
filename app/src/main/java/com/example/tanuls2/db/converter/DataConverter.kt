package com.example.tanuls2.db.converter

import androidx.room.TypeConverter
import com.example.tanuls2.model.Item
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataConverter {

    @TypeConverter
    fun itemListToJson(value: ArrayList<Item>) : String {
        return Gson().toJson(value, object: TypeToken<ArrayList<Item>>(){}.type)
    }

    @TypeConverter
    fun itemListFromJson(value: String) : ArrayList<Item> {
        return Gson().fromJson(value, object: TypeToken<ArrayList<Item>>(){}.type)
    }

}