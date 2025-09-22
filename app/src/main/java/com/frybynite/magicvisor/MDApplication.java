package com.frybynite.magicvisor;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

/**
 * Created by Admin on 3/6/2017.
 */

public class MDApplication extends Application {

    // The one bluetooth manager to rule them all.
    public BluetoothManager bluetoothManager;
    public BluetoothAdapter bluetoothAdapter;

    public SoundPool soundPool;
    public int flatSound;
    public int rotateSound;
    public int throwSound;
    public String textToDisplay;
    public int color = 0xFF00FF00; // Make sure alpha is 0xFF
    public int brightness = 10;
    public int pulseSpeedRate = 1;

    public int scrollRate = 10;

    @Override
    public void onCreate() {
        super.onCreate();
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        AudioAttributes aa = (new AudioAttributes.Builder())
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED | AudioAttributes.FLAG_LOW_LATENCY)
                .setUsage(AudioAttributes.USAGE_GAME).build();
        soundPool = (new SoundPool.Builder())
                .setAudioAttributes(aa)
                .setMaxStreams(2)
                .build();
        flatSound = soundPool.load(this, R.raw.flat_sound, 1);
        rotateSound = soundPool.load(this, R.raw.rotate_sound, 1);
        throwSound = soundPool.load(this, R.raw.throw_sound, 1);

    }

}
