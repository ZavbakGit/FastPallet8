package `fun`.gladkikh.app.fastpallet8.db

import `fun`.gladkikh.app.fastpallet8.db.dao.CreatePalletUpdateDao
import `fun`.gladkikh.app.fastpallet8.db.intity.BoxCreatePalletDb
import `fun`.gladkikh.app.fastpallet8.db.intity.CreatePalletDb
import `fun`.gladkikh.app.fastpallet8.db.intity.PalletCreatePalletDb
import `fun`.gladkikh.app.fastpallet8.db.intity.ProductCreatePalletDb



import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        CreatePalletDb::class,
        ProductCreatePalletDb::class,
        PalletCreatePalletDb::class,
        BoxCreatePalletDb::class], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getCreatePalletUpdateDao(): CreatePalletUpdateDao
}