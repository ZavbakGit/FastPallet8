package `fun`.gladkikh.app.fastpallet8.ui.creatpallet.pallet

import `fun`.gladkikh.app.fastpallet8.common.getNumberDocByBarCode
import `fun`.gladkikh.app.fastpallet8.domain.model.creatpallet.CreatePalletModelRx
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.CreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.PalletCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.ProductCreatePallet
import androidx.lifecycle.MutableLiveData
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Flowables
import io.reactivex.subjects.PublishSubject
import java.util.*

class SaveHandlerPallet(
    compositeDisposable: CompositeDisposable,
    private val modelRx: CreatePalletModelRx,
    private val messageError: MutableLiveData<String>,
    private val doAfterSave: (pallet: PalletCreatePallet) -> Unit
) {

    private val publishSubjectBarcode = PublishSubject.create<String>()

    //Документ устанавливаем потом
    var product: ProductCreatePallet? = null
    var doc: CreatePallet? = null

    init {
        compositeDisposable.add(
            publishSubjectBarcode
                .toFlowable(BackpressureStrategy.BUFFER)
                .map {
                    //Вытаскиваем номер
                    getNumberDocByBarCode(it)
                }
                .flatMap {

                    //Ищем паллету во всех документах
                    val numberFlow = Flowable.just(it)
                    val dataWrapperPalletSearchFlow = modelRx.getPalletByNumber(it)

                    return@flatMap Flowables.zip(
                        numberFlow,
                        dataWrapperPalletSearchFlow
                    ) { number, data ->
                        number to data
                    }
                }
                .flatMap {
                    //Если нашли, то нельзя
                    if (it.second.error != null) {
                        return@flatMap Flowable.error<Throwable>(Throwable("Паллета уже используется!"))
                    } else {
                        return@flatMap Flowable.just(it)
                    }
                }
                .map { number ->
                    //Создаем паллету
                    return@map PalletCreatePallet(
                        guid = UUID.randomUUID().toString(),
                        guidProduct = product!!.guid,
                        barcode = null,
                        count = null,
                        countBox = null,
                        countRow = null,
                        dateChanged = Date(),
                        nameProduct = product!!.nameProduct,
                        number = number as String,
                        numberView = null,
                        sclad = null,
                        state = null
                    )
                }
                .doOnNext {
                    //Записываем
                    modelRx.savePallet(it, doc!!)
                        .doFinally {
                            //Выполняем в конце
                            doAfterSave(it)
                        }
                        .subscribe({}, { throwable ->
                            messageError.postValue(throwable.message)
                        })
                }
                .subscribe()
        )
    }

    fun save(barcode: String) {
        publishSubjectBarcode.onNext(barcode)
    }
}