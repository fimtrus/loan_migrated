package com.fimtrus.loan.util;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.fimtrus.loan.model.CalculationModel;
import com.fimtrus.loan.model.RepaymentResultModel;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

/**
 * Created by fimtrus on 16. 4. 18..
 */
public class Calculator {

    private ArrayList<ArrayList<RepaymentResultModel>> mRepaymentResultLists;

    private ArrayList<RepaymentResultModel> mRepaymentSumList;

    private ArrayList<CalculationModel> mModelList;

    private CalculationModel mSum;

    private Context mContext;

    private static Calculator mCalculator;

    private Calculator () {
        super();
        mRepaymentResultLists = new ArrayList<>();
        mRepaymentSumList = new ArrayList<>();
        mSum = CalculationModel.newInstance();
    }

    public static Calculator newInstance(Context context, ArrayList<CalculationModel> list) {

        if ( mCalculator == null ) {
            mCalculator = new Calculator();
            mCalculator.mContext = context;
            mCalculator.mModelList = list;
        }

        return mCalculator;
    }

    public static Calculator getInstance() {

        if ( mCalculator == null ) {
            mCalculator = new Calculator();
        }

        return mCalculator;
    }

    public void calculate () {

        this.clear();

        for( CalculationModel c : mModelList ) {

            int selectedRepayment = c.getSelectRepayment();

            switch ( selectedRepayment ) {
                //원금 균등
                case 0 :
                    calculateRepaymentLoans( c );
                    break;
                //원리금 균등
                case 1 :
                    calculateRepaymentLoansAndInterest( c );
                    break;
                case 2 :
                    calculateRepaymentLastRepayment( c );
                    break;
            }
        }

        if ( mModelList.size() > 1 ) {
            calculateSum();
        } else {
            mSum = null;
            mRepaymentSumList.clear();
        }


    }

    private void calculateSum () {

        int repaymentResultLength = 0;
        RepaymentResultModel tempSumModel;
        RepaymentResultModel repaymentResultModel;
        for ( ArrayList<RepaymentResultModel> c : mRepaymentResultLists ) {

            repaymentResultLength = c.size();

            if ( repaymentResultLength > 0 ) {

                for( int i = 0; i < repaymentResultLength; i++ ) {

                    repaymentResultModel = c.get(i);

                    if ( mRepaymentSumList.size() <= i ) {

                        tempSumModel = RepaymentResultModel.newInstance(repaymentResultModel);

                        if ( mRepaymentSumList.size() > 1 ) {
                            tempSumModel.setTotalLoans( tempSumModel.getLoans().add( mRepaymentSumList.get(mRepaymentSumList.size()- 1).getTotalLoans() ) );
                        }

                        mRepaymentSumList.add( tempSumModel );

                    } else {
                        tempSumModel = mRepaymentSumList.get(i);

                        tempSumModel.setInterest(tempSumModel.getInterest().add(repaymentResultModel.getInterest()) );
                        tempSumModel.setLoans(tempSumModel.getLoans().add(repaymentResultModel.getLoans()));
                        tempSumModel.setRemainingAmount(tempSumModel.getRemainingAmount().add(repaymentResultModel.getRemainingAmount()));

                        tempSumModel.setRepayments(tempSumModel.getRepayments().add(repaymentResultModel.getRepayments()));
                        //낸 원금
//                        tempSumModel.setTotalLoans( tempSumModel.getLoans().add( mRepaymentSumList.get(mRepaymentSumList.size()- 1).getTotalLoans() ) );
                    }

                    if ( i == repaymentResultLength - 1 ) {
                        tempSumModel.setIsLast(true);
                    }

                }

            }
        }

        int sumListLength = mRepaymentSumList.size();

        for( int i = 0; i < sumListLength; i++ ) {

            tempSumModel = mRepaymentSumList.get(i);
            //낸 원금
            if ( i > 0 ) {
                tempSumModel.setTotalLoans( tempSumModel.getLoans().add( mRepaymentSumList.get(i - 1).getTotalLoans() ) );
            }


        }


        BigDecimal loans = null;
        BigDecimal term = null;
        BigDecimal interestRate = null;

        for ( CalculationModel c : mModelList ) {

            if ( loans == null ) {
//                interestRate = new BigDecimal(c.getInterestRateText());
//                term = new BigDecimal(c.getTermText());
                loans = new BigDecimal(c.getLoansText());
            } else {
//                interestRate = interestRate.add(new BigDecimal(c.getInterestRateText()));
//                term = term.add(new BigDecimal(c.getTermText()));
                loans = loans.add(new BigDecimal(c.getLoansText()));
            }
        }

        mSum = CalculationModel.newInstance();
        mSum.setIsSum(true);
//        mSum.setInterestRateText(interestRate.toPlainString());
//        mSum.setTermText(term.toPlainString());
        mSum.setLoansText(loans.toPlainString());

    }

