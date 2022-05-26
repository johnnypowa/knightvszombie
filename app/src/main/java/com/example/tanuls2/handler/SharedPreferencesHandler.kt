package com.example.tanuls2.handler

import android.content.Context
import android.content.SharedPreferences
import com.example.tanuls2.model.Item
import com.example.tanuls2.model.Knight
import com.example.tanuls2.model.Weapon
import com.example.tanuls2.model.Zombie
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPreferencesHandler {

    private const val NAME = "handler.preferences"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    fun init(appContext: Context) {
        preferences = appContext.getSharedPreferences(NAME, MODE)
    }

    var storedKnight: Knight?
        set(value) = preferences.edit().putString("myKnight", Gson().toJson(value)).apply()
        get() = Gson().fromJson(preferences.getString("myKnight", null), object: TypeToken<Knight>(){}.type)

    var storedZombie: Zombie?
        set(value) = preferences.edit().putString("enemyZombie", Gson().toJson(value)).apply()
        get() = Gson().fromJson(preferences.getString("enemyZombie", null), object: TypeToken<Zombie>(){}.type)

    var storedItemList:ArrayList<Item>
        set(value) = preferences.edit().putString("itemList", Gson().toJson(value)).apply()
        get() = Gson().fromJson(preferences.getString("itemList", "[]"), object: TypeToken<ArrayList<Item>>(){}.type)

    //var isDefeated : Boolean
    // get() = preferences.getBoolean("isDefeated", false)
    // set(value) = preferences.edit().putBoolean("isDefeated", value).apply()


    //var zombie: Zombie

    //fun isDefeated() : Boolean {
     //   return preferences.getBoolean("isDefeated", false)
    //}

    //fun setDefeated(value: Boolean) {
      //  preferences.edit().putBoolean("isDefeated", value).apply()
    //}

}