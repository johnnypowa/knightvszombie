package com.example.tanuls2.app

import android.app.Application
import android.content.Context
import com.example.tanuls2.handler.SharedPreferencesHandler
import com.example.tanuls2.service.datasource.InventoryLocalDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KnightVsZombie : Application() {

    lateinit var appContext: Context

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@KnightVsZombie)
            modules(listOf(dataSourceModule, repositoryModule, useCaseModule, viewModelModule))
        }

        appContext = this

        SharedPreferencesHandler.init(appContext)
    }
}