package com.example.chulili.shortcut;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by chulili on 2017/4/14.
 */

public class CallbackActivity extends Activity {
    public static final String TAG = CallbackActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
