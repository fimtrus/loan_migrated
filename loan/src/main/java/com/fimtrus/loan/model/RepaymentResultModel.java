package com.fimtrus.loan.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class RepaymentResultModel implements Parcelable {
	
	private BigDecimal repayments = new BigDecimal("0"); //월상환금
	private BigDecimal loans = new BigDecimal("0"); //납입원금
	private BigDecimal interest = new BigDecimal("0"); //납입이자
	private BigDecimal remainingAmount = new BigDecimal("0"); //갚은금액
	private BigDecimal totalLoans = new BigDecimal("0"); //남은 원금

	private boolean isLast = false;

	public static RepaymentResultModel newInstance() {

		RepaymentResultModel model = new RepaymentResultModel();
		return model;
	}

	public static RepaymentResultModel newInstance(RepaymentResultModel _model) {


		RepaymentResultModel model = new RepaymentResultModel();

		model.setTotalLoans(_model.getTotalLoans());
		model.setLoans(_model.getLoans());
		model.setRemainingAmount(_model.getRemainingAmount());
		model.setInterest(_model.getInterest());
		model.setRepayments(_model.getRepayments());

		return model;
	}

	public BigDecimal getRepayments() {
		return repayments;
	}
	public void setRepayments(BigDecimal repayments) {
		this.repayments = repayments;
	}
	public BigDecimal getLoans() {
		return loans;
	}
	public void setLoans(BigDecimal loans) {
		this.loans = loans;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public BigDecimal getRemainingAmount() {
		return remainingAmount;
	}
	public void setRemainingAmount(BigDecimal remainingAmount) {
		this.remainingAmount = remainingAmount;
	}
	public BigDecimal getTotalLoans() {
		return totalLoans;
	}
	public void setTotalLoans(BigDecimal totalLoans) {
		this.totalLoans = totalLoans;
	}

	public RepaymentResultModel() {
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeSerializable(this.repayments);
		dest.writeSerializable(this.loans);
		dest.writeSerializable(this.interest);
		dest.writeSerializable(this.remainingAmount);
		dest.writeSerializable(this.totalLoans);
		dest.writeByte(isLast ? (byte) 1 : (byte) 0);
	}

	protected RepaymentResultModel(Parcel in) {
		this.repayments = (BigDecimal) in.readSerializable();
		this.loans = (BigDecimal) in.readSerializable();
		this.interest = (BigDecimal) in.readSerializable();
		this.remainingAmount = (BigDecimal) in.readSerializable();
		this.totalLoans = (BigDecimal) in.readSerializable();
		this.isLast = in.readByte() != 0;
	}

	public static final Creator<RepaymentResultModel> CREATOR = new Creator<RepaymentResultModel>() {
		@Override
		public RepaymentResultModel createFromParcel(Parcel source) {
			return new RepaymentResultModel(source);
		}

		@Override
		public RepaymentResultModel[] newArray(int size) {
			return new RepaymentResultModel[size];
		}
	};

	public boolean isLast() {
		return isLast;
	}

	public void setIsLast(boolean isLast) {
		this.isLast = isLast;
	}
}
