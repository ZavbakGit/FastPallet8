package `fun`.gladkikh.app.fastpallet8.db.dao

import `fun`.gladkikh.app.fastpallet8.db.intity.BoxInventoryPalletDb
import `fun`.gladkikh.app.fastpallet8.db.intity.InventoryPalletDb
import androidx.room.*
import java.math.BigDecimal


interface InventoryPalletDao {
    //region function for InventoryPallet
    //******************************************************************************************
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnore(entity: InventoryPalletDb): Long

    @Update
    fun update(entity: InventoryPalletDb)

    @Delete
    fun delete(entity: InventoryPalletDb)

    @Query("SELECT * FROM InventoryPalletDb WHERE guid = :guid")
    fun getInventoryPalletByGuid(guid: String): InventoryPalletDb?

    //endregion

    //region function for Box
    //******************************************************************************************
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnore(entity: BoxInventoryPalletDb): Long

    @Update
    fun update(entity: BoxInventoryPalletDb)

    @Delete
    fun delete(entity: BoxInventoryPalletDb)

    @Query("SELECT * FROM BoxInventoryPalletDb WHERE guid = :guid")
    fun getBoxInventoryPalletByGuid(guid: String): BoxInventoryPalletDb

    @Transaction
    fun insertOrUpdate(entity: BoxInventoryPalletDb) {
        var countBox: Int = entity.countBox ?: 0
        var count: BigDecimal = (entity.count ?: 0f).toBigDecimal()
        var row = 1

        if (insertIgnore(entity) == -1L) {
            val box = getBoxInventoryPalletByGuid(entity.guid)

            countBox -= (box.countBox ?: 0)
            count -= (box.count ?: 0f).toBigDecimal()
            row -= 1

            update(entity)
        }

        val doc = getInventoryPalletByGuid(entity.guidDoc)
        doc!!.count = ((doc.count ?: 0f).toBigDecimal() + count).toFloat()
        doc.countBox = (doc.countBox ?: 0) + countBox
        doc.countRow = (doc.countRow ?: 0) + row

        update(doc)


    }

    @Transaction
    fun deleteTrigger(entity: BoxInventoryPalletDb) {
        val countBox: Int = entity.countBox ?: 0
        val count: BigDecimal = (entity.count ?: 0f).toBigDecimal()

        delete(entity)


        val doc = getInventoryPalletByGuid(entity.guidDoc)
        doc!!.count = ((doc.count ?: 0f).toBigDecimal() - count).toFloat()
        doc.countBox = (doc.countBox ?: 0) - countBox
        doc.countRow = (doc.countRow ?: 0) - 1

        update(doc)

    }

    @Query(
        "SELECT * FROM BoxInventoryPalletDb WHERE guidDoc = :guidDoc " +
                "  ORDER BY dateChanged DESC"
    )
    fun getListBoxByGuidDoc(guidDoc: String): List<BoxInventoryPalletDb>

    //endregion

}