package com.davis.kangpinhui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.Model.Address;
import com.davis.kangpinhui.Model.basemodel.BaseModel;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;
import com.davis.kangpinhui.api.ApiCallback;
import com.davis.kangpinhui.api.ApiInstant;

import java.util.ArrayList;

import retrofit2.Call;

public class MyAddressActivity extends BaseActivity {


    private ListView listView;

    public static void jumpMyAddressActivity(Context cot){
        Intent it=new Intent(cot,MyAddressActivity.class);
        cot.startActivity(it);
    }
    @Override
    protected int setLayoutView() {
        return R.layout.activity_my_address;
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void findViews() {
        showTopBar();
        setTitle("我的地址");
        listView=$(R.id.my_address_list);

    }

    @Override
    protected void initData() {

        Call<BaseModel<ArrayList<Address>>> call = ApiInstant.getInstant().getAddresslist(AppApplication.apptype,AppApplication.token);

        call.enqueue(new ApiCallback<BaseModel<ArrayList<Address>>>() {
            @Override
            public void onSucssce(BaseModel<ArrayList<Address>> arrayListBaseModel) {
                
            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void doClick(View view) {
        switch (view.getId()){
            case R.id.my_address_add_text:
                AddAddressActivity.jumpAddAddressActivity(this);
                break;
        }

    }
}
