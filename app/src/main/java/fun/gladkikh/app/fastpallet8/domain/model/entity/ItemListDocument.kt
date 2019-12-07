package `fun`.gladkikh.app.fastpallet8.domain.model.entity

import `fun`.gladkikh.fastpallet7.model.Type
import `fun`.gladkikh.app.fastpallet8.domain.model.Status
import java.util.*

data class ItemListDocument(
    val guid: String,
    val guidServer: String? = null,
    val type: Type,
    var status: Status?,
    var number: String? = null,
    var date: Date? = null,
    var dataChanged: Date? = null,
    var isLastLoad: Boolean? = false,
    var description: String? = null,
    var barcode: String? = null
)