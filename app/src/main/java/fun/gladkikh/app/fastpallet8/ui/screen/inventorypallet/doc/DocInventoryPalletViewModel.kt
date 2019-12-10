package `fun`.gladkikh.app.fastpallet8.ui.screen.inventorypallet.doc

import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.common.isPallet

import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.BoxInventoryPallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.InventoryPallet
import `fun`.gladkikh.app.fastpallet8.domain.model.inventorypallet.InventoryPalletModelRx
import `fun`.gladkikh.app.fastpallet8.ui.base.BaseViewModel
import `fun`.gladkikh.app.fastpallet8.ui.common.Command.*
import `fun`.gladkikh.app.fastpallet8.ui.screen.inventorypallet.WrapperGuidInventoryPallet

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import java.util.*

class DocInventoryPalletViewModel(private val modelRx: InventoryPalletModelRx) : BaseViewModel() {

    private val doc = MutableLiveData<InventoryPallet>()
    private val listBox = MutableLiveData<List<BoxInventoryPallet>>()

    fun getListBoxLiveData(): LiveData<List<BoxInventoryPallet>> = listBox
    fun getDocLiveData(): LiveData<InventoryPallet> = doc

    //Все пареметры запросов
    var wrapperGuid: WrapperGuidInventoryPallet? = null
        set(value) {
            modelRx.setDoc(value?.guidDoc)
            field = value
        }

    private val saveHandlerPallet = SaveHandlerPalletInventoryPallet(
        compositeDisposable = compositeDisposable,
        messageError = messageErrorChannel,
        modelRx = modelRx
    ) {
        modelRx.setDoc(it.guid)
    }


    init {
        compositeDisposable.add(
            modelRx.getDoc()
                .subscribe({
                    if (it.error != null) {
                        messageErrorChannel.postValue(it.error.message)
                    } else {
                        doc.postValue(it.data)
                        saveHandlerPallet.doc = it.data
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
                        it.data.mapIndexed { index, boxInventoryPallet ->
                            boxInventoryPallet.numberView = size - index
                            return@mapIndexed boxInventoryPallet
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
                            code = Constants.OPEN_BOX_INVENTORY_PALLET_FORM,
                            data = wrapperGuid!!.copy(guidBox = listBox.value!![position].guid)
                        )
                    )
                }
            }
            Constants.KEY_5 -> {
               loadInfoPallet()
            }
            Constants.KEY_4 -> {
                commandChannel.postValue(
                    OpenForm(
                        code = Constants.OPEN_PRODUCT_INVENTORY_PALLET_DIALOG_FORM,
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
                            wrapperGuid = wrapperGuid!!.copy()
                        }, {
                            messageErrorChannel.postValue(it.message)
                        })
                }
            }
        }
    }


    fun readBarcode(barcode: String) {
        if (isPallet(barcode)) {
            saveHandlerPallet.save(barcode)
        } else {
            saveBoxBybarcode(barcode)
        }
    }

    @SuppressLint("CheckResult")
    fun loadInfoPallet(){
        modelRx.loadInfoPalletFromServer(doc.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                wrapperGuid = wrapperGuid!!.copy()
            }, {
                messageErrorChannel.postValue(it.message)
            })
    }

    @SuppressLint("CheckResult")
    private fun saveBoxBybarcode(barcode:String){
        val dataWrapper = modelRx.getBoxByBarcode(
            barcode = barcode,
            doc = doc.value!!
        )

        if (dataWrapper.error != null) {
            messageErrorChannel.postValue(dataWrapper.error.message)
        } else {
            modelRx.saveBox(dataWrapper.data!!, doc.value!!)
                .observeOn(Schedulers.io())
                .subscribe({
                    commandChannel.postValue(
                        OpenForm(
                            code = Constants.OPEN_BOX_INVENTORY_PALLET_FORM,
                            data = wrapperGuid!!.copy(guidBox = dataWrapper.data.guid)
                        )
                    )
                }, {
                    messageErrorChannel.postValue(it.message)
                })
        }
    }
}