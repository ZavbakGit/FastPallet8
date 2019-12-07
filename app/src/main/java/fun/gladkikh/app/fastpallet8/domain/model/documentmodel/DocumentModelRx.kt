package `fun`.gladkikh.app.fastpallet8.domain.model.documentmodel

import `fun`.gladkikh.app.fastpallet8.domain.model.entity.ItemListDocument
import io.reactivex.Completable
import io.reactivex.Flowable

interface DocumentModelRx {
    fun getListDocument(): Flowable<List<ItemListDocument>>
    fun loadDocuments():Completable
    fun sendDocument(itemListDocument: ItemListDocument):Completable

}