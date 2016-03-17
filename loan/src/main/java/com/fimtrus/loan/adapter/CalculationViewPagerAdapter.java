package com.fimtrus.loan.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by fimtrus on 16. 3. 16..
 */
public class CalculationViewPagerAdapter extends PagerAdapter {

    private Context mContext = null;
    private ArrayList<View> mViewList = null;

    public CalculationViewPagerAdapter(Context context, ArrayList<View> list) {

        super();
        this.mContext = context;
        this.mViewList = list;
    }

    @Override
    public int getCount() {

        int count = 0;

        if ( mViewList != null ) {
            count = mViewList.size();
        }

        return count;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = null;
        if ( mViewList != null ) {

            view = mViewList.get(position);
            container.addView(mViewList.get(position) );

        }
//        super.instantiateItem(container, position);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        View view = null;
        if ( mViewList != null ) {

            view = mViewList.get(position);
            container.removeView(mViewList.get(position));

        }

//        super.destroyItem(container, position, object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {

        int index = -1;

        if ( mViewList != null ) {

            index = mViewList.indexOf(object);

        }
//        return super.getItemPosition(object);
        return index;
    }
}
