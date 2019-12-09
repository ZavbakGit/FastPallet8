package `fun`.gladkikh.app.fastpallet8.domain.usecase.documents


import `fun`.gladkikh.app.fastpallet8.domain.entity.SettingApp
import `fun`.gladkikh.app.fastpallet8.network.ApiFactory
import `fun`.gladkikh.app.fastpallet8.network.intity.ConfirmDocumentsLoadRequest
import `fun`.gladkikh.app.fastpallet8.network.intity.ConfirmResponse
import `fun`.gladkikh.app.fastpallet8.network.intity.DocConfirm
import `fun`.gladkikh.app.fastpallet8.network.intity.DocResponse
import `fun`.gladkikh.app.fastpallet8.domain.model.Type
import io.reactivex.Single


fun confirmLoadDocuments(
    listDocuments: List<DocResponse>
    , settingsPref: SettingApp
    , apiFactory: ApiFactory
): Single<List<DocResponse>> {


    val list = listDocuments.map {
        DocConfirm(it.guid, Type.CREATE_PALLET.nameServer)
    }


    return apiFactory.request(
        command = "command_confirm_doc",
        objRequest = ConfirmDocumentsLoadRequest(
            settingsPref.code ?: "",
            list = list
        ),
        classResponse = ConfirmResponse::class.java
    )
        .map {
            it as ConfirmResponse
        }
        .map { confirm ->
            //Сравним списки и увидим, что все пришло
            if (list.map { it.guid }.sortedBy { it } !=
                confirm.listConfirm.map { it.guid }.sortedBy { it }) {
                throw Throwable("Не верное подтверждение!")
            }

            //Проставим статус из подтверждения
            return@map listDocuments.map { docResponse ->

                val strStatus = confirm.listConfirm
                    .find { it.guid == docResponse.guid }?.status

                docResponse.status = strStatus!!
                return@map docResponse
            }
        }
}

