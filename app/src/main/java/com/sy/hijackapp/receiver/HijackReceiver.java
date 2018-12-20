package com.sy.hijackapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.sy.hijackapp.service.HijackService;

/**
 * @program: HijackAPP
 * @description: 在开机时得到开机启动的广播，用于启动劫持服务
 * @author: yao.song
 * @create: 2018-12-18 11:43
 **/
public class HijackReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.w(HijackService.TAG, "启动劫持服务：开机启动");
            Intent intent2 = new Intent(context, HijackService.class);
            context.startService(intent2);
            Log.w(HijackService.TAG, "启动劫持服务成功！");
        }
    }
}
