package `fun`.gladkikh.app.fastpallet8.domain.model.infopallet


import `fun`.gladkikh.app.fastpallet8.domain.entity.InfoPallet
import `fun`.gladkikh.app.fastpallet8.domain.usecase.GetInfoPalletUseCase
import io.reactivex.Single

class InfoPalletModelRxImpl(private val getInfoPalletUseCase: GetInfoPalletUseCase) :
    InfoPalletModelRx {

    override fun loadInfoPalletFromServer(list: List<String>): Single<List<InfoPallet>> {
        return getInfoPalletUseCase.get(list)
    }

}