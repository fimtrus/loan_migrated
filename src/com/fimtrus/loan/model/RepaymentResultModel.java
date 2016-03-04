package com.fimtrus.loan.model;

import java.math.BigDecimal;

public class RepaymentResultModel {
	
	private BigDecimal repayments = new BigDecimal("0"); //월상환금
	private BigDecimal loans = new BigDecimal("0"); //납입원금
	private BigDecimal interest = new BigDecimal("0"); //납입이자
	private BigDecimal remainingAmount = new BigDecimal("0"); //갚은금액
	private BigDecimal totalLoans = new BigDecimal("0"); //남은 원금
	
	
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
}
