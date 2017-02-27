package com.fimtrus.loan.activity;

import android.os.Bundle;
import android.view.Menu;

import com.fimtrus.loan.CommonApplication;
import com.fimtrus.loan.R;
import com.fimtrus.loan.fragment.ResultFragment;
import com.fimtrus.loan.model.CalculationModel;
import com.fimtrus.loan.model.Constant;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jhlibrary.util.CommonDialogs;


public class ResultActivity extends BaseActivity {

	private static final String TAG = ResultActivity.class.getSimpleName();

	private AdView mAdMobView;
	private android.support.v4.app.FragmentManager mFragmentManager;
	private ResultFragment mResultFragment;
	private CommonDialogs mDialogs;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		initialize();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void initialize() {

		initializeFragments();
		initializeFields();
		initializeListeners();
		initializeView();

	}

	private void initializeFields() {

		mAdMobView = (AdView) findViewById(R.id.adView);

		mDialogs = new CommonDialogs(this);
	}

	private void initializeView() {

		AdRequest adRequest = new AdRequest.Builder().build();
		mAdMobView.loadAd(adRequest);

		CommonApplication.getInstance().trackScreenView(TAG);

	}

	private void initializeFragments() {

		mFragmentManager = getSupportFragmentManager();
		CalculationModel c = getIntent().getParcelableExtra(Constant.EXTRA_CALCULATION_MODEL);
		mResultFragment = ResultFragment.newInstance(c);
		mFragmentManager.beginTransaction()
				.add(R.id.content_frame, mResultFragment, "result")
				.commit();
	}

	private void initializeListeners() {

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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.calculation, menu);
		return true;
	}

	public void onFragmentCreated ( int index, boolean isSucceed ) {

	}
}
