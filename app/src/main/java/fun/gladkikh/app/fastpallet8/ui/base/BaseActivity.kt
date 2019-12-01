package `fun`.gladkikh.app.fastpallet8.ui.base

import `fun`.gladkikh.app.fastpallet8.R
import `fun`.gladkikh.app.fastpallet8.ui.common.SingleLiveEvent

import android.os.Bundle
import android.view.KeyEvent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

abstract class BaseActivity : AppCompatActivity() {
    abstract val layoutRes: Int

    val isShowProgress = MutableLiveData<Boolean>()
    val barcodeLiveData = SingleLiveEvent<String>()

    lateinit var fadeInAnim: Animation

    private val keyDownMutableLiveData =
        SingleLiveEvent<Int>()

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (isShowProgress.value != true) {
            keyDownMutableLiveData.postValue(keyCode)
        }
        return super.onKeyDown(keyCode, event)
    }

    fun getKeyDownLiveData(): LiveData<Int> = keyDownMutableLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)

        fadeInAnim = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.fade_in
        )
    }
}