package `fun`.gladkikh.app.fastpallet8.domain.model.entity.document

import `fun`.gladkikh.app.fastpallet8.domain.model.Status
import `fun`.gladkikh.app.fastpallet8.domain.model.Type
import java.util.*

abstract class Document(val type: Type) {
    abstract val guid: String
    abstract val number: String?
    abstract val date: Date?
    abstract val status: Status?
    abstract var guidServer: String?
    abstract var dateChanged: Date?
    abstract var isLastLoad: Boolean?
    abstract var description: String?
    abstract var barcode: String?
}