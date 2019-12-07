package `fun`.gladkikh.app.fastpallet8.repository

import `fun`.gladkikh.app.fastpallet8.db.dao.DocumentDao
import `fun`.gladkikh.app.fastpallet8.map.toDb
import `fun`.gladkikh.app.fastpallet8.map.toObject
import `fun`.gladkikh.app.fastpallet8.domain.model.DataWrapper
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.BoxCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.CreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.PalletCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.ProductCreatePallet
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

/**
 * Класс общается с db и выдает уже объекты
 */
class CreatePalletRepositoryImpl(private val createPalletUpdateDao: DocumentDao) :
    CreatePalletRepository {

    private val guidDocPublishSubject = PublishSubject.create<String>()
    private val guidProductPublishSubject = PublishSubject.create<String>()
    private val guidPalletPublishSubject = PublishSubject.create<String>()
    private val guidBoxPublishSubject = PublishSubject.create<String?>()


    override fun getDoc(): Flowable<DataWrapper<CreatePallet>> {
        return guidDocPublishSubject
            .observeOn(Schedulers.io())
            .toFlowable(BackpressureStrategy.BUFFER)
            .map {
                return@map DataWrapper(data = createPalletUpdateDao.getDocByGuid(it)!!.toObject())
            }
            .onErrorReturn {
                DataWrapper(error = it)
            }
    }


    override fun getListProduct(): Flowable<DataWrapper<List<ProductCreatePallet>>> {
        return guidDocPublishSubject
            .observeOn(Schedulers.io())
            .toFlowable(BackpressureStrategy.BUFFER)
            .map {
                DataWrapper(data = createPalletUpdateDao.getProductListByGuidDoc(it).map { productDb ->
                    productDb.toObject()
                })
            }
            .onErrorReturn {
                DataWrapper(error = it)
            }

    }

    override fun getProduct(): Flowable<DataWrapper<ProductCreatePallet>> {
        return guidProductPublishSubject
            .observeOn(Schedulers.io())
            .toFlowable(BackpressureStrategy.BUFFER)
            .map {
                return@map DataWrapper(data = createPalletUpdateDao.getProductByGuid(it).toObject())
            }
            .onErrorReturn {
                DataWrapper(error = it)
            }

    }

    override fun getListPallet(): Flowable<DataWrapper<List<PalletCreatePallet>>> {
        return guidProductPublishSubject
            .observeOn(Schedulers.io())
            .toFlowable(BackpressureStrategy.BUFFER)

            .map {
                DataWrapper(data = createPalletUpdateDao.getListPalletByGuidProduct(it).map { productDb ->
                    productDb.toObject()
                })
            }
            .onErrorReturn {
                DataWrapper(error = it)
            }
    }

    override fun getPallet(): Flowable<DataWrapper<PalletCreatePallet>> {
        return guidPalletPublishSubject
            .observeOn(Schedulers.io())
            .toFlowable(BackpressureStrategy.BUFFER)
            .map {
                return@map DataWrapper(data = createPalletUpdateDao.getPalletByGuid(it).toObject())
            }
            .onErrorReturn {
                DataWrapper(error = it)
            }
    }


    override fun getPalletByNumber(numberPallet: String):Flowable<DataWrapper<PalletCreatePallet>> {
      return  Flowable.just(numberPallet)
            .map {
                return@map DataWrapper(data = createPalletUpdateDao.getPalletByNumber(it).toObject())
            }
            .onErrorReturn {
                DataWrapper(error = it)
            }

    }

    override fun getListBox(): Flowable<DataWrapper<List<BoxCreatePallet>>> {
        return guidPalletPublishSubject
            .observeOn(Schedulers.io())
            .toFlowable(BackpressureStrategy.BUFFER)
            .map {
                DataWrapper(data = createPalletUpdateDao.getListBoxByGuidPallet(it).map { productDb ->
                    productDb.toObject()
                })
            }
            .onErrorReturn {
                DataWrapper(error = it)
            }

    }



    override fun getBox(): Flowable<DataWrapper<BoxCreatePallet>> {
        return guidBoxPublishSubject
            .observeOn(Schedulers.io())
            .toFlowable(BackpressureStrategy.BUFFER)
            .map {
                return@map DataWrapper(data = createPalletUpdateDao.getBoxByGuid(it).toObject())
            }
            .onErrorReturn {
                DataWrapper(error = it)
            }
    }

    override fun setDoc(guid: String?) {
        guidDocPublishSubject.onNext(guid ?: "")
    }

    override fun setProduct(guid: String?) {
        guidProductPublishSubject.onNext(guid ?: "")
    }

    override fun setPallet(guid: String?) {
        guidPalletPublishSubject.onNext(guid ?: "")
    }

    override fun setBox(guid: String?) {
        guidBoxPublishSubject.onNext(guid ?: "")
    }

    override fun saveDoc(doc: CreatePallet): Completable {
        return Flowable.just(doc)
            .observeOn(Schedulers.io())
            .doOnNext {
                createPalletUpdateDao.insertOrUpdate(it.toDb())
            }.ignoreElements()
    }

    override fun dellDoc(doc: CreatePallet): Completable {
        return Flowable.just(doc)
            .observeOn(Schedulers.io())
            .doOnNext {
                createPalletUpdateDao.delete(it.toDb())
            }.ignoreElements()
    }

    override fun saveProduct(product: ProductCreatePallet): Completable {
        return Flowable.just(product)
            .observeOn(Schedulers.io())
            .doOnNext {
                createPalletUpdateDao.insertOrUpdate(it.toDb())
            }.ignoreElements()
    }


    override fun dellProduct(doc: ProductCreatePallet): Completable {
        return Flowable.just(doc)
            .observeOn(Schedulers.io())
            .doOnNext {
                createPalletUpdateDao.delete(it.toDb())
            }
            .ignoreElements()
    }

    override fun savePallet(pallet: PalletCreatePallet): Completable {
        return Flowable.just(pallet)
            .observeOn(Schedulers.io())
            .doOnNext {
                createPalletUpdateDao.insertOrUpdate(it.toDb())
            }.ignoreElements()
    }

    override fun dellPallet(pallet: PalletCreatePallet): Completable {
        return Flowable.just(pallet)
            .observeOn(Schedulers.io())
            .doOnNext {
                createPalletUpdateDao.deleteTrigger(it.toDb())
            }.ignoreElements()
    }


    override fun saveBox(box: BoxCreatePallet): Completable {
        return Flowable.just(box)
            .observeOn(Schedulers.io())
            .doOnNext {
                createPalletUpdateDao.insertOrUpdate(it.toDb())
            }.ignoreElements()
    }

    override fun dellBox(box: BoxCreatePallet): Completable {
        return Flowable.just(box)
            .observeOn(Schedulers.io())
            .doOnNext {
                createPalletUpdateDao.deleteTrigger(it.toDb())
            }.ignoreElements()
    }

    override fun recalcPallet() = createPalletUpdateDao.recalcPallet()
    override fun recalcProduct() = createPalletUpdateDao.reCalcProduct()
}