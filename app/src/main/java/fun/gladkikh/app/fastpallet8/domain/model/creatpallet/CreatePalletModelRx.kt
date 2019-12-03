package `fun`.gladkikh.app.fastpallet8.domain.model.creatpallet

import `fun`.gladkikh.app.fastpallet8.domain.model.DataWrapper
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.BoxCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.CreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.PalletCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.ProductCreatePallet
import io.reactivex.Completable
import io.reactivex.Flowable

interface CreatePalletModelRx {
    fun getDoc(): Flowable<DataWrapper<CreatePallet>>
    fun getListProduct():Flowable<DataWrapper<List<ProductCreatePallet>>>

    fun getProduct():Flowable<DataWrapper<ProductCreatePallet>>
    fun getListPallet():Flowable<DataWrapper<List<PalletCreatePallet>>>

    fun getPallet():Flowable<DataWrapper<PalletCreatePallet>>
    fun getListBox():Flowable<DataWrapper<List<BoxCreatePallet>>>

    fun getBox():Flowable<DataWrapper<BoxCreatePallet>>


    fun setDoc(guid:String?)
    fun setProduct(guid:String?)
    fun setPallet(guid:String?)
    fun setBox(guid:String?)

    fun saveDoc(doc:CreatePallet):Completable
    fun dellDoc(doc:CreatePallet):Completable

    fun saveProduct(product:ProductCreatePallet,doc:CreatePallet):Completable
    fun dellProduct(product:ProductCreatePallet,doc:CreatePallet):Completable

    fun savePallet(pallet:PalletCreatePallet,doc:CreatePallet):Completable
    fun dellPallet(pallet:PalletCreatePallet,doc:CreatePallet):Completable

    fun saveBox(box: BoxCreatePallet,doc:CreatePallet): Completable
    fun dellBox(box: BoxCreatePallet,doc:CreatePallet): Completable

    fun getPalletByNumber(numberPallet: String):Flowable<DataWrapper<PalletCreatePallet>>

    fun getBoxByBarcode(barcode:String, doc: CreatePallet,
                        pallet: PalletCreatePallet,
                        product: ProductCreatePallet)
            : DataWrapper<BoxCreatePallet>


}