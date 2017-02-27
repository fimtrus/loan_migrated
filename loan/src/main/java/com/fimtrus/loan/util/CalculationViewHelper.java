package com.fimtrus.loan.util;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.fimtrus.loan.AnalyticsTrackers;
import com.fimtrus.loan.CommonApplication;
import com.fimtrus.loan.R;
import com.fimtrus.loan.activity.ResultActivity;
import com.fimtrus.loan.adapter.CalculationViewPagerAdapter;
import com.fimtrus.loan.model.CalculationModel;
import com.fimtrus.loan.model.Constant;
import com.fimtrus.loan.view.WrapViewPager;

import java.util.ArrayList;

import javax.xml.transform.Result;

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
    private Button mCalculationButton;

    private Calculator mCalculator;

    public static final CalculationViewHelper newInstance(Context context, WrapViewPager viewPager, CircleIndicator circleIndicator, Button calculationButton) {

//        if ( mCalculationViewHelper == null ) {
            mCalculationViewHelper = new CalculationViewHelper();
            mCalculationViewHelper.mContext = context;
            mCalculationViewHelper.mInflater = LayoutInflater.from(context);
            mCalculationViewHelper.mModelList = new ArrayList<CalculationModel>();
            mCalculationViewHelper.mViewPager = viewPager;
            mCalculationViewHelper.mCircleIndicator = circleIndicator;
            mCalculationViewHelper.mCalculationButton = calculationButton;
            mCalculationViewHelper.mCalculator = Calculator.newInstance( context, mCalculationViewHelper.mModelList );

//            mCalculationViewHelper.mCalculationList = new ArrayList<CalculationModel>();
//        }

        return mCalculationViewHelper;
    }

    private CalculationViewHelper() {}


    public static final CalculationViewHelper getInstance() {

        if ( mCalculationViewHelper == null ) {
            mCalculationViewHelper = new CalculationViewHelper();
        }

        return mCalculationViewHelper;
    }

    public static final void clearField() {

        mCalculationViewHelper = null;

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

            mModelList.add(CalculationModel.newInstance());
//            mModelList.add(newCalculationView());
//            mModelList.add(newCalculationView());
//            mModelList.add(newCalculationView());
//            mModelList.add(newCalculationView());
//            this.add();
        }

        if ( mPagerAdapter == null ) {

            setPagerOptions();
            mPagerAdapter = new CalculationViewPagerAdapter(mContext, mViewPager, mModelList, mCalculationButton);
            mViewPager.setAdapter(mPagerAdapter);
            mCircleIndicator.setViewPager(mViewPager);
            mViewPager.setOverScrollMode(ViewPager.OVER_SCROLL_NEVER);

        }

        mCalculationButton.setOnClickListener(this);

        Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        };

