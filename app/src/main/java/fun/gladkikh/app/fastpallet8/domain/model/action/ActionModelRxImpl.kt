package `fun`.gladkikh.app.fastpallet8.domain.model.action

import `fun`.gladkikh.app.fastpallet8.common.getWeightByBarcode
import `fun`.gladkikh.app.fastpallet8.domain.model.DataWrapper
import `fun`.gladkikh.app.fastpallet8.domain.model.Status
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.*
import `fun`.gladkikh.app.fastpallet8.domain.usecase.GetInfoPalletUseCase

import `fun`.gladkikh.app.fastpallet8.repository.action.ActionRepository


import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*


class ActionModelRxImpl(
    private val repository: ActionRepository
    , private val getInfoPalletUseCase: GetInfoPalletUseCase
) : ActionModelRx {

    private fun checkEditDocByStatus(status: Status?): Boolean {
        return status in listOf(Status.LOADED, Status.NEW)
    }


    override fun getDoc(): Flowable<DataWrapper<Action>> = repository.getDoc()

    override fun getListProduct(): Flowable<DataWrapper<List<ProductAction>>> =
        repository.getListProduct()


    override fun getProduct(): Flowable<DataWrapper<ProductAction>> =
        repository.getProduct()


    override fun getListPallet(): Flowable<DataWrapper<List<PalletAction>>> =
        repository.getListPallet()

    override fun getPallet(): Flowable<DataWrapper<PalletAction>> =
        repository.getPallet()

    override fun getWraperListBoxListPallet(): Flowable<DataWrapper<WraperListBoxListPallet>> =
        repository.getWraperListBoxListPallet()


    override fun getListBox(): Flowable<DataWrapper<List<BoxAction>>> =
        repository.getListBox()

    override fun getBox(): Flowable<DataWrapper<BoxAction>> =
        repository.getBox()


    override fun setDoc(guid: String?) {
        repository.setDoc(guid)
    }

    override fun setProduct(guid: String?) {
        repository.setProduct(guid)
    }

    override fun setPallet(guid: String?) {
        repository.setPallet(guid)
    }

    override fun setBox(guid: String?) {
        repository.setBox(guid)
    }

    override fun saveDoc(doc: Action) = repository.saveDoc(doc)

    override fun dellDoc(doc: Action) = repository.dellDoc(doc)

    override fun saveProduct(product: ProductAction, doc: Action): Completable {
        return if (!checkEditDocByStatus(doc.status)) {
            Completable.error(Throwable("Нельзя изменять документ с этим статусом"))
        } else {
            repository.saveProduct(product)
        }
    }

    override fun dellProduct(product: ProductAction, doc: Action): Completable {
        return if (!checkEditDocByStatus(doc.status)) {
            Completable.error(Throwable("Нельзя изменять документ с этим статусом"))
        } else {
            repository.dellProduct(product)
        }
    }

    override fun savePallet(pallet: PalletAction, doc: Action): Completable {
        return if (!checkEditDocByStatus(doc.status)) {
            Completable.error(Throwable("Нельзя изменять документ с этим статусом"))
        } else {
            repository.savePallet(pallet)
        }
    }

    override fun dellPallet(pallet: PalletAction, doc: Action): Completable {
        return if (!checkEditDocByStatus(doc.status)) {
            Completable.error(Throwable("Нельзя изменять документ с этим статусом"))
        } else {
            repository.dellPallet(pallet)
        }
    }

    override fun saveBox(box: BoxAction, doc: Action): Completable {
        return if (!checkEditDocByStatus(doc.status)) {
            Completable.error(Throwable("Нельзя изменять документ с этим статусом"))
        } else {
            repository.saveBox(box)
        }
    }

    override fun dellBox(box: BoxAction, doc: Action): Completable {
        return if (!checkEditDocByStatus(doc.status)) {
            Completable.error(Throwable("Нельзя изменять документ с этим статусом"))
        } else {
            repository.dellBox(box)
        }
    }

    override fun getPalletByNumber(
        numberPallet: String,
        guidProduct: String
    ): Flowable<DataWrapper<PalletAction>> {
        return repository.getPalletByNumber(numberPallet, guidProduct)
    }

    override fun getBoxByBarcode(
        barcode: String, doc: Action
        , product: ProductAction
    ): DataWrapper<BoxAction> {
        if (!checkEditDocByStatus(doc.status)) {
            return DataWrapper(error = Throwable("Нельзя изменять документ с этим статусом"))
        }

        val weight = getWeightByBarcode(
            barcode = barcode,
            start = product?.weightStartProduct ?: 0,
            finish = product?.weightEndProduct ?: 0,
            coff = product?.weightCoffProduct ?: 0f
        )

        if (weight == 0f) {
            return DataWrapper(error = Throwable("Ошибка шаблона веса!"))
        }


        val box = BoxAction(
            guid = UUID.randomUUID().toString(),
            guidProduct = product.guid,
            barcode = barcode,
            countBox = 1,
            count = weight,
            dateChanged = Date()
        )

        return DataWrapper(data = box)
    }

    private fun getListPalletFromDoc(guidDoc: String): List<Pair<ProductAction, PalletAction>> {
        return repository.getListProductByGuidDoc(guidDoc)
            .flatMap { product ->
                repository.getListPalletByGuidProduct(product.guid).map {
                    product to it
                }
            }
    }

    override fun loadInfoPalletFromServer(doc: Action): Single<List<PalletAction>> {
        return Single.just(doc.guid)
            .map {
                //Выбираем все паллеты по документу
                getListPalletFromDoc(it)
            }
            .flatMap {
                val list = it
                    .map { pair ->
                        pair.second.number!!
                    }
                //Отправляем запрос
                return@flatMap getInfoPalletUseCase.get(list)
            }
            .map { listInfo ->
                //Изменяем паллеты
                getListPalletFromDoc(doc.guid)
                    .map {

                        val pallet = it.second
                        val product = it.first

                        val info = listInfo.find { info -> info.code == pallet.number }

                        if (info != null) {
                            val nameProduct = if (info.guidProduct != product.guidProductBack) {
                                if (info.nameProduct.isNullOrEmpty()){
                                    "Пустая на сервере!"
                                }else{
                                    info.nameProduct
                                }

                            } else {
                                null
                            }
                            return@map pallet.copy(
                                countBox = info.countBox,
                                count = info.count,
                                nameProduct = nameProduct
                            )
                        } else {
                            return@map pallet
                        }
                    }
            }
            .doOnSuccess {
                //Сохраняем
                it.forEach { pallet ->
                    repository.savePalletToBase(pallet)
                }
            }

    }

}