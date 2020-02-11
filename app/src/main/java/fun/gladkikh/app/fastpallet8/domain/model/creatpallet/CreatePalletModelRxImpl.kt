package `fun`.gladkikh.app.fastpallet8.domain.model.creatpallet

import `fun`.gladkikh.app.fastpallet8.common.getWeightByBarcode
import `fun`.gladkikh.app.fastpallet8.common.isPallet
import `fun`.gladkikh.app.fastpallet8.domain.model.DataWrapper
import `fun`.gladkikh.app.fastpallet8.domain.model.Status

import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.BoxCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.CreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.PalletCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.ProductCreatePallet
import `fun`.gladkikh.app.fastpallet8.repository.creatpallet.CreatePalletRepository
import `fun`.gladkikh.app.fastpallet8.repository.setting.SettingsRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import java.util.*


class CreatePalletModelRxImpl(
    val repository: CreatePalletRepository,
    val settingsRepository: SettingsRepository
) : CreatePalletModelRx {

    private fun checkEditDocByStatus(status: Status?): Boolean {
        return status in listOf(Status.LOADED, Status.NEW)
    }


    override fun getDoc(): Flowable<DataWrapper<CreatePallet>> = repository.getDoc()

    override fun getListProduct(): Flowable<DataWrapper<List<ProductCreatePallet>>> =
        repository.getListProduct()


    override fun getProduct(): Flowable<DataWrapper<ProductCreatePallet>> =
        repository.getProduct()


    override fun getListPallet(): Flowable<DataWrapper<List<PalletCreatePallet>>> =
        repository.getListPallet()

    override fun getPallet(): Flowable<DataWrapper<PalletCreatePallet>> =
        repository.getPallet()


    override fun getListBox(): Flowable<DataWrapper<List<BoxCreatePallet>>> =
        repository.getListBox()

    override fun getBox(): Flowable<DataWrapper<BoxCreatePallet>> =
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

    override fun saveDoc(doc: CreatePallet) = repository.saveDoc(doc)

    override fun dellDoc(doc: CreatePallet) = repository.dellDoc(doc)

    override fun saveProduct(product: ProductCreatePallet, doc: CreatePallet): Completable {
        return if (!checkEditDocByStatus(doc.status)) {
            Completable.error(Throwable("Нельзя изменять документ с этим статусом"))
        } else {
            repository.saveProduct(product)
        }
    }

    override fun dellProduct(product: ProductCreatePallet, doc: CreatePallet): Completable {
        return if (!checkEditDocByStatus(doc.status)) {
            Completable.error(Throwable("Нельзя изменять документ с этим статусом"))
        } else {
            repository.dellProduct(product)
        }
    }

    override fun savePallet(pallet: PalletCreatePallet, doc: CreatePallet): Completable {
        return if (!checkEditDocByStatus(doc.status)) {
            Completable.error(Throwable("Нельзя изменять документ с этим статусом"))
        } else {
            repository.savePallet(pallet)
        }
    }

    override fun dellPallet(pallet: PalletCreatePallet, doc: CreatePallet): Completable {
        return if (!checkEditDocByStatus(doc.status)) {
            Completable.error(Throwable("Нельзя изменять документ с этим статусом"))
        } else {
            repository.dellPallet(pallet)
        }
    }

    override fun saveBox(box: BoxCreatePallet, doc: CreatePallet): Completable {
        return if (!checkEditDocByStatus(doc.status)) {
            Completable.error(Throwable("Нельзя изменять документ с этим статусом"))
        } else {
            repository.saveBox(box)
        }
    }

    override fun dellBox(box: BoxCreatePallet, doc: CreatePallet): Completable {
        return if (!checkEditDocByStatus(doc.status)) {
            Completable.error(Throwable("Нельзя изменять документ с этим статусом"))
        } else {
            repository.dellBox(box)
        }
    }

    override fun getPalletByNumber(numberPallet: String): Flowable<DataWrapper<PalletCreatePallet>> {
       return repository.getPalletByNumber(numberPallet)
    }

    override fun getBoxByBarcode(
        barcode: String, doc: CreatePallet, pallet: PalletCreatePallet
        , product: ProductCreatePallet
    ): DataWrapper<BoxCreatePallet> {
        if (!checkEditDocByStatus(doc.status)) {
            return DataWrapper(error = Throwable("Нельзя изменять документ с этим статусом"))
        }

        if (isPallet(barcode)){
            return DataWrapper(error = Throwable("Это паллета!"))
        }

        if (!checkLengthBarcode(barcode,product)){
            return DataWrapper(error = Throwable("Не верная длинна ШК!"))
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


        val box = BoxCreatePallet(
            guid = UUID.randomUUID().toString(),
            guidPallet = pallet.guid,
            barcode = barcode,
            countBox = 1,
            count = weight,
            dateChanged = Date()
        )

        return DataWrapper(data = box)
    }

    override fun checkLengthBarcode(barcode:String,product: ProductCreatePallet):Boolean{
        val setting = settingsRepository.getSetting()
        return if (setting.checkLengthBarcode == false){
            true
        }else{
            if (product.weightBarcode.isNullOrBlank()) {
                return true
            }
            return barcode.length == product.weightBarcode!!.length
        }
    }


}