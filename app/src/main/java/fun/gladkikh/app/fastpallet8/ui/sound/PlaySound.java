package fun.gladkikh.app.fastpallet8.ui.sound;

import android.media.AudioManager;
import android.media.ToneGenerator;

/**
 * Created by Alex on 29.01.2018.
 */

public class PlaySound {

    public static void playError(){
        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
        toneG.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT , 800);
    }
}
