package `fun`.gladkikh.app.fastpallet8.domain.usecase

import `fun`.gladkikh.app.fastpallet8.common.getFloatByParseStr
import `fun`.gladkikh.app.fastpallet8.common.getIntByParseStr
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.InfoPallet
import `fun`.gladkikh.app.fastpallet8.network.ApiFactory
import `fun`.gladkikh.app.fastpallet8.network.intity.GetInfoPalletReqest
import `fun`.gladkikh.app.fastpallet8.network.intity.GetInfoPalletResponse
import `fun`.gladkikh.app.fastpallet8.repository.setting.SettingsRepository
import io.reactivex.Single

class GetInfoPalletUseCase(
    private val settingsRepository: SettingsRepository,
    private val apiFactory: ApiFactory
) {

    fun get(listNumber: List<String>): Single<List<InfoPallet>> {
        val command = "command_get_pallet_state"

        return Single.just(listNumber)
            .flatMap {
               return@flatMap apiFactory.request(
                    command = command,
                    objRequest = GetInfoPalletReqest(
                        codeTSD = settingsRepository.settingApp?.code!!,
                        list = listNumber
                    ),
                    classResponse = GetInfoPalletResponse::class.java
                )
            }
            .map {
                it as GetInfoPalletResponse
            }
            .map { response ->
                response.list.map {
                    InfoPallet(
                        code = it.PaletCode,
                        guid = it.PaletGuid,
                        count = it.PaletWeight?.getFloatByParseStr(),
                        countBox = it.PaletPlacesCount?.getIntByParseStr(),
                        sclad = it.PaletStore,
                        state = it.PaletState,
                        nameProduct = it.nameProduct,
                        guidProduct = it.guidProduct
                    )
                }
            }
    }
}