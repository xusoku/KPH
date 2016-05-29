package com.davis.kangpinhui.activity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.Model.Cart;
import com.davis.kangpinhui.Model.Coupon;
import com.davis.kangpinhui.Model.Extendedinfo;
import com.davis.kangpinhui.Model.Order;
import com.davis.kangpinhui.Model.TakeGoodsdate;
import com.davis.kangpinhui.Model.Topic;
import com.davis.kangpinhui.Model.basemodel.BaseModel;
import com.davis.kangpinhui.Model.basemodel.Page;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;
import com.davis.kangpinhui.adapter.CartListAdapter;
import com.davis.kangpinhui.adapter.base.CommonBaseAdapter;
import com.davis.kangpinhui.adapter.base.ViewHolder;
import com.davis.kangpinhui.api.ApiCallback;
import com.davis.kangpinhui.api.ApiInstant;
import com.davis.kangpinhui.api.ApiService;
import com.davis.kangpinhui.util.LogUtils;
import com.davis.kangpinhui.util.ToastUitl;
import com.davis.kangpinhui.views.StretchedListView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import view.TwoPickerView;

public class OrderActivity extends BaseActivity {


    private TextView order_number_text,order_paytype_text,order_paytype_time,order_paytypecoup_text;

    private TextView order_address_text,order_address_phone,order_address_pepole;

    private EditText order_beizhu_text;

    private StretchedListView order_address_lst;

    private String ids="";

    private  ArrayList<Cart> list;
    private  ArrayList<TakeGoodsdate> takeGoodsdateArrayList;
    private  ArrayList<Coupon> couponArrayList;

    private TwoPickerView twoPickerView;

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
    protected void onResume() {
        super.onResume();
        if(AppApplication.address!=null){
            order_address_text.setText(AppApplication.address.saddress);
            order_address_phone.setText(AppApplication.address.smobile);
            order_address_pepole.setText(AppApplication.address.saddressname);
        }else{
            order_address_text.setText("暂无地址");
            order_address_phone.setText("");
            order_address_pepole.setText("");
        }
    }

    @Override
    protected void initVariable() {
        ids=getIntent().getStringExtra("ids");
        list=new ArrayList<>();
        takeGoodsdateArrayList=new ArrayList<>();
        couponArrayList=new ArrayList<>();
    }

