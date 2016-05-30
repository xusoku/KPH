package com.davis.kangpinhui.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.model.Shop;
import com.davis.kangpinhui.model.basemodel.BaseModel;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;
import com.davis.kangpinhui.adapter.base.CommonBaseAdapter;
import com.davis.kangpinhui.adapter.base.ViewHolder;
import com.davis.kangpinhui.api.ApiCallback;
import com.davis.kangpinhui.api.ApiInstant;
import com.davis.kangpinhui.util.CommonManager;
import com.davis.kangpinhui.util.LocalUtil;
import com.davis.kangpinhui.util.ToastUitl;
import com.davis.kangpinhui.views.LoadMoreListView;
import com.davis.kangpinhui.views.MySwipeRefreshLayout;

import java.util.ArrayList;

import retrofit2.Call;



public class ShopActivity extends BaseActivity implements View.OnClickListener, AMapLocationListener {

    private LoadMoreListView listView;
    private  CommonBaseAdapter adapter;
    private MySwipeRefreshLayout shop_swiperefresh;
    private LinearLayout shop_location_headerid;
    private View view;
    private ArrayList<Shop> list;


    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    @Override
    protected int setLayoutView() {
        return R.layout.activity_shop;
    }

    @Override
    protected void initVariable() {
        list=new ArrayList<>();
    }

    @Override
    protected void findViews() {
        setTranslucentStatusBar(R.color.colormain);
        showTopBar();
        getRightTextButton().setText("查看商区");
        setTitle("选择商区");
        listView=$(R.id.shop_list);
         view=getLayoutInflater().inflate(R.layout.activity_shop_item_header, null);
        listView.addHeaderView(view);
        shop_location_headerid= (LinearLayout) view.findViewById(R.id.shop_location_headerid);
        shop_swiperefresh= $(R.id.shop_swiperefresh);


        shop_swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CommonManager.setRefreshingState(shop_swiperefresh,true);
                getData();
            }
        });
        getRightTextButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.size()==0){
                    ToastUitl.showToast("暂无数据");
                }else{
                    PolygonActivity.jumpPolygonActivity(ShopActivity.this,list);
                }
            }
        });

        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为低功耗模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        // 设置定位监听
        locationClient.setLocationListener(this);
        //设置为单次定位
        locationOption.setOnceLocation(true);
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(true);

    }

    @Override
    protected void onActivityLoading() {
        super.onActivityLoading();
        getData();
    }

    private void getData(){
        Call<BaseModel<ArrayList<Shop>>> call= ApiInstant.getInstant().getShoplist(AppApplication.apptype);

        call.enqueue(new ApiCallback<BaseModel<ArrayList<Shop>>>() {
            @Override
            public void onSucssce(BaseModel<ArrayList<Shop>> arrayListBaseModel) {
                CommonManager.setRefreshingState(shop_swiperefresh, false);
                onActivityLoadingSuccess();
                list.addAll( arrayListBaseModel.object);
                getBindView(list);
            }

            @Override
            public void onFailure() {
                CommonManager.setRefreshingState(shop_swiperefresh, false);
                onActivityLoadingFailed();
            }
        });
    }

    @Override
    protected void initData() {
        startActivityLoading();
    }

    public void getBindView(ArrayList<Shop> list){
         adapter= new CommonBaseAdapter<Shop>(this, list, R.layout.activity_shop_item) {
            @Override
            public void convert(ViewHolder holder, Shop itemData, int position) {
                holder.setText(R.id.stop_name,itemData.shopname);
                holder.setText(R.id.stop_address,itemData.address);
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
    @Override
    protected void setListener() {

        shop_location_headerid.setOnClickListener(this);
    }

    @Override
    public void doClick(View view) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.shop_location_headerid:
                locationClient.setLocationOption(locationOption);
                // 启动定位
                locationClient.startLocation();
                mHandler.sendEmptyMessage(0x101);
                break;
        }
    }


    Handler mHandler = new Handler(){
        public void dispatchMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x101:
                    ToastUitl.showToast("正在定位...");
                    break;
                //定位完成
                case 0x100:
                    AMapLocation loc = (AMapLocation)msg.obj;
                    String result = LocalUtil.getLocationStr(loc);
                    ToastUitl.showToast(result);
                    break;
                default:
                    break;
            }
        };
    };
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (null != aMapLocation) {
            Message msg = mHandler.obtainMessage();
            msg.obj = aMapLocation;
            msg.what = 0x100;
            mHandler.sendMessage(msg);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }
}
