package `fun`.gladkikh.app.fastpallet8.domain.usecase.documents

import `fun`.gladkikh.app.fastpallet8.domain.usecase.creatpallet.SaveLoadedCreatePalletUseCase
import `fun`.gladkikh.app.fastpallet8.network.ApiFactory
import `fun`.gladkikh.app.fastpallet8.repository.SettingsRepository
import io.reactivex.Completable

class LoadDocumentsUseCase(
    settingsRepository: SettingsRepository,
    apiFactory: ApiFactory,
    val saveLoadedCreatePalletUseCase: SaveLoadedCreatePalletUseCase
) {
    private val singleDocResponse = getListDocumentsDbFromServer(
        settingsRepository
        , apiFactory
    )

    fun getLoadCompletable(): Completable {
        return singleDocResponse
            .doOnSuccess {
                it.forEach {doc->
                    saveLoadedCreatePalletUseCase.save(doc)
                }
            }
            .doOnError {
                println(it)
            }
            .ignoreElement()

    }
}