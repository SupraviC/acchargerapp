package com.rxgradle.wifi.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.rxgradle.wifi.base.BaseActivity;

/**
 * Created by LRXx on 2017/12/1.
 */

public class FactoryLogicManager {

    //第二代主板(5.1)
    public static final String TAG_SECOND_MAINBOARD = "haier_310";
    //第三代主板(4.4)
    public static final String TAG_THIRD_MAINBOARD = "haier_310_mtk1";

    private long startTime;//开始时间
    private long endTime;//结束时间
    private Activity activity;

    private FactoryLogicManager(Activity activity, View goto_launcher_view,
                                View goto_factory_test, View goto_serial_factory_test,
                                View nearWifi) {
        this.activity = activity;
        goto_launcher_view.setOnTouchListener(launcherTouch);
        goto_factory_test.setOnTouchListener(factoryTouch);
        goto_serial_factory_test.setOnTouchListener(serialFactoryTouch);
        nearWifi.setOnTouchListener(nearWifiTouch);
    }

    public static FactoryLogicManager init(Activity activity, View goto_launcher_view,
                                           View goto_factory_test, View goto_serial_factory_test,
                                           View nearWifi) {
        return new FactoryLogicManager(activity, goto_launcher_view, goto_factory_test, goto_serial_factory_test, nearWifi);
    }

    View.OnTouchListener launcherTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    startTime = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_UP:
                    long endTime = System.currentTimeMillis();
                    //间隔超过2秒钟
                    if (endTime - startTime > 2000) {
                        gotoLauncher();
                    }
                    break;
                default:
                    break;
            }

            return true;
        }
    };

    View.OnTouchListener factoryTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                startTime = System.currentTimeMillis();
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                endTime = System.currentTimeMillis();
                if ((endTime - startTime) > 2000) {//大于3秒进入工厂测试应用
                    if (Build.DEVICE.equals(TAG_SECOND_MAINBOARD)) {
                        try {
                            Intent mIntent = new Intent();
                            mIntent.setAction("android.intent.action.DEVICETEST");
                            activity.startActivity(mIntent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (Build.DEVICE.equals(TAG_THIRD_MAINBOARD)) {
                        Intent intent = new Intent();
                        intent.setAction("android.provider.Telephony.SECRET_CODE");
                        intent.setData(Uri.parse("android_secret_code://66"));
                        activity.sendBroadcast(intent);
                    } else {
                        Intent intent = activity.getPackageManager().getLaunchIntentForPackage("com.ych.allen.firetest");
                        if (intent != null) {
                            activity.startActivity(intent);
                        } else {
                            Log.d("", "该功能未开放，敬请期待");
                        }
                    }
                }
            }
            return true;
        }
    };

    View.OnTouchListener serialFactoryTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                startTime = System.currentTimeMillis();
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                endTime = System.currentTimeMillis();
                if ((endTime - startTime) > 2000) {//大于2秒进入串口工厂测试
                    activity.startActivity(new Intent("compatibility.SerialPortFactoryActivity"));
                }
            }
            return true;
        }
    };


    View.OnTouchListener nearWifiTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                startTime = System.currentTimeMillis();
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                endTime = System.currentTimeMillis();
                if ((endTime - startTime) > 2000) {//大于3秒进入工厂测试应用
                    //进入RFID工厂测试页面
                    activity.startActivity(new Intent("compatibility.RFIDFactoryActivity"));
                }
            }
            return true;
        }
    };

    /**
     * 跳转至系统Launcher
     */
    private void gotoLauncher() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setComponent(new ComponentName("com.android.launcher3", "com.android.launcher3.Launcher"));
        activity.startActivity(intent);
    }

}
