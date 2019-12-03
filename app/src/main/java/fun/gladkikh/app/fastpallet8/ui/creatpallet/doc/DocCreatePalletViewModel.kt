package `fun`.gladkikh.app.fastpallet8.ui.creatpallet.doc

import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.domain.model.creatpallet.CreatePalletModelRx
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.CreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.PalletCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.ProductCreatePallet
import `fun`.gladkikh.app.fastpallet8.ui.base.BaseViewModel
import `fun`.gladkikh.app.fastpallet8.ui.common.Command
import `fun`.gladkikh.app.fastpallet8.ui.common.Command.ConfirmDialog
import `fun`.gladkikh.app.fastpallet8.ui.common.Command.EditNumberDialog
import `fun`.gladkikh.app.fastpallet8.ui.creatpallet.WrapperGuidCreatePallet
import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DocCreatePalletViewModel(private val modelRx: CreatePalletModelRx) : BaseViewModel() {

    private val doc = MutableLiveData<CreatePallet>()
    private val listProduct = MutableLiveData<List<ProductCreatePallet>>()

    fun getDocLiveData(): LiveData<CreatePallet> = doc
    fun getListProductLiveData(): LiveData<List<ProductCreatePallet>> = listProduct

    //Все пареметры запросов
    var wrapperGuid: WrapperGuidCreatePallet? = null
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


        compositeDisposable.add(
            modelRx.getListProduct()
                .map {
                    if (it.error == null) {
                        val size = it.data!!.size
                        it.data.mapIndexed { index, palletCreatePallet ->
                            palletCreatePallet.numberView = size - index
                            return@mapIndexed palletCreatePallet
                        }
                    }

                    return@map it
                }
                .subscribe({
                    if (it.error != null) {
                        messageErrorChannel.postValue(it.error.message)
                    } else {
                        listProduct.postValue(it.data)
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
                        Command.OpenForm(
                            code = Constants.OPEN_PRODUCT_FORM,
                            data = wrapperGuid!!.copy(guidProduct = listProduct.value!![position].guid)
                        )
                    )
                }
            }
        }

    }


    //Подтверждение удаления
    override fun callBackConfirmDialog(confirmDialog: ConfirmDialog) {
        super.callBackConfirmDialog(confirmDialog)

    }

    //Подтверждение диалога ввода числа
    override fun callBackEditNumberDialog(editNumberDialog: EditNumberDialog) {
        super.callBackEditNumberDialog(editNumberDialog)

    }

    @SuppressLint("CheckResult")
    fun readBarcode(barcode: String) {
    }

}