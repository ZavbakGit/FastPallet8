package `fun`.gladkikh.app.fastpallet8.domain.usecase.action

import `fun`.gladkikh.app.fastpallet8.common.getFloatByParseStr
import `fun`.gladkikh.app.fastpallet8.common.getIntByParseStr
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.Action
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.ProductAction
import `fun`.gladkikh.app.fastpallet8.domain.model.Status
import `fun`.gladkikh.app.fastpallet8.network.intity.DocResponse
import `fun`.gladkikh.app.fastpallet8.repository.document.DocumentRepository
import java.util.*

class SaveLoadedActionUseCase(private val repository: DocumentRepository) {

    fun save(docResponse: DocResponse){

        val docFromServer = Action(
            guid = UUID.randomUUID().toString(),
            dateChanged = Date(),
            isLastLoad = true,
            number = docResponse.number,
            barcode = null,
            date = docResponse.date,
            description = docResponse.description,
            guidServer = docResponse.guid,
            status = Status.getStatusByString(docResponse.status),
            typeFromServer1 = docResponse.type
        )


        val docDb: Action? = docFromServer.guidServer?.let {
            getDocFromDb(it)
        }

        val docForSave = if (docDb != null) {
            Action(
                guid = docDb.guid,
                dateChanged = Date(),
                isLastLoad = true,
                number = docFromServer.number,
                barcode = docFromServer.barcode,
                status = docFromServer.status,
                guidServer = docFromServer.guidServer,
                description = docFromServer.description,
                date = docFromServer.date,
                typeFromServer1 = docFromServer.typeFromServer
            )
        } else {
            docFromServer
        }


        val listFromServer = (docResponse.listStringsProduct)!!
            .map {
                ProductAction(
                    guidDoc = docForSave.guid,
                    guid = UUID.randomUUID().toString(),
                    barcode = null,
                    number = it.number,
                    isLastLoad = true,
                    dateChanged = Date(),
                    nameProduct = it.nameProduct,
                    countRow = null,
                    numberView = null,
                    countBox = null,
                    count = null,
                    codeProduct = it.codeProduct,
                    countBack = it.count?.getFloatByParseStr(),
                    countBoxBack = it.countBox?.getIntByParseStr(),
                    countPallet = null,
                    ed = it.ed,
                    edCoff = it.edCoff?.getFloatByParseStr(),
                    guidProductBack = it.guidProduct,
                    weightBarcode = null,
                    weightCoffProduct = it.weightCoffProduct?.getFloatByParseStr(),
                    weightEndProduct = it.weightEndProduct?.getIntByParseStr(),
                    weightStartProduct = it.weightStartProduct?.getIntByParseStr()
                )
            }


        val listDb = getListFromDb(docForSave.guid)


        //Удаляем где нет паллет и нет в базе списке
        val listForDell = listDb.filter { prod ->
            (prod.countPallet ?: 0 == 0 && prod.count?:0f == 0f )
                    && prod.guidProductBack !in listFromServer.map { it.guidProductBack }
        }

        val listForSave = listFromServer.map { prodServer ->

            val prodDb = listDb.find { it.guidProductBack == prodServer.guidProductBack }

            val product =
                if (prodDb != null) {
                    ProductAction(
                        guid = prodDb.guid,
                        barcode = prodDb.barcode,
                        isLastLoad = true,
                        dateChanged = Date(),
                        weightStartProduct = prodDb.weightStartProduct,
                        weightEndProduct = prodDb.weightEndProduct,
                        weightCoffProduct = prodDb.weightCoffProduct,
                        weightBarcode = prodDb.weightBarcode,
                        guidProductBack = prodDb.guidProductBack,
                        guidDoc = prodDb.guidDoc,
                        countPallet = prodDb.countPallet,
                        count = prodDb.count,
                        countBox = prodDb.countBox,
                        numberView = null,
                        countRow = prodDb.countRow,
                        countBoxBack = prodServer.countBoxBack,
                        countBack = prodServer.countBack,
                        codeProduct = prodServer.codeProduct,
                        edCoff = prodServer.edCoff,
                        ed = prodServer.ed,
                        nameProduct = prodServer.nameProduct,
                        number = prodServer.number
                    )

                } else {
                    prodServer
                }

            return@map product

        }

        //Очистим заказанное
        val listClearCount = listDb.filter {
            it.guid !in listForDell.map { it.guid }
        }.filter {
            it.guid !in listForSave.map { it.guid }
        }.map {
            it.copy(countBack = 0f, countBoxBack = 0)
        }

        repository.saveActionFromServer(
            doc = docForSave,
            listSave = listForSave +listClearCount,
            lisDell = listForDell
        )

    }

    private fun getWrapDocListProductLoad(docResponse: DocResponse): WrapDocListProduct {
        val docFromServer = Action(
            guid = UUID.randomUUID().toString(),
            dateChanged = Date(),
            isLastLoad = true,
            number = docResponse.number,
            barcode = null,
            date = docResponse.date,
            description = docResponse.description,
            guidServer = docResponse.guid,
            status = Status.getStatusByString(docResponse.status),
            typeFromServer1 = docResponse.type
        )

        val listProduct = (docResponse.listStringsProduct)!!
            .map {
                ProductAction(
                    guid = UUID.randomUUID().toString(),
                    barcode = null,
                    number = it.number,
                    isLastLoad = true,
                    dateChanged = Date(),
                    nameProduct = it.nameProduct,
                    countRow = null,
                    numberView = null,
                    countBox = null,
                    count = null,
                    codeProduct = it.codeProduct,
                    countBack = it.count?.getFloatByParseStr(),
                    countBoxBack = it.countBox?.getIntByParseStr(),
                    countPallet = null,
                    ed = it.ed,
                    edCoff = it.edCoff?.getFloatByParseStr(),
                    guidDoc = docFromServer.guid,
                    guidProductBack = it.guidProduct,
                    weightBarcode = null,
                    weightCoffProduct = it.weightCoffProduct?.getFloatByParseStr(),
                    weightEndProduct = it.weightEndProduct?.getIntByParseStr(),
                    weightStartProduct = it.weightStartProduct?.getIntByParseStr()
                )
            }

        return WrapDocListProduct(docFromServer, listProduct)
    }

    private fun getDocFromDb(guidServer: String): Action? {
        return repository.getActionByGuidServer(guidServer)
    }

    private fun getListFromDb(guidDoc: String?): List<ProductAction> {
        return if (guidDoc != null){
            repository.getListProductAction(guidDoc)
        }else{
            listOf()
        }
    }

    private data class WrapDocListProduct(
        val doc: Action,
        val list: List<ProductAction>
    )

}