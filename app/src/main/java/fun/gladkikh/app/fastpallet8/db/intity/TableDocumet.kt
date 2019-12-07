package `fun`.gladkikh.app.fastpallet8.db.intity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemListDocumentDb(
    @PrimaryKey @ColumnInfo(index = true) val guid: String,
    val guidServer: String? = null,
    val type: Int,
    var status: Int?,
    var number: String? = null,
    var date: Long? = null,
    var dataChanged: Long? = null,
    var isLastLoad: Boolean? = false,
    var description: String? = null,
    var barcode: String? = null
)