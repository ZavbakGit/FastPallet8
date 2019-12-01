package `fun`.gladkikh.app.fastpallet8.db.intity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class CreatePalletDb(
    @PrimaryKey @ColumnInfo(index = true) val guid: String,
    val number: String?,
    val date: Long?,
    val status: Int?,
    @ColumnInfo(index = true) var guidServer: String?,
    var dateChanged: Long?,
    var isLastLoad: Boolean?,
    var description: String?,
    var barcode: String?
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = CreatePalletDb::class,
        parentColumns = arrayOf("guid"),
        childColumns = arrayOf("guidDoc"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class ProductCreatePalletDb(
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
        entity = ProductCreatePalletDb::class,
        parentColumns = arrayOf("guid"),
        childColumns = arrayOf("guidProduct"),
        onDelete = ForeignKey.CASCADE
    )]
)

data class PalletCreatePalletDb(
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
        entity = PalletCreatePalletDb::class,
        parentColumns = arrayOf("guid"),
        childColumns = arrayOf("guidPallet"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class BoxCreatePalletDb(
    @PrimaryKey @ColumnInfo(index = true) val guid: String,
    @ColumnInfo(index = true) var guidPallet: String,
    var barcode: String?,

    var count: Float?, //Количество
    var countBox: Int?, //Количество мест

    var dateChanged: Long?
)