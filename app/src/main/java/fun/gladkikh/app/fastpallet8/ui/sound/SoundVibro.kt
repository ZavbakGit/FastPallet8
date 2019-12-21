package `fun`.gladkikh.app.fastpallet8.ui.sound

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Vibrator

class SoundVibro(context:Context) {

    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    private val  toneG =  ToneGenerator(AudioManager.STREAM_ALARM, 100)


    fun playError(){
        toneG.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT , 800)
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(500)
        }
    }
}