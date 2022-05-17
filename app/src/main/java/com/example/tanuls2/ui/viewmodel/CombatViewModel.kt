package com.example.tanuls2.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tanuls2.domain.LoadKnightDataUseCase
import com.example.tanuls2.domain.LoadZombieDataUseCase
import com.example.tanuls2.model.Knight
import com.example.tanuls2.model.Zombie
import com.example.tanuls2.util.OnceLiveEvent
import com.example.tanuls2.util.SingleEvent
import com.example.tanuls2.util.plusAssign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

//MVVM architektúra (Model - View - ViewModel)
class CombatViewModel(
    private val loadKnightDataUseCase: LoadKnightDataUseCase,
    private val loadZombieDataUseCase: LoadZombieDataUseCase
) : ViewModel() {

    val onceLiveEvent = OnceLiveEvent<SingleEvent>() //Live data

    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun loadMyKnightData() {
        compositeDisposable += loadKnightDataUseCase.execute()
            .delay(5L, TimeUnit.SECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    onceLiveEvent.postValue(ShowKnightData(it))
                },
                {
                    onceLiveEvent.postValue(ShowError(it.message))
                }
            )
    }

    fun loadEnemyZombieData(): Zombie {
        return loadZombieDataUseCase.execute()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}

class ShowKnightData(val knightData: Knight) : SingleEvent
class ShowError(val errorMessage: String?) : SingleEvent