package `fun`.gladkikh.app.fastpallet8.domain.usecase.creatpallet

import `fun`.gladkikh.app.fastpallet8.common.getFloatByParseStr
import `fun`.gladkikh.app.fastpallet8.common.getIntByParseStr
import `fun`.gladkikh.app.fastpallet8.domain.model.Status
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.CreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.ProductCreatePallet
import `fun`.gladkikh.app.fastpallet8.network.intity.DocResponse
import `fun`.gladkikh.app.fastpallet8.repository.document.DocumentRepository
import java.util.*

class SaveLoadedCreatePalletUseCase(private val repository: DocumentRepository) {

    fun save(docResponse: DocResponse){

        val docFromServer = CreatePallet(
            guid = UUID.randomUUID().toString(),
            dateChanged = Date(),
            isLastLoad = true,
            number = docResponse.number,
            barcode = null,
            date = docResponse.date,
            description = docResponse.description,
            guidServer = docResponse.guid,
            status = Status.getStatusByString(docResponse.status)
        )


        val docDb: CreatePallet? = getDocFromDb(docFromServer.guidServer!!)

        val docForSave = if (docDb != null) {
            CreatePallet(
                guid = docDb.guid,
                dateChanged = Date(),
                isLastLoad = true,
                number = docFromServer.number,
                barcode = docFromServer.barcode,
                status = docFromServer.status,
                guidServer = docFromServer.guidServer,
                description = docFromServer.description,
                date = docFromServer.date
            )
        } else {
            docFromServer
        }


        val listFromServer = (docResponse.listStringsProduct)!!
            .map {
                ProductCreatePallet(
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
            prod.countPallet ?: 0 == 0 && prod.guidProductBack !in listFromServer.map { it.guidProductBack }
        }

        val listForSave = listFromServer.map { prodServer ->

            val prodDb = listDb.find { it.guidProductBack == prodServer.guidProductBack }

            val product =
                if (prodDb != null) {
                    ProductCreatePallet(
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

        repository.saveCreatePalletFromServer(
            doc = docForSave,
            listSave = listForSave,
            lisDell = listForDell
        )

    }

    private fun getWrapDocListProductLoad(docResponse: DocResponse): WrapDocListProduct {
        val docFromServer = CreatePallet(
            guid = UUID.randomUUID().toString(),
            dateChanged = Date(),
            isLastLoad = true,
            number = docResponse.number,
            barcode = null,
            date = docResponse.date,
            description = docResponse.description,
            guidServer = docResponse.guid,
            status = Status.getStatusByString(docResponse.status)
        )

        val listProduct = (docResponse.listStringsProduct)!!
            .map {
                ProductCreatePallet(
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

    private fun getDocFromDb(guidServer: String): CreatePallet? {
        return repository.getCreatePalletByGuidServer(guidServer)
    }

    private fun getListFromDb(guidDoc: String?): List<ProductCreatePallet> {
        return if (guidDoc != null){
            repository.getListProduct(guidDoc)
        }else{
            listOf()
        }
    }

    private data class WrapDocListProduct(
        val doc: CreatePallet,
        val list: List<ProductCreatePallet>
    )

}