package com.fimtrus.loan.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fimtrus on 16. 3. 18..
 */
public class CalculationModel implements Parcelable {

    private int    selectRepayment   = -1; //월상환금
    private String loansText         = new String(); //납입원금
    private String interestRateText  = new String(); //납입이자
    private String termText          = new String(); //기간
    private String hodlingPeriodText = new String(); //거치기간

    public CalculationModel() {
    }

    public String getHodlingPeriodText() {
        return hodlingPeriodText;
    }

    public void setHodlingPeriodText(String hodlingPeriodText) {
        this.hodlingPeriodText = hodlingPeriodText;
    }


    private boolean isSum = false;

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


    public boolean isSum() {
        return isSum;
    }

    public void setIsSum(boolean isSum) {
        this.isSum = isSum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.selectRepayment);
        dest.writeString(this.loansText);
        dest.writeString(this.interestRateText);
        dest.writeString(this.termText);
        dest.writeString(this.hodlingPeriodText);
        dest.writeByte(this.isSum ? (byte) 1 : (byte) 0);
    }

    protected CalculationModel(Parcel in) {
        this.selectRepayment = in.readInt();
        this.loansText = in.readString();
        this.interestRateText = in.readString();
        this.termText = in.readString();
        this.hodlingPeriodText = in.readString();
        this.isSum = in.readByte() != 0;
    }

    public static final Creator<CalculationModel> CREATOR = new Creator<CalculationModel>() {
        @Override
        public CalculationModel createFromParcel(Parcel source) {
            return new CalculationModel(source);
        }

        @Override
        public CalculationModel[] newArray(int size) {
            return new CalculationModel[size];
        }
    };
}
