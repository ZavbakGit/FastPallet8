package `fun`.gladkikh.app.fastpallet8.domain.model.infopallet


import `fun`.gladkikh.app.fastpallet8.domain.entity.InfoPallet
import io.reactivex.Single

interface InfoPalletModelRx {
    fun loadInfoPalletFromServer(list:List<String>): Single<List<InfoPallet>>
}