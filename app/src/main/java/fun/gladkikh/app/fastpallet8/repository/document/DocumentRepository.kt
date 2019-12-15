package `fun`.gladkikh.app.fastpallet8.repository.document

import `fun`.gladkikh.app.fastpallet8.domain.entity.ItemListDocument
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.Action
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.BoxAction
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.PalletAction
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.ProductAction
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.BoxCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.CreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.PalletCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.ProductCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.BoxInventoryPallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.InventoryPallet
import io.reactivex.Flowable


interface DocumentRepository {
    fun getListDocument(): Flowable<List<ItemListDocument>>
    fun save(document: Any)
    fun delete(document: Any)
    fun delete(itemListDocument: ItemListDocument)
    fun saveCreatePalletFromServer(
        doc: CreatePallet,
        listSave: List<ProductCreatePallet>,
        lisDell: List<ProductCreatePallet>
    )
    fun getCreatePalletByGuidServer(guidServer:String): CreatePallet?
    fun getCreatePalletByGuid(guid:String): CreatePallet?
    fun getListProduct(guidDoc:String):List<ProductCreatePallet>
    fun getListPallet(guidProduct:String):List<PalletCreatePallet>
    fun getListBox(guidPallet:String):List<BoxCreatePallet>

    fun getInventoryPalletByGuid(guidDoc:String): InventoryPallet?
    fun getListBoxInventoryPallet(guidDoc:String):List<BoxInventoryPallet>

    fun getActionByGyid(guid:String):Action
    fun getListProductAction(guidDoc:String):List<ProductAction>
    fun getListPalletAction(guidProduct:String):List<PalletAction>
    fun getListBoxAction(guidProduct:String):List<BoxAction>
}