    @Override
    protected void findViews() {
        showTopBar();
        setTitle("订单结算");
        twoPickerView=new TwoPickerView(this);
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
                        holder.setImageByUrl(R.id.order_comfi_item_iv, ApiService.picurl + itemData.picurl);
                        holder.setText(R.id.order_comfi_item_title, itemData.productName);
                        holder.setText(R.id.order_comfi_item_sstandent, itemData.sstandard);
                        holder.setText(R.id.order_comfi_item_price, "¥" + itemData.iprice);
                        holder.setText(R.id.order_comfi_item_number, "数量:" + (int) Float.parseFloat(itemData.inumber));
                    }
                });
            }

            @Override
            public void onFailure() {
                onActivityLoadingFailed();
            }
        });

        getTimeList();



        final Call<BaseModel<ArrayList<Coupon>>> callCoup=ApiInstant.getInstant().getCouponByUid(AppApplication.apptype,AppApplication.token);
        callCoup.enqueue(new ApiCallback<BaseModel<ArrayList<Coupon>>>() {
            @Override
            public void onSucssce(BaseModel<ArrayList<Coupon>> arrayListBaseModel) {
                couponArrayList.addAll(arrayListBaseModel.object);
                Coupon coupon=new Coupon();
                coupon.usercouponid="";
                coupon.context="不使用优惠券";
                couponArrayList.add(0,coupon);
            }

            @Override
            public void onFailure() {

            }
        });
    }

   private void getTimeList(){
        Call<BaseModel<ArrayList<TakeGoodsdate>>> calltime=ApiInstant.getInstant().getTakegoodtimelist(AppApplication.apptype,
                AppApplication.shopid, ids, AppApplication.token);
        calltime.enqueue(new ApiCallback<BaseModel<ArrayList<TakeGoodsdate>>>() {
            @Override
            public void onSucssce(BaseModel<ArrayList<TakeGoodsdate>> arrayListBaseModel) {
                takeGoodsdateArrayList.addAll(arrayListBaseModel.object);
            }

            @Override
            public void onFailure() {

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

    private String payTape="3";
    private String couponId="";
    private String timeTape="";
    @Override
    public void doClick(View view) {

        switch (view.getId()){
            case R.id.order_coup_relatie:
                couponId="";

                if(couponArrayList.size()==0){
                    ToastUitl.showToast("暂无优惠券");
                    return;
                }
                final CharSequence[] charSequencess = new CharSequence[couponArrayList.size()];
                for (int i = 0; i < couponArrayList.size(); i++) {
                    charSequencess[i]=couponArrayList.get(i).context;
                }
                AlertDialog.Builder builde= new AlertDialog.Builder(this);
                builde.setTitle("优惠券列表")
                        .setItems(charSequencess, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                order_paytypecoup_text.setText(charSequencess[which].toString());
                                couponId=couponArrayList.get(which).usercouponid;
                            }
                        }).show();
                break;
            case R.id.order_paytime_relative:
                timeTape="";
                if(takeGoodsdateArrayList.size()==0){
                    ToastUitl.showToast("请等待");
                    getTimeList();
                    return;
                }
                 final ArrayList<String> list=new ArrayList<String>();
                 final ArrayList<ArrayList<String>> arrayLists=new ArrayList<ArrayList<String>>();
                for (TakeGoodsdate takeGoodsdate : takeGoodsdateArrayList){
                    list.add(takeGoodsdate.date);
                    arrayLists.add(takeGoodsdate.time);
                }

                twoPickerView.setPicker(list, arrayLists, false);
                twoPickerView.setCyclic(false);
                twoPickerView.setSelectOptions(0, 0);
                twoPickerView.show();
                twoPickerView.setOnoptionsSelectListener(new TwoPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2) {

                        timeTape=(list.get(options1) + " " + arrayLists.get(options1).get(option2));
                        order_paytype_time.setText(timeTape);
                    }
                });

                break;
            case R.id.order_paytype_relative:
                //付款方式  3:余额支付  2：货到付款 0：支付宝  1：财付通  4微信支付

                final CharSequence[] charSequences = {"余额支付","货到付款","支付宝支付","微信支付"};
                final String[] type = {"3","2","0","4"};
                AlertDialog.Builder builder= new AlertDialog.Builder(this);

                builder.setTitle("付款方式")
                        .setItems(charSequences,new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ToastUitl.showToast(charSequences[which].toString());
                                payTape=type[which];
                                order_paytype_text.setText(charSequences[which].toString());

                            }
                        }).show();
                break;
            case R.id.order_address_relative:
                MyAddressActivity.jumpMyAddressActivity(this,true);
                break;
            case R.id.order_list_addlinear:

                if(AppApplication.address==null|| TextUtils.isEmpty(AppApplication.address.iuseraddressid)){
                    ToastUitl.showToast("请选择收货地址");
                    return;
                }
                if(TextUtils.isEmpty(timeTape)){
                    ToastUitl.showToast("请选择配送时间");
                    return;
                }
                String beizhu=order_beizhu_text.getText().toString().trim();

                Call<BaseModel<Order>> call=ApiInstant.getInstant().orderSave(AppApplication.apptype, AppApplication.shopid,
                        ids, AppApplication.address.iuseraddressid, payTape, timeTape, beizhu, couponId, AppApplication.token);

                call.enqueue(new ApiCallback<BaseModel<Order>>() {
                    @Override
                    public void onSucssce(BaseModel<Order> baseModel) {
                        ToastUitl.showToast("订单提交成功");
                        EventBus.getDefault().post(new Extendedinfo());
                    }
                    @Override
                    public void onFailure() {

                    }
                });
                break;
        }
    }
}
