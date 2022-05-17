package com.example.tanuls2.app

import com.example.tanuls2.domain.InventoryDataUseCase
import com.example.tanuls2.domain.LoadKnightDataUseCase
import com.example.tanuls2.domain.LoadZombieDataUseCase
import com.example.tanuls2.domain.SkillsDataUseCase
import com.example.tanuls2.service.datasource.CombatLocalDataSource
import com.example.tanuls2.service.repository.CombatRepository
import com.example.tanuls2.ui.viewmodel.CombatViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.example.tanuls2.service.datasource.InventoryLocalDataSource
import com.example.tanuls2.service.datasource.SkillsLocalDataSource
import com.example.tanuls2.service.repository.InventoryRepository
import com.example.tanuls2.service.repository.SkillsRepository

val dataSourceModule = module {
    single { CombatLocalDataSource() }
    single { InventoryLocalDataSource() }
    single { SkillsLocalDataSource() }
}

val repositoryModule = module {
    single { CombatRepository(get()) }
    single { InventoryRepository(get()) }
    single { SkillsRepository(get()) }
}

val useCaseModule = module {
    single { LoadKnightDataUseCase(get()) }
    single { LoadZombieDataUseCase(get()) }
    single { InventoryDataUseCase(get()) }
    single { SkillsDataUseCase(get()) }
}

val viewModelModule = module {
    viewModel { CombatViewModel(get(), get()) }
}