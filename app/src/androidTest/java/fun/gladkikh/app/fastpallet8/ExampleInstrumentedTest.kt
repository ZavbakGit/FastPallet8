package `fun`.gladkikh.app.fastpallet8

import `fun`.gladkikh.app.fastpallet8.db.AppDatabase
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.reactivex.Flowable
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {




    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        //

        (0..10).flatMap {
            (0..5)
        }.forEach {
            println(it)
        }

        assertEquals("fun.gladkikh.app.fastpallet8", appContext.packageName)
    }


}
