package com.frybynite.magicvisor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * Created by Admin on 3/8/2017.
 */

public class WandActions {
    public static final String ACTION_CONNECTED   = "com.frybynite.magicvisor.wand.CONNECT";
    public static final String ACTION_DISCONNECT  = "com.frybynite.magicvisor.wand.DISCONNECT";
    public static final String ACTION_MOVE_DOWN   = "com.frybynite.magicvisor.wand.MOVE_DOWN";
    public static final String ACTION_MOVE_FLAT   = "com.frybynite.magicvisor.wand.MOVE_FLAT";
    public static final String ACTION_MOVE_ROTATE = "com.frybynite.magicvisor.wand.MOVE_ROTATE";
    public static final String ACTION_MOVE_THROW  = "com.frybynite.magicvisor.wand.MOVE_THROW";

    public static final String ACTION_HEARTS = "com.frybynite.magicvisor.action.HEARTS";
    public static final String ACTION_DARK = "com.frybynite.magicvisor.action.DARK";
    public static final String ACTION_RAINBOW = "com.frybynite.magicvisor.action.RAINBOW";
    public static final String ACTION_SCROLL_TEXT = "com.frybynite.magicvisor.action.SCROLL_TEXT";
    public static final String ACTION_POP_TEXT = "com.frybynite.magicvisor.action.POP_TEXT";
    public static final String ACTION_CHANGE_COLOR = "com.frybynite.magicvisor.action.CHANGE_COLOR";
    public static final String ACTION_SET_BRIGHTNESS = "com.frybynite.magicvisor.action.SET_BRIGHTNESS";
    public static final String ACTION_PULSE = "com.frybynite.magicvisor.action.PULSE";
    public static final String ACTION_SET_PULSE_SPEED = "com.frybynite.magicvisor.action.SET_PULSE_SPEED";
    public static final String ACTION_SET_SCROLL_RATE = "com.frybynite.magicvisor.action.SET_SCROLL_RATE";



    public static final String ACTION_SHIMMER = "com.frybynite.magicvisor.action.SHIMMER";
    public static final String ACTION_TWINKLE = "com.frybynite.magicvisor.action.TWINKLE";
    public static final String ACTION_LIGHTNING = "com.frybynite.magicvisor.action.LIGHTNING";
    public static final String ACTION_WHITE_SLIDE = "com.frybynite.magicvisor.action.WHITE_SLIDE";
    public static final String ACTION_TWINKLE_LIGHT = "com.frybynite.magicvisor.action.TWINKLE_LIGHT";
    public static final String ACTION_MAIZE_AND_BLUE = "com.frybynite.magicvisor.action.MAIZE_AND_BLUE";
    public static final String ACTION_PULSE_RIGHT = "com.frybynite.magicvisor.action.PULSE_RIGHT";
    public static final String ACTION_PULSE_LEFT = "com.frybynite.magicvisor.action.PULSE_LEFT";
    public static final String ACTION_MOSTLY_WHITE= "com.frybynite.magicvisor.action.MOSTLY_WHITE";


    /**
     * Registers a broadcast wandReceiver for all actions.
     * @param context
     * @param receiver
     */
    public static void registerReceiver(Context context, BroadcastReceiver receiver) {
        LocalBroadcastManager mgr = LocalBroadcastManager.getInstance(context);
        IntentFilter filter = new IntentFilter(ACTION_CONNECTED);
        filter.addAction(ACTION_DISCONNECT);
        filter.addAction(ACTION_MOVE_DOWN);
        filter.addAction(ACTION_MOVE_FLAT);
        filter.addAction(ACTION_MOVE_ROTATE);
        filter.addAction(ACTION_MOVE_THROW);

        filter.addAction(ACTION_HEARTS);
        filter.addAction(ACTION_RAINBOW);
        filter.addAction(ACTION_DARK);
        filter.addAction(ACTION_SCROLL_TEXT);
        filter.addAction(ACTION_POP_TEXT);
        filter.addAction(ACTION_CHANGE_COLOR);
        filter.addAction(ACTION_SET_BRIGHTNESS);
        filter.addAction(ACTION_SET_PULSE_SPEED);
        filter.addAction(ACTION_SET_SCROLL_RATE);
        filter.addAction(ACTION_PULSE);


        // These will be unused.
        /*
        filter.addAction(ACTION_SHIMMER);
        filter.addAction(ACTION_TWINKLE);
        filter.addAction(ACTION_LIGHTNING);
        filter.addAction(ACTION_RAINBOW_NO_PULSE);
        filter.addAction(ACTION_WHITE_SLIDE);
        filter.addAction(ACTION_TWINKLE_LIGHT);
        filter.addAction(ACTION_MAIZE_AND_BLUE);
        filter.addAction(ACTION_PULSE_LEFT);
        filter.addAction(ACTION_PULSE_RIGHT);
        filter.addAction(ACTION_MOSTLY_WHITE);
        */

        mgr.registerReceiver(receiver, filter);
    }

    public static void unregisterReceiver(Context context, BroadcastReceiver receiver) {
        LocalBroadcastManager mgr = LocalBroadcastManager.getInstance(context);
        mgr.unregisterReceiver(receiver);
    }


}
