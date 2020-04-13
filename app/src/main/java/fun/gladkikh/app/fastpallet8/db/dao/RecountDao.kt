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
                ",countPallet = (select COUNT(distinct pal.guid) from PalletCreatePalletDb pal where pal.guidProduct = ProductCreatePalletDb.guid) " +
                "WHERE guid = :guidProduct"
    )
    fun recalculateProductCreatePallet(guidProduct: String)



    @Query(
        "UPDATE ProductActionDb SET " +
                "count = coalesce((select ROUND(SUM(coalesce(pal.count,0))*1000)/1000 from PalletActionDb pal where pal.guidProduct = ProductActionDb.guid),0) + (select ROUND(SUM(coalesce(box.count,0))*1000)/1000 from BoxActionDb box where box.guidProduct = ProductActionDb.guid) " +
                ",countBox = coalesce((select ROUND(SUM(coalesce(pal.countBox,0))*1000)/1000 from PalletActionDb pal where pal.guidProduct = ProductActionDb.guid),0) + (select ROUND(SUM(box.countBox)*1000)/1000 from BoxActionDb box where box.guidProduct = ProductActionDb.guid) " +
                ",countRow = coalesce((select SUM(coalesce(pal.countRow,0)) from PalletActionDb pal where pal.guidProduct = ProductActionDb.guid),0) + (select COUNT(distinct box.guid) from BoxActionDb box where box.guidProduct = ProductActionDb.guid) " +
                ",countPallet = (select COUNT(distinct pal.guid) from PalletActionDb pal where pal.guidProduct = ProductActionDb.guid) " +
                "WHERE guid =:guidProduct"
    )
    fun recalculateProductAction(guidProduct: String)


    @Query(
        "UPDATE InventoryPalletDb SET " +
                "count = (select ROUND(SUM(box.count)*1000)/1000 from BoxInventoryPalletDb box where box.guidDoc = InventoryPalletDb.guid) " +
                ",countBox = (select ROUND(SUM(box.countBox)*1000)/1000 from BoxInventoryPalletDb box where box.guidDoc = InventoryPalletDb.guid) " +
                ",countRow = (select COUNT(distinct box.guid) from BoxInventoryPalletDb box where box.guidDoc = InventoryPalletDb.guid) " +
                "WHERE guid = :guidDoc"
    )
    fun recalculateInventoryPallet(guidDoc: String)



}