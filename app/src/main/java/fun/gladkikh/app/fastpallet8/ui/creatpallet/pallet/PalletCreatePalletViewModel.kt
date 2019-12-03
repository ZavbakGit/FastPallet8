package `fun`.gladkikh.app.fastpallet8.ui.creatpallet.pallet

import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.domain.model.creatpallet.CreatePalletModelRx
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.BoxCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.CreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.PalletCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.ProductCreatePallet
import `fun`.gladkikh.app.fastpallet8.ui.base.BaseViewModel
import `fun`.gladkikh.app.fastpallet8.ui.common.Command.*
import `fun`.gladkikh.app.fastpallet8.ui.creatpallet.WrapperGuidCreatePallet
import `fun`.gladkikh.app.fastpallet8.ui.navigate.NavigateHandler
import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

class PalletCreatePalletViewModel(private val modelRx: CreatePalletModelRx) : BaseViewModel() {

    private val doc = MutableLiveData<CreatePallet>()
    private val product = MutableLiveData<ProductCreatePallet>()
    private val pallet = MutableLiveData<PalletCreatePallet>()
    private val listBox = MutableLiveData<List<BoxCreatePallet>>()


    fun getProductLiveData(): LiveData<ProductCreatePallet> = product
    fun getPalletLiveData(): LiveData<PalletCreatePallet> = pallet
    fun getListBoxLiveData(): LiveData<List<BoxCreatePallet>> = listBox

    //Все пареметры запросов
    var wrapperGuid: WrapperGuidCreatePallet? = null
        set(value) {
            modelRx.setDoc(value?.guidDoc)
            modelRx.setProduct(value?.guidProduct)
            modelRx.setPallet(value?.guidPallet)
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
            modelRx.getListBox()
                .map {
                    if (it.error == null) {
                        val size = it.data!!.size
                        it.data.mapIndexed { index, boxCreatePallet ->
                            boxCreatePallet.numberView = size - index
                            return@mapIndexed boxCreatePallet
                        }
                    }

                    return@map it
                }
                .subscribe({
                    if (it.error != null) {
                        messageErrorChannel.postValue(it.error.message)
                    } else {
                        listBox.postValue(it.data)
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
            null -> {
                //Открываем Box
                if (position != null && position != -1) {
                    commandChannel.postValue(
                        OpenForm(
                            code = Constants.OPEN_BOX_FORM,
                            data = wrapperGuid!!.copy(guidBox = listBox.value!![position].guid)
                        )
                    )
                }
            }
            Constants.KEY_5 -> {
                commandChannel.postValue(
                    AnyCommand(
                        code = Constants.COMMAND_HIDE_FORM
                    )
                )
            }
            Constants.KEY_4 -> {
                commandChannel.postValue(
                    OpenForm(
                        code = Constants.OPEN_PRODUCT_DIALOG_FORM,
                        data = wrapperGuid!!
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
                if (position != -1) {
                    commandChannel.postValue(
                        ConfirmDialog(
                            "Удаляем!",
                            Constants.CONFIRM_DELETE_DIALOG,
                            position
                        )
                    )
                }
            }
        }
    }

    //Подтверждение удаления
    override fun callBackConfirmDialog(confirmDialog: ConfirmDialog) {
        super.callBackConfirmDialog(confirmDialog)
        when (confirmDialog.requestCode) {
            Constants.CONFIRM_DELETE_DIALOG -> {
                val position = confirmDialog.data as Int
                modelRx.dellBox(listBox.value!![position], doc.value!!)
                    .subscribe({
                        wrapperGuid = wrapperGuid!!.copy()
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
            Constants.ADD_COUNT_DIALOG -> {
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
                            wrapperGuid = wrapperGuid!!.copy()
                        }, {
                            messageErrorChannel.postValue(it.message)
                        })
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    fun readBarcode(barcode: String) {
        val dataWrapper = modelRx.getBoxByBarcode(
            barcode = barcode,
            doc = doc.value!!,
            product = product.value!!,
            pallet = pallet.value!!
        )

        if (dataWrapper.error != null) {
            messageErrorChannel.postValue(dataWrapper.error.message)
        } else {
            modelRx.saveBox(dataWrapper.data!!, doc.value!!)
                .subscribe({
                    commandChannel.postValue(
                        OpenForm(data = wrapperGuid!!.copy(guidBox = dataWrapper.data.guid))
                    )
                }, {
                    messageErrorChannel.postValue(it.message)
                })
        }

    }
}