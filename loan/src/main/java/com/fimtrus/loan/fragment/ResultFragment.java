package com.fimtrus.loan.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fimtrus.loan.AnalyticsTrackers;
import com.fimtrus.loan.CommonApplication;
import com.fimtrus.loan.R;
import com.fimtrus.loan.model.CalculationModel;
import com.fimtrus.loan.model.Constant;
import com.fimtrus.loan.model.RepaymentResultModel;
import com.fimtrus.loan.util.Calculator;
import com.fimtrus.loan.util.PinchToZoom;
import com.fimtrus.loan.util.Util;
import com.xrigau.syncscrolling.view.EnhancedScrollView;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;

/**
 * SearchFragment.java
 * 
 * 검색 화면
 * 
 * @auther jong-hyun.jeong
 * @date 2014. 7. 15.
 */
public class ResultFragment extends android.support.v4.app.Fragment {


	private final int REQUEST_PERMISSION_STORAGE = 1;

	private View mRootLayout;
	private FragmentManager mFragmentManager;
	private LayoutInflater mInflater;
	private LinearLayout mContentLayout;
	private Button mRetryButton;
	private EditText mLoansEditText;
	private EditText mInterestRateEditText;
	private EditText mTermEditText;
	private EditText mTotalInterestEditText;
	private EditText mHoldingPeriodEditText;
	private ArrayList<RepaymentResultModel> mRepaymentResultList;
	private BigDecimal mLoans;
	private Float mInterestRate;
	private Double mTerm;
	private EditText mTotalAmountEditText;

	private EnhancedScrollView mVerticalScrollView;


	private ScaleGestureDetector mScaleGestureDetector;

	private CalculationModel mCalculationModel;
	private PinchToZoom mPinchToZoom;
	private LinearLayout mZoomLayout;
	private HorizontalScrollView mContentHorizontalScrollView;
	private RelativeLayout mContentHorizontalScrollView2;

	public static final ResultFragment newInstance (CalculationModel c, ArrayList<RepaymentResultModel> resultList) {

		ResultFragment fragment = new ResultFragment();

		fragment.mCalculationModel = c;
		fragment.mRepaymentResultList = Calculator.calculate(c);

		return fragment;
	}
	public static final ResultFragment newInstance (CalculationModel c) {

		ResultFragment fragment = new ResultFragment();
		fragment.mCalculationModel = c;
        fragment.mRepaymentResultList = Calculator.calculate(c);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		mRootLayout = inflater.inflate(R.layout.fragment_loan_result, container, false);
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
		mContentLayout = (LinearLayout) mRootLayout.findViewById(R.id.layout_content);

		mVerticalScrollView = (EnhancedScrollView )mRootLayout.findViewById(R.id.layout_scrollview);
		mContentHorizontalScrollView = (HorizontalScrollView) mRootLayout.findViewById(R.id.horizontalScrollView1);
		mContentHorizontalScrollView2 = (RelativeLayout) mRootLayout.findViewById(R.id.horizontalScrollView2);

		mZoomLayout = (LinearLayout) mRootLayout.findViewById(R.id.layout_search_fields);
		mRetryButton = (Button) mRootLayout.findViewById(R.id.retry);
		mLoansEditText = (EditText) mRootLayout.findViewById(R.id.edittext_loans);
		mInterestRateEditText = (EditText) mRootLayout.findViewById(R.id.edittext_interest_rate);
		mTermEditText = (EditText) mRootLayout.findViewById(R.id.edittext_term);	
		mTotalInterestEditText = (EditText) mRootLayout.findViewById(R.id.edittext_total_interest);	
		mTotalAmountEditText = (EditText) mRootLayout.findViewById(R.id.edittext_total_amount);
		mHoldingPeriodEditText = (EditText) mRootLayout.findViewById(R.id.edittext_holding_period);

//		mPinchToZoom = PinchToZoom.newInstance(getActivity(), mContentLayout);

//		mScaleGestureDetector = new ScaleGestureDetector(getActivity(), mPinchToZoom);

	}
	

	private void initializeView() {


		setHasOptionsMenu(true );

		new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mLoansEditText.getWindowToken(), 0);
			}
		}.sendEmptyMessageDelayed(0, 500);

