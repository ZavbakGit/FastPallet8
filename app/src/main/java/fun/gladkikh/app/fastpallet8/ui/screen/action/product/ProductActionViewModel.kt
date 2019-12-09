package `fun`.gladkikh.app.fastpallet8.ui.screen.action.product

import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.common.isPallet
import `fun`.gladkikh.app.fastpallet8.domain.model.DataWrapper
import `fun`.gladkikh.app.fastpallet8.domain.model.action.ActionModelRx
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.action.Action
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.action.BoxAction
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.action.ProductAction
import `fun`.gladkikh.app.fastpallet8.ui.base.BaseViewModel
import `fun`.gladkikh.app.fastpallet8.ui.common.Command.*
import `fun`.gladkikh.app.fastpallet8.ui.screen.action.WrapperGuidAction
import `fun`.gladkikh.app.fastpallet8.ui.screen.action.product.TYPE_ITEM_ACTION.BOX
import `fun`.gladkikh.app.fastpallet8.ui.screen.action.product.TYPE_ITEM_ACTION.PALLET
import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import java.util.*

class ProductActionViewModel(private val modelRx: ActionModelRx) : BaseViewModel() {

    private val doc = MutableLiveData<Action>()
    private val product = MutableLiveData<ProductAction>()
    private val listItemAction = MutableLiveData<List<ItemAction>>()


    fun getProductLiveData(): LiveData<ProductAction> = product
    fun getListPalletLiveData(): LiveData<List<ItemAction>> = listItemAction

    private val saveHandlerPallet = SaveHandlerPalletAction(
        compositeDisposable = compositeDisposable,
        messageError = messageErrorChannel,
        modelRx = modelRx
    ) {
        modelRx.setProduct(it.guidProduct)
    }


    //Все пареметры запросов
    var wrapperGuid: WrapperGuidAction? = null
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
            modelRx.getWraperListBoxListPallet()
                .map {
                    if (it.error != null) {
                        return@map it
                    }

                    val listBox = it.data?.listBox?.map { box ->
                        ItemAction(
                            guid = box.guid,
                            countBox = box.countBox,
                            count = box.count,
                            numberView = null,
                            description = null,
                            box = box,
                            type = BOX
                        )
                    } ?: listOf()

                    val listPallet = it.data?.listPallet?.map { pallet ->
                        ItemAction(
                            guid = pallet.guid,
                            countBox = pallet.countBox,
                            count = pallet.count,
                            numberView = null,
                            description = pallet.number,
                            pallet = pallet,
                            type = PALLET
                        )
                    } ?: listOf()

                    val list = listPallet + listBox
                    val size = list.size

                    val data = list.mapIndexed { index, itemAction ->
                        itemAction.numberView = size - index
                        return@mapIndexed itemAction
                    }

                    return@map DataWrapper(data = data)


                }
                .subscribe({
                    if (it.error != null) {
                        messageErrorChannel.postValue(it.error.message)
                    } else {
                        listItemAction.postValue(it.data as List<ItemAction>)
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
                    openFormByPosition(position)
                }
            }
            Constants.KEY_4 -> {
                commandChannel.postValue(
                    OpenForm(
                        code = Constants.OPEN_PRODUCT_ACTION_DIALOG_FORM,
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
            Constants.KEY_5 -> {
               loadDataPallet()
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




    //Подтверждение диалога ввода числа
    override fun callBackEditNumberDialog(editNumberDialog: EditNumberDialog) {
        super.callBackEditNumberDialog(editNumberDialog)
        when (editNumberDialog.requestCode) {
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
                            wrapperGuid = wrapperGuid!!.copy()
                        }, {
                            messageErrorChannel.postValue(it.message)
                        })
                }
            }
        }
    }

    override fun callBackConfirmDialog(confirmDialog: ConfirmDialog) {
        super.callBackConfirmDialog(confirmDialog)
        when (confirmDialog.requestCode) {
            Constants.CONFIRM_DELETE_DIALOG -> {
                val position = confirmDialog.data as Int
                delete(position)
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun delete(position: Int) {
        val itemAction = listItemAction.value!![position]

        if (itemAction.type == PALLET) {
            modelRx.dellPallet(itemAction.pallet!!, doc.value!!)
                .subscribe({
                    wrapperGuid = wrapperGuid!!.copy()
                }, {
                    messageErrorChannel.postValue(it.message)
                })
        } else {
            modelRx.dellBox(itemAction.box!!, doc.value!!)
                .subscribe({
                    wrapperGuid = wrapperGuid!!.copy()
                }, {
                    messageErrorChannel.postValue(it.message)
                })
        }

    }

    private fun openFormByPosition(position: Int) {
        val item = listItemAction.value!![position]
        when (item.type) {
            BOX -> {
                commandChannel.postValue(
                    OpenForm(
                        code = Constants.OPEN_BOX_ACTION_FORM,
                        data = wrapperGuid!!.copy(guidBox = item.guid)
                    )
                )
            }

            else -> {
            }
        }


    }


    fun readBarcode(barcode: String) {
        if (isPallet(barcode)) {
            saveHandlerPallet.save(barcode)
        } else {
            saveBoxByBarcode(barcode)
        }
    }

    @SuppressLint("CheckResult")
    private fun loadDataPallet() {

        val list = listItemAction
            .value?.filter { it.type == PALLET }?.map { it.pallet!!.number!! }?: listOf()

        if (!list.isNullOrEmpty()){
            modelRx.loadInfoPalletFromServer(doc.value!!)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({
                    messageChannel.postValue("Загрузили!!")
                }, {
                    messageErrorChannel.postValue(it.message?:"")
                })
        }
    }

    @SuppressLint("CheckResult")
    private fun saveBoxByBarcode(barcode: String) {
        val dataWrapper = modelRx.getBoxByBarcode(
            barcode = barcode,
            doc = doc.value!!,
            product = product.value!!
        )

        if (dataWrapper.error != null) {
            messageErrorChannel.postValue(dataWrapper.error.message)
        } else {
            modelRx.saveBox(dataWrapper.data!!, doc.value!!)
                .subscribe({
                    commandChannel.postValue(
                        OpenForm(
                            code = Constants.OPEN_BOX_ACTION_FORM,
                            data = wrapperGuid!!.copy(guidBox = dataWrapper.data.guid)
                        )
                    )
                }, {
                    messageErrorChannel.postValue(it.message)
                })
        }
    }


}