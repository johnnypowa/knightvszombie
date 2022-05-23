package com.example.tanuls2.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tanuls2.domain.LoadKnightDataUseCase
import com.example.tanuls2.domain.LoadZombieDataUseCase
import com.example.tanuls2.model.CombatModel
import com.example.tanuls2.model.Knight
import com.example.tanuls2.model.Zombie
import com.example.tanuls2.util.OnceLiveEvent
import com.example.tanuls2.util.SingleEvent
import com.example.tanuls2.util.plusAssign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
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

    val knightDataFlow : Single<Knight> = loadKnightDataUseCase.execute().delay(1L, TimeUnit.SECONDS)
    val zombieDataFlow: Single<Zombie> = loadZombieDataUseCase.execute().delay(2L, TimeUnit.SECONDS)

    fun loadAllContent() {
        compositeDisposable += Single.zip(knightDataFlow, zombieDataFlow) { knightData, zombieData ->
                CombatModel(knightData, zombieData)
            }
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    onceLiveEvent.postValue(ShowAllContent(it.knight, it.zombie))
                },{
                    onceLiveEvent.postValue(ShowError(it.message))
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}

class ShowAllContent(val knightData: Knight, val zombieData: Zombie) : SingleEvent
class ShowError(val errorMessage: String?) : SingleEvent