//		mPinchToZoom.attach(mScaleGestureDetector);
//
//		mContentHorizontalScrollView.requestDisallowInterceptTouchEvent(true);
//
//		mVerticalScrollView.setOnTouchListener(new View.OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//
//				if (event.getPointerCount() > 1) {
//
//					if (event.getAction() == MotionEvent.ACTION_MOVE
//							|| event.getAction() == MotionEvent.ACTION_DOWN ) {
//						mScaleGestureDetector.onTouchEvent(event);
//					} else if (event.getAction() == MotionEvent.ACTION_POINTER_UP) {
//
////						mContentLayout.notify
////						mContentLayout.requestLayout();
//						mContentHorizontalScrollView2.requestLayout();
////						mContentHorizontalScrollView.requestLayout();
//
//
//					}
//
//					return true;
//				} else {
//					return false;
//				}
//			}
//		});

		mContentHorizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				//					if ( event.getAction() == MotionEvent.ACTION_MOVE ) {
//						mScaleGestureDetector.onTouchEvent(event);
//					} else if (event.getAction() == MotionEvent.ACTION_POINTER_UP ) {
//
//					}
				return event.getPointerCount() > 1;
			}
		});




		int selectRepayment = mCalculationModel.getSelectRepayment();
		String loansText = mCalculationModel.getLoansText();
		String interestRateText = mCalculationModel.getInterestRateText();
		String termText = mCalculationModel.getTermText();
		String holdingPeriodText = mCalculationModel.getHodlingPeriodText();


		boolean isSum = mCalculationModel.isSum();

		if ( isSum ) {
			( (LinearLayout) mInterestRateEditText.getParent() ).setVisibility(View.GONE);
			( (LinearLayout)mTermEditText.getParent() ).setVisibility(View.GONE);
			mInterestRate = 0.0f;
			mTerm = Double.valueOf(0);
		} else {
			mInterestRateEditText.setText( interestRateText );
			mTermEditText.setText( termText );

			if ( "".equals(interestRateText) ){
				mInterestRate = 0.0f;
			} else {

				mInterestRate = Float.valueOf(interestRateText);
			}
			if ( "".equals(termText) ){
				mTerm = Double.valueOf(0);
			} else {

				mTerm = Double.valueOf(termText);
			}
			if( "".equals(holdingPeriodText) || "0".equals(holdingPeriodText)) {
				((LinearLayout)mHoldingPeriodEditText.getParent()).setVisibility(View.GONE);
			} else {
				mHoldingPeriodEditText.setText(holdingPeriodText);
			}
		}

		mLoansEditText.setText( Util.toNumFormat(loansText) );

