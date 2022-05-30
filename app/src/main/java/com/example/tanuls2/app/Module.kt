package com.example.tanuls2.app

import com.example.tanuls2.domain.*
import com.example.tanuls2.service.datasource.*
import com.example.tanuls2.service.repository.CombatRepository
import com.example.tanuls2.ui.viewmodel.CombatViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.example.tanuls2.service.repository.InventoryRepository
import com.example.tanuls2.service.repository.SkillsRepository
import com.example.tanuls2.ui.viewmodel.InventoryViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val dataSourceModule = module {
    single { CombatLocalDataSource() }
    single { InventoryLocalDataSource() }
    single { SkillsLocalDataSource() }
    single {InventoryRemoteDataSource(get())}
}

val repositoryModule = module {
    single { CombatRepository(get()) }
    single { InventoryRepository(get(),get()) }
    single { SkillsRepository(get()) }
}

val useCaseModule = module {
    single { LoadKnightDataUseCase(get()) }
    single { LoadZombieDataUseCase(get()) }
    single { InventoryDataUseCase(get()) }
    single { SkillsDataUseCase(get()) }
    single { DroppedItemUseCase(get()) }
}

val viewModelModule = module {
    viewModel { CombatViewModel(get(), get(), get()) }
    viewModel { InventoryViewModel(get()) }
}

val apiModule = module {

    fun provideApiService(retrofit: Retrofit) : ApiService {
        return retrofit.create(ApiService::class.java)
    }

    single { provideApiService(get()) }
}

val retrofitModule = module {

    fun provideGson() : Gson {
        return GsonBuilder().create()
    }

    fun provideOkhttpClient() : OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://deckofcardsapi.com/api/deck/")
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    single { provideGson() }
    single { provideOkhttpClient() }
    single { provideRetrofit(get(), get()) }
}