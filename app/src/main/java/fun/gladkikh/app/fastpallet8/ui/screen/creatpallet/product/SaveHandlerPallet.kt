package `fun`.gladkikh.app.fastpallet8.ui.screen.creatpallet.product

import `fun`.gladkikh.app.fastpallet8.common.getNumberDocByBarCode
import `fun`.gladkikh.app.fastpallet8.domain.model.creatpallet.CreatePalletModelRx

import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.CreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.PalletCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.ProductCreatePallet
import androidx.lifecycle.MutableLiveData
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Flowables
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*

class SaveHandlerPallet(
    private val compositeDisposable: CompositeDisposable,
    private val modelRx: CreatePalletModelRx,
    private val messageError: MutableLiveData<String?>,
    private val doAfterSave: (pallet: PalletCreatePallet) -> Unit,
    private val doFoundPallet:(pallet: PalletCreatePallet) -> Unit
) {

    private val publishSubjectBarcode = PublishSubject.create<String>()

    fun getCompletableSave(barcode: String): Completable {
        return Flowable.just(barcode)
            .flatMap {
                try {
                    val number = getNumberDocByBarCode(it)

                    val pallet = PalletCreatePallet(
                        guid = UUID.randomUUID().toString(),
                        guidProduct = product!!.guid,
                        barcode = it,
                        count = null,
                        countBox = null,
                        countRow = null,
                        dateChanged = Date(),
                        nameProduct = product!!.nameProduct,
                        number = number,
                        numberView = null,
                        sclad = null,
                        state = null
                    )
                    return@flatMap Flowable.just(pallet)

                } catch (e: Exception) {
                    return@flatMap Flowable.error<Throwable>(e)
                }
            }
            .map {
                it as PalletCreatePallet
            }
            .flatMap {

                //Ищем паллету во всех документах
                val palletFlow = Flowable.just(it)
                val dataWrapperPalletSearchFlow = modelRx.getPalletByNumber(it.number!!)

                return@flatMap Flowables.zip(
                    palletFlow,
                    dataWrapperPalletSearchFlow
                ) { pallet, data ->
                    pallet to data
                }
            }
            .filter {
                //Если нашли, то нельзя
                if (it.second.data != null) {
                    doFoundPallet(it.first)
                    //messageError.postValue("Паллета уже используется! ${it.first.number}")
                    return@filter false
                } else {
                    return@filter true
                }
            }
            .doOnNext {
                it.first as PalletCreatePallet
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
            .doOnError {
                messageError.postValue(it.message)
            }
            .ignoreElements()

    }


    //Документ устанавливаем потом
    var product: ProductCreatePallet? = null
    var doc: CreatePallet? = null

    val disposable = publishSubjectBarcode
        .observeOn(Schedulers.io())
        .toFlowable(BackpressureStrategy.BUFFER)
        .doOnNext {
            getCompletableSave(it)
                .subscribe({},{})
        }
        .subscribe()


    init {
        compositeDisposable.add(disposable)
    }

    fun save(barcode: String) {
        publishSubjectBarcode.onNext(barcode)
    }
}