    /**
     * 이자율 등을 계산...오브젝트에 담는다.
     */
//    @Deprecated
//    private void calculateResult( int selectedRepayment ) {
//
//        switch ( selectedRepayment ) {
//            //원금 균등
//            case 0 :
//                calculateRepaymentLoans( selectedRepayment );
//                break;
//            //원리금 균등
//            case 1 :
//                calculateRepaymentLoansAndInterest( selectedRepayment );
//                break;
//            case 2 :
//                calculateRepaymentLastRepayment( selectedRepayment );
//                break;
//        }
//
//    }


    /**
     * 0.원금
     */
    private void calculateRepaymentLoans( final CalculationModel calculationModel ) {

        String loansText = calculationModel.getLoansText();
        String termText = calculationModel.getTermText();
        String interestRateText = calculationModel.getInterestRateText();

        BigDecimal loans = new BigDecimal(loansText );
        Double term =  Double.valueOf(termText);
        Float interestRate = Float.valueOf(interestRateText);
        //월불입금
        //((1+B4)^B5*B4)/((1+B4)^B5-1)/12
        BigDecimal repaymentResult = loans.divide( BigDecimal.valueOf(term), 10, BigDecimal.ROUND_HALF_UP );
//		double repaymentResult = mLoans * ( Math.pow( ( 1 + ( mInterestRate / 100 ) ) , mTerm / 12 ) * ( mInterestRate / 100 ) )
//				/ ( Math.pow( ( 1 + ( mInterestRate / 100 ) ) , mTerm / 12 ) - 1 ) / 12 ;

        Log.d("", "원금 : " + repaymentResult);

        BigDecimal loansTemp; //원금 남은 금액 ..

        loansTemp = loans;//.setScale(2, BigDecimal.ROUND_HALF_UP);
        RepaymentResultModel model;

        ArrayList<RepaymentResultModel> repaymentResultList = new ArrayList<RepaymentResultModel>();

        for ( int i = 0; i <= term.intValue(); i++ ) {

            if ( i == 0 ) {

                model = new RepaymentResultModel();
                model.setRemainingAmount( new BigDecimal( String.valueOf(loansTemp ) ) );
                repaymentResultList.add(model);
            } else {

                //이자. 잔액 / 12 * 할부금리
                BigDecimal interestResult =  loansTemp.divide(BigDecimal.valueOf(12), BigDecimal.ROUND_HALF_UP).multiply( BigDecimal.valueOf( (interestRate / 100 ) ), MathContext.DECIMAL128 );  //loansTemp / 12 * ( mInterestRate / 100 ) ;

                model = new RepaymentResultModel();
                model.setRepayments(  repaymentResult.add(interestResult ) ); //상환그,ㅁ
                model.setLoans(  repaymentResult ); //납입원금
                model.setInterest(  interestResult ); //이자
                //잔금
                model.setRemainingAmount(  repaymentResultList.get(repaymentResultList.size()- 1).getRemainingAmount().subtract(model.getLoans()) );
                //낸 원금
                model.setTotalLoans( model.getLoans().add( repaymentResultList.get(repaymentResultList.size()- 1).getTotalLoans() ) );

                loansTemp = model.getRemainingAmount();

                repaymentResultList.add(model);

                Log.d("", "납입원금 : " + model.getLoans() + " 납입이자 : " + model.getInterest() + " 남은 원금 : " + model.getTotalLoans());

            }
        }

        mRepaymentResultLists.add(repaymentResultList);
    }

