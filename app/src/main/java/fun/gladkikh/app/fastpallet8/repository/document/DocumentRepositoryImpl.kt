package `fun`.gladkikh.app.fastpallet8.repository.document

import `fun`.gladkikh.app.fastpallet8.db.dao.MainDao
import `fun`.gladkikh.app.fastpallet8.domain.entity.ItemListDocument
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.Action
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.BoxAction
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.InfoPallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.ProductAction
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.BoxCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.CreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.PalletCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.ProductCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.BoxInventoryPallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.InventoryPallet
import `fun`.gladkikh.app.fastpallet8.domain.model.Type
import `fun`.gladkikh.app.fastpallet8.map.toDb
import `fun`.gladkikh.app.fastpallet8.map.toObject
import `fun`.gladkikh.app.fastpallet8.map.toOject
import io.reactivex.Flowable


class DocumentRepositoryImpl(private val dao: MainDao) :
    DocumentRepository {
    override fun getListDocument(): Flowable<List<ItemListDocument>> {
        return dao.getListDocument()
            .map {
                it.map {
                    it.toOject()
                }
            }
    }

    override fun save(document: Any) {
        when (document) {
            is CreatePallet -> {
                dao.insertOrUpdate(document.toDb())
            }
            is InventoryPallet->{
                dao.insertOrUpdate(document.toDb())
            }
            is Action->{
                dao.insertOrUpdate(document.toDb())
            }
            else -> {
                throw Throwable("Неизвестный тип документа")
            }
        }
    }

    override fun delete(document: Any) {
        when (document) {
            is CreatePallet -> {
                dao.deleteTrigger(document.toDb())
            }
            is Action->{
                dao.deleteTrigger(document.toDb())
            }
            else -> {
                throw Throwable("Неизвестный тип документа")
            }
        }
    }

    override fun delete(itemListDocument: ItemListDocument) {
        when(itemListDocument.type){
            Type.CREATE_PALLET->{
                val doc = dao.getCreatePalletByGuid(itemListDocument.guid)
                dao.deleteTrigger(doc!!)
            }
            Type.INVENTORY_PALLET->{
                val doc = dao.getInventoryPalletByGuid(itemListDocument.guid)
                dao.deleteTrigger(doc!!)
            }
            Type.ACTION_PALLET->{
                val doc = dao.getActionByGuid(itemListDocument.guid)
                dao.deleteTrigger(doc!!)
            }
        }
    }

    override fun saveCreatePalletFromServer(
        doc: CreatePallet,
        listSave: List<ProductCreatePallet>,
        lisDell: List<ProductCreatePallet>
    ) {
        dao.saveCreatePalletFromServer(
            doc = doc.toDb(),
            lisDell = lisDell.map { it.toDb() },
            listSave = listSave.map { it.toDb() }
        )
    }

    override fun saveActionFromServer(
        doc: Action,
        listSave: List<ProductAction>,
        lisDell: List<ProductAction>
    ) {
        dao.saveActionFromServer(
            doc = doc.toDb(),
            lisDell = lisDell.map { it.toDb() },
            listSave = listSave.map { it.toDb() }
        )
    }

    override fun getCreatePalletByGuidServer(guidServer: String): CreatePallet? {
        val doc = dao.getCreatePalletByGuidServer(guidServer)
        return doc?.toObject()
    }

    override fun getCreatePalletByGuid(guid: String): CreatePallet? {
        val doc = dao.getCreatePalletByGuid(guid)
        return doc?.toObject()
    }

    override fun getListProduct(guidDoc: String): List<ProductCreatePallet> {
        return dao.getProductListByGuidDoc(guidDoc).map {
            it.toObject()
        }
    }

    override fun getListPallet(guidProduct: String): List<PalletCreatePallet> {
        return dao.getListPalletByGuidProduct(guidProduct).map {
            it.toObject()
        }
    }

    override fun getListBox(guidPallet: String): List<BoxCreatePallet> {
        return dao.getListBoxByGuidPallet(guidPallet).map {
            it.toObject()
        }
    }

    override fun getInventoryPalletByGuid(guidDoc: String): InventoryPallet? {
        val doc = dao.getInventoryPalletByGuid(guidDoc)
        return doc?.toObject()
    }

    override fun getActionByGuidServer(guid: String): Action? {
        val doc = dao.getActionByGuidServer(guid)
        return doc?.toObject()
    }

    override fun getInventoryPalletByGuidServer(guidDoc: String): InventoryPallet? {
        val doc = dao.getInventoryPalletByGuid(guidDoc)
        return doc?.toObject()
    }

    override fun getListBoxInventoryPallet(guidDoc: String): List<BoxInventoryPallet> {
        return dao.getListBoxByGuidDoc(guidDoc).map {
            it.toObject()
        }
    }

    override fun getActionByGuid(guidDoc: String): Action? {
        val doc = dao.getActionByGuid(guidDoc)
        return doc?.toObject()
    }

    override fun getListProductAction(guidDoc: String): List<ProductAction> {
        return dao.getListProductActionByGuidDoc(guidDoc).map {
            it.toObject()
        }
    }

    override fun getListPalletAction(guidProduct: String): List<InfoPallet> {
        return dao.getListPalletActionByGuidProduct(guidProduct).map {
            it.toObject()
        }
    }

    override fun getListBoxAction(guidProduct: String): List<BoxAction> {
        return dao.getListBoxByGuidProduct(guidProduct).map {
            it.toObject()
        }
    }

}