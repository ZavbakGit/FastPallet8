package `fun`.gladkikh.app.fastpallet8.db.dao

import `fun`.gladkikh.app.fastpallet8.db.intity.ActionDb
import `fun`.gladkikh.app.fastpallet8.db.intity.BoxActionDb
import `fun`.gladkikh.app.fastpallet8.db.intity.PalletActionDb
import `fun`.gladkikh.app.fastpallet8.db.intity.ProductActionDb
import androidx.room.*
import java.math.BigDecimal


interface ActionDao {

    //region function for Box
    //******************************************************************************************
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnoreAction(entity: BoxActionDb): Long

    @Update
    fun update(entity: BoxActionDb)

    @Insert
    fun insertListBoxAction(list: List<BoxActionDb>)

    @Transaction
    fun insertOrUpdate(entity: BoxActionDb) {
        var countBox: Int = entity.countBox ?: 0
        var count: BigDecimal = (entity.count ?: 0f).toBigDecimal()
        var row = 1
        if (insertIgnoreAction(entity) == -1L) {
            val box = getBoxActionByGuid(entity.guid)

            countBox -= (box.countBox ?: 0)
            count -= (box.count ?: 0f).toBigDecimal()
            row -= 1

            update(entity)
        }


        val product = getProductActionByGuid(entity.guidProduct)
        product.count = ((product.count ?: 0f).toBigDecimal() + count).toFloat()
        product.countBox = (product.countBox ?: 0) + countBox
        product.countRow = (product.countRow ?: 0) + row

        update(product)
    }

    @Transaction
    fun deleteTrigger(entity: BoxActionDb) {
        val countBox: Int = entity.countBox ?: 0
        val count: BigDecimal = (entity.count ?: 0f).toBigDecimal()

        delete(entity)

        val product = getProductActionByGuid(entity.guidProduct)
        product.count = ((product.count ?: 0f).toBigDecimal() - count).toFloat()
        product.countBox = (product.countBox ?: 0) - countBox
        product.countRow = (product.countRow ?: 0) - 1
        update(product)
    }

    @Delete
    fun delete(entity: BoxActionDb)

    @Query("SELECT * FROM BoxActionDb WHERE guid = :guid")
    fun getBoxActionByGuid(guid: String): BoxActionDb

    @Query(
        "SELECT * FROM BoxActionDb WHERE guidProduct = :guidProduct " +
                "  ORDER BY dateChanged DESC"
    )
    fun getListBoxByGuidProduct(guidProduct: String): List<BoxActionDb>

    //endregion

    //region function for Pallet
    //******************************************************************************************
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnore(entity: PalletActionDb): Long

    @Update
    fun update(entity: PalletActionDb)

    @Transaction
    fun insertOrUpdate(entity: PalletActionDb) {

        var countBox: Int = entity.countBox ?: 0
        var count: BigDecimal = (entity.count ?: 0f).toBigDecimal()
        var row = 1
        var countPalet = 1

        if (insertIgnore(entity) == -1L) {
            val pallet = getPalletActionByGuid(entity.guid)

            countBox -= (pallet.countBox ?: 0)
            count -= (pallet.count ?: 0f).toBigDecimal()
            row -= 1
            countPalet -= 1

            update(entity)
        }

        val product = getProductActionByGuid(entity.guidProduct)
        product.count = ((product.count ?: 0f).toBigDecimal() + count).toFloat()
        product.countBox = (product.countBox ?: 0) + countBox
        product.countRow = (product.countRow ?: 0) + row
        product.countPallet = (product.countPallet ?: 0) + countPalet

        update(product)
    }

    @Transaction
    fun insertOrUpdateListPallet(list: List<PalletActionDb>) {
        list.forEach {
            if (insertIgnore(it) == -1L) {
                update(it)
            }
        }
    }

    @Transaction
    fun deleteTrigger(entity: PalletActionDb) {
        val countBox: Int = entity.countBox ?: 0
        val count: BigDecimal = (entity.count ?: 0f).toBigDecimal()
        val countRow: Int = entity.countRow ?: 0

        delete(entity)

        val product = getProductActionByGuid(entity.guidProduct)
        product.count = ((product.count ?: 0f).toBigDecimal() - count).toFloat()
        product.countBox = (product.countBox ?: 0) - countBox
        product.countRow = (product.countRow ?: 0) - countRow
        product.countPallet = (product.countPallet ?: 0) - 1

        update(product)
    }

    @Delete
    fun delete(entity: PalletActionDb)

    @Query("SELECT * FROM PalletActionDb WHERE guid = :guid")
    fun getPalletActionByGuid(guid: String): PalletActionDb

    @Query(
        "SELECT * FROM PalletActionDb WHERE guidProduct = :guidProduct" +
                "  ORDER BY dateChanged DESC"
    )
    fun getListPalletActionByGuidProduct(guidProduct: String): List<PalletActionDb>

    @Query("SELECT * FROM PalletActionDb WHERE number = :numberPallet and guidProduct =:guidProduct")
    fun getPalletActionByNumber(numberPallet: String,guidProduct:String): PalletActionDb

    //endregion

    //region function for Product
    //******************************************************************************************
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnore(entity: ProductActionDb): Long

    @Update
    fun update(entity: ProductActionDb)

    @Transaction
    fun insertOrUpdate(entity: ProductActionDb) {
        if (insertIgnore(entity) == -1L) {
            val product = getProductActionByGuid(entity.guid)
            entity.count = product.count
            entity.countBox = product.countBox
            entity.countPallet = product.countPallet
            entity.countRow = product.countRow
            update(entity)
        }
    }

    @Delete
    fun delete(entity: ProductActionDb)

    @Query("SELECT * FROM ProductActionDb WHERE guid = :guid")
    fun getProductActionByGuid(guid: String): ProductActionDb

    @Query("SELECT * FROM ProductActionDb WHERE guidProductBack = :guidProductBack")
    fun getProductActionByGuidServer(guidProductBack: String): ProductActionDb

    @Query("SELECT * FROM ProductActionDb WHERE guidDoc = :guidDoc")
    fun getListProductActionByGuidDoc(guidDoc: String): List<ProductActionDb>


    //endregion
    

    //region function for Action
    //******************************************************************************************
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnore(entity: ActionDb): Long

    @Update
    fun update(entity: ActionDb)


    @Delete
    fun delete(entity: ActionDb)

    @Query("SELECT * FROM ActionDb WHERE guid = :guid")
    fun getActionByGuid(guid: String): ActionDb?

    @Query("SELECT * FROM ActionDb WHERE guidServer = :guidServer")
    fun getActionByGuidServer(guidServer: String): ActionDb?
    //endregion


}