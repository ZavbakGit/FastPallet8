package `fun`.gladkikh.app.fastpallet8.db.intity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class InventoryPalletDb(
    @PrimaryKey @ColumnInfo(index = true) val guid: String,
    val number: String?,
    val date: Long?,
    val status: Int?,
    @ColumnInfo(index = true) var guidServer: String?,
    var dateChanged: Long?,
    var isLastLoad: Boolean?,
    var description: String?,
    var barcode: String?,

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
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = InventoryPalletDb::class,
        parentColumns = arrayOf("guid"),
        childColumns = arrayOf("guidDoc"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class BoxInventoryPalletDb(
    @PrimaryKey @ColumnInfo(index = true) val guid: String,
    @ColumnInfo(index = true) var guidDoc: String,
    var barcode: String?,

    var count: Float?, //Количество
    var countBox: Int?, //Количество мест

    var dateChanged: Long?
)