    /**
     * 1.원리금
     */
    private void calculateRepaymentLoansAndInterest( final CalculationModel calculationModel ) {


        String loansText = calculationModel.getLoansText();
        String termText = calculationModel.getTermText();
        String interestRateText = calculationModel.getInterestRateText();

        BigDecimal loans = new BigDecimal(loansText );
        Double term =  Double.valueOf(termText);
        Float interestRate = Float.valueOf(interestRateText);


        ArrayList<RepaymentResultModel> repaymentResultList = new ArrayList<RepaymentResultModel>();
        BigDecimal loansTemp; //원금 남은 금액 ..

        loansTemp = loans;
        RepaymentResultModel model;


        //월불입금
        //월불입금 계산공식 : 대출원금 × 이자율 ÷ 12 × (1 + 이자율 ÷ 12)^기간 ÷((1 + 이자율 ÷ 12)^기간 -1)
        //대출원금 × 이자율 ÷ 12 × (1 + 이자율 ÷ 12)^기간 :
//		double temp = mLoans * ( mInterestRate / 100 ) / 12 * Math.pow( ( 1 + ( mInterestRate / 100 ) / 12 ) , mTerm );
//		//((1 + 이자율 ÷ 12)^기간
//		double temp2 = Math.pow( ( 1 + ( mInterestRate / 100 ) / 12 ), mTerm);

        BigDecimal temp = loans.multiply( BigDecimal.valueOf(  ((double)interestRate / 100) ), MathContext.DECIMAL128 ).divide( BigDecimal.valueOf((double)12), 10, BigDecimal.ROUND_HALF_UP).multiply( BigDecimal.valueOf( Math.pow( ( (double)1 + ( (double) interestRate / 100 ) / (double)12 ) , term )), MathContext.DECIMAL128);
        BigDecimal temp2 = BigDecimal.valueOf( Math.pow( ( (double)1 + ( (double)interestRate / 100 ) / 12 ), term));
        BigDecimal repaymentResult = temp.divide( temp2.subtract(BigDecimal.valueOf(1) ), BigDecimal.ROUND_HALF_UP );

        for ( int i = 0; i <= term.intValue(); i++ ) {

            if ( i == 0 ) {

                model = new RepaymentResultModel();
                model.setRemainingAmount( loansTemp );
                repaymentResultList.add(model);
            } else {

                //이자. 잔액 / 12 * 할부금리
                BigDecimal interestResult =  loansTemp.divide(BigDecimal.valueOf(12), 10, BigDecimal.ROUND_HALF_UP).multiply( BigDecimal.valueOf( ((double) interestRate / 100 ) ), MathContext.DECIMAL128 );  //loansTemp / 12 * ( mInterestRate / 100 ) ;

                model = new RepaymentResultModel();
                model.setRepayments(  repaymentResult  ); //상환그,ㅁ

                model.setLoans(  repaymentResult.subtract( interestResult ) ); //납입원금

                model.setInterest(  interestResult ); //이자
                //잔금
                model.setRemainingAmount(  repaymentResultList.get(repaymentResultList.size()- 1).getRemainingAmount().subtract( model.getLoans() ) );
                //낸 원금
                model.setTotalLoans( model.getLoans().add( repaymentResultList.get(repaymentResultList.size()- 1).getTotalLoans()));

                loansTemp = model.getRemainingAmount();

                repaymentResultList.add(model);

                Log.d("", repaymentResult.toString());

            }
        }
        mRepaymentResultLists.add(repaymentResultList);
//		Log.d("", "RESULT : " + Util.toNumFormat((int) repaymentResult));
//		model.setReplayments(replayments);
    }

