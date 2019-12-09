package `fun`.gladkikh.app.fastpallet8.domain.usecase.testdata

import `fun`.gladkikh.app.fastpallet8.common.toSimpleDate
import `fun`.gladkikh.app.fastpallet8.db.dao.MainDao
import `fun`.gladkikh.app.fastpallet8.domain.model.Status
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.Action
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.BoxAction
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.PalletAction
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.ProductAction
import `fun`.gladkikh.app.fastpallet8.map.toDb
import java.util.*

class AddTestDataActionUseCase (private val dao: MainDao) {

    fun save(){

        var countBox = 0

        val listDocuments =
            (0..5).map {
                Action(
                    guid = it.toString()+"_a",
                    status = Status.LOADED,
                    number = it.toString(),
                    guidServer = it.toString(),
                    isLastLoad = true,
                    description = "Реализация №${it} от ${Date().toSimpleDate()}",
                    date = Date(),
                    dateChanged = Date(),
                    barcode = it.toString()
                )
            }

        listDocuments.forEach { doc ->
            dao.insertOrUpdate(doc.toDb())
            Thread.sleep(100)
            val listProduct = getListProduct(doc.guid)

            listProduct.forEach { prod ->
                dao.insertOrUpdate(prod.toDb())
                Thread.sleep(100)

                val listPallet = getListPallets(prod.guid)
                listPallet.forEach { pall ->
                    //dao.insertOrUpdate(pall.toDb())
                    Thread.sleep(100)
                }

                val listBox = getListBox(prod.guid)
                listBox.forEach { box ->
                    dao.insertOrUpdate(box.toDb())
                    Thread.sleep(100)
                }

            }
        }

        //println(countBox)

    }

    private fun getListProduct(guidDoc: String): List<ProductAction> {
        return (0..3).map {
            ProductAction(
                guid = guidDoc + "_" + it,
                guidDoc = guidDoc,
                nameProduct = "Продукт $it",
                dateChanged = Date(),
                barcode = "3131116165165165",
                number = "1",
                isLastLoad = true,
                countBox = 0,
                count = 0f,
                codeProduct = "А00" + it,
                ed = "кг.",
                guidProductBack = "jhgkjhkj6455465",
                edCoff = 1f,
                weightBarcode = "12345515454",
                countPallet = 0,
                weightCoffProduct = 0.1f,
                weightStartProduct = 1,
                weightEndProduct = 4,
                countRow = null,
                countBoxBack = 50,
                countBack = 562.568f
            )
        }
    }

    private fun getListPallets(guidProduct: String): List<PalletAction> {
        return (0..1).map {
            PalletAction(
                guid = guidProduct + "_" + it,
                guidProduct = guidProduct,
                number = guidProduct + "_" + it,
                barcode = "65465546546548",
                sclad = "Основной",
                dateChanged = Date(),
                count = 10f,
                countBox = 10,
                state = null,
                nameProduct = "Продукт " + guidProduct + "_" + it,
                countRow = null
            )
        }
    }

    private fun getListBox(guidProduct: String): List<BoxAction> {
        return (0..1).map {
            BoxAction(
                guid = guidProduct + "_" + it,
                guidProduct = guidProduct,
                barcode = "654656516516516516",
                countBox = 1,
                dateChanged = Date(),
                count = 20f
            )
        }
    }

}