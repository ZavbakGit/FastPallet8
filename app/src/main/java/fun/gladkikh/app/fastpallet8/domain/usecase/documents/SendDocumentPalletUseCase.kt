package `fun`.gladkikh.app.fastpallet8.domain.usecase.documents

import `fun`.gladkikh.app.fastpallet8.domain.model.Status
import `fun`.gladkikh.app.fastpallet8.domain.entity.ItemListDocument
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.Action
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.CreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.InventoryPallet
import `fun`.gladkikh.app.fastpallet8.network.ApiFactory
import `fun`.gladkikh.app.fastpallet8.network.intity.SendDocumentsReqest
import `fun`.gladkikh.app.fastpallet8.network.intity.SendDocumentsResponse
import `fun`.gladkikh.app.fastpallet8.repository.document.DocumentRepository
import `fun`.gladkikh.app.fastpallet8.repository.setting.SettingsRepository
import `fun`.gladkikh.app.fastpallet8.domain.model.Type
import `fun`.gladkikh.app.fastpallet8.network.intity.old.MetaObjServer
import `fun`.gladkikh.app.fastpallet8.network.intity.old.metaobj.*
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

class SendDocumentPalletUseCase(
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


    private fun getSingleDoc(itemListDocument: ItemListDocument): Single<*> {
        return when (itemListDocument.type) {
            Type.CREATE_PALLET -> {
                return Single.just(
                    repository.getCreatePalletByGuid(
                        itemListDocument.guid
                    )!!.toServerDoc()
                )
            }
            Type.ACTION_PALLET -> {
                return Single.just(
                    repository.getActionByGuid(
                        itemListDocument.guid
                    )!!.toServerDoc()
                )
            }
            Type.INVENTORY_PALLET -> {
                return Single.just(
                    repository.getInventoryPalletByGuid(
                        itemListDocument.guid
                    )!!.toServerDoc()
                )
            }

            else -> {
                return Single.error<Throwable>(Throwable("Неизвестный тип!"))
            }
        }
    }

    fun send(itemListDocument: ItemListDocument): Completable {
        return checkEditDocByStatusFlowable(itemListDocument)
            .flatMap {
                return@flatMap getSingleDoc(itemListDocument)
            }
            .map {
                it as MetaObjServer
            }
            .map {
                val objReqest = SendDocumentsReqest(
                    settingsRepository.settingApp?.code ?: "",
                    list = listOf(it)
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

                    val type = itemListDocument.type

                    when (type) {
                        Type.CREATE_PALLET -> {
                            val docSave =
                                repository.getCreatePalletByGuidServer(it.guid)
                            repository.save(docSave!!.copy(status = Status.getStatusByString(it.status)))
                        }
                        Type.INVENTORY_PALLET -> {
                            val docSave = repository.getInventoryPalletByGuid(it.guid)
                            repository.save(docSave!!.copy(status = Status.getStatusByString(it.status)))
                        }
                        Type.ACTION_PALLET -> {
                            val docSave = repository.getActionByGuidServer(it.guid)
                            repository.save(docSave!!.copy(status = Status.getStatusByString(it.status)))
                        }
                    }


                }
            }
            .ignoreElement()

    }

    private fun CreatePallet.toServerDoc(): CreatePalletServer {

        fun getListBox(guidPallet: String): List<BoxServer> {
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

        fun getListPallet(guidProduct: String): List<PalletServer> {
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

        fun getListProduct(guidDoc: String): List<ProductServer> {
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

    private fun InventoryPallet.toServerDoc(): InventoryPalletServer {

        fun getListBox(doc: InventoryPallet): List<BoxServer> {
            return repository.getListBoxInventoryPallet(doc.guid)
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

        fun getStringProduct(doc: InventoryPallet): ProductServer {
            return ProductServer(
                guid = UUID.randomUUID().toString(),
                isWasLoadedLastTime = false,
                dataChanged = doc.dateChanged,
                number = doc.number,
                barcode = doc.barcode,
                weightCoffProduct = doc.weightCoffProduct ?: 0f,
                weightEndProduct = doc.weightStartProduct ?: 0,
                weightStartProduct = doc.weightEndProduct ?: 0,
                nameProduct = doc.nameProduct,
                weightBarcode = doc.weightBarcode,
                count = doc.count ?: 0f,
                countBox = doc.countBox ?: 0,
                guidProduct = doc.guidBackProduct,
                pallets = listOf(),
                edCoff = 0f,
                ed = null,
                countPallet = 1,
                codeProduct = doc.guid,
                boxes = getListBox(doc)
            )

        }

        return InventoryPalletServer(
            guid = guid,
            barcode = barcode,
            description = description,
            guidServer = guidServer,
            number = number,
            date = date,
            status = status,
            barcodePallet = barcodePallet,
            numberPallet = numberPallet,
            type = type,
            dataChanged = dateChanged,
            isWasLoadedLastTime = isLastLoad,
            typeFromServer = Type.INVENTORY_PALLET.nameServer,
            stringProduct = getStringProduct(this)


        )


    }

    private fun Action.toServerDoc(): ActionPalletServer {

        fun getListBox(guidProduct: String): List<BoxServer> {
            return repository.getListBoxAction(guidProduct)
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

        fun getListPallet(guidProduct: String): List<PalletServer> {
            return repository.getListPalletAction(guidProduct)
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

        fun getListProduct(guidDoc: String): List<ProductServer> {
            return repository.getListProductAction(guidDoc)
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
                        boxes = getListBox(it.guid),
                        pallets = getListPallet(it.guid)
                    )
                }
        }

        return ActionPalletServer(
            guid = guid,
            barcode = barcode,
            description = description,
            guidServer = guidServer,
            number = number,
            date = date,
            status = status,
            type = type,
            dataChanged = dateChanged,
            isWasLoadedLastTime = isLastLoad,
            typeFromServer = typeFromServer,
            stringProducts = getListProduct(guid)
        )
    }


}