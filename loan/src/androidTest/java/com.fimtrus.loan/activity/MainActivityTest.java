package com.fimtrus.loan.activity;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.fimtrus.loan.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mContext;

//    public MainActivityTest(Class<MainActivity> activityClass) throws InitializationError {
//        super(activityClass);
//
//    }

    public MainActivityTest() {
        super(MainActivity.class);
    }


    @Before
    public void setUp() throws Exception {

        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mContext = getActivity();
//        setActivityInitialTouchMode(false);
        Log.d("Test", "CONTRUCTOR");


    }
    @Test
    public void testAppName() throws Exception {
        String appName = "1";
        assertEquals(appName, "21");
    }
    @Test
    public void testName() throws Exception {
        Log.d("Test", "/Users/fimtrus/project_android/loan");
        String appName = "1";
        assertEquals(appName, "1");
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }
}