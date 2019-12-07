package `fun`.gladkikh.app.fastpallet8.di


import `fun`.gladkikh.app.fastpallet8.db.AppDatabase
import `fun`.gladkikh.app.fastpallet8.db.dao.DocumentDao
import `fun`.gladkikh.app.fastpallet8.domain.model.creatpallet.CreatePalletModelRx
import `fun`.gladkikh.app.fastpallet8.domain.model.creatpallet.CreatePalletModelRxImpl
import `fun`.gladkikh.app.fastpallet8.domain.model.documentmodel.DocumentModelImpl
import `fun`.gladkikh.app.fastpallet8.domain.model.documentmodel.DocumentModelRx
import `fun`.gladkikh.app.fastpallet8.domain.usecase.creatpallet.SaveLoadedCreatePalletUseCase
import `fun`.gladkikh.app.fastpallet8.domain.usecase.creatpallet.SendCreatePalletUseCase
import `fun`.gladkikh.app.fastpallet8.domain.usecase.documents.LoadDocumentsUseCase
import `fun`.gladkikh.app.fastpallet8.domain.usecase.recalcdb.RecalcDbUseCase
import `fun`.gladkikh.app.fastpallet8.domain.usecase.testdata.AddTestDataUseCase
import `fun`.gladkikh.app.fastpallet8.network.ApiFactory
import `fun`.gladkikh.app.fastpallet8.repository.*
import `fun`.gladkikh.app.fastpallet8.ui.creatpallet.box.BoxCreatePalletViewModel
import `fun`.gladkikh.app.fastpallet8.ui.creatpallet.doc.DocCreatePalletViewModel
import `fun`.gladkikh.app.fastpallet8.ui.creatpallet.pallet.PalletCreatePalletViewModel
import `fun`.gladkikh.app.fastpallet8.ui.creatpallet.product.ProductCreatePalletViewModel
import `fun`.gladkikh.app.fastpallet8.ui.creatpallet.productdialog.ProductDialogCreatePalletViewModel
import `fun`.gladkikh.app.fastpallet8.ui.documentlist.DocumentListViewModel
import `fun`.gladkikh.app.fastpallet8.ui.test.TestViewModel
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
        single { getDocumentRepository(get()) }

        //****************************************************************************************
        //USE CASE
        single { AddTestDataUseCase(get()) }
        single { RecalcDbUseCase(get()) }
        single { SaveLoadedCreatePalletUseCase(get()) }
        single { LoadDocumentsUseCase(get(), get(), get()) }
        single { SendCreatePalletUseCase(get(), get(), get()) }
        //****************************************************************************************
        //MODEL
        single { getCreatePalletModelRx(get()) }
        single { getDocumentModelRx(get(), get(),get()) }
        //****************************************************************************************
        //VIEW MODEL
        viewModel { DocumentListViewModel(get()) }
        viewModel { BoxCreatePalletViewModel(get()) }
        viewModel { PalletCreatePalletViewModel(get()) }
        viewModel { ProductDialogCreatePalletViewModel(get()) }
        viewModel { ProductCreatePalletViewModel(get()) }
        viewModel { DocCreatePalletViewModel(get()) }
        viewModel { TestViewModel(get(), get()) }
        //USECASE
        single { ApiFactory(get()) }
        //****************************************************************************************


    }

    //DAO
    private fun getCreatePalletUpdateDao(database: AppDatabase): DocumentDao {
        return database.getCreatePalletUpdateDao()
    }

    //REPOSITORY
    private fun getCreatePalletRepository(dao: DocumentDao): CreatePalletRepository {
        return CreatePalletRepositoryImpl(dao)
    }

    private fun getDocumentRepository(dao: DocumentDao): DocumentRepository {
        return DocumentRepositoryImpl(dao)
    }

    //MODEL
    private fun getCreatePalletModelRx(repository: CreatePalletRepository): CreatePalletModelRx {
        return CreatePalletModelRxImpl(repository)
    }

    private fun getDocumentModelRx(
        repository: DocumentRepository
        , loadDocumentsUseCase: LoadDocumentsUseCase
        , sendCreatePalletUseCase: SendCreatePalletUseCase
    ): DocumentModelRx {
        return DocumentModelImpl(repository, loadDocumentsUseCase,sendCreatePalletUseCase)
    }


    //DB
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


}
