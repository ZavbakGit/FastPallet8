package `fun`.gladkikh.app.fastpallet8.db.dao

import `fun`.gladkikh.app.fastpallet8.db.intity.ItemListDocumentDb
import androidx.room.*
import io.reactivex.Flowable

interface DocumentDao{
    //region function for Document
    //******************************************************************************************
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnore(entity: ItemListDocumentDb): Long

    @Update
    fun update(entity: ItemListDocumentDb)

    @Transaction
    fun insertOrUpdate(entity: ItemListDocumentDb) {
        if (insertIgnore(entity) == -1L) {
            update(entity)
        }
    }

    @Delete
    fun delete(entity: ItemListDocumentDb)

    @Query("SELECT * FROM ItemListDocumentDb WHERE guid = :guid")
    fun getDocumentByGuid(guid: String): ItemListDocumentDb

    @Query("SELECT * FROM ItemListDocumentDb WHERE guidServer = :guidServer")
    fun getDocumentByGuidServer(guidServer: String): ItemListDocumentDb


    @Query("SELECT * FROM ItemListDocumentDb")
    fun getListDocument(): Flowable<List<ItemListDocumentDb>>

    //endregion
}