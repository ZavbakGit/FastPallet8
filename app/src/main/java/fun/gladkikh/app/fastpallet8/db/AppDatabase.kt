package `fun`.gladkikh.app.fastpallet8.db

import `fun`.gladkikh.app.fastpallet8.db.dao.MainDao
import `fun`.gladkikh.app.fastpallet8.db.intity.*


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ItemListDocumentDb::class
        , CreatePalletDb::class
        , ProductCreatePalletDb::class
        , PalletCreatePalletDb::class
        , BoxCreatePalletDb::class
        , ActionDb::class
        , ProductActionDb::class
        , PalletActionDb::class
        , BoxActionDb::class
    ]
    , version = 1
    , exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getCreatePalletUpdateDao(): MainDao
}

