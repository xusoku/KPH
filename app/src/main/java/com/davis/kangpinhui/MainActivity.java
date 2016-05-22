package com.davis.kangpinhui;import android.support.v4.app.Fragment;import android.support.v4.view.ViewPager;import android.view.View;import android.widget.TextView;import com.davis.kangpinhui.Model.UserInfo;import com.davis.kangpinhui.adapter.base.CommonFragmentAdapter;import com.davis.kangpinhui.activity.base.BaseActivity;import com.davis.kangpinhui.fragment.ClassicFragment;import com.davis.kangpinhui.fragment.IndexFragment;import com.davis.kangpinhui.fragment.MineFragment;import com.davis.kangpinhui.util.AppManager;import com.davis.kangpinhui.views.HackyViewPager;import com.davis.kangpinhui.views.viewpagerindicator.FixPageIndicator;import com.davis.kangpinhui.views.viewpagerindicator.PageIndicator;import com.davis.kangpinhui.views.viewpagerindicator.scrollbar.ColorBar;import java.util.ArrayList;import de.greenrobot.event.EventBus;public class MainActivity extends BaseActivity {    int[] tabDrawables = {R.drawable.main_btn_home_selector, R.drawable.main_btn_home_selector, R.drawable.main_btn_home_selector, R.drawable.main_btn_film_review_selector, R.drawable.main_btn_price_selector, R.drawable.main_btn_share_ticket_selector};    String[] tabNames = {"首页", "生鲜分类", "我的"};    HackyViewPager viewPager;    FixPageIndicator indicator;    private IndexFragment indexFragment;    private ClassicFragment classicFragment;    private MineFragment mineFragment;    @Override    protected int setLayoutView() {        return R.layout.activity_main;    }    @Override    protected void initVariable() {    }    @Override    protected void findViews() {        EventBus.getDefault().register(this);        setTranslucentStatusBar(R.color.colormain);        viewPager = $(R.id.viewPager);//        viewPager.toggleLock();        indicator = $(R.id.indicator);        viewPager.setOffscreenPageLimit(6);    }    @Override    protected void initData() {        mineFragment=new MineFragment();        classicFragment=new ClassicFragment();        indexFragment=new IndexFragment();        ArrayList<Fragment> fragments = new ArrayList<Fragment>();        fragments.add(indexFragment);        fragments.add(classicFragment);        fragments.add(mineFragment);        viewPager.setAdapter(new CommonFragmentAdapter(getSupportFragmentManager(), fragments));        indicator.setViewPager(viewPager, 0);        indicator.setIndicatorAdapter(new PageIndicator.IndicatorAdapter() {            @Override            public View getIndicatorView(int position) {                TextView textView = (TextView) getLayoutInflater().inflate(R.layout.layout_main_tab_item, null);                textView.setText(tabNames[position]);                textView.setCompoundDrawablesWithIntrinsicBounds(0, tabDrawables[position], 0, 0);                return textView;            }            @Override            public void onPageScrolled(View view, int position, float selectPercent) {            }        });        ColorBar colorBar = new ColorBar(mContext, getResources().getColor(R.color.colorAccent));        //        colorBar.setWidth(DimenUtils.dp2px(mContext, 80));        indicator.setScrollBar(colorBar);        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {            @Override            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {            }            @Override            public void onPageSelected(int position) {                if (position == 2) {//                    startActivity(new Intent(MainActivity.this,Main22Activity.class));                }            }            @Override            public void onPageScrollStateChanged(int state) {            }        });    }    public void change2() {        viewPager.setCurrentItem(1);    }    @Override    protected void setListener() {    }    @Override    public void doClick(View view) {    }    public void onEvent(UserInfo userInfo) {        if(mineFragment!=null){            mineFragment.setUi(userInfo);        }    }    @Override    protected void onDestroy() {        super.onDestroy();        EventBus.getDefault().unregister(this);        AppManager.getAppManager().AppExit(this);    }}