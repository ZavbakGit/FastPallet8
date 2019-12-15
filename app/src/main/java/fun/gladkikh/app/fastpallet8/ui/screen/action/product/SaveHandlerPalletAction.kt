package `fun`.gladkikh.app.fastpallet8.ui.screen.action.product

import `fun`.gladkikh.app.fastpallet8.common.getNumberDocByBarCode
import `fun`.gladkikh.app.fastpallet8.domain.model.action.ActionModelRx

import `fun`.gladkikh.app.fastpallet8.domain.entity.action.Action
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.PalletAction
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.ProductAction


import androidx.lifecycle.MutableLiveData
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Flowables
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*

class SaveHandlerPalletAction(
    compositeDisposable: CompositeDisposable,
    private val modelRx: ActionModelRx,
    private val messageError: MutableLiveData<String?>,
    private val doAfterSave: (pallet: PalletAction) -> Unit
) {

    private val publishSubjectBarcode = PublishSubject.create<String>()

    //Документ устанавливаем потом
    var product: ProductAction? = null
    var doc: Action? = null

    init {
        compositeDisposable.add(
            publishSubjectBarcode
                .observeOn(Schedulers.io())
                .toFlowable(BackpressureStrategy.BUFFER)
                .map {
                    //Вытаскиваем номер

                    return@map PalletAction(
                        guid = UUID.randomUUID().toString(),
                        guidProduct = product!!.guid,
                        barcode = it,
                        count = null,
                        countBox = null,
                        countRow = null,
                        dateChanged = Date(),
                        nameProduct = product!!.nameProduct,
                        number = getNumberDocByBarCode(it),
                        numberView = null,
                        sclad = null,
                        state = null
                    )


                }
                .flatMap {

                    //Ищем паллету во всех документах
                    val palletFlow= Flowable.just(it)
                    val dataWrapperPalletSearchFlow = modelRx.
                        getPalletByNumber(it.number!!,product!!.guid)

                    return@flatMap Flowables.zip(
                        palletFlow,
                        dataWrapperPalletSearchFlow
                    ) { pallet, data ->
                        pallet to data
                    }
                }
                .filter{
                    //Если нашли, то нельзя
                    if (it.second.data != null) {
                        messageError.postValue("Паллета уже используется! ${it.first.number}")
                        return@filter false
                    }else{
                        return@filter true
                    }
                }
                .doOnNext {
                    it.first as PalletAction
                    //Записываем
                    modelRx.savePallet(it.first, doc!!)
                        .doFinally {
                            //Выполняем в конце
                            doAfterSave(it.first)
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