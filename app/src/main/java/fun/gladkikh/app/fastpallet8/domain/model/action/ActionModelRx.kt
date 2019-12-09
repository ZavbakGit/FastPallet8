package `fun`.gladkikh.app.fastpallet8.domain.model.action

import `fun`.gladkikh.app.fastpallet8.domain.model.DataWrapper
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.action.*

import io.reactivex.Completable
import io.reactivex.Flowable

interface ActionModelRx {
    fun getDoc(): Flowable<DataWrapper<Action>>
    fun getListProduct(): Flowable<DataWrapper<List<ProductAction>>>

    fun getProduct(): Flowable<DataWrapper<ProductAction>>
    fun getListPallet(): Flowable<DataWrapper<List<PalletAction>>>

    fun getPallet(): Flowable<DataWrapper<PalletAction>>
    fun getWraperListBoxListPallet(): Flowable<DataWrapper<WraperListBoxListPallet>>
    fun getListBox(): Flowable<DataWrapper<List<BoxAction>>>

    fun getBox(): Flowable<DataWrapper<BoxAction>>


    fun setDoc(guid: String?)
    fun setProduct(guid: String?)
    fun setPallet(guid: String?)
    fun setBox(guid: String?)

    fun saveDoc(doc: Action): Completable
    fun dellDoc(doc: Action): Completable

    fun saveProduct(product: ProductAction, doc: Action): Completable
    fun dellProduct(product: ProductAction, doc: Action): Completable

    fun savePallet(pallet: PalletAction, doc: Action): Completable
    fun dellPallet(pallet: PalletAction, doc: Action): Completable

    fun saveBox(box: BoxAction, doc: Action): Completable
    fun dellBox(box: BoxAction, doc: Action): Completable

    fun getPalletByNumber(numberPallet: String,guidProduct:String): Flowable<DataWrapper<PalletAction>>

    fun getBoxByBarcode(
        barcode: String, doc: Action,
        product: ProductAction
    )
            : DataWrapper<BoxAction>

    fun loadInfoPalletFromServer(doc:Action):Completable


}