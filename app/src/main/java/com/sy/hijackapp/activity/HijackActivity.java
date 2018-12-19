package com.sy.hijackapp.activity;

import android.app.Activity;
import android.os.Bundle;
import com.sy.hijackapp.R;

/**
 * @program: HijackAPP
 * @description: 劫持Activity
 * @author: yao.song
 * @create: 2018-12-18 14:01
 **/
public class HijackActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hijack);
    }
}
