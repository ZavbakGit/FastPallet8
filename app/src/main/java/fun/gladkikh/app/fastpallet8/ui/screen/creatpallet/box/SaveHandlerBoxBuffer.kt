package `fun`.gladkikh.app.fastpallet8.ui.screen.creatpallet.box


import `fun`.gladkikh.app.fastpallet8.domain.model.creatpallet.CreatePalletModelRx
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.BoxCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.CreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.PalletCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.ProductCreatePallet
import androidx.lifecycle.MutableLiveData
import io.reactivex.BackpressureStrategy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class SaveHandlerBoxBuffer(compositeDisposable: CompositeDisposable,
                           private val modelRx: CreatePalletModelRx,
                           private val messageError:MutableLiveData<String?>,
                           private val doAfterSaveBuffer:(lastBox: BoxCreatePallet) ->Unit) {

    private val publishSubjectSaveBuffer = PublishSubject.create<BoxCreatePallet>()

    //Документ устанавливаем потом
    var doc: CreatePallet? = null
    var pallet: PalletCreatePallet? = null
    var product: ProductCreatePallet? = null

    init {
        compositeDisposable.add(
            publishSubjectSaveBuffer
                .toFlowable(BackpressureStrategy.BUFFER)
                .buffer(1000, TimeUnit.MILLISECONDS)
                .filter { it.size != 0 }
                .doOnNext {listBox->
                    listBox.forEachIndexed { index, boxCreatePallet ->
                        modelRx.saveBox(boxCreatePallet,doc!!)

                            .doFinally {
                                //После последней записи оповещаем
                                if (index == listBox.size - 1) {
                                        modelRx.recalculatePallet(pallet!!,product!!,doc!!)
                                            .subscribe({
                                                doAfterSaveBuffer(boxCreatePallet)
                                            },{
                                                messageError.postValue(it.message)
                                            })

                                }
                            }
                            .subscribe({},{
                                messageError.postValue(it.message)
                            })
                    }
                }
                .subscribe()
        )
    }

    fun saveBuffer(box: BoxCreatePallet) {
        publishSubjectSaveBuffer.onNext(box)
    }
}