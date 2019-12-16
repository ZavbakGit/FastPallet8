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


        Flowable.fromIterable((0..100))
            .doOnNext {

                Flowable.just(it)
                    .map {
                        if (it == 10) {
                            throw Throwable("Eroror")
                        } else {
                            it
                        }
                    }
                    .subscribe({
                        println(it)
                    }, {
                        println(it)
                    })

            }
            .subscribe()


        assertEquals(4, 2 + 2)
    }


}
