package `fun`.gladkikh.app.fastpallet8.domain.entity.document

import `fun`.gladkikh.app.fastpallet8.domain.model.Status
import `fun`.gladkikh.app.fastpallet8.domain.model.Type
import java.util.*


abstract class Document(val type: Type, val typeFromServer:String? = null) {


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

abstract class Document1(val type: Type,typeFromServer:String) {

    val typeFromServer:String = typeFromServer
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