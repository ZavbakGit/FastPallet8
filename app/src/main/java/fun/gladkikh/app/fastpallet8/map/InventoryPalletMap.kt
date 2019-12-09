package `fun`.gladkikh.app.fastpallet8.map

import `fun`.gladkikh.app.fastpallet8.db.intity.InventoryPalletDb
import `fun`.gladkikh.app.fastpallet8.db.intity.BoxInventoryPalletDb
import `fun`.gladkikh.app.fastpallet8.domain.model.Status
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.BoxInventoryPallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.InventoryPallet
import java.util.*

fun InventoryPalletDb.toObject(): InventoryPallet {
    return InventoryPallet(
        guid = guid,
        barcode = barcode,
        number = number,
        guidServer = guidServer,
        date = this.date?.let { Date(it) },
        description = description,
        status = Status.getStatusById(status),
        dateChanged = this.dateChanged?.let { Date(it) },
        isLastLoad = isLastLoad,
        barcodePallet = barcodePallet,
        countRow = countRow,
        nameProduct = nameProduct,
        countBox = countBox,
        count = count,
        weightStartProduct = weightStartProduct,
        weightEndProduct = weightEndProduct,
        weightCoffProduct = weightCoffProduct,
        weightBarcode = weightBarcode,
        guidBackProduct = guidBackProduct,
        numberPallet = numberPallet
    )
}


fun InventoryPallet.toDb(): InventoryPalletDb {
    return InventoryPalletDb(
        guid = guid,
        barcode = barcode,
        number = number,
        guidServer = guidServer,
        date = date?.time,
        description = description,
        status = status?.id,
        dateChanged = dateChanged?.time,
        isLastLoad = isLastLoad,
        count = count,
        countBox = countBox,
        nameProduct = nameProduct,
        countRow = countRow,
        barcodePallet = barcodePallet,
        guidBackProduct = guidBackProduct,
        weightBarcode = weightBarcode,
        weightCoffProduct = weightCoffProduct,
        weightEndProduct = weightEndProduct,
        weightStartProduct = weightStartProduct,
        numberPallet = numberPallet
    )
}


fun BoxInventoryPalletDb.toObject(): BoxInventoryPallet {
    return BoxInventoryPallet(
        guid = guid,
        dateChanged = this.dateChanged?.let { Date(it) },
        barcode = barcode,
        countBox = countBox,
        count = count,
        guidDoc = guidDoc
    )
}

fun BoxInventoryPallet.toDb(): BoxInventoryPalletDb {
    return BoxInventoryPalletDb(
        guid = guid,
        dateChanged = dateChanged?.time,
        barcode = barcode,
        countBox = countBox,
        count = count,
        guidDoc = guidDoc
    )
}




