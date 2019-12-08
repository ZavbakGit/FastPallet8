package `fun`.gladkikh.app.fastpallet8.db.dao


import `fun`.gladkikh.app.fastpallet8.db.intity.ActionDb
import `fun`.gladkikh.app.fastpallet8.db.intity.CreatePalletDb
import `fun`.gladkikh.app.fastpallet8.db.intity.ProductActionDb
import `fun`.gladkikh.app.fastpallet8.db.intity.ProductCreatePalletDb
import `fun`.gladkikh.app.fastpallet8.map.toDocument
import androidx.room.Dao
import androidx.room.Transaction

@Dao
interface MainDao:DocumentDao,CreatPalletDao,ActionDao {


    //region function Action
    //******************************************************************************************
    @Transaction
    fun insertOrUpdate(entity: ActionDb) {
        if (insertIgnore(entity) == -1L) {
            update(entity)
        }
        insertOrUpdate(entity.toDocument())
    }

    @Transaction
    fun deleteTrigger(entity: ActionDb) {
        val doc = entity.toDocument()
        delete(entity)
        delete(doc)
    }

    @Transaction
    fun saveActionFromServer(
        doc: ActionDb,
        listSave: List<ProductActionDb>,
        lisDell: List<ProductActionDb>
    ) {
        insertOrUpdate(doc)
        lisDell.forEach {
            delete(it)
        }

        listSave.forEach {
            insertOrUpdate(it)
        }

    }

    //endregion


    //region function Create Pallet
    //******************************************************************************************
    @Transaction
    fun saveCreatePalletFromServer(
        doc: CreatePalletDb,
        listSave: List<ProductCreatePalletDb>,
        lisDell: List<ProductCreatePalletDb>
    ) {
        insertOrUpdate(doc)
        lisDell.forEach {
            delete(it)
        }

        listSave.forEach {
            insertOrUpdate(it)
        }

    }
    //endregion

    @Transaction
    fun insertOrUpdate(entity: CreatePalletDb) {
        if (insertIgnore(entity) == -1L) {
            update(entity)
        }
        insertOrUpdate(entity.toDocument())
    }

    @Transaction
    fun deleteTrigger(entity: CreatePalletDb) {
        val doc = entity.toDocument()
        delete(entity)
        delete(doc)
    }
    //endregion

}