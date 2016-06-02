package com.fimtrus.loan.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fimtrus.loan.R;
import com.fimtrus.loan.fragment.ResultFragment;
import com.fimtrus.loan.model.CalculationModel;
import com.fimtrus.loan.model.RepaymentResultModel;
import com.fimtrus.loan.util.Calculator;
import com.fimtrus.loan.util.Util;
import com.fimtrus.loan.view.WrapViewPager;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by fimtrus on 16. 3. 16..
 */
public class ResultViewPagerAdapter extends FragmentPagerAdapter {

    private CalculationModel mSum;
    private ArrayList<RepaymentResultModel> mRepaymentSumList;
    private ArrayList<ArrayList<RepaymentResultModel>> mRepaymentLists;
    private ArrayList<CalculationModel> mModelList;
    private Context mContext = null;
    private ViewPager mViewPager;

    private Calculator mCalculator;

    public ResultViewPagerAdapter(Context context, FragmentManager fm, ViewPager viewPager) {

        super(fm);

        this.mCalculator = Calculator.getInstance();

        this.mContext = context;
        this.mViewPager = viewPager;
        this.mModelList = mCalculator.getModelList();
        this.mRepaymentLists = mCalculator.getRepaymentResultLists();
        this.mRepaymentSumList = mCalculator.getRepaymentSumList();
        this.mSum = mCalculator.getSum();
    }

    public ResultViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        int modelLength = mModelList.size();


        if ( modelLength > 1 && modelLength == position ) {
            return ResultFragment.newInstance(mSum, mRepaymentSumList);
        } else {
            return ResultFragment.newInstance(mModelList.get(position), mRepaymentLists.get(position));
        }
    }


    @Override
    public int getCount() {

        if ( mCalculator.getSum() == null ) {
            return mModelList.size();
        } else {
            return mModelList.size() + 1;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {

        int modelLength = mModelList.size();

        if ( modelLength == position ) {
            return "합 계";
        } else {
            return " " + ( position + 1 ) + " ";
        }


    }
}
