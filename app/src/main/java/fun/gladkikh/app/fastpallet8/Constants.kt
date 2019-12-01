package `fun`.gladkikh.app.fastpallet8

import android.annotation.SuppressLint
import android.provider.Settings

@SuppressLint("HardwareIds")
object Constants {
    //const val BASE_URL = "http://172.31.255.168/rmmt/hs/api/"
    //const val BASE_URL = "http://172.31.255.150/rmmt/hs/api/"

    const val APP_VERSION = BuildConfig.VERSION_NAME
    val UID by lazy {
        App.appContext()?.let {
            Settings.Secure.getString(it.contentResolver, Settings.Secure.ANDROID_ID)
        }
    }
    val OS_VERSION by lazy { android.os.Build.VERSION.SDK_INT.toString() }

    val EXTRA_WRAP_GUID = "extra.GUID"

    val KEY_5 = 12 // 5
    val KEY_9 = 16 // 9
    val KEY_4 = 11 // 4
    val KEY_1 = 8 // 1
    val KEY_2 = 9 // 1
    val KEY_3 = 10 // 1
    val KEY_7 = 7 // 0
}

