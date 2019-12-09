package `fun`.gladkikh.app.fastpallet8.domain.model.creatpallet.old

import `fun`.gladkikh.app.fastpallet8.domain.model.DataWrapper
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.BoxCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.CreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.PalletCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.ProductCreatePallet
import androidx.lifecycle.LiveData
import io.reactivex.Completable

interface CreatePalletModel {
    fun getDoc():LiveData<DataWrapper<CreatePallet>>
    fun getListProduct():LiveData<DataWrapper<List<ProductCreatePallet>>>

    fun getProduct():LiveData<DataWrapper<ProductCreatePallet>>
    fun getListPallet():LiveData<DataWrapper<List<PalletCreatePallet>>>

    fun getPallet():LiveData<DataWrapper<PalletCreatePallet>>
    fun getListBox():LiveData<DataWrapper<List<BoxCreatePallet>>>

    fun getBox():LiveData<DataWrapper<BoxCreatePallet>>


    fun setDoc(guid:String?)
    fun setProduct(guid:String?)
    fun setPallet(guid:String?)
    fun setBox(guid:String?)

    fun saveDoc(doc: CreatePallet):Completable
    fun dellDoc(doc: CreatePallet):Completable

    fun saveProduct(product: ProductCreatePallet):Completable
    fun dellProduct(product: ProductCreatePallet):Completable

    fun savePallet(pallet: PalletCreatePallet):Completable
    fun dellPallet(pallet: PalletCreatePallet):Completable

    fun saveBox(box: BoxCreatePallet):Completable
    fun dellBox(box: BoxCreatePallet):Completable

}