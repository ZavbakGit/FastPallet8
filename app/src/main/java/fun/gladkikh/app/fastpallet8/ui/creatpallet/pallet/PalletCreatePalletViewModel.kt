package `fun`.gladkikh.app.fastpallet8.ui.creatpallet.pallet

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
import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

class PalletCreatePalletViewModel(val modelRx: CreatePalletModelRx) : BaseViewModel() {

    private val doc = MutableLiveData<CreatePallet>()
    private val product = MutableLiveData<ProductCreatePallet>()
    private val pallet = MutableLiveData<PalletCreatePallet>()
    private val boxList = MutableLiveData<List<BoxCreatePallet>>()

    fun getDocLiveData(): LiveData<CreatePallet> = doc
    fun getProductLiveData(): LiveData<ProductCreatePallet> = product
    fun getPalletLiveData(): LiveData<PalletCreatePallet> = pallet
    fun getListBoxLiveData(): LiveData<List<BoxCreatePallet>> = boxList


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
                    if (it.error == null){
                        val size = it.data!!.size
                        it.data.mapIndexed { index, boxCreatePallet ->
                            boxCreatePallet.number = size - index
                            return@mapIndexed boxCreatePallet
                        }
                    }

                    return@map it
                }
                .subscribe({
                    if (it.error != null) {
                        messageErrorChannel.postValue(it.error.message)
                    } else {
                        boxList.postValue(it.data)
                    }
                }, {
                    messageErrorChannel.postValue(it.message)
                })
        )
    }

    //Вызов диалога удаления
    private val CONFIRM_DELETE_DIALOG = 1


    //Вызов диалога добавления
    private val ADD_COUNT_DIALOG = 4

    //Нажатие клавиш
    override fun callKeyDown(keyCode: Int?, position: Int?) {
        super.callKeyDown(keyCode, position)
        when (keyCode) {
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
                if (position != -1) {
                    commandChannel.postValue(
                        ConfirmDialog(
                            "Удаляем!",
                            CONFIRM_DELETE_DIALOG,
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
            CONFIRM_DELETE_DIALOG -> {
                val position = confirmDialog.data as Int
                modelRx.dellBox(boxList.value!![position], doc.value!!)
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