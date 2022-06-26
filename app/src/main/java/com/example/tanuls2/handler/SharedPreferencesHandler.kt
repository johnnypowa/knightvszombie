package com.example.tanuls2.handler

import android.content.Context
import android.content.SharedPreferences
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

    fun isFirstStart() : Boolean {
        return preferences.getBoolean("isFirstStart", true)}

    fun setFirstStart(value: Boolean) {
        preferences.edit().putBoolean("isFirstStart", value).apply()}

    var storedZombie: Zombie?
        set(value) = preferences.edit().putString("enemyZombie", Gson().toJson(value)).apply()
        get() = Gson().fromJson(preferences.getString("enemyZombie", null), object: TypeToken<Zombie>(){}.type)

}