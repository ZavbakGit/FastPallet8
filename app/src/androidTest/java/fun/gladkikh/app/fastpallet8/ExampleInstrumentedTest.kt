package `fun`.gladkikh.app.fastpallet8

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.reactivex.Flowable

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

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

        Flowable.just(1)
            .map {
                return@map null
            }
            .map {
                println(" " + it)
                return@map it
            }
            .subscribe()

        assertEquals("fun.gladkikh.app.fastpallet8", appContext.packageName)
    }
}
