package com.example.chulili.shortcut;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by chulili on 2017/4/14.
 */

public class CreateShortcutActivity extends Activity {
    public static final String TAG = CreateShortcutActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_shortcut_layout);
        Log.d(TAG, "onCreate: action: " + getIntent().getAction());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
