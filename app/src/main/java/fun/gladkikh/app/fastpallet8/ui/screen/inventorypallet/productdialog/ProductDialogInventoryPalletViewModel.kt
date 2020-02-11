package `fun`.gladkikh.app.fastpallet8.ui.screen.inventorypallet.productdialog

import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.InventoryPallet
import `fun`.gladkikh.app.fastpallet8.domain.model.inventorypallet.InventoryPalletModelRx
import `fun`.gladkikh.app.fastpallet8.ui.base.BaseViewModel
import `fun`.gladkikh.app.fastpallet8.ui.common.Command
import `fun`.gladkikh.app.fastpallet8.ui.screen.inventorypallet.WrapperGuidInventoryPallet
import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ProductDialogInventoryPalletViewModel(private val modelRx: InventoryPalletModelRx) : BaseViewModel() {

    private val doc = MutableLiveData<InventoryPallet>()
    fun getDocLiveData(): LiveData<InventoryPallet> = doc

    //Все пареметры запросов
    var wrapperGuid: WrapperGuidInventoryPallet? = null
        set(value) {
            modelRx.setDoc(value?.guidDoc)
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
    }

    //Нажатие клавиш
    override fun callKeyDown(keyCode: Int?, position: Int?) {
        super.callKeyDown(keyCode, position)
        when (keyCode) {
            Constants.KEY_1 -> {
                commandChannel.postValue(
                    Command.EditNumberDialog(
                        "Начало",
                        Constants.EDIT_START_DIALOG,
                        false,
                        (doc.value!!.weightStartProduct ?: 0).toString()
                    )
                )
            }
            Constants.KEY_2 -> {
                commandChannel.postValue(
                    Command.EditNumberDialog(
                        "Конец",
                        Constants.EDIT_END_DIALOG,
                        false,
                        (doc.value!!.weightEndProduct ?: 0).toString()
                    )
                )
            }
            Constants.KEY_3 -> {
                commandChannel.postValue(
                    Command.EditNumberDialog(
                        "Коэффицент",
                        Constants.EDIT_COFF_DIALOG,
                        true,
                        (doc.value!!.weightCoffProduct ?: 0).toString()
                    )
                )
            }
        }
    }

    //Подтверждение диалога ввода числа
    override fun callBackEditNumberDialog(editNumberDialog: Command.EditNumberDialog) {
        super.callBackEditNumberDialog(editNumberDialog)
        when (editNumberDialog.requestCode) {
            Constants.EDIT_START_DIALOG -> {
                val start = editNumberDialog.data?.toIntOrNull()
                modelRx.saveDoc(doc.value!!.copy(weightStartProduct = start))
                    .subscribe({
                        wrapperGuid = wrapperGuid!!.copy()
                    }, {
                        messageErrorChannel.postValue(it.message)
                    })

            }
            Constants.EDIT_END_DIALOG -> {
                val end = editNumberDialog.data?.toIntOrNull()
                modelRx.saveDoc(doc.value!!.copy(weightEndProduct = end))
                    .subscribe({
                        wrapperGuid = wrapperGuid!!.copy()
                    }, {
                        messageErrorChannel.postValue(it.message)
                    })

            }
            Constants.EDIT_COFF_DIALOG -> {
                val coff = editNumberDialog.data?.toFloatOrNull()
                modelRx.saveDoc(doc.value!!.copy(weightCoffProduct = coff))
                    .subscribe({
                        wrapperGuid = wrapperGuid!!.copy()
                    }, {
                        messageErrorChannel.postValue(it.message)
                    })
            }
        }
    }

    @SuppressLint("CheckResult")
    fun readBarcode(barcode: String) {
        modelRx.saveDoc(doc.value!!.copy(weightBarcode = barcode))
            .subscribe({
                wrapperGuid = wrapperGuid!!.copy()
            }, {
                messageErrorChannel.postValue(it.message)
            })
    }


}