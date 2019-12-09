package `fun`.gladkikh.app.fastpallet8.ui.screen.documentlist

import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.domain.model.documentmodel.DocumentModelRx
import `fun`.gladkikh.app.fastpallet8.domain.entity.ItemListDocument
import `fun`.gladkikh.app.fastpallet8.ui.base.BaseViewModel
import `fun`.gladkikh.app.fastpallet8.ui.common.Command
import `fun`.gladkikh.app.fastpallet8.ui.screen.creatpallet.WrapperGuidCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.Type
import `fun`.gladkikh.app.fastpallet8.ui.screen.action.WrapperGuidAction
import `fun`.gladkikh.app.fastpallet8.ui.screen.inventorypallet.doc.WrapperGuidInventoryPallet
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
                    openDocument(position)
                }
            }
            Constants.KEY_1 -> {
                commandChannel.postValue(
                    Command.AnyCommand(
                        code = Constants.COMMAND_START_MENU
                    )
                )
            }
            Constants.KEY_5 -> {
                if ((position != null && position != -1)) {
                    commandChannel.postValue(
                        Command.ConfirmDialog(
                            "Отправляем!",
                            Constants.CONFIRM_SEND_DIALOG,
                            position
                        )
                    )
                }
            }
            Constants.KEY_9 -> {
                if ((position != null && position != -1)) {
                    commandChannel.postValue(
                        Command.ConfirmDialog(
                            "Удалить!",
                            Constants.CONFIRM_DELETE_DIALOG,
                            position
                        )
                    )
                }
            }
        }
    }

    //Подтверждение удаления
    override fun callBackConfirmDialog(confirmDialog: Command.ConfirmDialog) {
        super.callBackConfirmDialog(confirmDialog)
        when (confirmDialog.requestCode) {
            Constants.CONFIRM_SEND_DIALOG -> {
                val position = confirmDialog.data as Int
                sendDocument(position)
            }
            Constants.CONFIRM_DELETE_DIALOG -> {
                val position = confirmDialog.data as Int
                deleteDocument(position)
            }
        }
    }


    @SuppressLint("CheckResult")
    private fun deleteDocument(position: Int){
        model.deleteDocument(listDocument.value!![position])
            .subscribeOn(Schedulers.io())
            .subscribe({
                messageChannel.postValue("Удалили!")
            }, {
                messageErrorChannel.postValue(it.message)
            })
    }

    private fun openDocument(position: Int) {
        val document = listDocument.value!![position]
        when (document.type) {
            Type.CREATE_PALLET -> {
                commandChannel.postValue(
                    Command.OpenForm(
                        code = Constants.OPEN_DOC_CREATE_PALLET_FORM,
                        data = WrapperGuidCreatePallet(guidDoc = document.guid)
                    )
                )

            }
            Type.ACTION_PALLET -> {
                commandChannel.postValue(
                    Command.OpenForm(
                        code = Constants.OPEN_DOC_ACTION_FORM,
                        data = WrapperGuidAction(guidDoc = document.guid)
                    )
                )

            }
            Type.INVENTORY_PALLET -> {
                commandChannel.postValue(
                    Command.OpenForm(
                        code = Constants.OPEN_DOC_INVENTORY_PALLET_FORM,
                        data = WrapperGuidInventoryPallet(
                            guidDoc = document.guid
                        )
                    )
                )

            }

            else -> {
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun sendDocument(position: Int) {
        model.sendDocument(listDocument.value!![position])
            .subscribeOn(Schedulers.io())
            .subscribe({
                messageChannel.postValue("Отправили!")
            }, {
                messageErrorChannel.postValue(it.message)
            })
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

    @SuppressLint("CheckResult")
    fun addTestDataCreatePallet() {
        model.addTestDataCreatePallet()
            .subscribeOn(Schedulers.io())
            .subscribe({
                messageChannel.postValue("Загрузили!")
            }, {
                messageErrorChannel.postValue(it.message)
            })
    }

    @SuppressLint("CheckResult")
    fun addTestDataAction() {
        model.addTestDataAction()
            .subscribeOn(Schedulers.io())
            .subscribe({
                messageChannel.postValue("Загрузили!")
            }, {
                messageErrorChannel.postValue(it.message)
            })
    }

    @SuppressLint("CheckResult")
    fun addTestDataInventoryPallet() {
        model.addTestDataInventoryPallet()
            .subscribeOn(Schedulers.io())
            .subscribe({
                messageChannel.postValue("Загрузили!")
            }, {
                messageErrorChannel.postValue(it.message)
            })
    }

}