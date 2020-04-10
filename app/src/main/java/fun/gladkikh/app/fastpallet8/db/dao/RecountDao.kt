package `fun`.gladkikh.app.fastpallet8.db.dao

import androidx.room.Query

interface RecountDao {
    @Query(
"UPDATE PalletCreatePalletDb SET " +
        "count = (select ROUND(SUM(box.count)*1000)/1000 from BoxCreatePalletDb box where box.guidPallet = PalletCreatePalletDb.guid) " +
        ",countBox = (select ROUND(SUM(box.countBox)*1000)/1000 from BoxCreatePalletDb box where box.guidPallet = PalletCreatePalletDb.guid) " +
        ",countRow = (select COUNT(distinct box.guid) from BoxCreatePalletDb box where box.guidPallet = PalletCreatePalletDb.guid) " +
        "WHERE guid = :guidPallet"
    )
    fun recalculatePalletCreatePallet(guidPallet: String)

    @Query(
        "UPDATE ProductCreatePalletDb SET " +
                "count = (select ROUND(SUM(pal.count)*1000)/1000 from PalletCreatePalletDb pal where pal.guidProduct = ProductCreatePalletDb.guid) " +
                ",countBox = (select ROUND(SUM(pal.countBox)*1000)/1000 from PalletCreatePalletDb pal where pal.guidProduct = ProductCreatePalletDb.guid) " +
                ",countRow = (select SUM(pal.countRow) from PalletCreatePalletDb pal where pal.guidProduct = ProductCreatePalletDb.guid) " +
                "WHERE guid = :guidProduct"
    )
    fun recalculateProductCreatePallet(guidProduct: String)


}