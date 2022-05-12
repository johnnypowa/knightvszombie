package com.example.tanuls2.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tanuls2.domain.LoadKnightDataUseCase
import com.example.tanuls2.domain.LoadZombieDataUseCase
import com.example.tanuls2.model.Knight
import com.example.tanuls2.model.Zombie

//MVVM architektúra (Model - View - ViewModel)
class CombatViewModel(
    private val loadKnightDataUseCase: LoadKnightDataUseCase,
    private val loadZombieDataUseCase: LoadZombieDataUseCase
) : ViewModel() {

    fun loadMyKnightData(): Knight {
        return loadKnightDataUseCase.execute()
    }

    fun loadEnemyZombieData(): Zombie {
        return loadZombieDataUseCase.execute()
    }
}