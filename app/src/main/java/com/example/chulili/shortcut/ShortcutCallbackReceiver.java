package com.example.chulili.shortcut;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by chulili on 2017/7/12.
 */

public class ShortcutCallbackReceiver extends BroadcastReceiver{
    public static final String TAG = ShortcutCallbackReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: " + intent.getAction());
    }
}
