package `fun`.gladkikh.app.fastpallet8.di


import `fun`.gladkikh.app.fastpallet8.db.AppDatabase
import `fun`.gladkikh.app.fastpallet8.db.dao.CreatePalletUpdateDao
import `fun`.gladkikh.app.fastpallet8.domain.model.creatpallet.CreatePalletModelRx
import `fun`.gladkikh.app.fastpallet8.domain.model.creatpallet.CreatePalletModelRxImpl
import `fun`.gladkikh.app.fastpallet8.domain.usecase.recalcdb.RecalcDbUseCase
import `fun`.gladkikh.app.fastpallet8.repository.CreatePalletRepository
import `fun`.gladkikh.app.fastpallet8.repository.CreatePalletRepositoryImpl
import `fun`.gladkikh.app.fastpallet8.repository.SettingsRepository
import `fun`.gladkikh.app.fastpallet8.ui.creatpallet.box.BoxCreatePalletViewModel
import `fun`.gladkikh.app.fastpallet8.ui.creatpallet.doc.DocCreatePalletViewModel
import `fun`.gladkikh.app.fastpallet8.ui.creatpallet.pallet.PalletCreatePalletViewModel
import `fun`.gladkikh.app.fastpallet8.ui.creatpallet.product.ProductCreatePalletViewModel
import `fun`.gladkikh.app.fastpallet8.ui.creatpallet.productdialog.ProductDialogCreatePalletViewModel
import `fun`.gladkikh.app.fastpallet8.ui.test.TestViewModel
import `fun`.gladkikh.fastpallet7.model.usecase.testdata.AddTestDataUseCase
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object DependencyModule {

    val appModule = module {
        single { getDataBase(androidContext()) }

        //****************************************************************************************
        //DAO
        single { getCreatePalletUpdateDao(get()) }
        //****************************************************************************************
        //REPOSITORY
        single { getCreatePalletRepository(get()) }
        single { SettingsRepository(get()) }

        //****************************************************************************************
        //USECASE
        single { AddTestDataUseCase(get()) }
        single { RecalcDbUseCase(get()) }
        //****************************************************************************************
        //MODEL
        single { getCreatePalletModelRx(get()) }
        //****************************************************************************************
        //VIEW MODEL
        viewModel { BoxCreatePalletViewModel(get()) }
        viewModel { PalletCreatePalletViewModel(get()) }
        viewModel { ProductDialogCreatePalletViewModel(get()) }
        viewModel { ProductCreatePalletViewModel(get()) }
        viewModel { DocCreatePalletViewModel(get()) }
        viewModel { TestViewModel(get(),get()) }

    }




    private fun getCreatePalletRepository(dao: CreatePalletUpdateDao): CreatePalletRepository {
        return CreatePalletRepositoryImpl(dao)
    }

    private fun getCreatePalletModelRx(repository: CreatePalletRepository): CreatePalletModelRx {
        return CreatePalletModelRxImpl(repository)
    }


    private fun getDataBase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "mydatabase")
            .addCallback(
                object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

//                        getListTriggerCreatePallet().forEach {
//                            db.execSQL(it)
//                        }


                    }
                }

            )
            //.allowMainThreadQueries()
            .build()
    }


    private fun getCreatePalletUpdateDao(database: AppDatabase): CreatePalletUpdateDao {
        return database.getCreatePalletUpdateDao()
    }


}
