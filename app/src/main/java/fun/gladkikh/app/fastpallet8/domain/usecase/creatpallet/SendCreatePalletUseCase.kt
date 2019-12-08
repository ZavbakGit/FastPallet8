package `fun`.gladkikh.app.fastpallet8.domain.usecase.creatpallet

import `fun`.gladkikh.app.fastpallet8.domain.model.Status
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.ItemListDocument
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.CreatePallet
import `fun`.gladkikh.app.fastpallet8.network.ApiFactory
import `fun`.gladkikh.app.fastpallet8.network.intity.SendDocumentsReqest
import `fun`.gladkikh.app.fastpallet8.network.intity.SendDocumentsResponse
import `fun`.gladkikh.app.fastpallet8.network.intity.old.metaobj.BoxServer
import `fun`.gladkikh.app.fastpallet8.network.intity.old.metaobj.CreatePalletServer
import `fun`.gladkikh.app.fastpallet8.network.intity.old.metaobj.PalletServer
import `fun`.gladkikh.app.fastpallet8.network.intity.old.metaobj.ProductServer
import `fun`.gladkikh.app.fastpallet8.repository.document.DocumentRepository
import `fun`.gladkikh.app.fastpallet8.repository.setting.SettingsRepository
import `fun`.gladkikh.app.fastpallet8.domain.model.Type
import io.reactivex.Completable
import io.reactivex.Single

class SendCreatePalletUseCase(
    private val repository: DocumentRepository,
    private val settingsRepository: SettingsRepository,
    private val apiFactory: ApiFactory
) {

    private fun checkEditDocByStatusFlowable(itemListDocument: ItemListDocument?): Single<ItemListDocument> {
        return Single.just(itemListDocument)
            .flatMap {
                if (itemListDocument!!.status in listOf(Status.LOADED, Status.NEW)) {
                    Single.just(itemListDocument)
                } else {
                    Single.error<Throwable>(Throwable("Нельзя изменять документ с этим статусом!!!"))
                }
            }
            .map {
                it as ItemListDocument
            }
    }

    fun send(itemListDocument: ItemListDocument): Completable {
        return checkEditDocByStatusFlowable(itemListDocument)
            .map {
                val docDb =
                    repository.getCreatePalletByGuid(itemListDocument.guid)!!.toCreatePalletServer()

                val objReqest = SendDocumentsReqest(
                    settingsRepository.settingApp?.code ?: "",
                    list = listOf(docDb)
                )

                return@map objReqest
            }
            .flatMap {
                apiFactory.request(
                    command = "command_send_doc",
                    objRequest = it,
                    classResponse = SendDocumentsResponse::class.java
                )
            }
            .map {
                it as SendDocumentsResponse
            }
            .doOnSuccess { response ->
                response.listConfirm.forEach {
                    val docSave =
                        repository.getCreatePalletByGuidServer(it.guid)

                    repository.save(docSave!!.copy(status = Status.getStatusByString(it.status)))
                }
            }
            .ignoreElement()

    }

    private fun CreatePallet.toCreatePalletServer(): CreatePalletServer {
        return CreatePalletServer(
            guid = this.guid,
            barcode = this.barcode,
            status = this.status!!,
            number = this.number,
            description = this.description,
            date = this.date,
            dataChanged = this.dateChanged,
            guidServer = this.guidServer,

            isWasLoadedLastTime = this.isLastLoad,
            typeFromServer = Type.CREATE_PALLET.nameServer,
            listProduct = getListProduct(this.guid)
        )
    }


    private fun getListProduct(guidDoc: String): List<ProductServer> {
        return repository.getListProduct(guidDoc)
            .map {
                ProductServer(
                    guid = it.guid,
                    weightBarcode = it.weightBarcode,
                    nameProduct = it.nameProduct,
                    countBox = it.countBox ?: 0,
                    count = it.count ?: 0f,
                    codeProduct = it.codeProduct,
                    countPallet = it.countPallet ?: 0,
                    ed = it.ed,
                    edCoff = it.edCoff ?: 0f,
                    weightCoffProduct = it.weightCoffProduct ?: 0f,
                    weightEndProduct = it.weightEndProduct ?: 0,
                    weightStartProduct = it.weightStartProduct ?: 0,
                    number = it.number,
                    barcode = it.barcode,
                    dataChanged = it.dateChanged,
                    isWasLoadedLastTime = it.isLastLoad,
                    guidProduct = it.guidProductBack,
                    boxes = listOf(),
                    pallets = getListPallet(it.guid)
                )
            }
    }

    private fun getListPallet(guidProduct: String): List<PalletServer> {
        return repository.getListPallet(guidProduct)
            .map {
                PalletServer(
                    guid = it.guid,
                    guidProduct = it.guidProduct,
                    dataChanged = it.dateChanged,
                    barcode = it.barcode,
                    number = it.number,
                    count = it.count ?: 0f,
                    countBox = it.countBox ?: 0,
                    nameProduct = it.nameProduct,
                    state = it.state,
                    sclad = it.sclad,
                    boxes = getListBox(it.guid)


                )
            }
    }

    private fun getListBox(guidPallet: String): List<BoxServer> {
        return repository.getListBox(guidPallet)
            .map {
                BoxServer(
                    guid = it.guid,
                    countBox = it.countBox ?: 0,
                    barcode = it.barcode,
                    data = it.dateChanged,
                    weight = it.count ?: 0f
                )
            }
    }


}