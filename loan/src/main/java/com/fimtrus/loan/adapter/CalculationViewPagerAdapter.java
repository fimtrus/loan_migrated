package com.fimtrus.loan.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fimtrus.loan.R;
import com.fimtrus.loan.model.CalculationModel;

import java.util.ArrayList;

/**
 * Created by fimtrus on 16. 3. 16..
 */
public class CalculationViewPagerAdapter extends PagerAdapter {

    private final int CALCULATION_VIEW = R.layout.view_loan_search;
    private final LayoutInflater mInflator;

    private Context mContext = null;
    private ArrayList<CalculationModel> mModelList = null;
//    private int mCount = 3;
    public CalculationViewPagerAdapter(Context context, ArrayList<CalculationModel> list) {

        super();
        this.mContext = context;
        this.mModelList = list;
        this.mInflator = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {

        int count = 0;

        if ( mModelList != null ) {
            count = mModelList.size();
        }

        return count;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = mInflator.inflate(CALCULATION_VIEW, null);
        container.addView(view);

//        super.instantiateItem(container, position);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        //true 일 경우 리스트에 화면이 없는 것이기 때문에... 삭제
//        boolean doRemove = false;

        View view = (View) object;

//        container.removeAllViews();
//        for ( View c : mModelList) {
//            container.addView(c);
//        }


//        if ( mModelList != null ) {
//
//            //담겨 있으면 삭제할 리스트가 아니기 때문에..
//            doRemove = !mModelList.contains(view);
//
//            if ( doRemove ) {
        container.removeView(view);
//                mModelList.remove(view);
//            }
//        }
//        super.destroyItem(container, position, object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {

//        int index;
//
//        if ( mModelList != null ) {
//
//            index = mModelList.indexOf(object);
//
//            return index;
//        } else {

            return POSITION_NONE;
//        }
//        return super.getItemPosition(object);

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return " " + position + " ";
    }

//    public void setViewCount ( int count ) {
//        this.mCount = count;
//    }
//    public int getViewCount() {
//        return this.mCount;
//    }
}
