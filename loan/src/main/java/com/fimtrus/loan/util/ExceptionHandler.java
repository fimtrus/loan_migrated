package com.fimtrus.loan.util;

import android.content.Context;

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
        System.exit(0);

    }

    private void clear() {
        CalculationViewHelper.clearField();
    }
}
