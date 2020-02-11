package `fun`.gladkikh.app.fastpallet8.domain.entity.action

import `fun`.gladkikh.app.fastpallet8.domain.model.Status
import `fun`.gladkikh.app.fastpallet8.domain.model.Type
import `fun`.gladkikh.app.fastpallet8.domain.entity.document.Document
import java.util.*


data class Action(
    override val guid: String,
    override val number: String?,
    override val date: Date?,
    override val status: Status?,
    override var guidServer: String?,
    override var dateChanged: Date?,
    override var isLastLoad: Boolean?,
    override var description: String?,
    override var barcode: String?,
    val typeFromServer1: String? = null

) : Document(Type.ACTION_PALLET, typeFromServer1)

data class ProductAction(
    val guid: String,
    val guidDoc: String,
    var number: String?,
    var barcode: String?,

    var guidProductBack: String?,
    var nameProduct: String?,
    var codeProduct: String?,
    var ed: String?,
    var edCoff: Float?,

    var weightBarcode: String?,
    var weightStartProduct: Int?,
    var weightEndProduct: Int?,
    var weightCoffProduct: Float?,

    var countBack: Float?,  //Количество из Back
    var countBoxBack: Int?, //Количество Мест Back

    var count: Float?, //Количество
    var countBox: Int?, //Количество мест
    var countRow: Int?,  //Количество строк
    var countPallet: Int?, //Количество паллет

    var dateChanged: Date?,
    var isLastLoad: Boolean?,

    var numberView: Int? = null
)

data class InfoPallet(
    val guid: String,
    var guidProduct: String,
    var number: String?,
    var barcode: String?,

    var dateChanged: Date?,
    var nameProduct: String?,
    var state: String?,
    var sclad: String?,

    var count: Float?, //Количество
    var countBox: Int?, //Количество мест
    var countRow: Int?,  //Количество строк

    var numberView: Int? = null
)

data class BoxAction(
    val guid: String,
    var guidProduct: String,
    var barcode: String?,

    var count: Float?, //Количество
    var countBox: Int?, //Количество мест

    var dateChanged: Date?,
    /**
     * Порядковый номер заполнится в списке
     */
    var numberView: Int? = null
)