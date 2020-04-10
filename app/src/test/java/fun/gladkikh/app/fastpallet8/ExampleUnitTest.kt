package `fun`.gladkikh.app.fastpallet8

import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    fun get(i: Int): Flowable<Int> {
        return Flowable.just(i)
            .map {
                if (it == 10) {
                    throw Throwable("Eror")
                } else {
                    it
                }
            }
    }

    @Test
    fun addition_isCorrect() {

        val com1 = Flowable.just(1)
            .doOnNext {
                println(it)
            }
            .ignoreElements()

        val com2 = Flowable.just(2)
            .doOnNext {
                println(it)
            }
            .ignoreElements()


        com1
            .andThen ( Completable.defer{com2} )
            .subscribe()




        assertEquals(4, 2 + 2)
    }


}
