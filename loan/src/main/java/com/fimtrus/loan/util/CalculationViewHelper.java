package com.fimtrus.loan.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.fimtrus.loan.R;
import com.fimtrus.loan.adapter.CalculationViewPagerAdapter;

import java.util.ArrayList;

/**
 * Created by fimtrus on 16. 3. 16..
 * @auther Jonghyun.jeong
 *
 * Calculation View를 편하게 관리할 수 있도록 해주는 Class
 * This Class support CalculationView to make easy.
 */
public class CalculationViewHelper {

    private static CalculationViewHelper mCalculationViewHelper;

    private final int CALCULATION_VIEW = R.layout.view_loan_search;

    private Context mContext;
    private LayoutInflater mInflater;
    private ViewPager mViewPager;
    private CalculationViewPagerAdapter mPagerAdapter;
    private ArrayList<View> mViewList;

    public static final CalculationViewHelper newInstance(Context context, ViewPager viewPager) {

        if ( mCalculationViewHelper == null ) {
            mCalculationViewHelper = new CalculationViewHelper();
            mCalculationViewHelper.mContext = context;
            mCalculationViewHelper.mInflater = LayoutInflater.from(context);
            mCalculationViewHelper.mViewList = new ArrayList<View>();
            mCalculationViewHelper.mViewPager = viewPager;
//            mCalculationViewHelper.mPagerAdapter = new CalculationViewPagerAdapter(context, mCalculationViewHelper.mViewList);
        }

        return mCalculationViewHelper;
    }

    private CalculationViewHelper() {}


    public View newCalculationView() {

        View view = mInflater.inflate(CALCULATION_VIEW, null);

        return view;

    }

    /**
     * 화면을 세팅한다<br/>
     * 최초 한번만 부르면 된다<br/>
     */
    public void submit() {

        if ( mViewList.size() == 0 ) {
            this.add();
            this.add();
        }

        mPagerAdapter = new CalculationViewPagerAdapter(mContext, mViewList);
    }

    public void add() {
        mViewList.add(newCalculationView());
    }

    public void remove (int index) {
        mViewList.remove(index);
    }

    public void clear() {
        mViewList.clear();
    }
}
