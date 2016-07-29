package com.fimtrus.loan.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jhlibrary.util.ActivityManager;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().removeActivity(this);
//		isStoped = true;
    }
}
