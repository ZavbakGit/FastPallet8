package `fun`.gladkikh.app.fastpallet8.domain.model.documentmodel

import `fun`.gladkikh.app.fastpallet8.domain.entity.ItemListDocument
import `fun`.gladkikh.app.fastpallet8.domain.usecase.creatpallet.SendCreatePalletUseCase
import `fun`.gladkikh.app.fastpallet8.domain.usecase.documents.LoadDocumentsUseCase
import `fun`.gladkikh.app.fastpallet8.domain.usecase.testdata.AddTestDataActionUseCase
import `fun`.gladkikh.app.fastpallet8.domain.usecase.testdata.AddTestDataCreatePalletUseCase
import `fun`.gladkikh.app.fastpallet8.repository.document.DocumentRepository
import `fun`.gladkikh.app.fastpallet8.domain.model.Type
import `fun`.gladkikh.app.fastpallet8.domain.usecase.testdata.AddTestDataInventoryPalletUseCase
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers


class DocumentModelImpl(
    private val repository: DocumentRepository
    , private val loadDocumentsUseCase: LoadDocumentsUseCase
    , private val sendCreatePalletUseCase: SendCreatePalletUseCase
    , private val addTestDataCreatePalletUseCase: AddTestDataCreatePalletUseCase
    , private val addTestDataActionUseCase: AddTestDataActionUseCase
    , private val addTestDataInventoryPalletUseCase: AddTestDataInventoryPalletUseCase
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

    override fun deleteDocument(itemListDocument: ItemListDocument): Completable {
        return when (itemListDocument.type) {
            Type.CREATE_PALLET -> {
                Completable.fromAction {
                    val doc = repository.getCreatePalletByGuid(itemListDocument.guid)
                    repository.delete(doc!!)
                }
            }
            else -> {
                Completable.error(Throwable("Неизвестный документ!"))
                    .subscribeOn(Schedulers.io())
            }
        }

    }

    override fun addTestDataCreatePallet(): Completable {
        return Completable.fromCallable {
            addTestDataCreatePalletUseCase.save()
        }
    }

    override fun addTestDataAction(): Completable {
        return Completable.fromCallable {
            addTestDataActionUseCase.save()
        }
    }

    override fun addTestDataInventoryPallet(): Completable {
        return Completable.fromCallable {
            addTestDataInventoryPalletUseCase.save()
        }
    }


}