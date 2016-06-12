package com.davis.kangpinhui;import android.support.v4.app.Fragment;import android.support.v4.view.ViewPager;import android.text.TextUtils;import android.view.KeyEvent;import android.view.View;import android.widget.TextView;import com.davis.kangpinhui.activity.base.BaseActivity;import com.davis.kangpinhui.adapter.base.CommonFragmentAdapter;import com.davis.kangpinhui.api.ApiCallback;import com.davis.kangpinhui.api.ApiInstant;import com.davis.kangpinhui.fragment.ClassicFragment;import com.davis.kangpinhui.fragment.IndexFragment;import com.davis.kangpinhui.fragment.MineFragment;import com.davis.kangpinhui.model.Address;import com.davis.kangpinhui.model.Extendedinfo;import com.davis.kangpinhui.model.Shop;import com.davis.kangpinhui.model.UserInfo;import com.davis.kangpinhui.model.basemodel.BaseModel;import com.davis.kangpinhui.util.AppManager;import com.davis.kangpinhui.util.LocalUtil;import com.davis.kangpinhui.util.SharePreferenceUtils;import com.davis.kangpinhui.util.ToastUitl;import com.davis.kangpinhui.views.HackyViewPager;import com.davis.kangpinhui.views.viewpagerindicator.FixPageIndicator;import com.davis.kangpinhui.views.viewpagerindicator.PageIndicator;import com.davis.kangpinhui.views.viewpagerindicator.scrollbar.ColorBar;import java.util.ArrayList;import de.greenrobot.event.EventBus;import retrofit2.Call;public class MainActivity extends BaseActivity {    int[] tabDrawables = {R.drawable.main_btn_home_selector, R.drawable.main_btn_classic_selector, R.drawable.main_btn_mine_selector};    String[] tabNames = {"首页", "生鲜分类", "我的"};    HackyViewPager viewPager;    FixPageIndicator indicator;    private IndexFragment indexFragment;    private ClassicFragment classicFragment;    private MineFragment mineFragment;    @Override    protected int setLayoutView() {        return R.layout.activity_main;    }    @Override    protected void initVariable() {    }    @Override    protected void findViews() {        EventBus.getDefault().register(this);        setTranslucentStatusBar(R.color.colormain);        viewPager = $(R.id.viewPager);//        viewPager.toggleLock();        indicator = $(R.id.indicator);        viewPager.setOffscreenPageLimit(6);    }    @Override    protected void initData() {        mineFragment = new MineFragment();        classicFragment = new ClassicFragment();        indexFragment = new IndexFragment();        ArrayList<Fragment> fragments = new ArrayList<Fragment>();        fragments.add(indexFragment);        fragments.add(classicFragment);        fragments.add(mineFragment);        viewPager.setAdapter(new CommonFragmentAdapter(getSupportFragmentManager(), fragments));        indicator.setViewPager(viewPager, 0);        indicator.setIndicatorAdapter(new PageIndicator.IndicatorAdapter() {            @Override            public View getIndicatorView(int position) {                TextView textView = (TextView) getLayoutInflater().inflate(R.layout.layout_main_tab_item, null);                textView.setText(tabNames[position]);//                textView.setTextSize(CommonManager.dpToPx(20));                textView.setCompoundDrawablesWithIntrinsicBounds(0, tabDrawables[position], 0, 0);                return textView;            }            @Override            public void onPageScrolled(View view, int position, float selectPercent) {            }        });        ColorBar colorBar = new ColorBar(mContext, getResources().getColor(R.color.colorAccent));        indicator.setScrollBar(colorBar);        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {            @Override            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {            }            @Override            public void onPageSelected(int position) {                if (position == 2) {                    EventBus.getDefault().post(new Extendedinfo());                }            }            @Override            public void onPageScrollStateChanged(int state) {            }        });        if(!TextUtils.isEmpty(AppApplication.token)){            setUIOrder();        }        if(TextUtils.isEmpty(SharePreferenceUtils.getSharedPreferences().getString("shopid", ""))){            getShopid();        }    }    public void change2() {        viewPager.setCurrentItem(1);    }    @Override    protected void setListener() {    }    @Override    public void doClick(View view) {        }    private void getShopid() {        Call<BaseModel<ArrayList<Shop>>> call= ApiInstant.getInstant().getShoplist(AppApplication.apptype);        call.enqueue(new ApiCallback<BaseModel<ArrayList<Shop>>>() {            @Override            public void onSucssce(BaseModel<ArrayList<Shop>> arrayListBaseModel) {                AppApplication.shoplist.addAll(arrayListBaseModel.object);                LocalUtil util=new LocalUtil(false);                util.startLocation();            }            @Override            public void onFailure() {            }        });    }    public void onEvent(UserInfo userInfo) {        if(mineFragment!=null){            mineFragment.setUi(userInfo);        }    }    public void onEvent(Extendedinfo extendedinfo) {        setUIOrder();    }    public void onEvent(Address address) {       String add= SharePreferenceUtils.getSharedPreferences().getString("address","选择配送地址");        if(indexFragment!=null){            indexFragment.setindex_local_select(add);        }        if(classicFragment!=null){            if(classicFragment.getInit()) {                classicFragment.iniData();            }        }    }    public void onEvent(String  loginout) {        if(mineFragment!=null){            mineFragment.setLoginout();        }        if(indexFragment!=null){            indexFragment.setcartNumberLoginout();        }        viewPager.setCurrentItem(0);    }    public  void setUIOrder(){        Call<BaseModel<Extendedinfo>> call= ApiInstant.getInstant().getExtendedInfo(AppApplication.apptype,                AppApplication.shopid,AppApplication.token);        call.enqueue(new ApiCallback<BaseModel<Extendedinfo>>() {            @Override            public void onSucssce(BaseModel<Extendedinfo> extendedinfoBaseModel) {                AppApplication.extendedinfo = extendedinfoBaseModel.object;                if (indexFragment != null) {                    indexFragment.setcartNumber();                }                if (mineFragment != null) {                    mineFragment.setNumber();                }            }            @Override            public void onFailure() {            }        });    }    private long exitTime = 0;    @Override    public boolean onKeyDown(int keyCode, KeyEvent event)    {        if (keyCode == KeyEvent.KEYCODE_BACK) {            if(viewPager.getCurrentItem()!=0){                viewPager.setCurrentItem(0);                return true;            }            if ((System.currentTimeMillis() - exitTime) > 2000) {               ToastUitl.showToast("再按一次退出程序");                exitTime = System.currentTimeMillis();            }            else {                finish();            }            return true; // 返回true表示执行结束不需继续执行父类按键响应        }        return super.onKeyDown(keyCode, event);    }    @Override    protected void onDestroy() {        super.onDestroy();        EventBus.getDefault().unregister(this);        AppManager.getAppManager().AppExit(this);    }}