package com.frybynite.magicvisor;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

/**
 * Created by Admin on 3/11/2017.
 */

public class SoundService extends Service {

    protected MDApplication app;
    protected SoundWandReceiver wandReceiver;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = (MDApplication) getApplication();
        wandReceiver = new SoundWandReceiver();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAllSounds();
        WandActions.unregisterReceiver(getApplicationContext(), wandReceiver);
    }

    private void stopAllSounds() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId ) {
        super.onStartCommand(intent, flags, startId);
        WandActions.registerReceiver(getApplicationContext(), wandReceiver);

        return Service.START_STICKY;
    }


    public class SoundWandReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null) {
                switch (intent.getAction()) {
                    case WandActions.ACTION_CONNECTED:
                        break;
                    case WandActions.ACTION_DISCONNECT:
                        break;
                    case WandActions.ACTION_MOVE_DOWN:
                        break;
                    case WandActions.ACTION_MOVE_FLAT:
                        //play(app.flatSound);
                        break;
                    case WandActions.ACTION_MOVE_ROTATE:
                        play(app.rotateSound);
                        break;
                    case WandActions.ACTION_MOVE_THROW:
                        play(app.throwSound);
                        break;
                }
            }
        }
    }

    int lastStream = -1;
    protected void play(int soundId) {
        if (lastStream != -1) {
            app.soundPool.stop(lastStream);
            lastStream = -1;
        }
        lastStream = app.soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}
