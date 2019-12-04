package `fun`.gladkikh.app.fastpallet8.ui.creatpallet.box

import `fun`.gladkikh.app.fastpallet8.domain.model.creatpallet.CreatePalletModelRx
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.BoxCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.CreatePallet
import androidx.lifecycle.MutableLiveData
import io.reactivex.BackpressureStrategy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class SaveHandlerBoxBuffer(compositeDisposable: CompositeDisposable,
                           private val modelRx: CreatePalletModelRx,
                           private val messageError:MutableLiveData<String>,
                           private val doAfterSaveBuffer:(lastBox:BoxCreatePallet) ->Unit) {

    private val publishSubjectSaveBuffer = PublishSubject.create<BoxCreatePallet>()

    //Документ устанавливаем потом
    var doc:CreatePallet? = null

    init {
        compositeDisposable.add(
            publishSubjectSaveBuffer
                .toFlowable(BackpressureStrategy.BUFFER)
                .buffer(1000, TimeUnit.MILLISECONDS)
                .doOnNext {listBox->
                    listBox.forEachIndexed { index, boxCreatePallet ->
                        modelRx.saveBox(boxCreatePallet,doc!!)
                            .doFinally {
                                //После последней записи оповещаем
                                if (index == listBox.size - 1) {
                                    doAfterSaveBuffer(boxCreatePallet)
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