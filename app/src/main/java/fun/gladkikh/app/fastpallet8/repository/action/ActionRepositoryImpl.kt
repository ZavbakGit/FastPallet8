package `fun`.gladkikh.app.fastpallet8.repository.action

import `fun`.gladkikh.app.fastpallet8.db.dao.MainDao
import `fun`.gladkikh.app.fastpallet8.map.toDb
import `fun`.gladkikh.app.fastpallet8.map.toObject
import `fun`.gladkikh.app.fastpallet8.domain.model.DataWrapper
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.action.*

import `fun`.gladkikh.app.fastpallet8.repository.creatpallet.CreatePalletRepository
import android.annotation.SuppressLint
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

/**
 * Класс общается с db и выдает уже объекты
 */
class ActionRepositoryImpl(private val dao: MainDao) : ActionRepository {

    private val guidDocPublishSubject = PublishSubject.create<String>()
    private val guidProductPublishSubject = PublishSubject.create<String>()
    private val guidPalletPublishSubject = PublishSubject.create<String>()
    private val guidBoxPublishSubject = PublishSubject.create<String?>()


    override fun getDoc(): Flowable<DataWrapper<Action>> {
        return guidDocPublishSubject
            .observeOn(Schedulers.io())
            .toFlowable(BackpressureStrategy.BUFFER)
            .map {
                return@map DataWrapper(data = dao.getActionByGuid(it)!!.toObject())
            }
            .onErrorReturn {
                DataWrapper(error = it)
            }
    }


    override fun getListProduct(): Flowable<DataWrapper<List<ProductAction>>> {
        return guidDocPublishSubject
            .observeOn(Schedulers.io())
            .toFlowable(BackpressureStrategy.BUFFER)
            .map {
                DataWrapper(data = dao.getProductActionListByGuidDoc(it).map { productDb ->
                    productDb.toObject()
                })
            }
            .onErrorReturn {
                DataWrapper(error = it)
            }

    }



    override fun getProduct(): Flowable<DataWrapper<ProductAction>> {
        return guidProductPublishSubject
            .observeOn(Schedulers.io())
            .toFlowable(BackpressureStrategy.BUFFER)
            .map {
                return@map DataWrapper(data = dao.getProductActionByGuid(it).toObject())
            }
            .onErrorReturn {
                DataWrapper(error = it)
            }

    }

    override fun getListPallet(): Flowable<DataWrapper<List<PalletAction>>> {
        return guidProductPublishSubject
            .observeOn(Schedulers.io())
            .toFlowable(BackpressureStrategy.BUFFER)

            .map {
                DataWrapper(data = dao.getListPalletActionByGuidProduct(it).map { productDb ->
                    productDb.toObject()
                })
            }
            .onErrorReturn {
                DataWrapper(error = it)
            }
    }


   override fun getWraperListBoxListPallet(): Flowable<DataWrapper<WraperListBoxListPallet>> {
       return guidProductPublishSubject
            .observeOn(Schedulers.io())
            .toFlowable(BackpressureStrategy.BUFFER)

            .map {

                val listBox = dao.getListBoxByGuidProduct(it).map {boxDb->
                    boxDb.toObject()
                }

                val listPallet = dao.getListPalletActionByGuidProduct(it).map {boxDb->
                    boxDb.toObject()
                }


                val data = WraperListBoxListPallet(
                    listBox = listBox,
                    listPallet = listPallet
                )

                DataWrapper(data =data)
            }
            .onErrorReturn {
                DataWrapper(error = it)
            }
    }


    override fun getPallet(): Flowable<DataWrapper<PalletAction>> {
        return guidPalletPublishSubject
            .observeOn(Schedulers.io())
            .toFlowable(BackpressureStrategy.BUFFER)
            .map {
                return@map DataWrapper(data = dao.getPalletActionByGuid(it).toObject())
            }
            .onErrorReturn {
                DataWrapper(error = it)
            }
    }


    override fun getPalletByNumber(numberPallet: String,guidProduct:String): Flowable<DataWrapper<PalletAction>> {
        return Flowable.just(numberPallet)
            .map {
                return@map DataWrapper(data = dao.getPalletActionByNumber(it,guidProduct).toObject())
            }
            .onErrorReturn {
                DataWrapper(error = it)
            }

    }

    override fun getListBox(): Flowable<DataWrapper<List<BoxAction>>> {
        return guidProductPublishSubject
            .observeOn(Schedulers.io())
            .toFlowable(BackpressureStrategy.BUFFER)
            .map {
                DataWrapper(data = dao.getListBoxByGuidProduct(it).map { productDb ->
                    productDb.toObject()
                })
            }
            .onErrorReturn {
                DataWrapper(error = it)
            }

    }


    override fun getBox(): Flowable<DataWrapper<BoxAction>> {
        return guidBoxPublishSubject
            .observeOn(Schedulers.io())
            .toFlowable(BackpressureStrategy.BUFFER)
            .map {
                return@map DataWrapper(data = dao.getBoxActionByGuid(it).toObject())
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

    override fun saveDoc(doc: Action): Completable {
        return Flowable.just(doc)
            .observeOn(Schedulers.io())
            .doOnNext {
                dao.insertOrUpdate(it.toDb())
            }.ignoreElements()
    }

    override fun dellDoc(doc: Action): Completable {
        return Flowable.just(doc)
            .observeOn(Schedulers.io())
            .doOnNext {
                dao.delete(it.toDb())
            }.ignoreElements()
    }

    override fun saveProduct(product: ProductAction): Completable {
        return Flowable.just(product)
            .observeOn(Schedulers.io())
            .doOnNext {
                dao.insertOrUpdate(it.toDb())
            }.ignoreElements()
    }


    override fun dellProduct(doc: ProductAction): Completable {
        return Flowable.just(doc)
            .observeOn(Schedulers.io())
            .doOnNext {
                dao.delete(it.toDb())
            }
            .ignoreElements()
    }

    override fun savePallet(pallet: PalletAction): Completable {
        return Flowable.just(pallet)
            .observeOn(Schedulers.io())
            .doOnNext {
                dao.insertOrUpdate(it.toDb())
            }.ignoreElements()
    }

    override fun dellPallet(pallet: PalletAction): Completable {
        return Flowable.just(pallet)
            .observeOn(Schedulers.io())
            .doOnNext {
                dao.deleteTrigger(it.toDb())
            }.ignoreElements()
    }


    override fun saveBox(box: BoxAction): Completable {
        return Flowable.just(box)
            .observeOn(Schedulers.io())
            .doOnNext {
                dao.insertOrUpdate(it.toDb())
            }.ignoreElements()
    }

    override fun dellBox(box: BoxAction): Completable {
        return Flowable.just(box)
            .observeOn(Schedulers.io())
            .doOnNext {
                dao.deleteTrigger(it.toDb())
            }.ignoreElements()
    }

}