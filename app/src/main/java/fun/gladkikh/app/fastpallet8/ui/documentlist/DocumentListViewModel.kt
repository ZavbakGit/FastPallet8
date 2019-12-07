package `fun`.gladkikh.app.fastpallet8.ui.documentlist

import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.domain.model.documentmodel.DocumentModelRx
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.ItemListDocument
import `fun`.gladkikh.app.fastpallet8.ui.base.BaseViewModel
import `fun`.gladkikh.app.fastpallet8.ui.common.Command
import `fun`.gladkikh.app.fastpallet8.ui.creatpallet.WrapperGuidCreatePallet
import `fun`.gladkikh.fastpallet7.model.Type
import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers

class DocumentListViewModel(val model: DocumentModelRx) : BaseViewModel() {

    private val listDocument = MutableLiveData<List<ItemListDocument>>()
    fun getListDocumentLiveData(): LiveData<List<ItemListDocument>> = listDocument

    init {
        compositeDisposable.add(
            model.getListDocument()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    listDocument.postValue(it)
                }, {
                    messageErrorChannel.postValue(it.message)
                })
        )
    }

    override fun callKeyDown(keyCode: Int?, position: Int?) {
        super.callKeyDown(keyCode, position)
        when (keyCode) {
            null -> {
                //Открываем Документ
                if (position != null && position != -1) {
                    val document = listDocument.value!![position]
                    when (document.type) {
                        Type.CREATE_PALLET -> {
                            commandChannel.postValue(
                                Command.OpenForm(
                                    code = Constants.OPEN_DOC_CREATE_PALLET,
                                    data = WrapperGuidCreatePallet(guidDoc = document.guid)
                                )
                            )

                        }
                        else -> {
                        }
                    }
                }
            }
            Constants.KEY_5 -> {
                if ((position != null && position != -1)) {
                    model.sendDocument(listDocument.value!![position])
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            messageChannel.postValue("Отправили!")
                        }, {
                            messageErrorChannel.postValue(it.message)
                        })
                }
            }
            Constants.KEY_1 -> {
                commandChannel.postValue(
                    Command.AnyCommand(
                        code = Constants.COMMAND_START_MENU
                    )
                )
            }
        }
    }

    @SuppressLint("CheckResult")
    fun loadDocuments() {
        model.loadDocuments()
            .subscribeOn(Schedulers.io())
            .subscribe({
                messageChannel.postValue("Загрузили!")
            }, {
                messageErrorChannel.postValue(it.message)
            })
    }

}