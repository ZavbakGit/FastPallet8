package `fun`.gladkikh.app.fastpallet8.domain.model.inventorypallet

import `fun`.gladkikh.app.fastpallet8.common.getFloatByParseStr
import `fun`.gladkikh.app.fastpallet8.common.getIntByParseStr
import `fun`.gladkikh.app.fastpallet8.common.getWeightByBarcode
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.BoxInventoryPallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.inventorypallet.InventoryPallet
import `fun`.gladkikh.app.fastpallet8.domain.model.DataWrapper
import `fun`.gladkikh.app.fastpallet8.domain.model.Status
import `fun`.gladkikh.app.fastpallet8.domain.usecase.GetInfoPalletUseCase
import `fun`.gladkikh.app.fastpallet8.repository.inventorypallet.InventoryPalletRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

class InventoryPalletModelRxImpl(
    val repository: InventoryPalletRepository
    , private val getInfoPalletUseCase: GetInfoPalletUseCase
) : InventoryPalletModelRx {

    override fun checkEditDocByStatus(status: Status?): Boolean {
        return status in listOf(Status.LOADED, Status.NEW)
    }

    override fun getDoc(): Flowable<DataWrapper<InventoryPallet>> = repository.getDoc()

    override fun getBox(): Flowable<DataWrapper<BoxInventoryPallet>> =
        repository.getBox()

    override fun getListBox(): Flowable<DataWrapper<List<BoxInventoryPallet>>> =
        repository.getListBox()

    override fun setDoc(guid: String?) {
        repository.setDoc(guid)
    }

    override fun setBox(guid: String?) {
        repository.setBox(guid)
    }

    override fun getBoxByBarcode(
        barcode: String, doc: InventoryPallet

    ): DataWrapper<BoxInventoryPallet> {
        if (!checkEditDocByStatus(doc.status)) {
            return DataWrapper(error = Throwable("Нельзя изменять документ с этим статусом"))
        }

        val weight = getWeightByBarcode(
            barcode = barcode,
            start = doc?.weightStartProduct ?: 0,
            finish = doc?.weightEndProduct ?: 0,
            coff = doc?.weightCoffProduct ?: 0f
        )

        if (weight == 0f) {
            return DataWrapper(error = Throwable("Ошибка шаблона веса!"))
        }


        val box = BoxInventoryPallet(
            guid = UUID.randomUUID().toString(),
            guidDoc = doc.guid,
            barcode = barcode,
            countBox = 1,
            count = weight,
            dateChanged = Date()
        )

        return DataWrapper(data = box)
    }

    override fun saveBox(box: BoxInventoryPallet, doc: InventoryPallet): Completable {
        return if (!checkEditDocByStatus(doc.status)) {
            Completable.error(Throwable("Нельзя изменять документ с этим статусом"))
        } else {
            repository.saveBox(box)
        }
    }

    override fun dellBox(box: BoxInventoryPallet, doc: InventoryPallet): Completable {
        return if (!checkEditDocByStatus(doc.status)) {
            Completable.error(Throwable("Нельзя изменять документ с этим статусом"))
        } else {
            repository.dellBox(box)
        }
    }

    override fun saveDoc(doc: InventoryPallet) = repository.saveDoc(doc)

    override fun dellDoc(doc: InventoryPallet) = repository.dellDoc(doc)

    override fun loadInfoPalletFromServer(doc: InventoryPallet): Completable {
        return Single.just(doc)
            .flatMap {
                if (!checkEditDocByStatus(doc.status)){
                    return@flatMap Single.error<Throwable>(Throwable("Нельзя редактировать документ!"))
                }

                if (it.numberPallet == null) {
                    return@flatMap Single.error<Throwable>(Throwable("Пустая паллета!"))
                }
                Single.just(listOf(it.numberPallet))
            }
            .map {
                it as List<String>
            }
            .flatMap {

                //Отправляем запрос
                return@flatMap getInfoPalletUseCase.get(it)
            }
            .map { listInfo ->
                //Изменяем паллеты

                val info = listInfo.first()

                return@map doc.copy(
                    nameProduct = info.nameProduct,
                    barcode = info.barcode,
                    weightStartProduct = info.weightStartProduct?.getIntByParseStr(),
                    weightEndProduct = info.weightEndProduct?.getIntByParseStr(),
                    weightCoffProduct = info.weightCoffProduct?.getFloatByParseStr()

                )

            }
            .doOnSuccess {
                repository.savePalletToBase(it)
            }
            .ignoreElement()
    }


}