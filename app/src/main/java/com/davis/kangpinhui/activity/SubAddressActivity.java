package com.davis.kangpinhui.activity;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;
import com.davis.kangpinhui.adapter.base.CommonBaseAdapter;
import com.davis.kangpinhui.adapter.base.ViewHolder;
import com.davis.kangpinhui.api.ApiCallback;
import com.davis.kangpinhui.api.ApiInstant;
import com.davis.kangpinhui.model.Address;
import com.davis.kangpinhui.model.Extendedinfo;
import com.davis.kangpinhui.model.Shop;
import com.davis.kangpinhui.model.basemodel.BaseModel;
import com.davis.kangpinhui.util.AppManager;
import com.davis.kangpinhui.util.LocalUtil;
import com.davis.kangpinhui.util.SharePreferenceUtils;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import retrofit2.Call;

public class SubAddressActivity extends BaseActivity {

    public static void startactivity(Context context){
        Intent it = new Intent(context, SubAddressActivity.class);
        context.startActivity(it);
    }

    private ListView shop_list;
    private ArrayList<Shop> list;
    @Override
    protected int setLayoutView() {
        return R.layout.activity_sub_address;
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void findViews() {
        showTopBar();
        setTitle("代收地址");
        shop_list=$(R.id.shop_list);
    }

    @Override
    protected void initData() {
        startActivityLoading();
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void doClick(View view) {

    }


    @Override
    protected void onActivityLoading() {
        super.onActivityLoading();
        getShopid();
    }

    private void getShopid() {
            Call<BaseModel<ArrayList<Shop>>> call = ApiInstant.getInstant().getShoplist(AppApplication.apptype,AppApplication.shopid);

            call.enqueue(new ApiCallback<BaseModel<ArrayList<Shop>>>() {
                @Override
                public void onSucssce(BaseModel<ArrayList<Shop>> arrayListBaseModel) {
                    onActivityLoadingSuccess();
                    setBind(arrayListBaseModel.object);
                }

                @Override
                public void onFailure() {
                    onActivityLoadingFailed();
                }
            });
    }


    private void setBind(ArrayList<Shop> list){
        shop_list.setAdapter(new CommonBaseAdapter<Shop>(this, list, R.layout.activity_poikeywordsearch_item) {
            @Override
            public void convert(ViewHolder holder, final Shop itemData, int position) {
                holder.setText(R.id.poi_item_title,itemData.shopname);
                holder.setText(R.id.poi_item_address,"地址:" +itemData.address);
                if (!TextUtils.isEmpty(itemData.tel)) {
                    holder.getView(R.id.poi_item_phone).setVisibility(View.VISIBLE);
                    holder.setText(R.id.poi_item_phone, "电话:" + itemData.tel);
                } else {
                    holder.getView(R.id.poi_item_phone).setVisibility(View.GONE);
                }

                holder.getView(R.id.shop_item_linear).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Address address=new Address();
                        address.iuseraddressid=itemData.addressid;
                        address.saddress=itemData.address;
                        address.smobile=itemData.tel;
                        address.saddressname=itemData.shopname;
                        AppApplication.address = address;
                        AppManager.getAppManager().finishActivity(MyAddressActivity.class);
                        finish();
//                        if(itemData.id.equals("99")){
//                            AppApplication.shopid="99";
//                            SharePreferenceUtils.getSharedPreferences().putString("shopid", "99");
//                            SharePreferenceUtils.getSharedPreferences().putString("address", itemData.shopname);
//                            EventBus.getDefault().post(new Address());
//                            EventBus.getDefault().post(new Extendedinfo());
//                            finish();
//                        }else {
//                            PoiKeywordSearchActivity.jumpPoiKeywordSearchActivity(SubAddressActivity.this, itemData, type);
//                        }

                    }
                });

            }
        });
    }
}
