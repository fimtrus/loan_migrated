package com.fimtrus.loan.util;

import java.text.DecimalFormat;
import java.util.Locale;

import android.content.Context;

public class Util {
	public static String toNumFormat(double num) {
		
		if ( num == 0 ) {
			return "";
		}
		
		DecimalFormat df = new DecimalFormat("#,###");
		return df.format(num);
	}
	public static String toNumFormat(String num) {
		DecimalFormat df = new DecimalFormat("#,###");
		return df.format( Double.valueOf( num ) );
	}
	public static String convertNumberToKorean(String amt){
		
		if ( amt == null || amt.equals("")) {
			return "";
		}
		
		String tmpamt = "";
		amt = "000000000000000" + amt;
		int j = 0;
		for (int i = amt.length(); i > 0; i--) {
			j++;
			if (!amt.substring(i - 1, i).equals("0")) {
				if (j % 4 == 2)
					tmpamt = "십" + tmpamt;
				if (j % 4 == 3)
					tmpamt = "백" + tmpamt;
				if (j > 1 && (j % 4) == 0)
					tmpamt = "천" + tmpamt;
			}
			if (j == 5
					&& Integer.parseInt(amt.substring(amt.length() - 8,
							amt.length() - 4)) > 0)
				tmpamt = "만 " + tmpamt;
			if (j == 9
					&& Integer.parseInt(amt.substring(amt.length() - 12,
							amt.length() - 8)) > 0)
				tmpamt = "억 " + tmpamt;
			if (j == 13
					&& Integer.parseInt(amt.substring(amt.length() - 16,
							amt.length() - 12)) > 0)
				tmpamt = "조 " + tmpamt;
			if (amt.substring(i - 1, i).equals("1"))
				tmpamt = "일" + tmpamt;
			if (amt.substring(i - 1, i).equals("2"))
				tmpamt = "이" + tmpamt;
			if (amt.substring(i - 1, i).equals("3"))
				tmpamt = "삼" + tmpamt;
			if (amt.substring(i - 1, i).equals("4"))
				tmpamt = "사" + tmpamt;
			if (amt.substring(i - 1, i).equals("5"))
				tmpamt = "오" + tmpamt;
			if (amt.substring(i - 1, i).equals("6"))
				tmpamt = "육" + tmpamt;
			if (amt.substring(i - 1, i).equals("7"))
				tmpamt = "칠" + tmpamt;
			if (amt.substring(i - 1, i).equals("8"))
				tmpamt = "팔" + tmpamt;
			if (amt.substring(i - 1, i).equals("9"))
				tmpamt = "구" + tmpamt;
		}

//		tmpamt = tmpamt + " 원";
		return tmpamt;
	}
	
//	public static String getLocale(Context context) {
//		Locale locale = context.getResources().getConfiguration().locale;
//		
//		locale.getD
//	}
}
