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
import com.davis.kangpinhui.adapter.base.CommonBaseAdapter;
import com.davis.kangpinhui.adapter.base.ViewHolder;
import com.davis.kangpinhui.api.ApiCallback;
import com.davis.kangpinhui.api.ApiInstant;
import com.davis.kangpinhui.api.ApiService;
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

                list.addAll(arrayListBaseModel.object);

                DecimalFormat fnum = new DecimalFormat("##0.0");
                String str = fnum.format(getTotalPrice(list));
                str = str.endsWith(".0") ? str.substring(0, str.length() - 2) : str;
                order_number_text.setText("¥" + str);

                order_address_lst.setAdapter(new CommonBaseAdapter<Cart>(OrderActivity.this, list, R.layout.activity_order_item) {
                    @Override
                    public void convert(ViewHolder holder, Cart itemData, int position) {
                      holder.setImageByUrl(R.id.order_comfi_item_iv, ApiService.picurl+itemData.picurl);
                        holder.setText(R.id.order_comfi_item_title, itemData.productName);
                        holder.setText(R.id.order_comfi_item_sstandent, itemData.sstandard);
                        holder.setText(R.id.order_comfi_item_price,"¥"+itemData.iprice);
                        holder.setText(R.id.order_comfi_item_number,"数量:"+(int)Float.parseFloat(itemData.inumber));
                    }
                });
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
    private Float getTotalPrice(ArrayList<Cart> list) {
        Float total = 0.0f;

        for (Cart cart : list) {
            if (cart.flag) {
                int n = (int) Float.parseFloat(cart.inumber);
                Float f = Float.parseFloat(cart.iprice);
                total += n * f;
            }
        }

        return total;
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
        }
    }
}