//		mTotalInterestEditText.setText( Util.toNumFormat(loansText) );
		
		mLoans = new BigDecimal(loansText );

		CommonApplication.getInstance().trackEvent(
				AnalyticsTrackers.TRACKER_CATEGORY_RESULT,
				AnalyticsTrackers.TRACKER_ACTION_CALCULATION,
				"type:"+selectRepayment+";loans:"+loansText+";interestRate:"+interestRateText+";term:"+termText
		);

		showResult();
	}

	/**
	 * 유형에 따라 다른로직을 적용한다.
	 */
	private void showResult() {

		BigDecimal totalInterest = new BigDecimal("0");
		for ( int i = 0 ; i < mRepaymentResultList.size(); i++ ) {
			totalInterest = totalInterest.add( mRepaymentResultList.get(i).getInterest() );
			
		}

		mTotalInterestEditText.setText( Util.toNumFormat( totalInterest.toString() ) );
		mTotalAmountEditText.setText( Util.toNumFormat( mLoans.add( totalInterest ).toString()  ) );

		LinearLayout tableRow;
		//TODO : 날짜 및 금액 찍기
//		TextView tv;
//		String screenshotTitleText;
//		tableRow = (LinearLayout) mInflater.inflate(R.layout.view_screenshow_title, null);
//		tv = ((TextView) tableRow.findViewById(R.id.textview_screenshot_title) );
//		mContentLayout.addView(tableRow);



		//헤더.
		tableRow = (LinearLayout) mInflater.inflate(R.layout.table_row, null);
		mContentLayout.addView(tableRow);

		for ( int i = 0; i < mRepaymentResultList.size(); i++ ) {
			
			RepaymentResultModel model = mRepaymentResultList.get(i);
			
			tableRow = (LinearLayout) mInflater.inflate(R.layout.table_row, null);
			((TextView) tableRow.findViewById(R.id.textview_no) ).setText(i + "");

			((TextView) tableRow.findViewById(R.id.textview_replayments) )
			.setText(Util.toNumFormat(model.getRepayments().setScale(0, RoundingMode.HALF_UP).toString()));
			
			((TextView) tableRow.findViewById(R.id.textview_principal) )
			.setText( Util.toNumFormat( model.getLoans().setScale(0, RoundingMode.HALF_UP).toString() ) );
			
			((TextView) tableRow.findViewById(R.id.textview_interest) )
			.setText( Util.toNumFormat( model.getInterest().setScale(0, RoundingMode.HALF_UP).toString() ) );
			
			((TextView) tableRow.findViewById(R.id.textview_remaining_amount) )
			.setText( Util.toNumFormat( model.getRemainingAmount().setScale(0, RoundingMode.HALF_UP).toString() ) );
			((TextView) tableRow.findViewById(R.id.textview_total_principal) )
			.setText( Util.toNumFormat( model.getTotalLoans().setScale(0, RoundingMode.HALF_UP).toString() ) );

			if ( model.isLast() ) {

				int color = Color.RED;

				((TextView) tableRow.findViewById(R.id.textview_no) ).setTypeface(null, Typeface.BOLD);
				((TextView) tableRow.findViewById(R.id.textview_replayments) ).setTypeface(null, Typeface.BOLD);
				((TextView) tableRow.findViewById(R.id.textview_principal) ).setTypeface(null, Typeface.BOLD);
				((TextView) tableRow.findViewById(R.id.textview_interest) ).setTypeface(null, Typeface.BOLD);
				((TextView) tableRow.findViewById(R.id.textview_remaining_amount) ).setTypeface(null, Typeface.BOLD);
				((TextView) tableRow.findViewById(R.id.textview_total_principal) ).setTypeface(null, Typeface.BOLD);

				((TextView) tableRow.findViewById(R.id.textview_no) ).setTextColor(color);
				((TextView) tableRow.findViewById(R.id.textview_replayments) ).setTextColor(color);
				((TextView) tableRow.findViewById(R.id.textview_principal) ).setTextColor(color);
				((TextView) tableRow.findViewById(R.id.textview_interest) ).setTextColor(color);
				((TextView) tableRow.findViewById(R.id.textview_remaining_amount) ).setTextColor(color);
				((TextView) tableRow.findViewById(R.id.textview_total_principal) ).setTextColor(color);
			}


			mContentLayout.addView(tableRow);
		}
	}

	private void initializeListeners() {
		mRetryButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ResultFragment.this.getActivity().onBackPressed();
			}
		});
	}


	private void saveScreen () {

		File dirPath = new File(Constant.DIRECTORY_PATH);

		if ( !dirPath.exists() ) {
			dirPath.mkdir();
		}

		Date date = new Date();

		File file = new File( Constant.DIRECTORY_PATH, "loan_"+ date.getTime() + ".jpg");

		String filePath = file.getAbsolutePath();
		String message = getResources().getString(R.string.toast_saved);
		com.jhlibrary.util.Util.saveScreen(getActivity(), mContentLayout, file);

		CommonApplication.getInstance().trackEvent(
				AnalyticsTrackers.TRACKER_CATEGORY_BUTTON,
				AnalyticsTrackers.TRACKER_ACTION_SAVE,
				date.getTime() + ""
		);

		Toast.makeText(getActivity(), message + "\n" + filePath, Toast.LENGTH_SHORT).show();
	}

	private boolean checkPermission () {

		if ( ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED
				|| ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
			!= PackageManager.PERMISSION_GRANTED

				) {
			//Denied
			if ( shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ) {
				Toast.makeText(getActivity(), R.string.toast_save_denied, Toast.LENGTH_SHORT).show();
			}
//			shouldShowRequestPermissionRationale(Manifest.permission.STORAGE);
			requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
					android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);

		} else {
			//granted
			saveScreen();
		}


//		ContextCompat.checkSelfPermission(getActivity(),)

//		requestPermissions();
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		switch ( id ) {
			case R.id.action_save :
				checkPermission();

				break;

		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		getActivity().getMenuInflater().inflate(R.menu.result, menu);
	}
	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String[] permissions, int[] grantResults) {


		switch ( requestCode ) {
			case 1 :
				//STORAGE

		}

		// TODO Auto-generated method stub
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
}
