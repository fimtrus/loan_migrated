package com.fimtrus.loan.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fimtrus.loan.R;
import com.fimtrus.loan.model.CalculationModel;
import com.fimtrus.loan.util.Util;
import com.fimtrus.loan.view.WrapViewPager;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by fimtrus on 16. 3. 16..
 */
public class CalculationViewPagerAdapter extends PagerAdapter {

    private final int CALCULATION_VIEW = R.layout.view_loan_search;
    private final LayoutInflater mInflator;

    private Context mContext = null;
    private ArrayList<CalculationModel> mModelList = null;
    private Button mCalculationButton;
    private WrapViewPager mViewPager;
//    private int mCount = 5;

    private ArrayList<View> mViewList;
    public CalculationViewPagerAdapter(Context context, WrapViewPager viewPager, ArrayList<CalculationModel> list, Button calculationButton) {

        super();
        this.mContext = context;
        this.mModelList = list;
        this.mInflator = LayoutInflater.from(mContext);
        this.mCalculationButton = calculationButton;
        this.mViewPager = viewPager;
        this.mViewList = new ArrayList<View>();

        this.mViewList.add(mInflator.inflate(CALCULATION_VIEW, null));
        this.mViewList.add(mInflator.inflate(CALCULATION_VIEW, null));
        this.mViewList.add(mInflator.inflate(CALCULATION_VIEW, null));
        this.mViewList.add(mInflator.inflate(CALCULATION_VIEW, null));
        this.mViewList.add(mInflator.inflate(CALCULATION_VIEW, null));

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



        View view = this.mViewList.get(position);

        final EditText termEditText = (EditText) view.findViewById(R.id.edittext_term);
        final TextView loanTextView = (TextView) view.findViewById(R.id.textview_loans_text);
        final TextView loanNumberTextView = (TextView) view.findViewById(R.id.textview_loans_number);
        final EditText loansEditText = (EditText) view.findViewById(R.id.edittext_loans);

        Locale locale = Locale.getDefault();

        if ( locale.getLanguage().contains("ko") ) {

            loansEditText.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before,
										  int count) {

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
											  int after) {

				}

				@Override
				public void afterTextChanged(Editable s) {
					String text = s.toString();

					if (text == null || text.equals("")) {
						loanTextView.setText("");
						loanNumberTextView.setText("");
						return;
					}

					loanTextView.setText(Util.convertNumberToKorean(text) + " 원");
					loanNumberTextView.setText(Util.toNumFormat(text) + "원");
				}
			});
		} else {
            loanTextView.setVisibility(View.GONE);
            loanNumberTextView.setVisibility(View.GONE);
        }

        termEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (v.getId() == R.id.edittext_term ) { // 뷰의 id를 식별, 키보드의
                    // 완료 키 입력 검출

                    if ( actionId == EditorInfo.IME_ACTION_DONE ) {
                        mCalculationButton.performClick();
                    } else if ( actionId == EditorInfo.IME_ACTION_NEXT ) {
                        int currentIndex = mViewPager.getCurrentItem();
                        int maxIndex = mViewPager.getChildCount() - 1;
                        int nextIndex = currentIndex + 1;

                        if ( nextIndex <= maxIndex ) {
                            mViewPager.setCurrentItem( nextIndex, true ) ;
//                            v.setNextFocusDownId();
                        }

                    }
                }
                return false;
            }
        });

        int index = container.indexOfChild(view);

        if ( index > -1 ) {

        } else {
            container.addView(view);
        }


//        super.instantiateItem(container, position);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        View view = (View) object;

        container.removeView(view);
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

    public ArrayList<View> getViewList() {
        return mViewList;
    }
}
