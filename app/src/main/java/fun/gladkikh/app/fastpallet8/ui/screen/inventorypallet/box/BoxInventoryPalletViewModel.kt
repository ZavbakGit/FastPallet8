package `fun`.gladkikh.app.fastpallet8.ui.screen.inventorypallet.box

import `fun`.gladkikh.app.fastpallet8.Constants

import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.BoxInventoryPallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.InventoryPallet
import `fun`.gladkikh.app.fastpallet8.domain.model.inventorypallet.InventoryPalletModelRx


import `fun`.gladkikh.app.fastpallet8.ui.base.BaseViewModel
import `fun`.gladkikh.app.fastpallet8.ui.common.Command.*
import `fun`.gladkikh.app.fastpallet8.ui.screen.inventorypallet.doc.WrapperGuidInventoryPallet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

class BoxInventoryPalletViewModel(private val modelRx: InventoryPalletModelRx) : BaseViewModel() {

    private val doc = MutableLiveData<InventoryPallet>()

    private val box = MutableLiveData<BoxInventoryPallet>()

    fun getDocLiveData(): LiveData<InventoryPallet> = doc
    fun getBoxLiveData(): LiveData<BoxInventoryPallet> = box

    //Сохраняем через буффер 2 секунды
    private val saveHandlerBox =
        SaveHandlerBoxInventoryPalletBuffer(compositeDisposable, modelRx, messageErrorChannel) {
            modelRx.setBox(it.guid)
            modelRx.setDoc(it.guidDoc)
        }

    //Все пареметры запросов
    var wrapperGuid: WrapperGuidInventoryPallet? = null
        set(value) {
            modelRx.setDoc(value?.guidDoc)
            modelRx.setBox(value?.guidBox)

            field = value
        }

    init {
        compositeDisposable.add(
            modelRx.getDoc()
                .subscribe({
                    if (it.error != null) {
                        messageErrorChannel.postValue(it.error.message)
                    } else {
                        doc.postValue(it.data)
                        saveHandlerBox.doc = it.data
                    }
                }, {
                    messageErrorChannel.postValue(it.message)
                })
        )

        compositeDisposable.add(
            modelRx.getBox()
                .subscribe({
                    if (it.error != null) {
                        messageErrorChannel.postValue(it.error.message)
                    } else {
                        box.postValue(it.data)
                    }
                }, {
                    messageErrorChannel.postValue(it.message)
                })
        )
    }


    //Нажатие клавиш
    override fun callKeyDown(keyCode: Int?, position: Int?) {
        super.callKeyDown(keyCode, position)
        when (keyCode) {
            Constants.KEY_1 -> {
                commandChannel.postValue(
                    EditNumberDialog(
                        "Количество",
                        Constants.EDIT_COUNT_DIALOG,
                        true,
                        box.value!!.count.toString()
                    )
                )
            }
            Constants.KEY_2 -> {
                commandChannel.postValue(
                    EditNumberDialog(
                        "Мест",
                        Constants.EDIT_PLACE_DIALOG,
                        false,
                        box.value!!.countBox.toString()
                    )
                )
            }
            Constants.KEY_3 -> {
                commandChannel.postValue(
                    EditNumberDialog(
                        "Количество",
                        Constants.ADD_COUNT_DIALOG,
                        true,
                        "0"
                    )
                )
            }
            Constants.KEY_9 -> {
                commandChannel.postValue(ConfirmDialog("Удаляем!", Constants.CONFIRM_DELETE_DIALOG))
            }
        }
    }

    //Подтверждение
    override fun callBackConfirmDialog(confirmDialog: ConfirmDialog) {
        super.callBackConfirmDialog(confirmDialog)
        when (confirmDialog.requestCode) {
            Constants.CONFIRM_DELETE_DIALOG -> {
                modelRx.dellBox(box.value!!, doc.value!!)
                    .subscribe({
                        commandChannel.postValue(Close)
                    }, {
                        messageErrorChannel.postValue(it.message)
                    })
            }
        }
    }

    //Подтверждение диалога ввода числа
    override fun callBackEditNumberDialog(editNumberDialog: EditNumberDialog) {
        super.callBackEditNumberDialog(editNumberDialog)
        when (editNumberDialog.requestCode) {
            Constants.EDIT_PLACE_DIALOG -> {
                val place = editNumberDialog.data?.toIntOrNull()
                if (place == null) {
                    messageErrorChannel.postValue("Не верное число!")
                } else {
                    box.value!!.countBox = place
                    modelRx.saveBox(box.value!!, doc.value!!)
                        .subscribe({
                            wrapperGuid = wrapperGuid!!.copy()
                        }, {
                            messageErrorChannel.postValue(it.message)
                        })
                }
            }
            Constants.EDIT_COUNT_DIALOG -> {
                val count = editNumberDialog.data?.toFloatOrNull()
                if (count == null) {
                    messageErrorChannel.postValue("Не верное число!")
                } else {
                    box.value!!.count = count
                    modelRx.saveBox(box.value!!, doc.value!!)
                        .subscribe({
                            wrapperGuid = wrapperGuid!!.copy()
                        }, {
                            messageErrorChannel.postValue(it.message)
                        })
                }
            }
            Constants.ADD_COUNT_DIALOG -> {
                val count = editNumberDialog.data?.toFloatOrNull() ?: 0f
                if (count == 0f) {
                    messageErrorChannel.postValue("Не верное число!")
                } else {
                    val box = BoxInventoryPallet(
                        guid = UUID.randomUUID().toString(),
                        guidDoc = doc.value!!.guid,
                        barcode = "",
                        countBox = 1,
                        count = count,
                        dateChanged = Date()
                    )
                    modelRx.saveBox(box, doc.value!!)
                        .subscribe({
                            wrapperGuid = wrapperGuid!!.copy(guidBox = box.guid)
                        }, {
                            messageErrorChannel.postValue(it.message)
                        })
                }
            }


        }
    }

    fun readBarcode(barcode: String) {
        val data = modelRx.getBoxByBarcode(
            barcode = barcode,
            doc = doc.value!!
        )

        if (data.error != null) {
            messageErrorChannel.postValue(data.error.message)
        } else {
            saveHandlerBox.saveBuffer(data.data!!)
        }

    }
}