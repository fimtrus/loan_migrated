package com.fimtrus.loan.fragment;

import java.util.Locale;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import com.fimtrus.loan.util.CalculationViewHelper;
import com.fimtrus.loan.util.Calculator;
import com.fimtrus.loan.util.Util;
import com.fimtrus.loan.view.WrapViewPager;

import me.relex.circleindicator.CircleIndicator;

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
	private FragmentManager mFragmentManager;
	private Spinner mRepaymentSpinner;
	private EditText mLoansEditText;
	private EditText mInterestRateEditText;
	private EditText mTermEditText;
	private Button mCalculationButton;
	private TextView mHintTextView;
	private TextView mLoanTextView;
	private TextView mLoanNumberTextView;

    private ScrollView mScrollView;

	private WrapViewPager mCalculationViewpager;
    private CalculationViewHelper mCalculationViewHelper;
	private CircleIndicator mCircleIndicator;
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

		mFragmentManager = getFragmentManager();
		mRepaymentSpinner = (Spinner) mRootLayout
				.findViewById(R.id.spinner_repayment);
		mLoansEditText = (EditText) mRootLayout
				.findViewById(R.id.edittext_loans);
		mInterestRateEditText = (EditText) mRootLayout
				.findViewById(R.id.edittext_interest_rate);
		mTermEditText = (EditText) mRootLayout.findViewById(R.id.edittext_term);
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


//        mCalculationViewHelper = CalculationViewHelper.newInstance(this.getActivity(), mCalculationViewpager , mCircleIndicator, mCalculationButton);
//        mCalculationViewHelper.submit();

		//메뉴아이템 활성화
//		setHasOptionsMenu(true);
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

        mTermEditText.setOnEditorActionListener(this);

	}

	private void startResultActivity() {

        CalculationModel c = CalculationModel.newInstance();

        int selectedType = 0;
        String loansText = "";
        String interestRateText = "";
        String termText = "";

        selectedType = mRepaymentSpinner.getSelectedItemPosition();
        loansText = mLoansEditText.getText().toString();
        interestRateText = mInterestRateEditText.getText().toString();
        termText = mTermEditText.getText().toString();

        //값이 없을 경우 예외처리.
        boolean isEmpty = false;

        if ("".equals(loansText)
                || "".equals(interestRateText)
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

        c.setSelectRepayment(selectedType);
        c.setLoansText(loansText);
        c.setInterestRateText(interestRateText);
        c.setTermText(termText);

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
		if (v.getId() == R.id.edittext_term
				&& actionId == EditorInfo.IME_ACTION_DONE) { // 뷰의 id를 식별, 키보드의
																// 완료 키 입력 검출
			mCalculationButton.performClick();
		}
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		switch ( id ) {
			case R.id.action_add :
				mCalculationViewHelper.add();
				break;
			case R.id.action_remove :
				mCalculationViewHelper.remove();
				break;
		}

		return super.onOptionsItemSelected(item);
	}


}
