package `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet

import `fun`.gladkikh.app.fastpallet8.domain.model.Status
import `fun`.gladkikh.app.fastpallet8.domain.model.Type
import `fun`.gladkikh.app.fastpallet8.domain.entity.document.Document
import java.util.*


data class InventoryPallet(
    override val guid: String,
    override val number: String?,
    override val date: Date?,
    override val status: Status?,
    override var guidServer: String?,
    override var dateChanged: Date?,
    override var isLastLoad: Boolean?,
    override var description: String?,
    override var barcode: String?,

    var numberPallet: String?,

    var barcodePallet: String?,
    var nameProduct:String?,
    var guidBackProduct:String?,

    var weightBarcode: String?,
    var weightStartProduct: Int?,
    var weightEndProduct: Int?,
    var weightCoffProduct: Float?,

    var count: Float?, //Количество
    var countBox: Int?, //Количество мест
    var countRow: Int?  //Количество строк
) : Document(Type.INVENTORY_PALLET)



data class BoxInventoryPallet(
    val guid: String,
    var guidDoc: String,
    var barcode: String?,

    var count: Float?, //Количество
    var countBox: Int?, //Количество мест

    var dateChanged: Date?,
    /**
     * Порядковый номер заполнится в списке
     */
    var numberView: Int? = null
)