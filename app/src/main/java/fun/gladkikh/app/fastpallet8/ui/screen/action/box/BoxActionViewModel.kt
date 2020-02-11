package `fun`.gladkikh.app.fastpallet8.ui.screen.action.box

import `fun`.gladkikh.app.fastpallet8.Constants

import `fun`.gladkikh.app.fastpallet8.domain.entity.action.Action
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.BoxAction
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.ProductAction
import `fun`.gladkikh.app.fastpallet8.domain.model.action.ActionModelRx


import `fun`.gladkikh.app.fastpallet8.ui.base.BaseViewModel
import `fun`.gladkikh.app.fastpallet8.ui.common.Command.*
import `fun`.gladkikh.app.fastpallet8.ui.screen.action.WrapperGuidAction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

class BoxActionViewModel(private val modelRx: ActionModelRx) : BaseViewModel() {

    private val doc = MutableLiveData<Action>()
    private val product = MutableLiveData<ProductAction>()

    private val box = MutableLiveData<BoxAction>()

    fun getProductLiveData(): LiveData<ProductAction> = product
    fun getBoxLiveData(): LiveData<BoxAction> = box

    //Сохраняем через буффер 2 секунды
    private val saveHandlerBox =
        SaveHandlerBoxActionBuffer(compositeDisposable, modelRx, messageErrorChannel) {
            modelRx.setBox(it.guid)
            modelRx.setProduct(product.value!!.guid)
        }

    //Все пареметры запросов
    var wrapperGuid: WrapperGuidAction? = null
        set(value) {
            modelRx.setDoc(value?.guidDoc)
            modelRx.setProduct(value?.guidProduct)
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
            modelRx.getProduct()
                .subscribe({
                    if (it.error != null) {
                        messageErrorChannel.postValue(it.error.message)
                    } else {
                        product.postValue(it.data)
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
        super.callKeyDown(keyCode,position)
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
                    val box = BoxAction(
                        guid = UUID.randomUUID().toString(),
                        guidProduct = product.value!!.guid,
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
            doc = doc.value!!,
            product = product.value!!
        )

        if (data.error != null) {
            messageErrorChannel.postValue(data.error.message)
        } else {
            saveHandlerBox.saveBuffer(data.data!!)
        }

    }
}