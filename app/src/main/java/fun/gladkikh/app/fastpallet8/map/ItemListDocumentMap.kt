package `fun`.gladkikh.app.fastpallet8.map

import `fun`.gladkikh.app.fastpallet8.db.intity.ActionDb
import `fun`.gladkikh.app.fastpallet8.db.intity.CreatePalletDb
import `fun`.gladkikh.app.fastpallet8.db.intity.InventoryPalletDb
import `fun`.gladkikh.app.fastpallet8.db.intity.ItemListDocumentDb
import `fun`.gladkikh.app.fastpallet8.domain.model.Status
import `fun`.gladkikh.app.fastpallet8.domain.entity.ItemListDocument
import `fun`.gladkikh.app.fastpallet8.domain.model.Type
import java.util.*



fun InventoryPalletDb.toDocument(): ItemListDocumentDb {
    return ItemListDocumentDb(
        guid = this.guid,
        isLastLoad = this.isLastLoad,
        number = this.number,
        barcode = this.barcode,
        date = this.date,
        description = this.description,
        guidServer = this.guidServer,
        status = this.status,
        type = 2, //Action
        dataChanged = this.dateChanged
    )
}

fun CreatePalletDb.toDocument(): ItemListDocumentDb {
    return ItemListDocumentDb(
        guid = this.guid,
        isLastLoad = this.isLastLoad,
        number = this.number,
        barcode = this.barcode,
        date = this.date,
        description = this.description,
        guidServer = this.guidServer,
        status = this.status,
        type = 1, //CreatePallet
        dataChanged = this.dateChanged
    )
}

fun ActionDb.toDocument(): ItemListDocumentDb {
    return ItemListDocumentDb(
        guid = this.guid,
        isLastLoad = this.isLastLoad,
        number = this.number,
        barcode = this.barcode,
        date = this.date,
        description = this.description,
        guidServer = this.guidServer,
        status = this.status,
        type = 3, //Action
        dataChanged = this.dateChanged
    )
}


fun ItemListDocumentDb.toOject(): ItemListDocument {
    return ItemListDocument(
        guid = this.guid,
        dataChanged = this.dataChanged?.let { Date(it) },
        type = Type.getTypeById(this.type),
        status = Status.getStatusById(this.status),
        guidServer = this.guidServer,
        description = this.description,
        date = this.date?.let { Date(it) },
        barcode = this.barcode,
        number = this.number,
        isLastLoad = this.isLastLoad
    )
}






