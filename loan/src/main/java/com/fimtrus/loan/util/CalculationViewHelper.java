package com.fimtrus.loan.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.fimtrus.loan.R;
import com.fimtrus.loan.adapter.CalculationViewPagerAdapter;
import com.fimtrus.loan.model.CalculationModel;
import com.fimtrus.loan.view.WrapViewPager;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by fimtrus on 16. 3. 16..
 * @auther Jonghyun.jeong
 *
 * Calculation View를 편하게 관리할 수 있도록 해주는 Class
 * This Class support CalculationView to make easy.
 */
public class CalculationViewHelper implements View.OnClickListener {

    private static CalculationViewHelper mCalculationViewHelper;



    private Context mContext;
    private LayoutInflater mInflater;
    private WrapViewPager mViewPager;
    private CalculationViewPagerAdapter mPagerAdapter;
    private ArrayList<CalculationModel> mModelList;
    private CircleIndicator mCircleIndicator;

    public static final CalculationViewHelper newInstance(Context context, WrapViewPager viewPager, CircleIndicator circleIndicator) {

        if ( mCalculationViewHelper == null ) {
            mCalculationViewHelper = new CalculationViewHelper();
            mCalculationViewHelper.mContext = context;
            mCalculationViewHelper.mInflater = LayoutInflater.from(context);
            mCalculationViewHelper.mModelList = new ArrayList<CalculationModel>();
            mCalculationViewHelper.mViewPager = viewPager;
            mCalculationViewHelper.mCircleIndicator = circleIndicator;

//            mCalculationViewHelper.mCalculationList = new ArrayList<CalculationModel>();
        }

        return mCalculationViewHelper;
    }

    private CalculationViewHelper() {}


    public static final CalculationViewHelper getInstance() {

        if ( mCalculationViewHelper == null ) {
            mCalculationViewHelper = new CalculationViewHelper();
        }

        return mCalculationViewHelper;
    }


    /**
     * 새로운 화면을 만든다. add method 를 통해 화면에 추가된다.
     * @return view : 추가할 화면
     */
//    public View newCalculationView() {
//
//        View view = mInflater.inflate(CALCULATION_VIEW, null);
//        ImageButton removeButton = (ImageButton) view.findViewById(R.id.button_remove);
//        CalculationModel calculationModel = CalculationModel.newInstance();
//        view.setTag(calculationModel);
//
//        removeButton.setOnClickListener(this);
//
//        return view;
//
//    }

    /**
     * 화면을 세팅한다<br/>
     * 최초 한번만 부르면 된다<br/>
     */
    public void submit() {

        if ( mModelList.size() == 0 ) {

            mModelList.add( CalculationModel.newInstance() );
//            mModelList.add(newCalculationView());
//            mModelList.add(newCalculationView());
//            mModelList.add(newCalculationView());
//            mModelList.add(newCalculationView());
//            this.add();
        }

        if ( mPagerAdapter == null ) {

            setPagerOptions();
            mPagerAdapter = new CalculationViewPagerAdapter(mContext, mModelList);
            mViewPager.setAdapter(mPagerAdapter);
            mCircleIndicator.setViewPager(mViewPager);
            mViewPager.setOverScrollMode(ViewPager.OVER_SCROLL_NEVER);

        }

    }

    private void setPagerOptions() {
//        PagerTabStrip pagerTabStrip = (PagerTabStrip) mViewPager.findViewById(R.id.tabstrip_calculation);
//        pagerTabStrip.setGravity(android.view.Gravity.LEFT);

    }

    /**
     * 화면을 추가한다.<br/>
     * 실제로는 mModelList 내에 화면을 추가하면 페이저가 알아서 화면을 만들어줌
     */
    public void add() {

        int count = mModelList.size();

        if ( count < 5 ) {
            mModelList.add(CalculationModel.newInstance());
//            mViewPager.setCurrentItem( count );
            if ( mPagerAdapter != null ) {
                mPagerAdapter.notifyDataSetChanged();
            }
            mViewPager.setCurrentItem( count, true);
        } else {
            Toast.makeText(mContext, "undefined", Toast.LENGTH_SHORT);
        }

//        if ( mModelList.size() < 5 ) {

//            mModelList.add(newCalculationView());
//
//            if ( mPagerAdapter != null ) {
//                mPagerAdapter.notifyDataSetChanged();
////                setIndex(mModelList.size() - 1);
//            }
//        } else {
//            Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
//        }

//        mCalculationList.add(CalculationModel.newInstance());


    }

