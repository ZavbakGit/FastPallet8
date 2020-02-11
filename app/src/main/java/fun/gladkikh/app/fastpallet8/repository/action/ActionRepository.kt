package `fun`.gladkikh.app.fastpallet8.repository.action

import `fun`.gladkikh.app.fastpallet8.domain.model.DataWrapper
import `fun`.gladkikh.app.fastpallet8.domain.entity.action.*
import io.reactivex.Completable
import io.reactivex.Flowable

interface ActionRepository {

    fun getDoc(): Flowable<DataWrapper<Action>>
    fun getListProduct(): Flowable<DataWrapper<List<ProductAction>>>

    fun getProduct(): Flowable<DataWrapper<ProductAction>>
    fun getListPallet(): Flowable<DataWrapper<List<InfoPallet>>>
    fun getWraperListBoxListPallet(): Flowable<DataWrapper<WraperListBoxListPallet>>

    fun getPallet(): Flowable<DataWrapper<InfoPallet>>
    fun getListBox(): Flowable<DataWrapper<List<BoxAction>>>
    fun getPalletByNumber(numberPallet:String,guidProduct:String): Flowable<DataWrapper<InfoPallet>>
    fun getBox(): Flowable<DataWrapper<BoxAction>>

    fun setDoc(guid:String?)
    fun setProduct(guid:String?)
    fun setPallet(guid:String?)
    fun setBox(guid:String?)

    fun saveDoc(doc:Action):Completable
    fun dellDoc(doc:Action):Completable

    fun saveProduct(product:ProductAction):Completable
    fun dellProduct(doc:ProductAction):Completable

    fun savePallet(pallet:InfoPallet):Completable
    fun dellPallet(pallet:InfoPallet):Completable

    fun saveBox(box:BoxAction):Completable
    fun dellBox(box:BoxAction):Completable


    fun getListProductByGuidDoc(guidDoc: String): List<ProductAction>
    fun getListPalletByGuidProduct(guidProduct: String): List<InfoPallet>
    fun savePalletToBase(pallet: InfoPallet)
}