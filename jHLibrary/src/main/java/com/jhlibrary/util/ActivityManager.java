package com.jhlibrary.util;

import android.app.Activity;

import java.util.ArrayList;

/**
 * ActivityManager.java
 *
 * 현재 패키지 내에 생성되어 있는 액티비티를 관리.<br>
 *
 * @auther jong-hyun.jeong
 * @date 2013. 9. 10.
 */
public class ActivityManager {
	private static ActivityManager activityMananger = null;
	private ArrayList<Activity> activityList = null;

	private ActivityManager() {
		activityList = new ArrayList<Activity>();
	}

	public static ActivityManager getInstance() {

		if (ActivityManager.activityMananger == null) {
			activityMananger = new ActivityManager();
		}
		return activityMananger;
	}

	/**
	 * 액티비티 리스트 getter.
	 *
	 * @return activityList
	 */
	public ArrayList<Activity> getActivityList() {
		return activityList;
	}

	/**
	 * 액티비티 리스트에 추가.
	 *
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	/**
	 * 액티비티 리스트에서 삭제.
	 *
	 * @param activity
	 * @return boolean
	 */
	public boolean removeActivity(Activity activity) {
		return activityList.remove(activity);
	}

	/**
	 * 모든 액티비티 종료.
	 */
	public void finishAllActivity() {
		for (Activity activity : activityList) {
			activity.finish();
		}
	}
}
