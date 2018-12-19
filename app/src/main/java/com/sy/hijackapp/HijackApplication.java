package com.sy.hijackapp;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: HijackAPP
 * @description: 保存已经劫持过的包名，防止我们多次劫持增加暴露风险。同时也提供上下文用以启动Intent
 * @author: yao.song
 * @create: 2018-12-18 13:45
 **/
public class HijackApplication extends Application {

    public static String TAG = "HijackAPP";

    private List<String> hijackAppList = new ArrayList();

    public void addHijackedApp(String paramString) {
        hijackAppList.add(paramString);
    }

    public void clearHijackedApp() {
        hijackAppList.clear();
    }

    public boolean hasAppBeHijacked(String paramString) {
        return hijackAppList.contains(paramString);
    }
}
