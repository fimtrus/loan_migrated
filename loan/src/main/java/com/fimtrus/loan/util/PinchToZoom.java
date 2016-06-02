package com.fimtrus.loan.util;

import android.content.Context;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by fimtrus on 16. 4. 22..
 */
public class PinchToZoom extends ScaleGestureDetector.SimpleOnScaleGestureListener
    implements View.OnTouchListener {

    private float mScale;
    private Matrix mMatrix;

    private Context mContext;
    private View mZoomView;
    private ScaleGestureDetector mScaleGestureDetector;

    private int mZoomViewWidth = 0;
    private int mZoomViewHeight = 0;

    public static final PinchToZoom newInstance(Context context, View v ) {

        PinchToZoom listener = new PinchToZoom();

        listener.mContext = context;
        listener.mZoomView = v;
        listener.mMatrix = new Matrix();
        return listener;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        mScale = mZoomView.getScaleX();
        mScale *= detector.getScaleFactor();
        mScale = Math.max(0.1f, Math.min(mScale, 1.0f));

        mMatrix.setScale(mScale, mScale);

        mZoomView.setScaleX(mScale);
        mZoomView.setScaleY(mScale);

        if ( mZoomViewWidth == 0 ) {
            int width = mZoomView.getWidth();
            int height = mZoomView.getHeight();

            mZoomViewWidth = width;
            mZoomViewHeight = height;
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mZoomView.getLayoutParams();

        params.width = (int) (mZoomViewWidth * mScale);
        params.height = (int) (mZoomViewHeight * mScale);

        mZoomView.setLayoutParams(params);

        return true;
//        return super.onScale(detector);
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return super.onScaleBegin(detector);
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        super.onScaleEnd(detector);
    }


    public void attach(ScaleGestureDetector detector) {

        mScaleGestureDetector = detector;
//        mZoomView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        if ( event.getPointerCount() > 1 ) {
            mScaleGestureDetector.onTouchEvent(event);
            return true;
        } else {
            mScaleGestureDetector.onTouchEvent(event);
            return false;
        }
    }
}
