package com.davis.kangpinhui;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.davis.kangpinhui.adapter.CommonFragmentAdapter;
import com.davis.kangpinhui.activity.base.BaseActivity;
import com.davis.kangpinhui.fragment.SampleFragment;
import com.davis.kangpinhui.views.viewpagerindicator.FixPageIndicator;
import com.davis.kangpinhui.views.viewpagerindicator.PageIndicator;
import com.davis.kangpinhui.views.viewpagerindicator.scrollbar.ColorBar;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    int []  tabDrawables = {R.drawable.main_btn_home_selector, R.drawable.main_btn_home_selector, R.drawable.main_btn_home_selector, R.drawable.main_btn_film_review_selector, R.drawable.main_btn_price_selector, R.drawable.main_btn_share_ticket_selector};
    String [] tabNames = {"性感", "韩日", "丝袜", "写真", "清纯", "车模"};


    ViewPager viewPager;
    FixPageIndicator indicator;
    @Override
    protected int setLayoutView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initVariable() {

    }


    @Override
    protected void findViews() {


        showTopBar();


        setTitle("主页");
        getTitleView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        getLeftButton().setImageResource(R.drawable.ic_favorites);
        getRightButton().setImageResource(R.drawable.ic_favorites);


        viewPager=$(R.id.viewPager);
        indicator=$(R.id.indicator);
        viewPager.setOffscreenPageLimit(6);



    }

    @Override
    protected void initData() {
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new SampleFragment());
        fragments.add(new SampleFragment());
        fragments.add(new SampleFragment());

        viewPager.setAdapter(new CommonFragmentAdapter(getSupportFragmentManager(), fragments));
        indicator.setViewPager(viewPager, 0);


        indicator.setIndicatorAdapter(new PageIndicator.IndicatorAdapter() {
            @Override
            public View getIndicatorView(int position) {
                TextView textView = (TextView) getLayoutInflater().inflate(R.layout.layout_main_tab_item, null);
                textView.setText(tabNames[position]);
                textView.setCompoundDrawablesWithIntrinsicBounds(0, tabDrawables[position], 0, 0);
                return textView;
            }

            @Override
            public void onPageScrolled(View view, int position, float selectPercent) {

            }
        });
        ColorBar colorBar =new ColorBar(mContext,getResources().getColor(R.color.colorAccent));
        //        colorBar.setWidth(DimenUtils.dp2px(mContext, 80));

        indicator.setScrollBar(colorBar);


        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(position==2){
//                    startActivity(new Intent(MainActivity.this,Main22Activity.class));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void doClick(View view) {

    }
}

