package `fun`.gladkikh.app.fastpallet8.ui.screen.inventorypallet.box


import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.BoxInventoryPallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.InventoryPallet
import `fun`.gladkikh.app.fastpallet8.domain.model.inventorypallet.InventoryPalletModelRx
import androidx.lifecycle.MutableLiveData
import io.reactivex.BackpressureStrategy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class SaveHandlerBoxInventoryPalletBuffer(
    compositeDisposable: CompositeDisposable,
    private val modelRx: InventoryPalletModelRx,
    private val messageError: MutableLiveData<String>,
    private val doAfterSaveBuffer: (lastBox: BoxInventoryPallet) -> Unit
) {

    private val publishSubjectSaveBuffer = PublishSubject.create<BoxInventoryPallet>()

    //Документ устанавливаем потом
    var doc: InventoryPallet? = null

    init {
        compositeDisposable.add(
            publishSubjectSaveBuffer
                .toFlowable(BackpressureStrategy.BUFFER)
                .buffer(1000, TimeUnit.MILLISECONDS)
                .doOnNext { listBox ->
                    listBox.forEachIndexed { index, box ->
                        modelRx.saveBox(box, doc!!)
                            .doFinally {
                                //После последней записи оповещаем
                                if (index == listBox.size - 1) {
                                    doAfterSaveBuffer(box)
                                }
                            }
                            .subscribe({}, {
                                messageError.postValue(it.message)
                            })
                    }
                }
                .subscribe()
        )
    }

    fun saveBuffer(box: BoxInventoryPallet) {
        publishSubjectSaveBuffer.onNext(box)
    }
}