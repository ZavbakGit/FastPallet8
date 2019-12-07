package `fun`.gladkikh.app.fastpallet8.domain.usecase.testdata


import `fun`.gladkikh.app.fastpallet8.common.toSimpleDate
import `fun`.gladkikh.app.fastpallet8.db.dao.DocumentDao
import `fun`.gladkikh.app.fastpallet8.domain.model.Status
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.BoxCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.CreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.PalletCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.ProductCreatePallet
import `fun`.gladkikh.app.fastpallet8.map.toDb

import java.util.*

class AddTestDataUseCase(private val dao: DocumentDao) {

    fun save() {

        var countBox = 0

        val listDocuments =
            (0..5).map {
                CreatePallet(
                    guid = it.toString(),
                    status = Status.LOADED,
                    number = it.toString(),
                    guidServer = it.toString(),
                    isLastLoad = true,
                    description = "Формирование паллет №${it} от ${Date().toSimpleDate()}",
                    date = Date(),
                    dateChanged = Date(),
                    barcode = it.toString()
                )
            }



        listDocuments.forEach { doc ->
            dao.insertOrUpdate(doc.toDb())
            val listProduct = getListProduct(doc.guid)

            listProduct.forEach { prod ->
                dao.insertOrUpdate(prod.toDb())

                val listPallet = getListPallets(prod.guid)

                listPallet.forEach { pall ->
                    dao.insertOrUpdate(pall.toDb())
                    val listBox = getListBox(pall.guid)
                    listBox.forEach {
                        dao.insertOrUpdate(it.toDb())
                        println(it.guid)
                    }
                }
            }
        }

        println(countBox)
    }

    private fun getListProduct(guidDoc: String): List<ProductCreatePallet> {
        return (0..10).map {
            ProductCreatePallet(
                guid = guidDoc + "_" + it,
                guidDoc = guidDoc,
                nameProduct = "Продукт $it",
                dateChanged = Date(),
                barcode = "3131116165165165",
                number = "1",
                isLastLoad = true,
                countBox = 10,
                count = 150f,
                codeProduct = "А00" + it,
                ed = "кг.",
                guidProductBack = "jhgkjhkj6455465",
                edCoff = 1f,
                weightBarcode = "12345515454",
                countPallet = 5,
                weightCoffProduct = 0.1f,
                weightStartProduct = 1,
                weightEndProduct = 4,
                countRow = null,
                countBoxBack = 50,
                countBack = 562.568f
            )
        }
    }

    private fun getListPallets(guidProduct: String): List<PalletCreatePallet> {
        return (0..100).map {
            PalletCreatePallet(
                guid = guidProduct + "_" + it,
                guidProduct = guidProduct,
                number = guidProduct + "_" + it,
                barcode = "65465546546548",
                sclad = "Основной",
                dateChanged = Date(),
                count = null,
                countBox = null,
                state = null,
                nameProduct = "Продукт " + guidProduct + "_" + it,
                countRow = null
            )
        }
    }

    private fun getListBox(guidPallet: String): List<BoxCreatePallet> {
        return (0..100).map {
            BoxCreatePallet(
                guid = guidPallet + "_" + it,
                guidPallet = guidPallet,
                barcode = "654656516516516516",
                countBox = 1,
                dateChanged = Date(),
                count = 25.50f
            )
        }
    }
}