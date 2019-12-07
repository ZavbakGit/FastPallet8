package `fun`.gladkikh.app.fastpallet8.domain.usecase.documents


import `fun`.gladkikh.app.fastpallet8.domain.usecase.creatpallet.SaveLoadedCreatePalletUseCase
import `fun`.gladkikh.app.fastpallet8.network.ApiFactory
import `fun`.gladkikh.app.fastpallet8.network.intity.DocResponse
import `fun`.gladkikh.app.fastpallet8.network.intity.GetListDocsRequest
import `fun`.gladkikh.app.fastpallet8.network.intity.ListDocResponse
import `fun`.gladkikh.app.fastpallet8.repository.SettingsRepository
import io.reactivex.Single

/**
 * 1. Получаем ответ со списком документов DocResponse
 * 2. в confirmLoadDocuments отправляем подтверждение в виде guid типа документа
 *    получаем ответ, сравниваем со старым полученным списком вдруг с 1С, что то не дошло
 *    проставляем новый пришедщий в подтверждении статус
 * 4. Потом при сохранении не будем убивать все
 */
fun getListDocumentsDbFromServer(
    settingsRepository: SettingsRepository,
    apiFactory: ApiFactory
): Single<List<DocResponse>> {

    val settingsPref = settingsRepository.getSetting()

    return Single.just(settingsPref)
        .map {
            if (it.code.isNullOrEmpty()) {
                throw Throwable("Не заполнен код ТСД")
            }
            return@map GetListDocsRequest(codeTSD = settingsPref.code!!)
        }.flatMap {
            apiFactory.request(
                command = "command_get_doc",
                objRequest = it,
                classResponse = ListDocResponse::class.java
            )
        }
        .map {
            it as ListDocResponse
        }
        .flatMap {
            //Отправляем подтверждение и проверяем что в 1С применился новый статус
            confirmLoadDocuments(
                listDocuments = it.listDocuments!!,
                settingsPref = settingsPref,
                apiFactory = apiFactory
            )
        }
}



