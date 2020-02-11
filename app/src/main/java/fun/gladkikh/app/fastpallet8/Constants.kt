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

    const val OPEN_DOC_CREATE_PALLET_FORM = 1
    const val OPEN_PRODUCT_CREATE_PALLET_FORM = 2
    /**
     * Редактируем продукт шаблон
     */
    const val OPEN_PRODUCT_CREATE_PALLET_DIALOG_FORM = 3
    const val OPEN_PALLET_CREATE_PALLET_FORM = 4
    const val OPEN_BOX_CREATE_PALLET_FORM = 4

    const val OPEN_DOC_ACTION_FORM = 5
    const val OPEN_PRODUCT_ACTION_FORM = 6
    /**
     * Редактируем продукт шаблон
     */
    const val OPEN_PRODUCT_ACTION_DIALOG_FORM = 7
    const val OPEN_PALLET_ACTION_FORM = 8
    const val OPEN_BOX_ACTION_FORM = 9

    const val OPEN_DOC_INVENTORY_PALLET_FORM = 10
    const val OPEN_PRODUCT_INVENTORY_PALLET_DIALOG_FORM = 11
    const val OPEN_BOX_INVENTORY_PALLET_FORM = 12

    const val OPEN_INFO_PALLET = 13



    //Вызов диалога удаления
    const val CONFIRM_DELETE_DIALOG = 1

    //Вызов диалога ввода мест
    const val EDIT_PLACE_DIALOG = 2

    //Вызов диалога ввода количества
    const val EDIT_COUNT_DIALOG = 3

    //Вызов диалога добавления
    const val ADD_COUNT_DIALOG = 4

    //Свернуть часть формы
    const val COMMAND_HIDE_FORM = 5

    const val EDIT_START_DIALOG = 6
    const val EDIT_END_DIALOG = 7
    const val EDIT_COFF_DIALOG = 8

    const val COMMAND_START_MENU = 9

    const val CONFIRM_SEND_DIALOG = 10


    const val IS_TEST_BUILD = true
}

