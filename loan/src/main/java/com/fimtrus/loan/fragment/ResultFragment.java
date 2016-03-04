package com.fimtrus.loan.fragment;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fimtrus.loan.R;
import com.fimtrus.loan.Util;
import com.fimtrus.loan.model.Constant;
import com.fimtrus.loan.model.RepaymentResultModel;

/**
 * SearchFragment.java
 * 
 * 검색 화면
 * 
 * @auther jong-hyun.jeong
 * @date 2014. 7. 15.
 */
public class ResultFragment extends Fragment {

	private View mRootLayout;
	private FragmentManager mFragmentManager;
	private LayoutInflater mInflater;
	private LinearLayout mContentLayout;
	private Button mRetryButton;
	private EditText mLoansEditText;
	private EditText mInterestRateEditText;
	private EditText mTermEditText;
	private EditText mTotalInterestEditText;
	private ArrayList<RepaymentResultModel> mRepaymentResultList;
	private BigDecimal mLoans;
	private Float mInterestRate;
	private Double mTerm;
	private EditText mTotalAmountEditText;

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
		mRetryButton = (Button) mRootLayout.findViewById(R.id.retry);
		mLoansEditText = (EditText) mRootLayout.findViewById(R.id.edittext_loans);
		mInterestRateEditText = (EditText) mRootLayout.findViewById(R.id.edittext_interest_rate);
		mTermEditText = (EditText) mRootLayout.findViewById(R.id.edittext_term);	
		mTotalInterestEditText = (EditText) mRootLayout.findViewById(R.id.edittext_total_interest);	
		mTotalAmountEditText = (EditText) mRootLayout.findViewById(R.id.edittext_total_amount);	
		
		
		mRepaymentResultList = new ArrayList<RepaymentResultModel>();
		
	}
	

	private void initializeView() {
		
		Intent intent = getActivity().getIntent();
		
		int selectRepayment = intent.getIntExtra(Constant.EXTRA_SELECTED_INDEX, -1);
		String loansText = intent.getStringExtra(Constant.EXTRA_LOANS);
		String interestRateText = intent.getStringExtra(Constant.EXTRA_INTEREST_RATE);
		String termText = intent.getStringExtra(Constant.EXTRA_TERM);
		
		mLoansEditText.setText( Util.toNumFormat(loansText) );
		mInterestRateEditText.setText( interestRateText );
		mTermEditText.setText( termText );
//		mTotalInterestEditText.setText( Util.toNumFormat(loansText) );
		
		mLoans = new BigDecimal(loansText );
		mInterestRate = Float.valueOf(interestRateText);
		mTerm = Double.valueOf(termText);
		
		calculateResult(selectRepayment);
		
		showResult();
	}

	/**
	 * 이자율 등을 계산...오브젝트에 담는다.
	 */
	private void calculateResult( int selectedRepayment ) {
		
		switch ( selectedRepayment ) {
		//원금 균등
		case 0 :
			calculateRepaymentLoans();
			break;
		//원리금 균등
		case 1 :
			calculateRepaymentLoansAndInterest();
			break;
		case 2 :
			calculateRepaymentLastRepayment();
			break;
		}
		
	}


	/**
	 * 0.원금
	 */
	private void calculateRepaymentLoans() {
		//월불입금
		//((1+B4)^B5*B4)/((1+B4)^B5-1)/12
		BigDecimal repaymentResult = mLoans.divide( BigDecimal.valueOf(mTerm), 10, BigDecimal.ROUND_HALF_UP );
//		double repaymentResult = mLoans * ( Math.pow( ( 1 + ( mInterestRate / 100 ) ) , mTerm / 12 ) * ( mInterestRate / 100 ) ) 
//				/ ( Math.pow( ( 1 + ( mInterestRate / 100 ) ) , mTerm / 12 ) - 1 ) / 12 ;
		
		Log.d("", "원금 : " + repaymentResult);
		
		BigDecimal loansTemp; //원금 남은 금액 ..
		
		loansTemp = mLoans;//.setScale(2, BigDecimal.ROUND_HALF_UP);
		RepaymentResultModel model;
		
		for ( int i = 0; i <= mTerm.intValue(); i++ ) {
			
			if ( i == 0 ) {
				
				model = new RepaymentResultModel();
				model.setRemainingAmount( new BigDecimal( String.valueOf(loansTemp ) ) );
				mRepaymentResultList.add(model);
			} else {

				//이자. 잔액 / 12 * 할부금리
				BigDecimal interestResult =  loansTemp.divide(BigDecimal.valueOf(12), BigDecimal.ROUND_HALF_UP).multiply( BigDecimal.valueOf( (mInterestRate / 100 ) ), MathContext.DECIMAL128 );  //loansTemp / 12 * ( mInterestRate / 100 ) ;
				
				model = new RepaymentResultModel();
				model.setRepayments(  repaymentResult.add(interestResult ) ); //상환그,ㅁ
				model.setLoans(  repaymentResult ); //납입원금
				model.setInterest(  interestResult ); //이자
				//잔금
				model.setRemainingAmount(  mRepaymentResultList.get(mRepaymentResultList.size()- 1).getRemainingAmount().subtract(model.getLoans()) );
				//낸 원금
				model.setTotalLoans( model.getLoans().add( mRepaymentResultList.get(mRepaymentResultList.size()- 1).getTotalLoans() ) );
				
				loansTemp = model.getRemainingAmount();
				
				mRepaymentResultList.add(model);
				
				Log.d("", "납입원금 : " + model.getLoans() + " 납입이자 : " + model.getInterest() + " 남은 원금 : " + model.getTotalLoans());
				
			}
		}
	}
	
	/**
	 * 1.원리금
	 */
	private void calculateRepaymentLoansAndInterest() {
		
		BigDecimal loansTemp; //원금 남은 금액 ..
		
		
		loansTemp = mLoans;
		RepaymentResultModel model;
		
		//월불입금
		//월불입금 계산공식 : 대출원금 × 이자율 ÷ 12 × (1 + 이자율 ÷ 12)^기간 ÷((1 + 이자율 ÷ 12)^기간 -1)
		//대출원금 × 이자율 ÷ 12 × (1 + 이자율 ÷ 12)^기간 :  
//		double temp = mLoans * ( mInterestRate / 100 ) / 12 * Math.pow( ( 1 + ( mInterestRate / 100 ) / 12 ) , mTerm );
//		//((1 + 이자율 ÷ 12)^기간
//		double temp2 = Math.pow( ( 1 + ( mInterestRate / 100 ) / 12 ), mTerm);
		
		BigDecimal temp = mLoans.multiply( BigDecimal.valueOf(  ((double)mInterestRate / 100) ), MathContext.DECIMAL128 ).divide( BigDecimal.valueOf((double)12), 10, BigDecimal.ROUND_HALF_UP).multiply( BigDecimal.valueOf( Math.pow( ( (double)1 + ( (double) mInterestRate / 100 ) / (double)12 ) , mTerm )), MathContext.DECIMAL128);
		BigDecimal temp2 = BigDecimal.valueOf( Math.pow( ( (double)1 + ( (double)mInterestRate / 100 ) / 12 ), mTerm));
		BigDecimal repaymentResult = temp.divide( temp2.subtract(BigDecimal.valueOf(1) ), BigDecimal.ROUND_HALF_UP );
		
		for ( int i = 0; i <= mTerm.intValue(); i++ ) {
			
			if ( i == 0 ) {
				
				model = new RepaymentResultModel();
				model.setRemainingAmount( loansTemp );
				mRepaymentResultList.add(model);
			} else {

				//이자. 잔액 / 12 * 할부금리
				BigDecimal interestResult =  loansTemp.divide(BigDecimal.valueOf(12), 10, BigDecimal.ROUND_HALF_UP).multiply( BigDecimal.valueOf( ((double) mInterestRate / 100 ) ), MathContext.DECIMAL128 );  //loansTemp / 12 * ( mInterestRate / 100 ) ;
				
				model = new RepaymentResultModel();
				model.setRepayments(  repaymentResult  ); //상환그,ㅁ
				
				model.setLoans(  repaymentResult.subtract( interestResult ) ); //납입원금
				
				model.setInterest(  interestResult ); //이자
				//잔금
				model.setRemainingAmount(  mRepaymentResultList.get(mRepaymentResultList.size()- 1).getRemainingAmount().subtract( model.getLoans() ) );
				//낸 원금
				model.setTotalLoans( model.getLoans().add( mRepaymentResultList.get(mRepaymentResultList.size()- 1).getTotalLoans()));
				
				loansTemp = model.getRemainingAmount();
				
				mRepaymentResultList.add(model);
				
				Log.d("", repaymentResult.toString());
				
			}
		}
		
//		Log.d("", "RESULT : " + Util.toNumFormat((int) repaymentResult));
//		model.setReplayments(replayments);
	}
	
	/**
	 * 2.만기일시
	 */
	private void calculateRepaymentLastRepayment() {
		// 1000000 * 5.9 / 100 * 24 / 12 / 24
		//월불입금
		//((1+B4)^B5*B4)/((1+B4)^B5-1)/12
		BigDecimal repaymentResult = new BigDecimal("0");
//		double repaymentResult = mLoans * ( Math.pow( ( 1 + ( mInterestRate / 100 ) ) , mTerm / 12 ) * ( mInterestRate / 100 ) ) 
//				/ ( Math.pow( ( 1 + ( mInterestRate / 100 ) ) , mTerm / 12 ) - 1 ) / 12 ;
		
		
		BigDecimal loansTemp; //원금 남은 금액 ..
		
		loansTemp = mLoans;
		RepaymentResultModel model;
		
		for ( int i = 0; i <=  mTerm; i++ ) {
			
			if ( i == 0 ) {
				
				model = new RepaymentResultModel();
				model.setRemainingAmount( loansTemp );
				mRepaymentResultList.add(model);
			} else if ( i == mTerm ) {
				
				//이자. 잔액 / 12 * 할부금리
				BigDecimal interestResult = loansTemp.multiply(BigDecimal.valueOf( (( mInterestRate / 100 ) / 12)), MathContext.DECIMAL128);
				
				model = new RepaymentResultModel();
				model.setRepayments( mLoans.add( interestResult ) ); //상환그,ㅁ
				model.setLoans(  mLoans ); //납입원금
				model.setInterest(  interestResult ); //이자
				model.setRemainingAmount( repaymentResult );
				model.setTotalLoans( mLoans );
				
				mRepaymentResultList.add(model);
				
			} else {

				//1000000 * 5.9 / 100 * 24 / 12 / 24
				BigDecimal interestResult = loansTemp.multiply(BigDecimal.valueOf( (( mInterestRate / 100 ) / 12)), MathContext.DECIMAL128);
				
				model = new RepaymentResultModel();
				model.setRepayments(  repaymentResult.add( interestResult ) ); //상환그,ㅁ
				model.setLoans(  repaymentResult ); //납입원금
				model.setInterest(  interestResult ); //이자
				model.setTotalLoans( repaymentResult );
				model.setRemainingAmount( mLoans );
				
				mRepaymentResultList.add(model);
				
			}
		}
	}


	/**
	 * 유형에 따라 다른로직을 적용한다.
	 * @param selectedRepayment
	 */
	private void showResult() {
		
		BigDecimal totalInterest = new BigDecimal("0");
		for ( int i = 0 ; i < mRepaymentResultList.size(); i++ ) {
			totalInterest = totalInterest.add( mRepaymentResultList.get(i).getInterest() );
			
		}
		
		mTotalInterestEditText.setText( Util.toNumFormat( totalInterest.toString() ) );
		mTotalAmountEditText.setText( Util.toNumFormat( mLoans.add( totalInterest ).toString()  ) );
		
		
		//헤더.
		LinearLayout tableRow;
		tableRow = (LinearLayout) mInflater.inflate(R.layout.table_row, null);
		mContentLayout.addView(tableRow);
		
		
		for ( int i = 0; i < mRepaymentResultList.size(); i++ ) {
			
			RepaymentResultModel model = mRepaymentResultList.get(i);
			
			tableRow = (LinearLayout) mInflater.inflate(R.layout.table_row, null);
			((TextView) tableRow.findViewById(R.id.textview_no) ).setText(i + "");
			
			((TextView) tableRow.findViewById(R.id.textview_replayments) )
			.setText( Util.toNumFormat(model.getRepayments().setScale(0, RoundingMode.HALF_UP).toString() ) );
			
			((TextView) tableRow.findViewById(R.id.textview_principal) )
			.setText( Util.toNumFormat( model.getLoans().setScale(0, RoundingMode.HALF_UP).toString() ) );
			
			((TextView) tableRow.findViewById(R.id.textview_interest) )
			.setText( Util.toNumFormat( model.getInterest().setScale(0, RoundingMode.HALF_UP).toString() ) );
			
			((TextView) tableRow.findViewById(R.id.textview_remaining_amount) )
			.setText( Util.toNumFormat( model.getRemainingAmount().setScale(0, RoundingMode.HALF_UP).toString() ) );
			((TextView) tableRow.findViewById(R.id.textview_total_principal) )
			.setText( Util.toNumFormat( model.getTotalLoans().setScale(0, RoundingMode.HALF_UP).toString() ) );
			
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
}