    /**
     * 2.만기일시
     */
    private void calculateRepaymentLastRepayment( final CalculationModel calculationModel ) {


        String loansText = calculationModel.getLoansText();
        String termText = calculationModel.getTermText();
        String interestRateText = calculationModel.getInterestRateText();

        BigDecimal loans = new BigDecimal(loansText );
        Double term =  Double.valueOf(termText);
        Float interestRate = Float.valueOf(interestRateText);


        ArrayList<RepaymentResultModel> repaymentResultList = new ArrayList<RepaymentResultModel>();

        // 1000000 * 5.9 / 100 * 24 / 12 / 24
        //월불입금
        //((1+B4)^B5*B4)/((1+B4)^B5-1)/12
        BigDecimal repaymentResult = new BigDecimal("0");
//		double repaymentResult = mLoans * ( Math.pow( ( 1 + ( mInterestRate / 100 ) ) , mTerm / 12 ) * ( mInterestRate / 100 ) )
//				/ ( Math.pow( ( 1 + ( mInterestRate / 100 ) ) , mTerm / 12 ) - 1 ) / 12 ;


        BigDecimal loansTemp; //원금 남은 금액 ..

        loansTemp = loans;
        RepaymentResultModel model;

        for ( int i = 0; i <=  term; i++ ) {

            if ( i == 0 ) {

                model = new RepaymentResultModel();
                model.setRemainingAmount( loansTemp );
                repaymentResultList.add(model);
            } else if ( i == term ) {

                //이자. 잔액 / 12 * 할부금리
                BigDecimal interestResult = loansTemp.multiply(BigDecimal.valueOf( (( interestRate / 100 ) / 12)), MathContext.DECIMAL128);

                model = new RepaymentResultModel();
                model.setRepayments( loans.add( interestResult ) ); //상환그,ㅁ
                model.setLoans(  loans ); //납입원금
                model.setInterest(  interestResult ); //이자
                model.setRemainingAmount( repaymentResult );
                model.setTotalLoans( loans );

                repaymentResultList.add(model);

            } else {

                //1000000 * 5.9 / 100 * 24 / 12 / 24
                BigDecimal interestResult = loansTemp.multiply(BigDecimal.valueOf( (( interestRate / 100 ) / 12)), MathContext.DECIMAL128);

                model = new RepaymentResultModel();
                model.setRepayments(  repaymentResult.add( interestResult ) ); //상환그,ㅁ
                model.setLoans(  repaymentResult ); //납입원금
                model.setInterest(  interestResult ); //이자
                model.setTotalLoans( repaymentResult );
                model.setRemainingAmount( loans );

                repaymentResultList.add(model);

            }
        }
        mRepaymentResultLists.add(repaymentResultList);
    }

    public ArrayList<RepaymentResultModel> getRepaymentSumList() {
        return mRepaymentSumList;
    }

    public CalculationModel getSum() {
        return mSum;
    }

    public ArrayList<ArrayList<RepaymentResultModel>> getRepaymentResultLists() {
        return mRepaymentResultLists;
    }

    public ArrayList<CalculationModel> getModelList() {
        return mModelList;
    }

    public void setmModelList(ArrayList<CalculationModel> mModelList) {
        this.mModelList = mModelList;
    }

    public void clear() {
        if ( mRepaymentSumList != null ) {
            this.mRepaymentSumList.clear();
        }
        if ( mRepaymentResultLists != null ) {
            this.mRepaymentResultLists.clear();
        }
        this.mSum = null;
    }
}
