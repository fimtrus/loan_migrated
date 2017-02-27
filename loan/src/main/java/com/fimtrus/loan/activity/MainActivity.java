package com.fimtrus.loan.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.fimtrus.loan.CommonApplication;
import com.fimtrus.loan.R;
import com.fimtrus.loan.fragment.SearchFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


import java.util.HashMap;

public class MainActivity extends BaseActivity {

	private static final String TAG = MainActivity.class.getSimpleName();

	private FragmentManager mFragmentManager;
	private Handler mBackHandler;
	protected boolean flag;
	private SearchFragment mSearchFragment;
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

		AdRequest adRequest = new AdRequest.Builder().build();
		mAdMobView.loadAd(adRequest);
		CommonApplication.getInstance().trackScreenView(TAG);
	}

	private void initializeFragments() {

		mFragmentManager = getFragmentManager();

		mSearchFragment = new SearchFragment();
		mFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
		.add(R.id.content_frame, mSearchFragment, "search")
		.commit();
	}

	private void initializeListeners() {

	}

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
