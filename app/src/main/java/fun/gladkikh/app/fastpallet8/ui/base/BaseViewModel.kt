package `fun`.gladkikh.app.fastpallet8.ui.base


import `fun`.gladkikh.app.fastpallet8.ui.common.Command
import `fun`.gladkikh.app.fastpallet8.ui.common.Command.ConfirmDialog
import `fun`.gladkikh.app.fastpallet8.ui.common.Command.EditNumberDialog
import `fun`.gladkikh.app.fastpallet8.ui.common.SingleLiveEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {

    protected val messageChannel = SingleLiveEvent<String>()
    protected val messageErrorChannel = SingleLiveEvent<String>()
    protected val showProgressChannel = MutableLiveData<Boolean>()
    protected val commandChannel = SingleLiveEvent<Command>()

    fun getCommandChannel(): LiveData<Command> = commandChannel
    fun getMessageChannel(): LiveData<String> = messageChannel
    fun getMessageErrorChannel(): LiveData<String> = messageErrorChannel
    fun getShowProgressChannel(): LiveData<Boolean> = showProgressChannel


    open fun callKeyDown(keyCode: Int? = null,position:Int? = null){

    }

    open fun callBackConfirmDialog(confirmDialog: ConfirmDialog) {

    }

    open fun callBackEditNumberDialog(editNumberDialog: EditNumberDialog) {

    }

    val compositeDisposable = CompositeDisposable()
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


}