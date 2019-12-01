package `fun`.gladkikh.app.fastpallet8.ui.creatpallet.box

import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.domain.model.creatpallet.CreatePalletModelRx
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.BoxCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.CreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.PalletCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.ProductCreatePallet
import `fun`.gladkikh.app.fastpallet8.ui.base.BaseViewModel
import `fun`.gladkikh.app.fastpallet8.ui.common.Command
import `fun`.gladkikh.app.fastpallet8.ui.common.Command.*
import `fun`.gladkikh.app.fastpallet8.ui.creatpallet.WrapperGuidCreatePallet
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

class BoxCreatePalletViewModel(val modelRx: CreatePalletModelRx) : BaseViewModel() {

    private val doc = MutableLiveData<CreatePallet>()
    private val product = MutableLiveData<ProductCreatePallet>()
    private val pallet = MutableLiveData<PalletCreatePallet>()
    private val box = MutableLiveData<BoxCreatePallet>()

    fun getDocLiveData(): LiveData<CreatePallet> = doc
    fun getProductLiveData(): LiveData<ProductCreatePallet> = product
    fun getPalletLiveData(): LiveData<PalletCreatePallet> = pallet
    fun getBoxLiveData(): LiveData<BoxCreatePallet> = box

    //Сохраняем через буффер 2 секунды
    val saveHandlerBox =
        SaveHandlerBoxBuffer(compositeDisposable, modelRx, messageErrorChannel) {
            modelRx.setBox(it.guid)
            modelRx.setProduct(product.value!!.guid)
            modelRx.setPallet(pallet.value!!.guid)
        }

    //Все пареметры запросов
    var wrapperGuid: WrapperGuidCreatePallet? = null
        set(value) {
            modelRx.setDoc(value?.guidDoc)
            modelRx.setProduct(value?.guidProduct)
            modelRx.setPallet(value?.guidPallet)
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
            modelRx.getPallet()
                .subscribe({
                    if (it.error != null) {
                        messageErrorChannel.postValue(it.error.message)
                    } else {
                        pallet.postValue(it.data)
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

    //Вызов диалога удаления
    private val CONFIRM_DELETE_DIALOG = 1

    //Вызов диалога ввода мест
    private val EDIT_PLACE_DIALOG = 2

    //Вызов диалога ввода количества
    private val EDIT_COUNT_DIALOG = 3

    //Вызов диалога добавления
    private val ADD_COUNT_DIALOG = 4



    //Нажатие клавиш
    override fun callKeyDown(keyCode: Int?, position: Int?) {
        super.callKeyDown(keyCode,position)
        when (keyCode) {
            Constants.KEY_1 -> {
                commandChannel.postValue(
                    Command.EditNumberDialog(
                        "Количество",
                        EDIT_COUNT_DIALOG,
                        true,
                        box.value!!.count.toString()
                    )
                )
            }
            Constants.KEY_2 -> {
                commandChannel.postValue(
                    EditNumberDialog(
                        "Мест",
                        EDIT_PLACE_DIALOG,
                        false,
                        box.value!!.countBox.toString()
                    )
                )
            }
            Constants.KEY_3 -> {
                commandChannel.postValue(
                    Command.EditNumberDialog(
                        "Количество",
                        ADD_COUNT_DIALOG,
                        true,
                        "0"
                    )
                )
            }
            Constants.KEY_9 -> {
                commandChannel.postValue(ConfirmDialog("Удаляем!", CONFIRM_DELETE_DIALOG))
            }
        }
    }

    //Подтверждение
    override fun callBackConfirmDialog(confirmDialog: ConfirmDialog) {
        super.callBackConfirmDialog(confirmDialog)
        when (confirmDialog.requestCode) {
            CONFIRM_DELETE_DIALOG -> {
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
            EDIT_PLACE_DIALOG -> {
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
            EDIT_COUNT_DIALOG -> {
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
            ADD_COUNT_DIALOG -> {
                val count = editNumberDialog.data?.toFloatOrNull() ?: 0f
                if (count == 0f) {
                    messageErrorChannel.postValue("Не верное число!")
                } else {
                    val box = BoxCreatePallet(
                        guid = UUID.randomUUID().toString(),
                        guidPallet = pallet.value!!.guid,
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
            product = product.value!!,
            pallet = pallet.value!!
        )

        if (data.error != null) {
            messageErrorChannel.postValue(data.error.message)
        } else {
            saveHandlerBox.saveBuffer(data.data!!)
        }

    }
}