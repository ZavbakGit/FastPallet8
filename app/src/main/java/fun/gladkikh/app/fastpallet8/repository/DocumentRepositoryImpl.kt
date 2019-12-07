package `fun`.gladkikh.app.fastpallet8.repository

import `fun`.gladkikh.app.fastpallet8.db.dao.DocumentDao
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.ItemListDocument
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.BoxCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.CreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.PalletCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.ProductCreatePallet
import `fun`.gladkikh.app.fastpallet8.map.toDb
import `fun`.gladkikh.app.fastpallet8.map.toObject
import `fun`.gladkikh.app.fastpallet8.map.toOject
import io.reactivex.Completable
import io.reactivex.Flowable


class DocumentRepositoryImpl(val dao: DocumentDao) : DocumentRepository {
    override fun getListDocument(): Flowable<List<ItemListDocument>> {
        return dao.getListDocument()
            .map {
                it.map {
                    it.toOject()
                }
            }
    }

    override fun save(document: Any): Completable {
        return when (document) {
            is CreatePallet -> {
                Completable.fromAction {
                    dao.insertOrUpdate(document.toDb())
                }
            }
            else -> {
                return Completable.error { Throwable("Неизвестный тип документа") }
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