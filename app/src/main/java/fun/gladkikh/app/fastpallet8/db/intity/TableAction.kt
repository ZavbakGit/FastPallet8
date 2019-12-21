package `fun`.gladkikh.app.fastpallet8.db.intity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class ActionDb(
    @PrimaryKey @ColumnInfo(index = true) val guid: String,
    val number: String?,
    val date: Long?,
    val status: Int?,
    @ColumnInfo(index = true) var guidServer: String?,
    var dateChanged: Long?,
    var isLastLoad: Boolean?,
    var description: String?,
    var barcode: String?,
    var typeFromServer:String?
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = ActionDb::class,
        parentColumns = arrayOf("guid"),
        childColumns = arrayOf("guidDoc"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class ProductActionDb(
    @PrimaryKey @ColumnInfo(index = true) val guid: String,
    @ColumnInfo(index = true) val guidDoc: String,
    var number: String?,
    var barcode: String?,

    @ColumnInfo(index = true) var guidProductBack: String?,
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

    var dateChanged: Long?,
    var isLastLoad: Boolean?
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = ProductActionDb::class,
        parentColumns = arrayOf("guid"),
        childColumns = arrayOf("guidProduct"),
        onDelete = ForeignKey.CASCADE
    )]
)

data class PalletActionDb(
    @PrimaryKey @ColumnInfo(index = true) val guid: String,
    @ColumnInfo(index = true) var guidProduct: String,
    var number: String?,
    var barcode: String?,

    var dateChanged: Long?,
    var nameProduct: String?,
    var state: String?,
    var sclad: String?,

    var count: Float?, //Количество
    var countBox: Int?, //Количество мест
    var countRow: Int?  //Количество строк
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = ProductActionDb::class,
        parentColumns = arrayOf("guid"),
        childColumns = arrayOf("guidProduct"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class BoxActionDb(
    @PrimaryKey @ColumnInfo(index = true) val guid: String,
    @ColumnInfo(index = true) var guidProduct: String,
    var barcode: String?,

    var count: Float?, //Количество
    var countBox: Int?, //Количество мест

    var dateChanged: Long?
)