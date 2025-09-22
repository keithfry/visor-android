package com.frybynite.magicvisor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * Created by Admin on 3/8/2017.
 */

public class DressActions {
    public static final String ACTION_CONNECTED   = "com.frybynite.magicvisor.visor.CONNECT";
    public static final String ACTION_DISCONNECT  = "com.frybynite.magicvisor.visor.DISCONNECT";
    public static final String ACTION_SCANNING  = "com.frybynite.magicvisor.visor.SCANNING";
    public static final String ACTION_MESSAGE = "com.frybynite.magicvisor.visor.MESSAGE";
    /**
     * Registers a broadcast wandReceiver for all actions.
     * @param context
     * @param receiver
     */
    public static void registerReceiver(Context context, BroadcastReceiver receiver) {
        LocalBroadcastManager mgr = LocalBroadcastManager.getInstance(context);
        IntentFilter filter = new IntentFilter(ACTION_CONNECTED);
        filter.addAction(ACTION_DISCONNECT);
        filter.addAction(ACTION_SCANNING);
        filter.addAction(ACTION_MESSAGE);
        mgr.registerReceiver(receiver, filter);
    }

    public static void unregisterReceiver(Context context, BroadcastReceiver receiver) {
        LocalBroadcastManager mgr = LocalBroadcastManager.getInstance(context);
        mgr.unregisterReceiver(receiver);
    }


}
