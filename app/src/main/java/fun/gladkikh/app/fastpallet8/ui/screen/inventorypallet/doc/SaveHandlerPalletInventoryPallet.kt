package `fun`.gladkikh.app.fastpallet8.ui.screen.inventorypallet.doc

import `fun`.gladkikh.app.fastpallet8.common.getNumberDocByBarCode
import `fun`.gladkikh.app.fastpallet8.common.toSimpleDate
import `fun`.gladkikh.app.fastpallet8.common.toSimpleDateTime
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.InventoryPallet
import `fun`.gladkikh.app.fastpallet8.domain.model.inventorypallet.InventoryPalletModelRx
import androidx.lifecycle.MutableLiveData
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class SaveHandlerPalletInventoryPallet(
    compositeDisposable: CompositeDisposable,
    private val modelRx: InventoryPalletModelRx,
    private val messageError: MutableLiveData<String?>,
    private val doAfterSave: (doc: InventoryPallet) -> Unit
) {

    private val publishSubjectBarcode = PublishSubject.create<String>()

    //Документ устанавливаем потом
    var doc: InventoryPallet? = null

    init {
        compositeDisposable.add(
            publishSubjectBarcode
                .observeOn(Schedulers.io())
                .toFlowable(BackpressureStrategy.BUFFER)
                .flatMap {
                    if (!modelRx.checkEditDocByStatus(doc!!.status)) {
                        return@flatMap Flowable.error<Throwable>(Throwable("Нельзя изменять документ!"))
                    }

                    if (doc!!.numberPallet != null) {
                        return@flatMap Flowable.error<Throwable>(Throwable("Паллета уже установлена!"))
                    }

                    val number = getNumberDocByBarCode(it)

                    if (number.isEmpty()) {
                        return@flatMap Flowable.error<Throwable>(Throwable("Пустой номер!"))
                    }

                    return@flatMap Flowable.just(getNumberDocByBarCode(it))
                }
                .map {
                    //Вытаскиваем номер
                    it as String
                }

                .doOnNext {

                    //Записываем
                    modelRx.saveDoc(doc!!.copy(numberPallet = it
                        ,description = "Инвентаризация паллеты №${it} от ${doc!!.date.toSimpleDateTime()}"
                    ))
                        .doFinally {
                            //Выполняем в конце
                            doAfterSave(doc!!)
                        }
                        .subscribe({}, { throwable ->
                            messageError.postValue(throwable.message)
                        })
                }
                .subscribe({

                }, {
                    messageError.postValue(it.message)
                })
        )
    }

    fun save(barcode: String) {
        publishSubjectBarcode.onNext(barcode)
    }
}