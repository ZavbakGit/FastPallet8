package `fun`.gladkikh.app.fastpallet8.ui.screen.action.productdialog

import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.domain.model.action.ActionModelRx

import `fun`.gladkikh.app.fastpallet8.domain.entity.action.Action
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.ProductAction


import `fun`.gladkikh.app.fastpallet8.ui.base.BaseViewModel
import `fun`.gladkikh.app.fastpallet8.ui.common.Command
import `fun`.gladkikh.app.fastpallet8.ui.screen.action.WrapperGuidAction

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ProductDialogActionViewModel(private val modelRx: ActionModelRx) : BaseViewModel() {

    private val doc = MutableLiveData<Action>()
    private val product = MutableLiveData<ProductAction>()

    fun getProductLiveData(): LiveData<ProductAction> = product

    //Все пареметры запросов
    var wrapperGuid: WrapperGuidAction? = null
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
    }

    //Нажатие клавиш
    override fun callKeyDown(keyCode: Int?, position: Int?) {
        super.callKeyDown(keyCode, position)
        when (keyCode) {
            Constants.KEY_2 -> {
                commandChannel.postValue(
                    Command.EditNumberDialog(
                        "Начало",
                        Constants.EDIT_START_DIALOG,
                        false,
                        (product.value!!.weightStartProduct ?: 0).toString()
                    )
                )
            }
            Constants.KEY_3 -> {
                commandChannel.postValue(
                    Command.EditNumberDialog(
                        "Начало",
                        Constants.EDIT_END_DIALOG,
                        false,
                        (product.value!!.weightEndProduct ?: 0).toString()
                    )
                )
            }
            Constants.KEY_4 -> {
                commandChannel.postValue(
                    Command.EditNumberDialog(
                        "Начало",
                        Constants.EDIT_COFF_DIALOG,
                        false,
                        (product.value!!.weightCoffProduct ?: 0).toString()
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
                modelRx.saveProduct(product.value!!.copy(weightStartProduct = start), doc.value!!)
                    .subscribe({
                        wrapperGuid = wrapperGuid!!.copy()
                    }, {
                        messageErrorChannel.postValue(it.message)
                    })

            }
            Constants.EDIT_END_DIALOG -> {
                val end = editNumberDialog.data?.toIntOrNull()
                modelRx.saveProduct(product.value!!.copy(weightEndProduct = end), doc.value!!)
                    .subscribe({
                        wrapperGuid = wrapperGuid!!.copy()
                    }, {
                        messageErrorChannel.postValue(it.message)
                    })

            }
            Constants.EDIT_COFF_DIALOG -> {
                val coff = editNumberDialog.data?.toFloatOrNull()
                modelRx.saveProduct(product.value!!.copy(weightCoffProduct = coff), doc.value!!)
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
        modelRx.saveProduct(product.value!!.copy(barcode = barcode), doc.value!!)
            .subscribe({
                wrapperGuid = wrapperGuid!!.copy()
            }, {
                messageErrorChannel.postValue(it.message)
            })
    }


}