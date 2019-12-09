package `fun`.gladkikh.app.fastpallet8.repository.creatpallet

import `fun`.gladkikh.app.fastpallet8.domain.model.DataWrapper
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.BoxCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.CreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.PalletCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.ProductCreatePallet
import io.reactivex.Completable
import io.reactivex.Flowable

interface CreatePalletRepository {

    fun getDoc(): Flowable<DataWrapper<CreatePallet>>
    fun getListProduct(): Flowable<DataWrapper<List<ProductCreatePallet>>>

    fun getProduct(): Flowable<DataWrapper<ProductCreatePallet>>
    fun getListPallet(): Flowable<DataWrapper<List<PalletCreatePallet>>>

    fun getPallet(): Flowable<DataWrapper<PalletCreatePallet>>
    fun getListBox(): Flowable<DataWrapper<List<BoxCreatePallet>>>
    fun getPalletByNumber(numberPallet:String): Flowable<DataWrapper<PalletCreatePallet>>
    fun getBox(): Flowable<DataWrapper<BoxCreatePallet>>

    fun setDoc(guid:String?)
    fun setProduct(guid:String?)
    fun setPallet(guid:String?)
    fun setBox(guid:String?)

    fun saveDoc(doc: CreatePallet):Completable
    fun dellDoc(doc: CreatePallet):Completable

    fun saveProduct(product: ProductCreatePallet):Completable
    fun dellProduct(doc: ProductCreatePallet):Completable

    fun savePallet(pallet: PalletCreatePallet):Completable
    fun dellPallet(pallet: PalletCreatePallet):Completable

    fun saveBox(box: BoxCreatePallet):Completable
    fun dellBox(box: BoxCreatePallet):Completable

    fun recalcPallet()
    fun recalcProduct()



}