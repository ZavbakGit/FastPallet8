package `fun`.gladkikh.app.fastpallet8.ui.activity

import `fun`.gladkikh.app.fastpallet8.R
import `fun`.gladkikh.app.fastpallet8.repository.setting.SettingsRepository
import `fun`.gladkikh.app.fastpallet8.ui.base.BaseActivity
import `fun`.gladkikh.app.fastpallet8.ui.navigate.NavigateHandler
import `fun`.gladkikh.app.fastpallet8.ui.sound.SoundVibro

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bosphere.filelogger.FL
import com.gladkikh.mylibrary.BarcodeHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.progress_overlay.*
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity() {
    override val layoutRes = R.layout.activity_main

    private var barcodeHelper: BarcodeHelper? = null
    private val settingsRepository: SettingsRepository by inject()

    private val barcodeObserver = Observer<String> {
        if (isShowProgress.value != true) {
            barcodeLiveData.postValue(it)
        }
    }

    lateinit var soundVibro: SoundVibro


    lateinit var navigateHandler: NavigateHandler
        private set


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigateHandler =
            NavigateHandler(Navigation.findNavController(this, R.id.nav_host_fragment))

        navigateHandler.navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.documentListFragment) {
                refreshSettingApp()
            }
        }

        soundVibro = SoundVibro(this)

        refreshSettingApp()
    }

    fun refreshSettingApp() {
        settingsRepository.refresh()
        //Toast.makeText(this,settingsRepository.getSetting().code,Toast.LENGTH_SHORT).show()

        barcodeHelper?.getBarcodeLiveData()?.removeObserver(barcodeObserver)

        barcodeHelper = BarcodeHelper(
            this,
            BarcodeHelper.TYPE_TSD.getTypeTSD(settingsRepository.settingApp!!.typeTsd)
        )
        barcodeHelper?.getBarcodeLiveData()?.observe(this, barcodeObserver)
    }

    fun showMessage(text: CharSequence?) {
        Snackbar.make(root, text ?: "", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
        Log.d("anit", "showMessage $text")
        FL.d("anit","showMessage $text")
    }

    fun showErrorMessage(text: CharSequence?) {
        Snackbar.make(root, text ?: "", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()

        Log.e("anit", "showErrorMessage $text")
        FL.e("anit","showErrorMessage $text")

        soundVibro.playError()
    }

    fun showProgress() {
        progressView.visibility = View.VISIBLE
        isShowProgress.postValue(true)
    }

    fun hideProgress() {
        progressView.visibility = View.GONE
        isShowProgress.postValue(false)
    }
}