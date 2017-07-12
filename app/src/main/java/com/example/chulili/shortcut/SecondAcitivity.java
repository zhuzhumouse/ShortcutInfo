package com.example.chulili.shortcut;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by chulili on 2017/4/14.
 */

public class SecondAcitivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
