package com.davis.kangpinhui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.Model.Shop;
import com.davis.kangpinhui.Model.basemodel.BaseModel;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;
import com.davis.kangpinhui.adapter.base.CommonBaseAdapter;
import com.davis.kangpinhui.adapter.base.ViewHolder;
import com.davis.kangpinhui.api.ApiCallback;
import com.davis.kangpinhui.api.ApiInstant;
import com.davis.kangpinhui.util.CommonManager;
import com.davis.kangpinhui.util.ToastUitl;
import com.davis.kangpinhui.views.LoadMoreListView;
import com.davis.kangpinhui.views.MySwipeRefreshLayout;

import java.util.ArrayList;

import retrofit2.Call;


public class ShopActivity extends BaseActivity implements View.OnClickListener {

    private LoadMoreListView listView;
    private  CommonBaseAdapter adapter;
    private MySwipeRefreshLayout shop_swiperefresh;
    private LinearLayout shop_location_headerid;
    private View view;
    @Override
    protected int setLayoutView() {
        return R.layout.activity_shop;
    }

    @Override
    protected void initVariable() {

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
                CommonManager.setRefreshingState(shop_swiperefresh,false);
                onActivityLoadingSuccess();
                ArrayList<Shop> list = arrayListBaseModel.object;
                getBindView(list);
            }

            @Override
            public void onFailure() {
                CommonManager.setRefreshingState(shop_swiperefresh,false);
                onActivityLoadingFailed();
            }
        });
    }

    @Override
    protected void initData() {

        onActivityLoading();


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
                ToastUitl.showToast("aaa");
                break;
        }
    }
}
