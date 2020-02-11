package `fun`.gladkikh.app.fastpallet8



import `fun`.gladkikh.app.fastpallet8.di.DependencyModule
import android.app.Application
import android.content.Context
import android.os.Environment
import com.bosphere.filelogger.FL
import com.bosphere.filelogger.FLConfig
import com.bosphere.filelogger.FLConst
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.io.File


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

        FL.init(
            FLConfig.Builder(this)
                .minLevel(FLConst.Level.V)
                .logToFile(true)
                .dir(File(Environment.getExternalStorageDirectory(), "file_logger_demo"))
                .retentionPolicy(FLConst.RetentionPolicy.FILE_COUNT)
                .build()
        )
        FL.setEnabled(true)



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