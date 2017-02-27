package com.fimtrus.loan.util;

import android.content.Context;

import com.jhlibrary.util.ActivityManager;

/**
 * Created by fimtrus on 16. 6. 2..
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Context mContext;

    public ExceptionHandler ( Context context ) {
        this.mContext = context;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        clear();
        //System.exit(0);
        ActivityManager.getInstance().finishAllActivity();

    }

    private void clear() {
    }
}