//        handler.sendEmptyMessageDelayed(0, 500);

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

            if ( mPagerAdapter != null ) {
                mPagerAdapter.notifyDataSetChanged();
            }
            setActionDoneKey();
            mViewPager.setCurrentItem(count, true);

        } else {
            Toast.makeText(mContext, R.string.add_max, Toast.LENGTH_SHORT).show();
        }



    }

    private void setActionDoneKey () {
        View v = null;
        CalculationModel c;

        EditText termEditText;

        int count = mModelList.size();

        ArrayList<View> viewList = mPagerAdapter.getViewList();

        for ( int i = 0; i < count; i++ ) {
            v = viewList.get(i);

            termEditText = (EditText) v.findViewById(R.id.edittext_term);

            if ( i < count - 1 ) {

                termEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);

            } else {
                termEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
            }

        }
    }

    public void remove() {
        int count = mModelList.size();


        if ( count > 1 ) {
//            setCalculationData();
            mModelList.remove(count - 1);

            View v = mPagerAdapter.getViewList().get(count - 1);

            Spinner repaymentSpinner;
            EditText loansEditText;
            EditText interestRateEditText;
            EditText termEditText;

            repaymentSpinner = (Spinner) v.findViewById(R.id.spinner_repayment);
            loansEditText = (EditText) v.findViewById(R.id.edittext_loans);
            interestRateEditText = (EditText) v.findViewById(R.id.edittext_interest_rate);
            termEditText = (EditText) v.findViewById(R.id.edittext_term);

            repaymentSpinner.setSelection(0);
            loansEditText.setText("");
            interestRateEditText.setText("");
            termEditText.setText("");


            if ( mPagerAdapter != null ) {
                mPagerAdapter.notifyDataSetChanged();
            }
            mViewPager.setCurrentItem(count - 2, true);

            setActionDoneKey();
//            displayCalculationData();
        } else {
//            Toast.makeText(mContext, "undefined", Toast.LENGTH_SHORT);
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
        boolean isEmpty = setCalculationData();

        if ( isEmpty ) {
            Toast.makeText(mContext, R.string.input_add_field, Toast.LENGTH_SHORT).show();

            CommonApplication.getInstance().trackEvent(
                    AnalyticsTrackers.TRACKER_CATEGORY_BUTTON,
                    AnalyticsTrackers.TRACKER_ACTION_CALCULATION,
                    "No Input Field"
            );

        } else {

            CommonApplication.getInstance().trackEvent(
                    AnalyticsTrackers.TRACKER_CATEGORY_BUTTON,
                    AnalyticsTrackers.TRACKER_ACTION_CALCULATION,
                    "count:" + Calculator.getInstance().getModelList().size()
            );


//            mCalculator.calculate();

            callResultActivity();
        }

    }

    /**
     * 계산 결과 화면 호출.
     */
    private void callResultActivity() {
        Intent intent = new Intent(mContext, ResultActivity.class);
//        intent.putParcelableArrayListExtra(Constant.EXTRA_CALCULATION_MODEL, mModelList);
        mContext.startActivity(intent);
    }

    /**
     * 화면을 통해 입력 받은 값을 모델에 세팅.
     * calculate 함수에서 호출함.
     */
    private boolean setCalculationData() {

        boolean isEmpty = false;

        View v = null;
        CalculationModel c;
        Spinner repaymentSpinner;
        EditText loansEditText;
        EditText interestRateEditText;
        EditText termEditText;

        int selectedType = 0;
        String loansText = "";
        String interestRateText = "";
        String termText = "";

        String log = "";

        for ( int i = 0; i < mModelList.size(); i++ ) {
            v = mPagerAdapter.getViewList().get(i);
            c = mModelList.get(i);
//            c = mCalculationList.get(i);
            if ( v != null ) {
                repaymentSpinner = (Spinner) v.findViewById(R.id.spinner_repayment);
                loansEditText = (EditText) v.findViewById(R.id.edittext_loans);
                interestRateEditText = (EditText) v.findViewById(R.id.edittext_interest_rate);
                termEditText = (EditText) v.findViewById(R.id.edittext_term);

                selectedType = repaymentSpinner.getSelectedItemPosition();
                loansText = loansEditText.getText().toString();
                interestRateText = interestRateEditText.getText().toString();
                termText = termEditText.getText().toString();

                //값이 없을 경우 예외처리.
                if ( "".equals( loansText )
                        || "".equals( interestRateText )
                        || "".equals( termText )) {
                    isEmpty = true;
                    break;
                }

                c.setSelectRepayment(selectedType);
                c.setLoansText(loansText);
                c.setInterestRateText(interestRateText);
                c.setTermText(termText);

                log +=  "index " + i
                        + " / " + "repayment : " + c.getSelectRepayment()
                        + " / " + "Loans : " + c.getLoansText()
                        + " / " + "InterestRate : " + c.getInterestRateText()
                        + " / " + "Term : " + c.getTermText();

            }
        }
        Log.d("LOAN", log);

        return isEmpty;
    }

    private void displayCalculationData () {
        View v = null;
        CalculationModel c;
        Spinner repaymentSpinner;
        EditText loansEditText;
        EditText interestRateEditText;
        EditText termEditText;

        String log = "";

        for ( int i = 0; i < mModelList.size(); i++ ) {
            v = mPagerAdapter.getViewList().get(i);
            c = mModelList.get(i);
//            c = mCalculationList.get(i);
            if ( v != null ) {
                repaymentSpinner = (Spinner) v.findViewById(R.id.spinner_repayment);
                loansEditText = (EditText) v.findViewById(R.id.edittext_loans);
                interestRateEditText = (EditText) v.findViewById(R.id.edittext_interest_rate);
                termEditText = (EditText) v.findViewById(R.id.edittext_term);

                repaymentSpinner.setSelection(c.getSelectRepayment());
                loansEditText.setText(c.getLoansText());
                interestRateEditText.setText(c.getInterestRateText());
                termEditText.setText(c.getTermText());
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch( id ) {
            case R.id.calculation :
                calculate();
                break;
        }

    }
}
