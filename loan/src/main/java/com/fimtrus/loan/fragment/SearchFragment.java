package com.fimtrus.loan.fragment;

import java.util.Locale;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.fimtrus.loan.R;
import com.fimtrus.loan.util.CalculationViewHelper;
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
//	private Spinner mRepaymentSpinner;
//	private EditText mLoansEditText;
//	private EditText mInterestRateEditText;
//	private EditText mTermEditText;
	private Button mCalculationButton;
	private TextView mHintTextView;
//	private TextView mLoanTextView;
//	private TextView mLoanNumberTextView;

	private WrapViewPager mCalculationViewpager;
    private CalculationViewHelper mCalculationViewHelper;
	private CircleIndicator mCircleIndicator;
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		setRetainInstance(true);

		mRootLayout = inflater.inflate(R.layout.fragment_loan_search,
				container, false);

		initialize();
		
		return mRootLayout;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		getActivity().getMenuInflater().inflate(R.menu.calculation, menu);
	}

	private void initialize() {

		initializeFields();
		initializeListeners();
		initializeView();
	}

	private void initializeFields() {

		mFragmentManager = getFragmentManager();
//		mRepaymentSpinner = (Spinner) mRootLayout
//				.findViewById(R.id.spinner_repayment);
//		mLoansEditText = (EditText) mRootLayout
//				.findViewById(R.id.edittext_loans);
//		mInterestRateEditText = (EditText) mRootLayout
//				.findViewById(R.id.edittext_interest_rate);
//		mTermEditText = (EditText) mRootLayout.findViewById(R.id.edittext_term);
		mCalculationButton = (Button) mRootLayout
				.findViewById(R.id.calculation);
		mHintTextView = (TextView) mRootLayout.findViewById(R.id.textview_hint);
//
//		mLoanTextView = (TextView) mRootLayout
//				.findViewById(R.id.textview_loans_text);
//		mLoanNumberTextView = (TextView) mRootLayout
//				.findViewById(R.id.textview_loans_number);

		mCalculationViewpager = (WrapViewPager) mRootLayout.findViewById(R.id.viewpager_calculation);
		mCircleIndicator = (CircleIndicator) mRootLayout.findViewById(R.id.indicator);

	}

	private void initializeView() {
		mHintTextView.setText(Html.fromHtml(getResources().getString(
				R.string.hint_text)));

		Locale locale = Locale.getDefault();

//		if ( !locale.getLanguage().contains("ko") ) {
//			mLoanTextView.setVisibility(View.GONE);
//			mLoanNumberTextView.setVisibility(View.GONE);
//		}


        mCalculationViewHelper = CalculationViewHelper.newInstance(this.getActivity(), mCalculationViewpager , mCircleIndicator, mCalculationButton);
        mCalculationViewHelper.submit();

		//메뉴아이템 활성화
		setHasOptionsMenu(true);
	}

	private void initializeListeners() {
//		mCalculationButton.setOnClickListener(this);

		
		Locale locale = Locale.getDefault();
		
//		if ( locale.getLanguage().contains("ko") ) {
//
//			mLoansEditText.addTextChangedListener(new TextWatcher() {
//
//				@Override
//				public void onTextChanged(CharSequence s, int start, int before,
//										  int count) {
//
//				}
//
//				@Override
//				public void beforeTextChanged(CharSequence s, int start, int count,
//											  int after) {
//
//				}
//
//				@Override
//				public void afterTextChanged(Editable s) {
//					String text = s.toString();
//
//					if (text == null || text.equals("")) {
//						mLoanTextView.setText("");
//						mLoanNumberTextView.setText("");
//						return;
//					}
//
//					mLoanTextView.setText(Util.convertNumberToKorean(text) + " 원");
//					mLoanNumberTextView.setText(Util.toNumFormat(text) + "원");
//				}
//			});
//		}
//
//		mTermEditText.setOnEditorActionListener(this);
	}

	private void startResultActivity() {



	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.calculation:
			
			startResultActivity();
			break;
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (v.getId() == R.id.edittext_term
				&& actionId == EditorInfo.IME_ACTION_DONE) { // 뷰의 id를 식별, 키보드의
																// 완료 키 입력 검출
//			mCalculationButton.performClick();
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
