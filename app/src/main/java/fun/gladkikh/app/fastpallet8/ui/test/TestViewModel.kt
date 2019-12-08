package `fun`.gladkikh.app.fastpallet8.ui.test

import `fun`.gladkikh.app.fastpallet8.domain.usecase.recalcdb.RecalcDbUseCase
import `fun`.gladkikh.app.fastpallet8.ui.base.BaseViewModel
import `fun`.gladkikh.app.fastpallet8.domain.usecase.testdata.AddTestDataCreatePalletUseCase
import android.annotation.SuppressLint
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class TestViewModel(
    private val addTestDataUseCase: AddTestDataCreatePalletUseCase,
    private val recalcDbUseCase: RecalcDbUseCase
) : BaseViewModel() {

    @SuppressLint("CheckResult")
    fun addTestData() {

        Completable.fromCallable {
            addTestDataUseCase.save()
        }
            .subscribeOn(Schedulers.io())
            .subscribe {
                messageChannel.postValue("Закончили!")
            }
    }

    @SuppressLint("CheckResult")
    fun recalc() {
        Completable.fromCallable {
            recalcDbUseCase.recalc()
        }
            .subscribeOn(Schedulers.io())
            .subscribe {
                messageChannel.postValue("Закончили!")
            }
    }
}