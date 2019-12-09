package `fun`.gladkikh.app.fastpallet8.ui.screen.action.doc


import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.domain.model.action.ActionModelRx
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.Action
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.ProductAction
import `fun`.gladkikh.app.fastpallet8.ui.base.BaseViewModel
import `fun`.gladkikh.app.fastpallet8.ui.common.Command
import `fun`.gladkikh.app.fastpallet8.ui.screen.action.WrapperGuidAction
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DocActionViewModel(private val modelRx: ActionModelRx) : BaseViewModel() {

    private val doc = MutableLiveData<Action>()
    private val listProduct = MutableLiveData<List<ProductAction>>()

    fun getDocLiveData(): LiveData<Action> = doc
    fun getListProductLiveData(): LiveData<List<ProductAction>> = listProduct

    //Все пареметры запросов
    var wrapperGuid: WrapperGuidAction? = null
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
                        it.data.mapIndexed { index, palletAction ->
                            palletAction.numberView = size - index
                            return@mapIndexed palletAction
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
                            code = Constants.OPEN_PRODUCT_ACTION_FORM,
                            data = wrapperGuid!!.copy(guidProduct = listProduct.value!![position].guid)
                        )
                    )
                }
            }
        }

    }
}