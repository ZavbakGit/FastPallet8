package `fun`.gladkikh.app.fastpallet8.map

import `fun`.gladkikh.app.fastpallet8.db.intity.ActionDb
import `fun`.gladkikh.app.fastpallet8.db.intity.BoxActionDb
import `fun`.gladkikh.app.fastpallet8.db.intity.PalletActionDb
import `fun`.gladkikh.app.fastpallet8.db.intity.ProductActionDb
import `fun`.gladkikh.app.fastpallet8.domain.model.Status
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.action.Action
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.action.BoxAction
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.action.PalletAction
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.action.ProductAction
import java.util.*

fun ActionDb.toObject(): Action {
    return Action(
        guid = guid,
        barcode = barcode,
        number = number,
        guidServer = guidServer,
        date = this.date?.let { Date(it) },
        description = description,
        status = Status.getStatusById(status),
        dateChanged = this.dateChanged?.let { Date(it) },
        isLastLoad = isLastLoad
    )
}


fun Action.toDb(): ActionDb {
    return ActionDb(
        guid = guid,
        barcode = barcode,
        number = number,
        guidServer = guidServer,
        date = date?.time,
        description = description,
        status = status?.id,
        dateChanged = dateChanged?.time,
        isLastLoad = isLastLoad
    )
}

fun ProductActionDb.toObject(): ProductAction {
    return ProductAction(
        guid = guid,
        isLastLoad = isLastLoad,
        dateChanged = this.dateChanged?.let { Date(it) },
        number = number,
        barcode = barcode,
        guidProductBack = guidProductBack,
        guidDoc = guidDoc,
        countBox = countBox,
        countPallet = countPallet,
        weightCoffProduct = weightCoffProduct,
        weightEndProduct = weightEndProduct,
        weightStartProduct = weightStartProduct,
        weightBarcode = weightBarcode,
        edCoff = edCoff,
        ed = ed,
        codeProduct = codeProduct,
        nameProduct = nameProduct,
        count = count,
        countBack = countBack,
        countBoxBack = countBoxBack,
        countRow = countRow
    )
}

fun ProductAction.toDb(): ProductActionDb {
    return ProductActionDb(
        guid = guid,
        isLastLoad = isLastLoad,
        dateChanged = dateChanged?.time,
        number = number,
        barcode = barcode,
        guidProductBack = guidProductBack,
        guidDoc = guidDoc,
        countBox = countBox,
        countPallet = countPallet,
        weightCoffProduct = weightCoffProduct,
        weightEndProduct = weightEndProduct,
        weightStartProduct = weightStartProduct,
        weightBarcode = weightBarcode,
        edCoff = edCoff,
        ed = ed,
        codeProduct = codeProduct,
        nameProduct = nameProduct,
        count = count,
        countBack = countBack,
        countBoxBack = countBoxBack,
        countRow = countRow
    )
}

fun PalletActionDb.toObject(): PalletAction {
    return PalletAction(
        guid = guid,
        countRow = countRow,
        count = count,
        nameProduct = nameProduct,
        countBox = countBox,
        guidProduct = guidProduct,
        barcode = barcode,
        number = number,
        dateChanged = this.dateChanged?.let { Date(it) },
        state = state,
        sclad = sclad
    )
}

fun PalletAction.toDb(): PalletActionDb {
    return PalletActionDb(
        guid = guid,
        countRow = countRow,
        count = count,
        nameProduct = nameProduct,
        countBox = countBox,
        guidProduct = guidProduct,
        barcode = barcode,
        number = number,
        dateChanged = dateChanged?.time,
        state = state,
        sclad = sclad
    )
}

fun BoxActionDb.toObject(): BoxAction {
    return BoxAction(
        guid = guid,
        dateChanged = this.dateChanged?.let { Date(it) },
        barcode = barcode,
        countBox = countBox,
        count = count,
        guidProduct = guidProduct
    )
}

fun BoxAction.toDb(): BoxActionDb {
    return BoxActionDb(
        guid = guid,
        dateChanged = dateChanged?.time,
        barcode = barcode,
        countBox = countBox,
        count = count,
        guidProduct = guidProduct
    )
}




