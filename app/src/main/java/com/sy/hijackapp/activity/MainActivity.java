package com.sy.hijackapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.sy.hijackapp.HijackApplication;
import com.sy.hijackapp.R;
import com.sy.hijackapp.service.HijackService;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button btn_hijack;

    private boolean serviceHasStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_hijack = findViewById(R.id.btn_hijack);
        btn_hijack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_hijack) {
            if (serviceHasStarted) {

                Log.w(HijackApplication.TAG, "停止劫持服务：Activity停止");
                Intent intent = new Intent(this, HijackService.class);
                stopService(intent);//停止劫持服务
                Log.w(HijackApplication.TAG, "停止劫持服务成功！");

                btn_hijack.setText("启动劫持服务");
                serviceHasStarted = !serviceHasStarted;
            } else {

                Log.w(HijackApplication.TAG, "启动劫持服务：Activity启动");
                Intent intent = new Intent(this, HijackService.class);
                startService(intent);//启动劫持服务
                Log.w(HijackApplication.TAG, "启动劫持服务成功！");

                btn_hijack.setText("停止劫持服务");
                serviceHasStarted = !serviceHasStarted;
            }
        }
    }
}
