package com.fimtrus.loan.util;

import android.content.Context;
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

    private Context mContext;

    private static Calculator mCalculator;

    private Calculator () {
        super();
    }

    public static ArrayList<RepaymentResultModel> calculate (CalculationModel c) {

        ArrayList<RepaymentResultModel> list = new ArrayList<>();
        int selectedRepayment = c.getSelectRepayment();

        switch ( selectedRepayment ) {
            //원금 균등
            case 0 :
                list = calculateRepaymentLoans( c );
                break;
            //원리금 균등
            case 1 :
                list = calculateRepaymentLoansAndInterest( c );
                break;
            case 2 :
                list = calculateRepaymentLastRepayment( c );
                break;
        }
        return list;
    }

    /**
     * 0.원금
     */
    private static ArrayList<RepaymentResultModel> calculateRepaymentLoans( final CalculationModel calculationModel ) {

        String loansText = calculationModel.getLoansText();
        String termText = calculationModel.getTermText();
        String interestRateText = calculationModel.getInterestRateText();
        String hodlingPeriodText = calculationModel.getHodlingPeriodText();

        BigDecimal loans = new BigDecimal(loansText );
        Double term =  Double.valueOf(termText);
        Double holdingPeriod =  Double.valueOf(hodlingPeriodText);
        Float interestRate = Float.valueOf(interestRateText);
        //월불입금
        //((1+B4)^B5*B4)/((1+B4)^B5-1)/12
        BigDecimal repaymentResult = loans.divide( BigDecimal.valueOf(term - holdingPeriod), 10, BigDecimal.ROUND_HALF_UP );
//		double repaymentResult = mLoans * ( Math.pow( ( 1 + ( mInterestRate / 100 ) ) , mTerm / 12 ) * ( mInterestRate / 100 ) )
//				/ ( Math.pow( ( 1 + ( mInterestRate / 100 ) ) , mTerm / 12 ) - 1 ) / 12 ;

        Log.d("", "원금 : " + repaymentResult);

        BigDecimal loansTemp; //원금 남은 금액 ..

        loansTemp = loans;//.setScale(2, BigDecimal.ROUND_HALF_UP);
        RepaymentResultModel model;
        //상환기간 + 거치기간
        int totalTerm = term.intValue();

        ArrayList<RepaymentResultModel> repaymentResultList = new ArrayList<RepaymentResultModel>();

        for ( int i = 0; i <= totalTerm; i++ ) {

            if ( i == 0 ) {

                model = new RepaymentResultModel();
                model.setRemainingAmount( new BigDecimal( String.valueOf(loansTemp ) ) );
                repaymentResultList.add(model);
            } else {

                //이자. 잔액 / 12 * 할부금리
                BigDecimal interestResult =  loansTemp.divide(BigDecimal.valueOf(12), BigDecimal.ROUND_HALF_UP).multiply( BigDecimal.valueOf( (interestRate / 100 ) ), MathContext.DECIMAL128 );  //loansTemp / 12 * ( mInterestRate / 100 ) ;

                model = new RepaymentResultModel();
                model.setInterest(  interestResult ); //이자

                if(i <= holdingPeriod.intValue()) {
                    model.setRepayments(  interestResult ); //상환금
                    model.setLoans( new BigDecimal(0) ); //납입원금
                    model.setRemainingAmount(  repaymentResultList.get(repaymentResultList.size()- 1).getRemainingAmount() );//잔금
                } else {
                    model.setRepayments(  repaymentResult.add(interestResult ) ); //상환금
                    model.setLoans(  repaymentResult ); //납입원금
                    model.setRemainingAmount(  repaymentResultList.get(repaymentResultList.size()- 1).getRemainingAmount().subtract(model.getLoans()) );//잔금
                }
                //낸 원금
                model.setTotalLoans( model.getLoans().add( repaymentResultList.get(repaymentResultList.size()- 1).getTotalLoans() ) );

                loansTemp = model.getRemainingAmount();

                repaymentResultList.add(model);

                Log.d("", "납입원금 : " + model.getLoans() + " 납입이자 : " + model.getInterest() + " 남은 원금 : " + model.getTotalLoans());

            }
        }
        return repaymentResultList;
    }

    /**
     * 1.원리금
     */
    private static ArrayList<RepaymentResultModel> calculateRepaymentLoansAndInterest( final CalculationModel calculationModel ) {


        String loansText = calculationModel.getLoansText();
        String termText = calculationModel.getTermText();
        String interestRateText = calculationModel.getInterestRateText();
        String holdingPeriodText = calculationModel.getHodlingPeriodText();

        BigDecimal loans = new BigDecimal(loansText );
        Double term =  Double.valueOf(termText);
        Double holdingPeriod = Double.valueOf(holdingPeriodText);
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

        BigDecimal temp = loans.multiply( BigDecimal.valueOf(  ((double)interestRate / 100) ), MathContext.DECIMAL128 ).divide( BigDecimal.valueOf((double)12), 10, BigDecimal.ROUND_HALF_UP).multiply( BigDecimal.valueOf( Math.pow( ( (double)1 + ( (double) interestRate / 100 ) / (double)12 ) , term - holdingPeriod )), MathContext.DECIMAL128);
        BigDecimal temp2 = BigDecimal.valueOf( Math.pow( ( (double)1 + ( (double)interestRate / 100 ) / 12 ), term - holdingPeriod));
        BigDecimal repaymentResult = temp.divide( temp2.subtract(BigDecimal.valueOf(1) ), BigDecimal.ROUND_HALF_UP );

        int totalTerm = term.intValue();

        for ( int i = 0; i <= totalTerm; i++ ) {

            if ( i == 0 ) {

                model = new RepaymentResultModel();
                model.setRemainingAmount( loansTemp );
                repaymentResultList.add(model);
            } else {

                //이자. 잔액 / 12 * 할부금리
                BigDecimal interestResult =  loansTemp.divide(BigDecimal.valueOf(12), 10, BigDecimal.ROUND_HALF_UP).multiply( BigDecimal.valueOf( ((double) interestRate / 100 ) ), MathContext.DECIMAL128 );  //loansTemp / 12 * ( mInterestRate / 100 ) ;

                model = new RepaymentResultModel();



                if(i <= holdingPeriod.intValue()) {
                    model.setRepayments(  interestResult ); //상환금
                    model.setLoans( new BigDecimal(0) ); //납입원금
                    model.setRemainingAmount(  repaymentResultList.get(repaymentResultList.size()- 1).getRemainingAmount() );//잔금
                } else {
                    model.setRepayments(  repaymentResult  ); //상환그,ㅁ
                    model.setLoans(  repaymentResult.subtract( interestResult ) ); //납입원금
                    model.setRemainingAmount(  repaymentResultList.get(repaymentResultList.size()- 1).getRemainingAmount().subtract( model.getLoans() ) );//잔금
                }

                model.setInterest(  interestResult ); //이자
                //낸 원금
                model.setTotalLoans( model.getLoans().add( repaymentResultList.get(repaymentResultList.size()- 1).getTotalLoans()));

                loansTemp = model.getRemainingAmount();

                repaymentResultList.add(model);

                Log.d("", repaymentResult.toString());

            }
        }
        return repaymentResultList;
    }

    /**
     * 2.만기일시
     */
    private static ArrayList<RepaymentResultModel> calculateRepaymentLastRepayment( final CalculationModel calculationModel ) {


        String loansText = calculationModel.getLoansText();
        String termText = calculationModel.getTermText();
        String interestRateText = calculationModel.getInterestRateText();
        String holdingPeriodText = calculationModel.getHodlingPeriodText();

        BigDecimal loans = new BigDecimal(loansText );
        Double term =  Double.valueOf(termText);
        Double hodlingPeriod =  Double.valueOf(holdingPeriodText);
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

        int totalTerm = term.intValue();

        for ( int i = 0; i <=  totalTerm; i++ ) {

            if ( i == 0 ) {

                model = new RepaymentResultModel();
                model.setRemainingAmount( loansTemp );
                repaymentResultList.add(model);
            } else if ( i == totalTerm ) {

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
        return repaymentResultList;
    }

}
