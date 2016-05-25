package com.davis.kangpinhui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.Model.Cart;
import com.davis.kangpinhui.Model.basemodel.BaseModel;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;
import com.davis.kangpinhui.adapter.CartListAdapter;
import com.davis.kangpinhui.api.ApiCallback;
import com.davis.kangpinhui.api.ApiInstant;
import com.davis.kangpinhui.views.StretchedListView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;

public class OrderActivity extends BaseActivity {


    private TextView order_number_text,order_paytype_text,order_paytype_time,order_paytypecoup_text;

    private TextView order_address_text,order_address_phone,order_address_pepole;

    private EditText order_beizhu_text;

    private StretchedListView order_address_lst;

    private String ids="";

    private  ArrayList<Cart> list;

    public static void jumpOrderActivity(Context cot,String ids) {
        Intent it = new Intent(cot, OrderActivity.class);
        it.putExtra("ids", ids);
        cot.startActivity(it);
    }
    @Override
    protected int setLayoutView() {
        return R.layout.activity_order;
    }

    @Override
    protected void initVariable() {
        ids=getIntent().getStringExtra("ids");
        list=new ArrayList<>();
    }

    @Override
    protected void findViews() {
        showTopBar();
        setTitle("订单结算");
        order_number_text=$(R.id.order_number_text);
        order_paytype_text=$(R.id.order_paytype_text);
        order_paytype_time=$(R.id.order_paytype_time);
        order_paytypecoup_text=$(R.id.order_paytypecoup_text);
        order_address_text=$(R.id.order_address_text);
        order_address_phone=$(R.id.order_address_phone);
        order_address_pepole=$(R.id.order_address_pepole);
        order_beizhu_text=$(R.id.order_beizhu_text);
        order_address_lst=$(R.id.order_address_lst);

    }

    @Override
    protected void onActivityLoading() {
        super.onActivityLoading();

        Call<BaseModel<ArrayList<Cart>>> call = ApiInstant.getInstant().getCartlist(AppApplication.apptype, AppApplication.shopid, "", AppApplication.token);
        call.enqueue(new ApiCallback<BaseModel<ArrayList<Cart>>>() {
            @Override
            public void onSucssce(BaseModel<ArrayList<Cart>> arrayListBaseModel) {
                onActivityLoadingSuccess();

//                list.addAll(arrayListBaseModel.object);
//
//                adapter = new CartListAdapter(CartListActivity.this, list, R.layout.activity_cart_list_item);
//
//                cart_listvew.setAdapter(adapter);
//
//                DecimalFormat fnum = new DecimalFormat("##0.0");
//                String str = fnum.format(getTotalPrice(list));
//                str = str.endsWith(".0") ? str.substring(0, str.length() - 2) : str;
//                add_cart_number_text.setText("¥" + str);
//
//                adapter.setOnPriceChange(new CartListAdapter.OnPriceChange() {
//                    @Override
//                    public void priceChange() {
//
//                        DecimalFormat fnum = new DecimalFormat("##0.0");
//                        String str = fnum.format(getTotalPrice(list));
//                        str = str.endsWith(".0") ? str.substring(0, str.length() - 2) : str;
//                        add_cart_number_text.setText("¥" + str);
//                    }
//                });
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

        switch (view.getId()){
            case R.id.order_coup_relatie:

                break;
            case R.id.order_paytime_relative:

                break;
            case R.id.order_paytype_relative:

                break;
            case R.id.order_address_relative:

                break;
            case R.id.order_list_addlinear:

                break;
            case R.id.order_list_addlinear:

                break;
        }
    }
}
