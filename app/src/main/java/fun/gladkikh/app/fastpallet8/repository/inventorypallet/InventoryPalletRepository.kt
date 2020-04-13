package `fun`.gladkikh.app.fastpallet8.repository.inventorypallet

import `fun`.gladkikh.app.fastpallet8.domain.model.DataWrapper
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.*
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.BoxInventoryPallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.InventoryPallet
import io.reactivex.Completable
import io.reactivex.Flowable

interface InventoryPalletRepository {

    fun getDoc(): Flowable<DataWrapper<InventoryPallet>>

    fun getListBox(): Flowable<DataWrapper<List<BoxInventoryPallet>>>
    fun getBox(): Flowable<DataWrapper<BoxInventoryPallet>>

    fun setDoc(guid:String?)
    fun setBox(guid:String?)

    fun saveDoc(doc:InventoryPallet):Completable
    fun dellDoc(doc:InventoryPallet):Completable

    fun saveBox(box:BoxInventoryPallet):Completable
    fun dellBox(box:BoxInventoryPallet):Completable

    fun getListBoxByGuidDoc(guidDoc: String): List<BoxInventoryPallet>

    fun savePalletToBase(doc: InventoryPallet)
    fun recalculateProductAction(doc: InventoryPallet): Completable
}