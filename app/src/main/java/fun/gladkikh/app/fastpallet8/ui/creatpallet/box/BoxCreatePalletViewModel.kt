package `fun`.gladkikh.app.fastpallet8.ui.creatpallet.box

import `fun`.gladkikh.app.fastpallet8.domain.model.creatpallet.CreatePalletModelRx
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.BoxCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.CreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.PalletCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.ProductCreatePallet
import `fun`.gladkikh.app.fastpallet8.ui.base.BaseViewModel
import `fun`.gladkikh.app.fastpallet8.ui.creatpallet.WrapperGuidCreatePaleet
import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class BoxCreatePalletViewModel(val modelRx: CreatePalletModelRx) : BaseViewModel() {

    private val doc = MutableLiveData<CreatePallet>()
    private val product = MutableLiveData<ProductCreatePallet>()
    private val pallet = MutableLiveData<PalletCreatePallet>()
    private val box = MutableLiveData<BoxCreatePallet>()

    fun getDocLiveData(): LiveData<CreatePallet> = doc
    fun getProductLiveData(): LiveData<ProductCreatePallet> = product
    fun getPalletLiveData(): LiveData<PalletCreatePallet> = pallet
    fun getBoxLiveData(): LiveData<BoxCreatePallet> = box

    val saveHandlerBox =
        SaveHandlerBoxBuffer(compositeDisposable, modelRx, messageErrorChannel) {
            modelRx.setBox(it.guid)
            modelRx.setProduct(product.value!!.guid)
            modelRx.setPallet(pallet.value!!.guid)
        }

    var wrapperGuid: WrapperGuidCreatePaleet? = null
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

    private val CONFIRM_DELETE_DIALOG = 1
    private val EDIT_PLACE_DIALOG = 2
    private val EDIT_COUNT_DIALOG = 3
    private val ADD_COUNT_DIALOG = 4


    @SuppressLint("CheckResult")
    fun editCount(count:Float){
        val box = box.value!!
        box.count = count
        modelRx.saveBox(box,doc.value!!)
            .subscribe({
                modelRx.setBox(box.guid)
                modelRx.setProduct(product.value!!.guid)
                modelRx.setPallet(pallet.value!!.guid)
            },{
                messageErrorChannel.postValue(it.message)
            })
    }

    @SuppressLint("CheckResult")
    fun editPlace(countBox:Int){
        val box = box.value!!
        box.countBox = countBox
        modelRx.saveBox(box,doc.value!!)
            .subscribe({
                modelRx.setBox(box.guid)
                modelRx.setProduct(product.value!!.guid)
                modelRx.setPallet(pallet.value!!.guid)
            },{
                messageErrorChannel.postValue(it.message)
            })
    }

    fun readBarcode(barcode: String) {
        val data = modelRx.getBoxByBarcode(
            barcode = barcode,
            doc = doc.value!!,
            product = product.value!!,
            pallet = pallet.value!!
        )

        if (data.error != null){
            messageErrorChannel.postValue(data.error.message)
        }else{
            saveHandlerBox.saveBuffer(data.data!!)
        }

    }
}