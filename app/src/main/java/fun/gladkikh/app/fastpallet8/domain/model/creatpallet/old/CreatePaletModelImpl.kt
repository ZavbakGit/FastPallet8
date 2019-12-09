package `fun`.gladkikh.app.fastpallet8.domain.model.creatpallet.old

import `fun`.gladkikh.app.fastpallet8.domain.model.DataWrapper

import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.BoxCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.CreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.PalletCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.entity.creatpallet.ProductCreatePallet
import `fun`.gladkikh.app.fastpallet8.repository.creatpallet.CreatePalletRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams


class CreatePaletModelImpl(
    val repository: CreatePalletRepository
) : CreatePalletModel {

    var currentDoc: CreatePallet? = null
    var currentProduct: ProductCreatePallet? = null
    var currentPallet: PalletCreatePallet? = null
    var currentBox: BoxCreatePallet? = null


    override fun getDoc(): LiveData<DataWrapper<CreatePallet>> =
        LiveDataReactiveStreams.fromPublisher(
            repository.getDoc()
                .doOnNext {
                    currentDoc = it.data
                }
        )

    override fun getListProduct(): LiveData<DataWrapper<List<ProductCreatePallet>>> =
        LiveDataReactiveStreams.fromPublisher(
            repository.getListProduct()
        )

    override fun getProduct(): LiveData<DataWrapper<ProductCreatePallet>> =
        LiveDataReactiveStreams.fromPublisher(repository.getProduct()
            .doOnNext {
                currentProduct = it.data
            }
        )

    override fun getListPallet(): LiveData<DataWrapper<List<PalletCreatePallet>>> =
        LiveDataReactiveStreams.fromPublisher(repository.getListPallet())

    override fun getPallet(): LiveData<DataWrapper<PalletCreatePallet>> =
        LiveDataReactiveStreams.fromPublisher(repository.getPallet()
            .doOnNext {
                currentPallet = it.data
            })

    override fun getListBox(): LiveData<DataWrapper<List<BoxCreatePallet>>> =
        LiveDataReactiveStreams.fromPublisher(repository.getListBox())

    override fun getBox(): LiveData<DataWrapper<BoxCreatePallet>> =
        LiveDataReactiveStreams.fromPublisher(repository.getBox()
            .doOnNext {
                currentBox = it.data
            })


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

    override fun saveProduct(product: ProductCreatePallet) = repository.saveProduct(product)

    override fun dellProduct(product: ProductCreatePallet) = repository.dellProduct(product)

    override fun savePallet(pallet: PalletCreatePallet) = repository.savePallet(pallet)

    override fun dellPallet(pallet: PalletCreatePallet) = repository.dellPallet(pallet)

    override fun saveBox(box: BoxCreatePallet) = repository.saveBox(box)

    override fun dellBox(box: BoxCreatePallet) = repository.dellBox(box)
}