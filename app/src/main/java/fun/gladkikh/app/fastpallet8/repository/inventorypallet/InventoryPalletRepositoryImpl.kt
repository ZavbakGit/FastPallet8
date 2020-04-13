package `fun`.gladkikh.app.fastpallet8.repository.inventorypallet

import `fun`.gladkikh.app.fastpallet8.db.dao.MainDao
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.ProductAction
import `fun`.gladkikh.app.fastpallet8.map.toDb
import `fun`.gladkikh.app.fastpallet8.map.toObject
import `fun`.gladkikh.app.fastpallet8.domain.model.DataWrapper

import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.BoxInventoryPallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.InventoryPallet
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

/**
 * Класс общается с db и выдает уже объекты
 */
class InventoryPalletRepositoryImpl(private val dao: MainDao) : InventoryPalletRepository {

    private val guidDocPublishSubject = PublishSubject.create<String>()
    private val guidBoxPublishSubject = PublishSubject.create<String?>()

    override fun setDoc(guid: String?) {
        guidDocPublishSubject.onNext(guid ?: "")
    }

    override fun setBox(guid: String?) {
        guidBoxPublishSubject.onNext(guid ?: "")
    }

    override fun getDoc(): Flowable<DataWrapper<InventoryPallet>> {
        return guidDocPublishSubject
            .observeOn(Schedulers.io())
            .toFlowable(BackpressureStrategy.BUFFER)
            .map {
                return@map DataWrapper(data = dao.getInventoryPalletByGuid(it)!!.toObject())
            }
            .onErrorReturn {
                DataWrapper(error = it)
            }
    }

    override fun getBox(): Flowable<DataWrapper<BoxInventoryPallet>> {
        return guidBoxPublishSubject
            .observeOn(Schedulers.io())
            .toFlowable(BackpressureStrategy.BUFFER)
            .map {
                return@map DataWrapper(data = dao.getBoxInventoryPalletByGuid(it).toObject())
            }
            .onErrorReturn {
                DataWrapper(error = it)
            }
    }

    override fun getListBox(): Flowable<DataWrapper<List<BoxInventoryPallet>>> {
        return guidDocPublishSubject
            .observeOn(Schedulers.io())
            .toFlowable(BackpressureStrategy.BUFFER)
            .map {
                DataWrapper(data = dao.getListBoxByGuidDoc(it).map { productDb ->
                    productDb.toObject()
                })
            }
            .onErrorReturn {
                DataWrapper(error = it)
            }
    }

    override fun getListBoxByGuidDoc(guidDoc: String): List<BoxInventoryPallet> {
        return dao.getListBoxByGuidDoc(guidDoc).map {
            it.toObject()
        }
    }

    override fun saveDoc(doc: InventoryPallet): Completable {
        return Flowable.just(doc)
            .observeOn(Schedulers.io())
            .doOnNext {
                dao.insertOrUpdate(it.toDb())
            }.ignoreElements()
    }

    override fun dellDoc(doc: InventoryPallet): Completable {
        return Flowable.just(doc)
            .observeOn(Schedulers.io())
            .doOnNext {
                dao.deleteTrigger(it.toDb())
            }.ignoreElements()
    }

    override fun saveBox(box: BoxInventoryPallet): Completable {
        return Flowable.just(box)
            .observeOn(Schedulers.io())
            .doOnNext {
                dao.insertOrUpdate(it.toDb())
            }.ignoreElements()
    }

    override fun dellBox(box: BoxInventoryPallet): Completable {
        return Flowable.just(box)
            .observeOn(Schedulers.io())
            .doOnNext {
                dao.deleteTrigger(it.toDb())
            }.ignoreElements()
    }

    override fun savePalletToBase(doc: InventoryPallet){
        dao.insertOrUpdate(doc.toDb())
    }

    override fun recalculateProductAction(doc: InventoryPallet):Completable{
        return Flowable.just(doc)
            .observeOn(Schedulers.io())
            .doOnNext {
                dao.recalculateInventoryPallet(doc.guid)
            }.ignoreElements()
    }


}