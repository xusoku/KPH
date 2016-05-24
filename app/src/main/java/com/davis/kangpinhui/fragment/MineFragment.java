package com.davis.kangpinhui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.Model.UserInfo;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.FeedBackActivity;
import com.davis.kangpinhui.activity.MyAddressActivity;
import com.davis.kangpinhui.activity.MyTiHuoActivity;
import com.davis.kangpinhui.activity.SettingActivity;
import com.davis.kangpinhui.fragment.base.BaseFragment;
import com.davis.kangpinhui.util.SharePreferenceUtils;
import com.davis.kangpinhui.util.ToastUitl;
import com.davis.kangpinhui.util.UtilText;
import com.davis.kangpinhui.views.MineCustomLayout;

/**
 * Created by davis on 16/5/19.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    private MineCustomLayout mine_allorder;
    private MineCustomLayout mine_feedback_linear, mine_histroy_bill, mine_rechange, mine_setting, mine_kefu, mine_ti_huo, mine_myaddress, mine_mycoup, mine_cart, mine_allorder_sending,
            mine_allorder_unpay;
    private TextView fragment_mine_name;

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_allorder:
                ToastUitl.showToast("aaa");
                break;
            case R.id.mine_feedback_linear:
                FeedBackActivity.jumpFeedBackActivity(getActivity());
                break;
            case R.id.mine_histroy_bill:
                FeedBackActivity.jumpFeedBackActivity(getActivity());
                break;
            case R.id.mine_rechange:
                FeedBackActivity.jumpFeedBackActivity(getActivity());
                break;
            case R.id.mine_setting:
                SettingActivity.jumpSettingActivity(getActivity());
                break;
            case R.id.mine_kefu:
                FeedBackActivity.jumpFeedBackActivity(getActivity());
                break;
            case R.id.mine_ti_huo:
                MyTiHuoActivity.jumpMyTiHuoActivity(getActivity());
                break;
            case R.id.mine_myaddress:
                MyAddressActivity.jumpMyAddressActivity(getActivity());
                break;
            case R.id.mine_mycoup:
                FeedBackActivity.jumpFeedBackActivity(getActivity());
                break;
            case R.id.mine_cart:
                FeedBackActivity.jumpFeedBackActivity(getActivity());
                break;
            case R.id.mine_allorder_sending:
                FeedBackActivity.jumpFeedBackActivity(getActivity());
                break;
            case R.id.mine_allorder_unpay:
                FeedBackActivity.jumpFeedBackActivity(getActivity());
                break;
        }
    }
}
