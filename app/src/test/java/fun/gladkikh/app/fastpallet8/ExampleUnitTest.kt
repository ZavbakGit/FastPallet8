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
    @Test
    fun addition_isCorrect() {

        var count = 50

        Flowable.interval(500, TimeUnit.MILLISECONDS)
            .buffer(1000, TimeUnit.MILLISECONDS)
            .doOnNext {
                it.forEach {
                    save(it)
                        .subscribe({

                        }, {
                            println(it.message)
                        })
                }

                println("Установить ${it.last()}")

            }
            .subscribe()



        Thread.sleep(10000)
        assertEquals(4, 2 + 2)
    }

    fun save(i: Long): Completable {
        return Completable.fromAction {
            if (i == 10L) {
                throw Throwable("Error!!!!")
            }
            println("Hi $i")
        }
    }
}
