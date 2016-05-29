package com.davis.kangpinhui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.Model.Address;
import com.davis.kangpinhui.Model.basemodel.BaseModel;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;
import com.davis.kangpinhui.api.ApiInstant;
import com.davis.kangpinhui.util.LogUtils;
import com.davis.kangpinhui.util.ToastUitl;

import retrofit2.Call;

public class RechargeActivity extends BaseActivity {

    private String price = "500";
    private String payTape = "4"; //付款方式  4: '微信'   2:'支付宝'

    final CharSequence[] pricetext = {"500", "1000", "2000"};
    final CharSequence[] typepaytext = { "在线支付 微信支付","在线支付 支付宝支付"};
    final String[] typepay = {"4","2" };


    private TextView recharge_letterhead, recharge_price, recharge_pay, recharge_content, recharge_price_text, recharge_commit;

    public static void jumpRechangeActivity(Context cot) {
        if (AppApplication.isLogin(cot)) {
            Intent it = new Intent(cot, RechargeActivity.class);
            cot.startActivity(it);
        }
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_rechange;
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void findViews() {
        showTopBar();
        setTitle("账户充值");

        recharge_letterhead = $(R.id.recharge_letterhead);
        recharge_price = $(R.id.recharge_price);
        recharge_pay = $(R.id.recharge_pay);
        recharge_content = $(R.id.recharge_content);
        recharge_price_text = $(R.id.recharge_price_text);
        recharge_commit = $(R.id.recharge_commit);

    }

    @Override
    protected void initData() {


    }

    @Override
    protected void setListener() {

    }

    private Address address;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==resultCode){
            address= (Address) data.getSerializableExtra("lerrter");
            LogUtils.e("address",address.slock);
        }
    }

    @Override
    public void doClick(View view) {
        switch (view.getId()){
            case R.id.recharge_letterhead:
                LetterheadActivity.jumpLetterheadActivity(this);
                break;
            case R.id.recharge_price:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);

                builder1.setTitle("充值金额")
                        .setItems(pricetext, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                price = pricetext[which] + "";
                                recharge_price.setText(pricetext[which] + "");

                            }
                        }).show();
                break;
            case R.id.recharge_pay:

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("支付方式")
                        .setItems(typepaytext,new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ToastUitl.showToast(typepaytext[which].toString());
                                payTape = typepaytext[which].toString();
                                recharge_pay.setText(typepaytext[which].toString());

                            }
                        }).show();
                break;
            case R.id.recharge_commit:

//                Call<BaseModel> call= ApiInstant.getInstant().saveRecharge(AppApplication.apptype,
//                        )
                break;
        }

    }
}
