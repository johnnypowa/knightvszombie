package com.example.tanuls2.app

import com.example.tanuls2.domain.LoadKnightDataUseCase
import com.example.tanuls2.domain.LoadZombieDataUseCase
import com.example.tanuls2.service.datasource.LocalCombatDataSource
import com.example.tanuls2.service.repository.CombatRepository
import com.example.tanuls2.ui.viewmodel.CombatViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataSourceModule = module {
   single { LocalCombatDataSource() }
}

val repositoryModule = module {
    single { CombatRepository(get()) }
}

val useCaseModule = module {
    single { LoadKnightDataUseCase(get()) }
    single { LoadZombieDataUseCase(get()) }
}

val viewModelModule = module {
    viewModel { CombatViewModel(get(), get()) }
}