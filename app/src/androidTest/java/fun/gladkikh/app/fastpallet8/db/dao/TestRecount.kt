package `fun`.gladkikh.app.fastpallet8.db.dao

import `fun`.gladkikh.app.fastpallet8.db.AppDatabase
import `fun`.gladkikh.app.fastpallet8.db.intity.BoxCreatePalletDb
import `fun`.gladkikh.app.fastpallet8.db.intity.CreatePalletDb
import `fun`.gladkikh.app.fastpallet8.db.intity.PalletCreatePalletDb
import `fun`.gladkikh.app.fastpallet8.db.intity.ProductCreatePalletDb
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import java.math.BigDecimal

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class TestRecount {

    lateinit var appDatabase: AppDatabase


    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    lateinit var mainDao: MainDao

    val doc = CreatePalletDb(
        guid = "1",
        barcode = "1",
        date = Date().time,
        dateChanged = Date().time,
        description = "Test",
        guidServer = "1",
        isLastLoad = true,
        number = "1",
        status = 3
    )

    val listProduct = (1..3).map {
        ProductCreatePalletDb(
            guid = it.toString(),
            number = it.toString(),
            isLastLoad = true,
            dateChanged = Date().time,
            barcode = it.toString(),
            codeProduct = it.toString(),
            count = 0f,
            countBack = 0f,
            countBox = 0,
            countBoxBack = 0,
            countPallet = 0,
            countRow = 0,
            ed = "кг.",
            edCoff = 1f,
            guidDoc = doc.guid,
            guidProductBack = doc.guidServer,
            nameProduct = "Товар $it",
            weightBarcode = "154154",
            weightCoffProduct = null,
            weightEndProduct = null,
            weightStartProduct = null

        )
    }

    val listPallet = listProduct.flatMap { product ->
        (1..3).map {
            PalletCreatePalletDb(
                guid = product.guid + "_" + it,
                guidProduct = product.guid,
                nameProduct = product.nameProduct,
                countRow = null,
                countBox = null,
                count = null,
                barcode = null,
                dateChanged = Date().time,
                number = product.guid + "_" + it,
                sclad = null,
                state = null
            )
        }
    }

    val listBox = listPallet.flatMap { pallet ->
        (1..10).map {
            BoxCreatePalletDb(
                guid = pallet.guid + "_" + it,
                dateChanged = Date().time,
                barcode = null,
                count = 10.75f,
                countBox = 1,
                guidPallet = pallet.guid
            )
        }
    }


    @Before
    fun init() {
        //appDatabase = AppDatabase.getInstance(appContext)
        appDatabase = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java).build()
        mainDao = appDatabase.getMainDao()

    }


    @Test
    fun useAppContext() {
        equalsObjectAndDb()

        listProduct.forEach {
            println(getSumByProduct(it.guid))
        }

        assertTrue(true)
//
//        listPallet.forEach {
//            println(it.guid)
//        }
    }


    private fun equalsObjectAndDb() {
        with(mainDao) {
            var docDb = mainDao.getCreatePalletByGuid(doc.guid)
            assertNull("База не пуста!", docDb)


            insertOrUpdate(doc)

            listProduct.forEach {
                Thread.sleep(100)
                insertOrUpdate(it)
            }

            listPallet.forEach {
                Thread.sleep(100)
                insertOrUpdate(it)
            }

            listBox.forEach {
                Thread.sleep(100)
                insertOrUpdate(it)
            }



            docDb = getCreatePalletByGuid(doc.guid)
            assertEquals(docDb, doc)


            assertEquals(
                "Разные списки продуктов!",
                listProduct.sortedBy { it.guid },
                getProductListByGuidDoc(doc.guid).sortedBy { it.guid }
            )

            listProduct.sortedBy { it.guid }.forEach { product ->

                val listPalletByProduct =
                    listPallet.filter { it.guidProduct == product.guid }.sortedBy { it.guid }

                assertEquals(
                    "Разные списки паллет!",
                    listPalletByProduct,
                    getListPalletByGuidProduct(product.guid).sortedBy { it.guid }
                )

                listPalletByProduct.forEach { pallet ->
                    val listBoxByPallet =
                        listBox.filter { it.guidPallet == pallet.guid }.sortedBy { it.guid }

                    assertEquals(
                        "Разные списки Коробок!",
                        listBoxByPallet,
                        getListBoxByGuidPallet(pallet.guid).sortedBy { it.guid }
                    )

                }
            }

        }
    }

    private fun getSumByPallet(guidPallet: String): WrapperSum =
        listBox.filter { it.guidPallet == guidPallet }
            .fold(WrapperSum(0f, 0, 0)) { total, next ->
                val countNext = (next.count ?: 0f).toBigDecimal()
                val countBoxNex = next.countBox ?: 0
                val countRowNex = 1

                total.copy(
                    count = ((total.count ?: 0f).toBigDecimal() + countNext).toFloat(),
                    countBox = total.countBox + countBoxNex,
                    countRow = total.countRow + countRowNex
                )
            }

    private fun getSumByProduct(guidProduct: String): WrapperSum =
        listPallet.filter { it.guidProduct == guidProduct }
            .fold(WrapperSum(0f, 0, 0)) { total, next ->

                val sumPallet = getSumByPallet(next.guid)

                total.copy(
                    count = ((sumPallet.count ?: 0f).toBigDecimal() + (total.count
                        ?: 0f).toBigDecimal()).toFloat(),
                    countRow = total.countRow + sumPallet.countRow,
                    countBox = total.countBox + sumPallet.countBox
                )
            }


    data class WrapperSum(val count: Float?, val countBox: Int, val countRow: Int)

    @After
    fun closeDb() {
        appDatabase.close()
    }
}
