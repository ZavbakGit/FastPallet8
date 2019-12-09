package `fun`.gladkikh.app.fastpallet8.repository.document

import `fun`.gladkikh.app.fastpallet8.domain.entity.ItemListDocument
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.BoxCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.CreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.PalletCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.ProductCreatePallet
import io.reactivex.Flowable


interface DocumentRepository {
    fun getListDocument(): Flowable<List<ItemListDocument>>
    fun save(document: Any)
    fun delete(document: Any)
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

}