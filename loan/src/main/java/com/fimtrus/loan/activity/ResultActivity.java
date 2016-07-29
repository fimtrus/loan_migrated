package com.fimtrus.loan.activity;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.ScaleGestureDetector;
import android.widget.EditText;
import android.widget.Spinner;

import com.fimtrus.loan.CommonApplication;
import com.fimtrus.loan.R;
import com.fimtrus.loan.adapter.ResultViewPagerAdapter;
import com.fimtrus.loan.fragment.ResultFragment;
import com.fimtrus.loan.model.CalculationModel;
import com.fimtrus.loan.model.Constant;
import com.fimtrus.loan.util.Calculator;
import com.fimtrus.loan.view.SlidingTabLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jhlibrary.util.CommonDialogs;

import java.util.ArrayList;

public class ResultActivity extends BaseActivity {

	private static final String TAG = ResultActivity.class.getSimpleName();

	private AdView mAdMobView;
	private ArrayList<CalculationModel> mModelList = null;
	private FragmentManager mFragmentManager;
	private ResultFragment mResultFragment;

	private ViewPager mViewPager;
	private ResultViewPagerAdapter mPagerAdapter;
	private SlidingTabLayout mSlidingTabLayout;
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


		mModelList = Calculator.getInstance().getModelList();

		mViewPager = (ViewPager) findViewById(R.id.viewpager_result);

		mSlidingTabLayout = (SlidingTabLayout ) findViewById(R.id.sliding_tab);

		mPagerAdapter = new ResultViewPagerAdapter(this, getSupportFragmentManager(), mViewPager);

		mViewPager.setAdapter(mPagerAdapter);
		mSlidingTabLayout.setDistributeEvenly(true);
		mSlidingTabLayout.setViewPager(mViewPager);

		mDialogs = new CommonDialogs(this);



//		CalculationModel c;
//		String log = "";
//		for ( int i = 0; i < mModelList.size(); i++ ) {
//			c = mModelList.get(i);
////            c = mCalculationList.get(i);
//			if ( c != null ) {
//
//				log +=  "index " + i
//						+ " / " + "repayment : " + c.getSelectRepayment()
//						+ " / " + "Loans : " + c.getLoansText()
//						+ " / " + "InterestRate : " + c.getInterestRateText()
//						+ " / " + "Term : " + c.getTermText() + "\n";
//
//			}
//
//		}
//		Log.d("LOAN", log);

	}

	private void initializeView() {
		// mSplashHandler.sendEmptyMessageDelayed(0, 2000);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdMobView.loadAd(adRequest);

		CommonApplication.getInstance().trackScreenView(TAG);

		new ResultTask().execute();
	}

	private void initializeFragments() {

		mFragmentManager = getFragmentManager();

//		mFragmentManager.beginTransaction()
//				.add(R.id.content_frame, mResultFragment, "result")
//				.commit();
//		 mFragmentManager.beginTransaction().add(R.id.fragment_splash,
//		 mSplashFragment, "splash").commit();
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

	private class ResultTask extends AsyncTask<Void, Void, Void > {

		private Dialog mProgressDialog;

		@Override
		protected Void doInBackground(Void... params) {

			Calculator.getInstance().calculate();

			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			mProgressDialog = mDialogs.showDialog(null);
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);

			initializeFields();

			mProgressDialog.dismiss();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
	}

	public void onFragmentCreated ( int index, boolean isSucceed ) {

	}
}
