package `fun`.gladkikh.app.fastpallet8.di


import `fun`.gladkikh.app.fastpallet8.db.AppDatabase
import `fun`.gladkikh.app.fastpallet8.db.dao.MainDao
import `fun`.gladkikh.app.fastpallet8.domain.model.action.ActionModelRx
import `fun`.gladkikh.app.fastpallet8.domain.model.action.ActionModelRxImpl
import `fun`.gladkikh.app.fastpallet8.domain.model.creatpallet.CreatePalletModelRx
import `fun`.gladkikh.app.fastpallet8.domain.model.creatpallet.CreatePalletModelRxImpl
import `fun`.gladkikh.app.fastpallet8.domain.model.documentmodel.DocumentModelImpl
import `fun`.gladkikh.app.fastpallet8.domain.model.documentmodel.DocumentModelRx
import `fun`.gladkikh.app.fastpallet8.domain.model.inventorypallet.InventoryPalletModelRx
import `fun`.gladkikh.app.fastpallet8.domain.model.inventorypallet.InventoryPalletModelRxImpl
import `fun`.gladkikh.app.fastpallet8.domain.usecase.GetInfoPalletUseCase
import `fun`.gladkikh.app.fastpallet8.domain.usecase.creatpallet.SaveLoadedCreatePalletUseCase
import `fun`.gladkikh.app.fastpallet8.domain.usecase.creatpallet.SendCreatePalletUseCase
import `fun`.gladkikh.app.fastpallet8.domain.usecase.documents.LoadDocumentsUseCase
import `fun`.gladkikh.app.fastpallet8.domain.usecase.recalcdb.RecalcDbUseCase
import `fun`.gladkikh.app.fastpallet8.domain.usecase.testdata.AddTestDataActionUseCase
import `fun`.gladkikh.app.fastpallet8.domain.usecase.testdata.AddTestDataCreatePalletUseCase
import `fun`.gladkikh.app.fastpallet8.domain.usecase.testdata.AddTestDataInventoryPalletUseCase
import `fun`.gladkikh.app.fastpallet8.network.ApiFactory
import `fun`.gladkikh.app.fastpallet8.repository.action.ActionRepository
import `fun`.gladkikh.app.fastpallet8.repository.action.ActionRepositoryImpl
import `fun`.gladkikh.app.fastpallet8.repository.creatpallet.CreatePalletRepository
import `fun`.gladkikh.app.fastpallet8.repository.creatpallet.CreatePalletRepositoryImpl
import `fun`.gladkikh.app.fastpallet8.repository.document.DocumentRepository
import `fun`.gladkikh.app.fastpallet8.repository.document.DocumentRepositoryImpl
import `fun`.gladkikh.app.fastpallet8.repository.inventorypallet.InventoryPalletRepository
import `fun`.gladkikh.app.fastpallet8.repository.inventorypallet.InventoryPalletRepositoryImpl
import `fun`.gladkikh.app.fastpallet8.repository.setting.SettingsRepository
import `fun`.gladkikh.app.fastpallet8.ui.screen.action.box.BoxActionViewModel
import `fun`.gladkikh.app.fastpallet8.ui.screen.action.doc.DocActionViewModel
import `fun`.gladkikh.app.fastpallet8.ui.screen.action.product.ProductActionViewModel
import `fun`.gladkikh.app.fastpallet8.ui.screen.action.productdialog.ProductDialogActionViewModel
import `fun`.gladkikh.app.fastpallet8.ui.screen.creatpallet.box.BoxCreatePalletViewModel
import `fun`.gladkikh.app.fastpallet8.ui.screen.creatpallet.doc.DocCreatePalletViewModel
import `fun`.gladkikh.app.fastpallet8.ui.screen.creatpallet.pallet.PalletCreatePalletDocViewModel
import `fun`.gladkikh.app.fastpallet8.ui.screen.creatpallet.product.ProductCreatePalletViewModel
import `fun`.gladkikh.app.fastpallet8.ui.screen.creatpallet.productdialog.ProductDialogCreatePalletViewModel
import `fun`.gladkikh.app.fastpallet8.ui.screen.documentlist.DocumentListViewModel
import `fun`.gladkikh.app.fastpallet8.ui.screen.inventorypallet.box.BoxInventoryPalletViewModel
import `fun`.gladkikh.app.fastpallet8.ui.screen.inventorypallet.doc.DocInventoryPalletViewModel
import `fun`.gladkikh.app.fastpallet8.ui.screen.inventorypallet.productdialog.ProductDialogInventoryPalletViewModel
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
        single { getInventoryPalletRepository(get()) }
        single { getActionRepositoryImpl(get()) }
        single { getCreatePalletRepository(get()) }
        single { SettingsRepository(get()) }
        single { getDocumentRepository(get()) }

        //****************************************************************************************
        //USE CASE
        single { AddTestDataInventoryPalletUseCase(get()) }
        single { AddTestDataCreatePalletUseCase(get()) }
        single { AddTestDataActionUseCase(get()) }
        single { RecalcDbUseCase(get()) }
        single { SaveLoadedCreatePalletUseCase(get()) }
        single { LoadDocumentsUseCase(get(), get(), get()) }
        single { SendCreatePalletUseCase(get(), get(), get()) }


        single { GetInfoPalletUseCase(get(), get()) }


        //****************************************************************************************
        //MODEL
        single { getInventoryPalletModelRx(get(),get()) }
        single { getActionModelRx(get(), get()) }
        single { getCreatePalletModelRx(get()) }
        single { getDocumentModelRx(get(), get(), get(), get(), get(), get()) }
        //****************************************************************************************
        //VIEW MODEL
        viewModel { DocumentListViewModel(get()) }
        viewModel { BoxCreatePalletViewModel(get()) }
        viewModel { PalletCreatePalletDocViewModel(get()) }
        viewModel { ProductDialogCreatePalletViewModel(get()) }
        viewModel { ProductCreatePalletViewModel(get()) }
        viewModel { DocCreatePalletViewModel(get()) }


        viewModel { DocActionViewModel(get()) }
        viewModel { ProductActionViewModel(get()) }
        viewModel { ProductDialogActionViewModel(get()) }
        viewModel { BoxActionViewModel(get()) }


        viewModel { DocInventoryPalletViewModel(get()) }
        viewModel { BoxInventoryPalletViewModel(get()) }
        viewModel { ProductDialogInventoryPalletViewModel(get()) }


        viewModel { TestViewModel(get(), get()) }
        //USECASE
        single { ApiFactory(get()) }
        //****************************************************************************************


    }

    //DAO
    private fun getCreatePalletUpdateDao(database: AppDatabase): MainDao {
        return database.getMainDao()
    }

    //REPOSITORY
    private fun getInventoryPalletRepository(dao: MainDao): InventoryPalletRepository {
        return InventoryPalletRepositoryImpl(dao)
    }

    private fun getActionRepositoryImpl(dao: MainDao): ActionRepository {
        return ActionRepositoryImpl(
            dao
        )
    }


    private fun getCreatePalletRepository(dao: MainDao): CreatePalletRepository {
        return CreatePalletRepositoryImpl(
            dao
        )
    }

    private fun getDocumentRepository(dao: MainDao): DocumentRepository {
        return DocumentRepositoryImpl(
            dao
        )
    }

    //MODEL
    private fun getInventoryPalletModelRx(
        repository: InventoryPalletRepository
        , getInfoPalletUseCase: GetInfoPalletUseCase
    ): InventoryPalletModelRx {
        return InventoryPalletModelRxImpl(repository,getInfoPalletUseCase)
    }

    private fun getActionModelRx(
        repository: ActionRepository,
        getInfoPalletUseCase: GetInfoPalletUseCase
    ): ActionModelRx {
        return ActionModelRxImpl(repository, getInfoPalletUseCase)
    }

    private fun getCreatePalletModelRx(repository: CreatePalletRepository): CreatePalletModelRx {
        return CreatePalletModelRxImpl(repository)
    }


    private fun getDocumentModelRx(
        repository: DocumentRepository
        , loadDocumentsUseCase: LoadDocumentsUseCase
        , sendCreatePalletUseCase: SendCreatePalletUseCase
        , addTestDataUseCaseCreatePallet: AddTestDataCreatePalletUseCase
        , addTestDataActionUseCase: AddTestDataActionUseCase
        , addTestDataInventoryPalletUseCase: AddTestDataInventoryPalletUseCase
    ): DocumentModelRx {
        return DocumentModelImpl(
            repository,
            loadDocumentsUseCase,
            sendCreatePalletUseCase,
            addTestDataUseCaseCreatePallet,
            addTestDataActionUseCase,
            addTestDataInventoryPalletUseCase
        )
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
