package com.davis.kangpinhui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.Model.Address;
import com.davis.kangpinhui.Model.basemodel.BaseModel;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;
import com.davis.kangpinhui.api.ApiCallback;
import com.davis.kangpinhui.api.ApiInstant;
import com.davis.kangpinhui.util.RegexUtils;
import com.davis.kangpinhui.util.ToastUitl;

import retrofit2.Call;

public class AddAddressActivity extends BaseActivity {


    private TextView delete_address;
    private EditText add_des_address_text;
    private EditText add_address_people;
    private EditText add_address_phone;
    private TextView add_address_text;

    public static void jumpAddAddressActivity(Context cot) {
        Intent it = new Intent(cot, AddAddressActivity.class);
        cot.startActivity(it);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_add_address;
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void findViews() {
        showTopBar();
        setTitle("新增收获地址");
        getRightTextButton().setText("确定");

        add_des_address_text = $(R.id.add_des_address_text);
        add_address_people = $(R.id.add_address_people);
        add_address_phone = $(R.id.add_address_phone);
        delete_address = $(R.id.delete_address);
        add_address_text = $(R.id.add_address_text);


        getRightTextButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAddress();
            }
        });

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    private void addAddress() {
        String address = add_address_text.getText().toString().trim();
        String addressdes = add_des_address_text.getText().toString().trim();
        String pepole = add_address_people.getText().toString().trim();
        String phone = add_address_phone.getText().toString().trim();

        if (TextUtils.isEmpty(address)) {
            ToastUitl.showToast("地址不能为空");
            return;
        }
        if (TextUtils.isEmpty(addressdes)) {
            ToastUitl.showToast("详细地址不能为空");
            return;
        }
        if (TextUtils.isEmpty(pepole)) {
            ToastUitl.showToast("联系人不能为空");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            ToastUitl.showToast("联系方式不能为空");
            return;
        }
        if (!RegexUtils.isMobilePhoneNumber(phone)) {
            ToastUitl.showToast("联系方式不正确");
            return;
        }

        Call<BaseModel<Address>> call= ApiInstant.getInstant().Addaddress(AppApplication.apptype,
                AppApplication.token,"",pepole,address+addressdes,phone,AppApplication.shopid,"");
        call.enqueue(new ApiCallback<BaseModel<Address>>() {
            @Override
            public void onSucssce(BaseModel<Address> addressBaseModel) {
                ToastUitl.showToast("添加成功");
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void bindView() {

    }

    @Override
    public void doClick(View view) {

    }
}
