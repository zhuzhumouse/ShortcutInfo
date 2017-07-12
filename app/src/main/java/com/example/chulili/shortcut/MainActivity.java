package com.example.chulili.shortcut;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.pm.ShortcutManagerCompat;
import android.support.v4.os.BuildCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    private static final String TAG = MainActivity.class.getSimpleName();
    private Context mContext_;
    private String shortId;
    private ShortcutManager mShortcutManager = null;
    private boolean appNeedsNotifying = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext_ = this;
        shortId = "shortcut_old";

        findViewById(R.id.create_shortcut_old).setOnClickListener(this);
        findViewById(R.id.resort_shortcut).setOnClickListener(this);
        findViewById(R.id.create_shortcut_new).setOnClickListener(this);


        //25之后，提供3D TOUCH快捷方式的支持
        if(Build.VERSION.SDK_INT >= 25) {
            AddDynamicWebShortcut();
        }

    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private void AddDynamicWebShortcut(){

        mShortcutManager = mContext_.getSystemService(ShortcutManager.class);

        ShortcutInfo shortcutInfo = new  ShortcutInfo.Builder(mContext_, "shortcut_baidu")
                .setShortLabel("www.baidu.com")
                .setLongLabel("www.baidu.com web site.")
                .setIcon(Icon.createWithResource(this, R.drawable.baidu_icon))
                .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.baidu.com"))).build();

        ShortcutInfo shortcutInfo_biying = new  ShortcutInfo.Builder(mContext_, "shortcut_biying")
                .setShortLabel("www.biying.com")
                .setLongLabel("www.biying.com web site.")
                .setIcon(Icon.createWithResource(this, R.drawable.biying_icon))
                .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.biying.com"))).build();

        mShortcutManager.setDynamicShortcuts(Arrays.asList(shortcutInfo, shortcutInfo_biying));

    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private void reSort(){

        //改变颜色值
//        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().
//                getColor(android.R.color.holo_red_dark, getTheme()));
//        String label = "biying.com";
//        SpannableStringBuilder colouredLabel = new SpannableStringBuilder(label);
//        colouredLabel.setSpan(colorSpan, 0, label.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        List<ShortcutInfo> dynamicShortcuts = mShortcutManager.getDynamicShortcuts();
        List<ShortcutInfo> newOrderDynamicShortcuts = new LinkedList<>();

        int count_dym_shortcut = dynamicShortcuts.size();
        ShortcutInfo newShortcutInfo = null;

        for (ShortcutInfo info : dynamicShortcuts) {
            Log.d(TAG, "RANK: " + info.getRank());
            newShortcutInfo = new ShortcutInfo.Builder(mContext_, info.getId()).setRank(count_dym_shortcut - info.getRank() - 1).build();
            newOrderDynamicShortcuts.add(newShortcutInfo);
        }

        mShortcutManager.updateShortcuts(newOrderDynamicShortcuts);

    }

    @RequiresApi(26)
    private void createNewShotcut(){
        Log.d(TAG, "createNewShotcut");
        Log.d(TAG, "BuildCompat.isAtLeastO(): " + BuildCompat.isAtLeastO());

        if (ShortcutManagerCompat.isRequestPinShortcutSupported(mContext_)) {
            Log.d(TAG, "createNewShotcut: isRequestPinShortcutSupported");
            ShortcutInfo pinShortcutInfo = getShortcutInfo(shortId, mContext_);

            if (pinShortcutInfo == null) {
                pinShortcutInfo = new  ShortcutInfo.Builder(mContext_, "shortcut_new")
                        .setShortLabel("主应用")
                        .setLongLabel("www.biying.com web site.")
                        .setIcon(Icon.createWithResource(this, R.mipmap.ic_launcher))
                        .setIntent(new Intent(this, ThirdActivity.class).setAction("android.intent.action.callback")).build();
            }

            //mShortcutManager.requestPinShortcut(pinShortcutInfo, null);

            IntentSender intentSender = null;

            //method first
            // Create the following Intent and PendingIntent objects only if your app
            // needs to be notified that the user allowed the shortcut to be pinned.
            // Use a boolean value, such as "appNeedsNotifying", to define this behavior.
//            if (appNeedsNotifying) {
//                // We assume here the app has a manifest-declared receiver "MyReceiver".
//                Intent pinnedShortcutCallbackIntent = new Intent(mContext_, ShortcutCallbackReceiver.class);
//
//                // Configure the intent so that your app's broadcast receiver gets
//                // the callback successfully.
//                PendingIntent successCallback = PendingIntent.getBroadcast(mContext_, 0,
//                        pinnedShortcutCallbackIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                intentSender = successCallback.getIntentSender();
//            }

            //method two
            if (appNeedsNotifying) {
                // Configure the intent so that your app's broadcast receiver gets
                // the callback successfully.
                Intent pinnedShortcutCallbackIntent = mShortcutManager.createShortcutResultIntent(pinShortcutInfo);
//                pinnedShortcutCallbackIntent.setClass(mContext_, ShortcutCallbackReceiver.class);

                PendingIntent successCallback = PendingIntent.getBroadcast(mContext_, 0,
                        pinnedShortcutCallbackIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                intentSender = successCallback.getIntentSender();
            }

            mShortcutManager.requestPinShortcut(pinShortcutInfo, intentSender);

        } else {
            Toast.makeText(mContext_, "not support pin short cut!!!", Toast.LENGTH_SHORT).show();
        }

    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private boolean isExistShortcutInfo(String shortcutId, Context context){
        ShortcutManager shortcutManager = context.getSystemService(ShortcutManager.class);
        List<ShortcutInfo> shortcutInfoList = shortcutManager.getPinnedShortcuts();

        for (ShortcutInfo info : shortcutInfoList){
            if (info.getId().equals(shortcutId)) {
                return true;
            }
        }

        return false;

    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private ShortcutInfo getShortcutInfo(String shortcutId, Context context){
        ShortcutManager shortcutManager = context.getSystemService(ShortcutManager.class);
        List<ShortcutInfo> shortcutInfoList = shortcutManager.getPinnedShortcuts();

        for (ShortcutInfo info : shortcutInfoList){
            if (info.getId().equals(shortcutId)) {
                return info;
            }
        }

        return null;

    }


    public void createOldShotcut(){
        Log.d(TAG, "createOldShotcut");
        //创建一个添加快捷方式的Intent
        Intent addSC=new Intent(ACTION_ADD_SHORTCUT);
        //快捷键的标题
        String title=getResources().getString(R.string.shotcut_title);
        //快捷键的图标
        Parcelable icon=Intent.ShortcutIconResource.fromContext(
                MainActivity.this, R.mipmap.ic_launcher);
        //创建单击快捷键启动本程序的Intent
        Intent launcherIntent=new Intent(MainActivity.this, MainActivity.class);
        //设置快捷键的标题
        addSC.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        //设置快捷键的图标
        addSC.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        //设置单击此快捷键启动的程序
        addSC.putExtra(Intent.EXTRA_SHORTCUT_INTENT,launcherIntent);
        addSC.putExtra("duplicate", true);
        //向系统发送添加快捷键的广播
        sendBroadcast(addSC);

    }

    @Override
    @RequiresApi(Build.VERSION_CODES.O)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_shortcut_old:
                if (Build.VERSION.SDK_INT <= 25) {
                    createOldShotcut();
                } else {
                    Toast.makeText(mContext_, "系统版本高于25，请使用最新方式获取shortcut!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.create_shortcut_new:
                Log.d(TAG, ("Build.VERSION.SDK_INT:" + Build.VERSION.SDK_INT));
                Log.d(TAG, ("Build.VERSION.SDK_INT:" + Build.VERSION_CODES.O));
                if(Build.VERSION.SDK_INT >= 25) {
                    createNewShotcut();
                } else {
                    Toast.makeText(mContext_, "系统版本低于25，不支持pin short cut", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.resort_shortcut:
                if(Build.VERSION.SDK_INT >= 25)
                    reSort();
                break;
            default:
                break;
        }
    }

    private void testListSort(){
        List<String> nameList = new ArrayList<>();
        nameList.add("chu");
        nameList.add("zhu");
        nameList.add("zhang");
        nameList.add("gan");
        Collections.sort(nameList);
        Log.d(TAG, nameList.toString());
    }


}
