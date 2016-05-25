package com.davis.kangpinhui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.Model.Cart;
import com.davis.kangpinhui.Model.basemodel.BaseModel;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;
import com.davis.kangpinhui.adapter.CartListAdapter;
import com.davis.kangpinhui.api.ApiCallback;
import com.davis.kangpinhui.api.ApiInstant;

import java.util.ArrayList;

import retrofit2.Call;

public class CartListActivity extends BaseActivity {

    private ListView cart_listvew;
    private CartListAdapter adapter;


    public static void jumpCartListActivity(Context context){
        if(AppApplication.isLogin(context)){

            Intent it=new Intent(context,CartListActivity.class);
            context.startActivity(it);
        }


    }
    @Override
    protected int setLayoutView() {
        return R.layout.activity_cart_list;
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void findViews() {
        showTopBar();
        setTitle("购物车");
        cart_listvew = $(R.id.content);

    }

    @Override
    protected void onActivityLoading() {
        super.onActivityLoading();

        Call<BaseModel<ArrayList<Cart>>> call= ApiInstant.getInstant().getCartlist(AppApplication.apptype,AppApplication.shopid,"",AppApplication.token);
                call.enqueue(new ApiCallback<BaseModel<ArrayList<Cart>>>() {
                    @Override
                    public void onSucssce(BaseModel<ArrayList<Cart>> arrayListBaseModel) {
                        onActivityLoadingSuccess();

                        ArrayList<Cart> list=arrayListBaseModel.object;

                        adapter=new CartListAdapter(CartListActivity.this,list,R.layout.activity_cart_list_item);

                        cart_listvew.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure() {

                        onActivityLoadingFailed();
                    }
                });
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
}
