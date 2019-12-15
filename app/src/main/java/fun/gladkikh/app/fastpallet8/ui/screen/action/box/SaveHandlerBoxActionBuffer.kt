package `fun`.gladkikh.app.fastpallet8.ui.screen.action.box


import `fun`.gladkikh.app.fastpallet8.domain.model.action.ActionModelRx
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.Action
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.BoxAction

import androidx.lifecycle.MutableLiveData
import io.reactivex.BackpressureStrategy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class SaveHandlerBoxActionBuffer(compositeDisposable: CompositeDisposable,
                                 private val modelRx: ActionModelRx,
                                 private val messageError:MutableLiveData<String?>,
                                 private val doAfterSaveBuffer:(lastBox: BoxAction) ->Unit) {

    private val publishSubjectSaveBuffer = PublishSubject.create<BoxAction>()

    //Документ устанавливаем потом
    var doc: Action? = null

    init {
        compositeDisposable.add(
            publishSubjectSaveBuffer
                .toFlowable(BackpressureStrategy.BUFFER)
                .buffer(1000, TimeUnit.MILLISECONDS)
                .doOnNext {listBox->
                    listBox.forEachIndexed { index, boxAction ->
                        modelRx.saveBox(boxAction,doc!!)
                            .doFinally {
                                //После последней записи оповещаем
                                if (index == listBox.size - 1) {
                                    doAfterSaveBuffer(boxAction)
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

    fun saveBuffer(box: BoxAction) {
        publishSubjectSaveBuffer.onNext(box)
    }
}