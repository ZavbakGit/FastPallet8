package `fun`.gladkikh.app.fastpallet8.repository.document

import `fun`.gladkikh.app.fastpallet8.db.dao.MainDao
import `fun`.gladkikh.app.fastpallet8.domain.entity.ItemListDocument
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.BoxCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.CreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.PalletCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.ProductCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.InventoryPallet
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
            else -> {
                throw Throwable("Неизвестный тип документа")
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

    override fun getCreatePalletByGuidServer(guidServer: String): CreatePallet? {
        val doc = dao.getDocByGuidServer(guidServer)
        return doc?.toObject()
    }

    override fun getCreatePalletByGuid(guid: String): CreatePallet? {
        val doc = dao.getDocByGuid(guid)
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
}