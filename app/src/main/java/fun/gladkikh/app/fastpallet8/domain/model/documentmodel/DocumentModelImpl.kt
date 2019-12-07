package `fun`.gladkikh.app.fastpallet8.domain.model.documentmodel

import `fun`.gladkikh.app.fastpallet8.domain.model.entity.ItemListDocument
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.CreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.model.entity.creatpallet.ProductCreatePallet
import `fun`.gladkikh.app.fastpallet8.domain.usecase.creatpallet.SendCreatePalletUseCase
import `fun`.gladkikh.app.fastpallet8.domain.usecase.documents.LoadDocumentsUseCase
import `fun`.gladkikh.app.fastpallet8.repository.DocumentRepository
import `fun`.gladkikh.fastpallet7.model.Type
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers


class DocumentModelImpl(
    private val repository: DocumentRepository
    , private val loadDocumentsUseCase: LoadDocumentsUseCase
    , private val sendCreatePalletUseCase: SendCreatePalletUseCase
) : DocumentModelRx {
    override fun getListDocument(): Flowable<List<ItemListDocument>> {
        return repository.getListDocument()
    }

    override fun loadDocuments(): Completable {
        return loadDocumentsUseCase.getLoadCompletable()
    }

    override fun sendDocument(itemListDocument: ItemListDocument): Completable {
        return when (itemListDocument.type) {
            Type.CREATE_PALLET -> {
                sendCreatePalletUseCase.send(itemListDocument)
            }
            else -> {
                Completable.error(Throwable("Неизвестный документ!"))
                    .subscribeOn(Schedulers.io())
            }
        }
    }


}