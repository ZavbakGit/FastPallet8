package `fun`.gladkikh.app.fastpallet8.ui.screen.infopallet

import `fun`.gladkikh.app.fastpallet8.Constants
import `fun`.gladkikh.app.fastpallet8.common.getNumberDocByBarCode
import `fun`.gladkikh.app.fastpallet8.common.isPallet
import `fun`.gladkikh.app.fastpallet8.domain.model.infopallet.InfoPalletModelRx
import `fun`.gladkikh.app.fastpallet8.ui.base.BaseViewModel
import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import java.util.*

class InfoPalletViewModel(val model: InfoPalletModelRx) : BaseViewModel() {
    private val listItem = MutableLiveData<List<ItemInfoPallet>>()
    fun getListItem(): LiveData<List<ItemInfoPallet>> = listItem

    //Нажатие клавиш
    override fun callKeyDown(keyCode: Int?, position: Int?) {
        super.callKeyDown(keyCode, position)
        when (keyCode) {
            Constants.KEY_9 -> {
                listItem.postValue(listOf())
            }

            Constants.KEY_5 -> {
                loadDataPallet()
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun loadDataPallet() {

        val list = listItem
            .value?.map { it.number }

        if (!list.isNullOrEmpty()) {
            model.loadInfoPalletFromServer(list)
                .doOnSuccess { listInfo ->
                    listItem.postValue(
                        listItem.value?.map { item ->
                            ItemInfoPallet(
                                number = item.number,
                                dateAdd = item.dateAdd,
                                infoPallet = listInfo.find { it.code == item.number }
                            )
                        })
                    messageChannel.postValue("Загрузили!")
                }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                    {
                    },
                    {
                        messageErrorChannel.postValue(it.message ?: "")
                    })
        } else {
            messageErrorChannel.postValue("Нет паллет!")
        }
    }

    fun readBarcode(barcode: String) {
        if (!isPallet(barcode)) {
            messageErrorChannel.postValue("Это не паллета!")
        } else {
            val number = getNumberDocByBarCode(barcode)
            val list = listItem.value ?: listOf()

            listItem.postValue(
                if (list.find { it.number == number } == null) {
                    list + ItemInfoPallet(
                        number = number,
                        dateAdd = Date()
                    )
                } else {
                    list.map {
                        if (it.number == number) {
                            return@map it.copy(dateAdd = Date())
                        } else {
                            return@map it
                        }
                    }
                }.sortedByDescending { it.dateAdd }
            )
        }
    }

}