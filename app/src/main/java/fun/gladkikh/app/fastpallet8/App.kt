package `fun`.gladkikh.app.fastpallet8



import `fun`.gladkikh.app.fastpallet8.di.DependencyModule
import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class App : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: App? = null
        fun appContext(): Context? = instance?.applicationContext
    }

    override fun onCreate() {
        super.onCreate()



        startKoin {
            androidLogger()
            // Android context
            androidContext(this@App)
            // modules
            modules(DependencyModule.appModule)
        }
        println("App -> Constants.UID: ${Constants.UID}; Platform: Android; APP Version: ${Constants.APP_VERSION}; OS Version: ${Constants.OS_VERSION}")
    }
}