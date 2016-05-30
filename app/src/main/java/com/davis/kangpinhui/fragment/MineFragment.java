package com.davis.kangpinhui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.model.UserInfo;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.AllOrderActivity;
import com.davis.kangpinhui.activity.CartListActivity;
import com.davis.kangpinhui.activity.CouponActivity;
import com.davis.kangpinhui.activity.FeedBackActivity;
import com.davis.kangpinhui.activity.MyAddressActivity;
import com.davis.kangpinhui.activity.MyTiHuoActivity;
import com.davis.kangpinhui.activity.RechargeActivity;
import com.davis.kangpinhui.activity.RechargeListActivity;
import com.davis.kangpinhui.activity.SettingActivity;
import com.davis.kangpinhui.fragment.base.BaseFragment;
import com.davis.kangpinhui.util.SharePreferenceUtils;
import com.davis.kangpinhui.util.UtilText;
import com.davis.kangpinhui.views.MineCustomLayout;

/**
 * Created by davis on 16/5/19.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    private MineCustomLayout mine_allorder;
    private MineCustomLayout mine_feedback_linear, mine_histroy_bill, mine_rechange, mine_setting, mine_kefu, mine_ti_huo, mine_myaddress, mine_mycoup, mine_cart, mine_allorder_sending,
            mine_allorder_unpay;
    private TextView fragment_mine_name,fragment_mine_price,fragment_mine_price_info;

    private ImageView fragment_mine_photo;

    @Override
    protected void initVariable() {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void findViews(View view) {

        fragment_mine_name = $(R.id.fragment_mine_name);
        fragment_mine_photo = $(R.id.fragment_mine_photo);
        mine_allorder = $(R.id.mine_allorder);
        mine_feedback_linear = $(R.id.mine_feedback_linear);
        mine_histroy_bill = $(R.id.mine_histroy_bill);
        mine_rechange = $(R.id.mine_rechange);
        mine_setting = $(R.id.mine_setting);
        mine_kefu = $(R.id.mine_kefu);
        mine_ti_huo = $(R.id.mine_ti_huo);
        mine_myaddress = $(R.id.mine_myaddress);
        mine_mycoup = $(R.id.mine_mycoup);
        mine_cart = $(R.id.mine_cart);
        mine_allorder_sending = $(R.id.mine_allorder_sending);
        mine_allorder_unpay = $(R.id.mine_allorder_unpay);
        fragment_mine_price = $(R.id.fragment_mine_price);
        fragment_mine_price_info = $(R.id.fragment_mine_price_info);
    }

    @Override
    protected void initData() {

        String name= SharePreferenceUtils.getSharedPreferences("kph").getString("username","");
        if(!TextUtils.isEmpty(name)){
            name=name.substring(0,3)+"****"+name.substring(7,11);
            fragment_mine_name.setText(name);
        }

    }

    public void setUi(UserInfo userInfo){
        String name=userInfo.susername.substring(0,3)+"****"+userInfo.susername.substring(7,11);
        fragment_mine_name.setText(name);
    }
    public void setNumber(){

        if(mine_allorder_unpay!=null) {
            mine_allorder_unpay.setText(UtilText.getminenumber("待付款  (" + AppApplication.getOrderunpaid() + ")"));
            mine_allorder_sending.setText(UtilText.getminenumber("配送中 (" + AppApplication.getOrdersending() + ")"));
            mine_cart.setText(UtilText.getminenumber("购物车 (" + (int)Float.parseFloat(AppApplication.getCartcount()) + ")"));
            mine_mycoup.setText(("我的优惠券 (" + AppApplication.getCouponcount() + "张)"));
        }
    }

    @Override
    protected void setListener() {
        mine_allorder.setOnClickListener(this);
        mine_feedback_linear.setOnClickListener(this);
        mine_histroy_bill.setOnClickListener(this);
        mine_rechange.setOnClickListener(this);
        mine_setting.setOnClickListener(this);
        mine_kefu.setOnClickListener(this);
        mine_ti_huo.setOnClickListener(this);
        mine_myaddress.setOnClickListener(this);
        mine_mycoup.setOnClickListener(this);
        mine_cart.setOnClickListener(this);
        mine_allorder_sending.setOnClickListener(this);
        mine_allorder_unpay.setOnClickListener(this);
        fragment_mine_price_info.setOnClickListener(this);
        fragment_mine_price.setOnClickListener(this);
        fragment_mine_photo.setOnClickListener(this);
        fragment_mine_name.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_allorder:
                AllOrderActivity.jumpAllOrderActivity(getActivity(),0);
                break;
            case R.id.mine_feedback_linear:
                FeedBackActivity.jumpFeedBackActivity(getActivity());
                break;
            case R.id.mine_histroy_bill:
                FeedBackActivity.jumpFeedBackActivity(getActivity());
                break;
            case R.id.mine_rechange:
                RechargeListActivity.jumpRechargeListActivity(getActivity());
                break;
            case R.id.mine_setting:
                SettingActivity.jumpSettingActivity(getActivity());
                break;
            case R.id.mine_kefu:
                new AlertDialog.Builder(getActivity())
                        .setTitle("联系客服")
                        .setMessage("客服电话：18516548570")
                        .setPositiveButton("呼叫", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "18516548570"));
                                startActivity(intent);
                            }
                        }).setNegativeButton("取消",null)
                        .show();
                break;
            case R.id.mine_ti_huo:
                MyTiHuoActivity.jumpMyTiHuoActivity(getActivity());
                break;
            case R.id.mine_myaddress:
                MyAddressActivity.jumpMyAddressActivity(getActivity());
                break;
            case R.id.mine_mycoup:
                CouponActivity.jumpCouponActivity(getActivity(), 0);
                break;
            case R.id.mine_cart:
                CartListActivity.jumpCartListActivity(getActivity());
                break;
            case R.id.mine_allorder_sending:
                AllOrderActivity.jumpAllOrderActivity(getActivity(), 3);
                break;
            case R.id.mine_allorder_unpay:
                AllOrderActivity.jumpAllOrderActivity(getActivity(), 1);
                break;
            case R.id.fragment_mine_price:
                RechargeActivity.jumpRechangeActivity(getActivity());
                break;
            case R.id.fragment_mine_price_info:
                RechargeActivity.jumpRechangeActivity(getActivity());
                break;
            case R.id.fragment_mine_photo:
                AppApplication.isLogin(getActivity());
                break;
            case R.id.fragment_mine_name:
                AppApplication.isLogin(getActivity());
                break;
        }
    }
}
