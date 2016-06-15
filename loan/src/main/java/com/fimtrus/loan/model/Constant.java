package com.fimtrus.loan.model;

import android.os.Environment;

import java.io.File;

public class Constant {
	public static final String EXTRA_LOANS = "extra_loans";
	public static final String EXTRA_INTEREST_RATE = "extra_interest_rate";
	public static final String EXTRA_TERM = "extra_term";
	public static final String EXTRA_SELECTED_INDEX = "extra_selected_index";
	public static final String EXTRA_CALCULATION_MODEL = "extra_calculation_model";
	public static final String EXTRA_RELAYMENT_RESULT_MODEL = "extra_repayment_result_model";


	public static final String DIRECTORY_PATH = Environment.getExternalStorageDirectory() + File.separator + "loan";


	public enum TrackerName {
		APP_TRACKER,           // 앱 별로 트래킹
		GLOBAL_TRACKER,        // 모든 앱을 통틀어 트래킹
		ECOMMERCE_TRACKER,     // 아마 유료 결재 트래킹 개념 같음
	}
}
