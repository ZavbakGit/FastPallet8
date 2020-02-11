package `fun`.gladkikh.app.fastpallet8.domain.model.inventorypallet

import `fun`.gladkikh.app.fastpallet8.domain.model.DataWrapper
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.BoxInventoryPallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.InventoryPallet
import `fun`.gladkikh.app.fastpallet8.domain.model.Status
import io.reactivex.Completable
import io.reactivex.Flowable

interface InventoryPalletModelRx {
    fun getDoc(): Flowable<DataWrapper<InventoryPallet>>
    fun getListBox():Flowable<DataWrapper<List<BoxInventoryPallet>>>
    fun getBox():Flowable<DataWrapper<BoxInventoryPallet>>

    fun setDoc(guid:String?)
    fun setBox(guid:String?)

    fun saveDoc(doc: InventoryPallet):Completable
    fun dellDoc(doc: InventoryPallet):Completable

    fun saveBox(box: BoxInventoryPallet, doc: InventoryPallet): Completable
    fun dellBox(box: BoxInventoryPallet, doc: InventoryPallet): Completable

    fun getBoxByBarcode(barcode:String, doc: InventoryPallet)
            : DataWrapper<BoxInventoryPallet>

    fun checkEditDocByStatus(status: Status?): Boolean
    fun loadInfoPalletFromServer(doc: InventoryPallet): Completable
    fun checkLengthBarcode(barcode: String, doc: InventoryPallet): Boolean
}