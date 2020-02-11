package `fun`.gladkikh.app.fastpallet8.ui.screen.creatpallet.product


import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.domain.model.creatpallet.CreatePalletModelRx
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.CreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.PalletCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.ProductCreatePallet
import `fun`.gladkikh.app.fastpallet8.ui.base.BaseViewModel
import `fun`.gladkikh.app.fastpallet8.ui.common.Command
import `fun`.gladkikh.app.fastpallet8.ui.common.SingleLiveEvent
import `fun`.gladkikh.app.fastpallet8.ui.screen.creatpallet.WrapperGuidCreatePallet
import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ProductCreatePalletViewModel(private val modelRx: CreatePalletModelRx) : BaseViewModel() {

    private val doc = MutableLiveData<CreatePallet>()
    private val product = MutableLiveData<ProductCreatePallet>()
    private val listPallet = MutableLiveData<List<PalletCreatePallet>>()

    private val setCurrentPosition = SingleLiveEvent<Int>()


    fun getProductLiveData(): LiveData<ProductCreatePallet> = product
    fun getListPalletLiveData(): LiveData<List<PalletCreatePallet>> = listPallet
    fun getSetCurrentPosition(): LiveData<Int> = setCurrentPosition



    private val saveHandlerPallet = SaveHandlerPallet(
        compositeDisposable = compositeDisposable,
        messageError = messageErrorChannel,
        modelRx = modelRx,
        doAfterSave = {
            modelRx.setProduct(it.guidProduct)
        },
        doFoundPallet = {pall->
            val item =
                getListPalletLiveData().value!!.withIndex().find { it.value.number == pall.number  }

            if (item != null){
                setCurrentPosition.postValue(item.index)
            }else{
                messageErrorChannel.postValue("Паллета уже используется!")
            }

        }
    )


    //Все пареметры запросов
    var wrapperGuid: WrapperGuidCreatePallet? = null
        set(value) {
            modelRx.setDoc(value?.guidDoc)
            modelRx.setProduct(value?.guidProduct)
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
                        saveHandlerPallet.doc = it.data
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
                        saveHandlerPallet.product = it.data
                    }
                }, {
                    messageErrorChannel.postValue(it.message)
                })
        )

        compositeDisposable.add(
            modelRx.getListPallet()
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
                        listPallet.postValue(it.data)
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
                //Открываем палету
                if (position != null && position != -1) {
                    commandChannel.postValue(
                        Command.OpenForm(
                            code = Constants.OPEN_PALLET_CREATE_PALLET_FORM,
                            data = wrapperGuid!!.copy(guidPallet = listPallet.value!![position].guid)
                        )
                    )
                }
            }
            Constants.KEY_4 -> {
                commandChannel.postValue(
                    Command.OpenForm(
                        code = Constants.OPEN_PRODUCT_CREATE_PALLET_DIALOG_FORM,
                        data = wrapperGuid!!
                    )
                )
            }
            Constants.KEY_9 -> {
                if (position != -1) {
                    commandChannel.postValue(
                        Command.ConfirmDialog(
                            "Удаляем!",
                            Constants.CONFIRM_DELETE_DIALOG,
                            position
                        )
                    )
                }
            }

        }
    }

    override fun callBackConfirmDialog(confirmDialog: Command.ConfirmDialog) {
        super.callBackConfirmDialog(confirmDialog)
        when (confirmDialog.requestCode) {
            Constants.CONFIRM_DELETE_DIALOG -> {
                val position = confirmDialog.data as Int
                modelRx.dellPallet(listPallet.value!![position], doc.value!!)
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
        saveHandlerPallet.save(barcode)
    }
}