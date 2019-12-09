package `fun`.gladkikh.app.fastpallet8.domain.usecase.testdata

import `fun`.gladkikh.app.fastpallet8.common.toSimpleDate
import `fun`.gladkikh.app.fastpallet8.db.dao.MainDao
import `fun`.gladkikh.app.fastpallet8.domain.model.Status
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.BoxInventoryPallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.InventoryPallet
import `fun`.gladkikh.app.fastpallet8.map.toDb
import java.util.*

class AddTestDataInventoryPalletUseCase(private val dao: MainDao) {

    fun save() {

        var countBox = 0

        val listDocuments =
            (0..5).map {
                InventoryPallet(
                    guid = it.toString()+"ip",
                    status = Status.LOADED,
                    number = it.toString(),
                    guidServer = it.toString(),
                    isLastLoad = true,
                    description = "Инвентаризация паллеты №${it} от ${Date().toSimpleDate()}",
                    date = Date(),
                    dateChanged = Date(),
                    barcode = it.toString(),
                    weightStartProduct = 1,
                    weightEndProduct = 4,
                    weightCoffProduct = 0.1f,
                    weightBarcode = null,
                    guidBackProduct = null,
                    count = 0f,
                    countBox = 0,
                    nameProduct = "Паллета №${{ it }}",
                    countRow = 0,
                    barcodePallet = it.toString(),
                    numberPallet = "$it"

                )
            }

        listDocuments.forEach { doc ->
            dao.insertOrUpdate(doc.toDb())
            Thread.sleep(100)


            val listBox = getListBox(doc.guid)
            listBox.forEach { box ->
                dao.insertOrUpdate(box.toDb())
                Thread.sleep(100)
            }
        }

        //println(countBox)

    }

    private fun getListBox(guidDoc: String): List<BoxInventoryPallet> {
        return (0..7).map {
            BoxInventoryPallet(
                guid = guidDoc + "_" + it,
                guidDoc = guidDoc,
                barcode = "654656516516516516",
                countBox = 1,
                dateChanged = Date(),
                count = 20f
            )
        }
    }

}