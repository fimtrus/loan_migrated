package com.fimtrus.loan.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.fimtrus.loan.AnalyticsTrackers;
import com.fimtrus.loan.CommonApplication;
import com.fimtrus.loan.R;
import com.fimtrus.loan.activity.ResultActivity;
import com.fimtrus.loan.model.CalculationModel;
import com.fimtrus.loan.model.Constant;
import com.fimtrus.loan.util.Util;

import java.util.Locale;

/**
 * SearchFragment.java
 * 
 * 검색 화면
 * 
 * @auther jong-hyun.jeong
 * @date 2014. 7. 15.
 */
public class SearchFragment extends Fragment implements View.OnClickListener,
		OnEditorActionListener {

	private View mRootLayout;
	private Spinner mRepaymentSpinner;
	private EditText mLoansEditText;
	private EditText mInterestRateEditText;
	private EditText mTermEditText;
	private EditText mHoldingPeriodEditText;
	private Button mCalculationButton;
	private TextView mHintTextView;
	private TextView mLoanTextView;
	private TextView mLoanNumberTextView;

    private ScrollView mScrollView;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		setRetainInstance(true);

		if ( mRootLayout == null ) {
			mRootLayout = inflater.inflate(R.layout.fragment_loan_search,
					container, false);
		}

		initialize();
		
		return mRootLayout;
	}



	private void initialize() {

		initializeFields();
		initializeListeners();
		initializeView();
	}

	private void initializeFields() {

		mRepaymentSpinner = (Spinner) mRootLayout
				.findViewById(R.id.spinner_repayment);
		mLoansEditText = (EditText) mRootLayout
				.findViewById(R.id.edittext_loans);
		mInterestRateEditText = (EditText) mRootLayout
				.findViewById(R.id.edittext_interest_rate);
		mTermEditText = (EditText) mRootLayout.findViewById(R.id.edittext_term);
		mHoldingPeriodEditText = (EditText) mRootLayout.findViewById(R.id.edittext_holding_period);
		mCalculationButton = (Button) mRootLayout
				.findViewById(R.id.calculation);
		mHintTextView = (TextView) mRootLayout.findViewById(R.id.textview_hint);
//
		mLoanTextView = (TextView) mRootLayout
				.findViewById(R.id.textview_loans_text);
		mLoanNumberTextView = (TextView) mRootLayout
				.findViewById(R.id.textview_loans_number);

        mScrollView = (ScrollView) mRootLayout.findViewById(R.id.scrollview);

	}

	private void initializeView() {
		mHintTextView.setText(Html.fromHtml(getResources().getString(
				R.string.hint_text)));

		Locale locale = Locale.getDefault();

		if ( !locale.getLanguage().contains("ko") ) {
			mLoanTextView.setVisibility(View.GONE);
			mLoanNumberTextView.setVisibility(View.GONE);
		}
 	}

	private void initializeListeners() {

		mCalculationButton.setOnClickListener(this);

		
		Locale locale = Locale.getDefault();
		
		if ( locale.getLanguage().contains("ko") ) {

			mLoansEditText.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before,
										  int count) {

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
											  int after) {

				}

				@Override
				public void afterTextChanged(Editable s) {
					String text = s.toString();

					if (text == null || text.equals("")) {
						mLoanTextView.setText("");
						mLoanNumberTextView.setText("");
						return;
					}

					mLoanTextView.setText(Util.convertNumberToKorean(text) + " 원");
					mLoanNumberTextView.setText(Util.toNumFormat(text) + "원");
				}
			});
		}

        mLoansEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if ( hasFocus ) {
                    mScrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            mScrollView.smoothScrollTo(0, mLoansEditText.getTop());
                        }
                    });

                }
            }
        });

        mInterestRateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if ( hasFocus ) {
                    mScrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            mScrollView.smoothScrollTo(0, mInterestRateEditText.getBottom());
                        }
                    });

                }
            }
        });

        mTermEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if ( hasFocus ) {
                    mScrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            mScrollView.smoothScrollTo(0, mTermEditText.getBottom());
                        }
                    });

                }
            }
        });

        mHoldingPeriodEditText.setOnEditorActionListener(this);

        mRepaymentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("JJH", "position : " + position);
                if( position == 2 ) {
                    ((View)mHoldingPeriodEditText.getParent()).setVisibility(View.GONE);
                } else {
                    ((View)mHoldingPeriodEditText.getParent()).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

	}

	private void startResultActivity() {

        CalculationModel c = CalculationModel.newInstance();

        int selectedType = 0;
        String loansText = "";
        String interestRateText = "";
        String termText = "";
        String holdingPeriodText = "";

        selectedType = mRepaymentSpinner.getSelectedItemPosition();
        loansText = mLoansEditText.getText().toString();
        interestRateText = mInterestRateEditText.getText().toString();
        termText = mTermEditText.getText().toString();
		holdingPeriodText = mHoldingPeriodEditText.getText().toString();

        //값이 없을 경우 예외처리.
        boolean isEmpty = false;

        if("".equals(holdingPeriodText) || selectedType == 2) {
            holdingPeriodText = "0";
        }
        if ("".equals(loansText)
                || "".equals(interestRateText)
                || "".equals(holdingPeriodText)
                || "".equals(termText)) {
            isEmpty = true;
        }

        if ( isEmpty ) {
            Toast.makeText(getActivity(), R.string.input_add_field, Toast.LENGTH_SHORT).show();

            CommonApplication.getInstance().trackEvent(
                    AnalyticsTrackers.TRACKER_CATEGORY_BUTTON,
                    AnalyticsTrackers.TRACKER_ACTION_CALCULATION,
                    "No Input Field"
            );
            return;
        }

        if(Integer.valueOf(holdingPeriodText) >= Integer.valueOf(termText)) {
            Toast.makeText(getActivity(), R.string.holding_period_must_be_less_than_term, Toast.LENGTH_SHORT).show();
            return;
        }

        c.setSelectRepayment(selectedType);
        c.setLoansText(loansText);
        c.setInterestRateText(interestRateText);
        c.setTermText(termText);
        c.setHodlingPeriodText(holdingPeriodText);


        Intent intent = new Intent(getActivity(), ResultActivity.class);
        intent.putExtra(Constant.EXTRA_CALCULATION_MODEL, c);
        getActivity().startActivity(intent);
    }

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.calculation:
			CommonApplication.getInstance().trackEvent(
					AnalyticsTrackers.TRACKER_CATEGORY_BUTTON,
					AnalyticsTrackers.TRACKER_ACTION_CALCULATION,
					"CALCULATE"
			);
			startResultActivity();
			break;
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (v.getId() == R.id.edittext_holding_period
				&& actionId == EditorInfo.IME_ACTION_DONE) { // 뷰의 id를 식별, 키보드의
																// 완료 키 입력 검출
			mCalculationButton.performClick();
		}
		return false;
	}
}
