package `fun`.gladkikh.app.fastpallet8.domain.usecase.documents

import `fun`.gladkikh.app.fastpallet8.domain.model.Type
import `fun`.gladkikh.app.fastpallet8.domain.usecase.action.SaveLoadedActionUseCase
import `fun`.gladkikh.app.fastpallet8.domain.usecase.creatpallet.SaveLoadedCreatePalletUseCase
import `fun`.gladkikh.app.fastpallet8.network.ApiFactory
import `fun`.gladkikh.app.fastpallet8.repository.setting.SettingsRepository
import io.reactivex.Completable

class LoadDocumentsUseCase(
    settingsRepository: SettingsRepository,
    apiFactory: ApiFactory,
    private val saveLoadedCreatePalletUseCase: SaveLoadedCreatePalletUseCase,
    private val saveLoadedActionUseCase: SaveLoadedActionUseCase
) {
    private val singleDocResponse = getListDocumentsDbFromServer(
        settingsRepository
        , apiFactory
    )

    fun getLoadCompletable(): Completable {
        return singleDocResponse
            .doOnSuccess {
                it.forEach { doc ->

                    when (doc.type) {
                        "ФормированиеПалет" -> {
                            saveLoadedCreatePalletUseCase.save(doc)
                        }
                        "РеализацияТоваровУслуг", "СписаниеТоваров",
                        "ПеремещениеТоваров", "ИнвентиризацияМестВДокументе" -> {
                            saveLoadedActionUseCase.save(doc)
                        }
                    }
                }
            }
            .doOnError {
                println(it)
            }
            .ignoreElement()

    }
}