    /**
     * 화면을 삭제한다.
     * 화면 삭제시 태그 정보를 다시 세팅해 준다.
     * @param index : 삭제할 화면의 index;
     */
    public void remove (int index) {



//        View v = null;
//
//        if ( mModelList.size() > 1  && mModelList.size() < 6 ) {
//
//            v = mModelList.get(index);
//
//            remove(v);
//
//        } else {
//            Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
//        }

    }
    /**
     * 화면을 삭제한다.
     * 화면 삭제시 태그 정보를 다시 세팅해 준다.
     * @param v : 삭제할 화면의 View ;
     */
    public void remove (View v) {

//        if ( mModelList.size() > 1 ) {
//
//            v.setTag(null);
//
//            mModelList.remove(v);
//            mViewPager.clearDisappearingChildren();
//            if ( mPagerAdapter != null ) {
//                mPagerAdapter.notifyDataSetChanged();
//            }
//        } else {
//            Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
//        }

    }

    public void remove() {
        int count = mModelList.size();


        if ( count > 1 ) {
            mModelList.remove(count - 1);

            if ( mPagerAdapter != null ) {
                mPagerAdapter.notifyDataSetChanged();
            }
            mViewPager.setCurrentItem( count - 2, true );

            mViewPager.refreshDrawableState();
        } else {
            Toast.makeText(mContext, "undefined", Toast.LENGTH_SHORT);
        }
    }

    public void setIndex(int index) {

        mViewPager.setCurrentItem(index, true);

    }

    /**
     * 화면을 클리어하고 새로 세팅한다.
     *
     */
    public void clear() {

//        for ( View c : mModelList ) {
//            c.setTag(null);
//        }

        mModelList.clear();
//        mCalculationList.clear();


        this.submit();

        if ( mPagerAdapter != null ) {
            mPagerAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 계산하기.
     *
     */
    public void calculate() {
        setCalculationData();
        callResultFragment();
    }

    /**
     * 계산 결과 화면 호출.
     */
    private void callResultFragment() {

    }

    /**
     * 화면을 통해 입력 받은 값을 모델에 세팅.
     * calculate 함수에서 호출함.
     */
    private void setCalculationData() {

        View v = null;
        CalculationModel c;
        Spinner repaymentSpinner;
        EditText loansEditText;
        EditText interestRateEditText;
        EditText termEditText;

        String log = "";

        for ( int i = 0; i < mModelList.size(); i++ ) {
            v = mViewPager.getChildAt(i);
            c = mModelList.get(i);
//            c = mCalculationList.get(i);

            repaymentSpinner = (Spinner) v.findViewById(R.id.spinner_repayment);
            loansEditText = (EditText) v.findViewById(R.id.edittext_loans);
            interestRateEditText = (EditText) v.findViewById(R.id.edittext_interest_rate);
            termEditText = (EditText) v.findViewById(R.id.edittext_term);

            c.setSelectRepayment(repaymentSpinner.getSelectedItemPosition());
            c.setLoansText(loansEditText.getText().toString());
            c.setInterestRateText(interestRateEditText.getText().toString());
            c.setTermText(termEditText.getText().toString());

            log +=  "index " + i
                    + " / " + "repayment : " + c.getSelectRepayment()
                    + " / " + "Loans : " + c.getLoansText()
                    + " / " + "InterestRate : " + c.getInterestRateText()
                    + " / " + "Term : " + c.getTermText();
        }
        Log.d("LOAN", log);
    }

    @Override
    public void onClick(View v) {
//        View parentView = (View) v.getParent();
        remove();
    }
}
