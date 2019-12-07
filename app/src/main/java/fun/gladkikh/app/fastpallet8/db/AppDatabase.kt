package `fun`.gladkikh.app.fastpallet8.db

import `fun`.gladkikh.app.fastpallet8.db.dao.DocumentDao
import `fun`.gladkikh.app.fastpallet8.db.intity.*


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ItemListDocumentDb::class,
        CreatePalletDb::class,
        ProductCreatePalletDb::class,
        PalletCreatePalletDb::class,
        BoxCreatePalletDb::class], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getCreatePalletUpdateDao(): DocumentDao
}