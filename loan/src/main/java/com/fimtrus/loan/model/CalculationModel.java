package com.fimtrus.loan.model;

import android.os.Bundle;

import java.math.BigDecimal;

/**
 * Created by fimtrus on 16. 3. 18..
 */
public class CalculationModel {

    private int selectRepayment = -1; //월상환금
    private String loansText = new String(); //납입원금
    private String interestRateText = new String(); //납입이자
    private String termText = new String(); //기간

    public static final CalculationModel newInstance() {

        CalculationModel model = new CalculationModel();
        return model;
    }

    public String getInterestRateText() {
        return interestRateText;
    }

    public void setInterestRateText(String interestRateText) {
        this.interestRateText = interestRateText;
    }

    public String getLoansText() {
        return loansText;
    }

    public void setLoansText(String loansText) {
        this.loansText = loansText;
    }

    public int getSelectRepayment() {
        return selectRepayment;
    }

    public void setSelectRepayment(int selectRepayment) {
        this.selectRepayment = selectRepayment;
    }

    public String getTermText() {
        return termText;
    }

    public void setTermText(String termText) {
        this.termText = termText;
    }
}
