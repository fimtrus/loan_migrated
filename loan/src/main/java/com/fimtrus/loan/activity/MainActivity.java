package com.fimtrus.loan.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.fimtrus.loan.CommonApplication;
import com.fimtrus.loan.R;
import com.fimtrus.loan.fragment.ResultFragment;
import com.fimtrus.loan.fragment.SearchFragment;
import com.fimtrus.loan.util.CalculationViewHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


import java.util.HashMap;

public class MainActivity extends BaseActivity {

	private static final String TAG = MainActivity.class.getSimpleName();

	private FragmentManager mFragmentManager;
	private Handler mBackHandler;
	protected boolean flag;
	private SearchFragment mSearchFragment;
	private ResultFragment mResultFragment;
	private AdView mAdMobView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initialize();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mSearchFragment = null;
	}

	private void initialize() {

		initializeFragments();
		initializeFields();
		initializeListeners();
		initializeView();
	}

	private void initializeFields() {

		CalculationViewHelper.clearField();

		mAdMobView = (AdView) findViewById(R.id.adView);

		mBackHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0) {
					flag = false;
				}
			}
		};
	}

	private void initializeView() {
		// mSplashHandler.sendEmptyMessageDelayed(0, 2000);

		AdRequest adRequest = new AdRequest.Builder().build();
		mAdMobView.loadAd(adRequest);
		CommonApplication.getInstance().trackScreenView(TAG);
	}

	private void initializeFragments() {
		// Utils.setPreference(Key.INTIALIZED_HELP, false);

		mFragmentManager = getFragmentManager();

		mSearchFragment = new SearchFragment();
//		mResultFragment = new ResultFragment();
//		mSplashFragment = new SplashFragment();
//
//		mFragmentManager.beginTransaction()
//				.add(R.id.content_frame, mResultFragment, "result")
//				.commit();
		mFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
		.add(R.id.content_frame, mSearchFragment, "search")
		.commit();
//		 mFragmentManager.beginTransaction().add(R.id.fragment_splash,
//		 mSplashFragment, "splash").commit();
	}

	private void initializeListeners() {

	}
//	@Override
//	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//
//
//	}
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.calculation, menu);
//		getMenuInflater().inflate(R.menu.calculation, menu);
//		return true;
//	}

	@Override
	public void onBackPressed() {

		if ( mFragmentManager.getBackStackEntryCount() > 0 ) {

			super.onBackPressed();
		} else {
			if (!flag) {
				Toast.makeText(this, R.string.exit_text, Toast.LENGTH_SHORT).show();
				flag = true;
				mBackHandler.sendEmptyMessageDelayed(0, 3000);
			} else {
				super.onBackPressed();
			}
		}

	}

	@Override
	protected void onPause() {
		if (mAdMobView != null) {
			mAdMobView.pause();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (mAdMobView != null) {
			mAdMobView.resume();
		}
		super.onResume();
	}

}
