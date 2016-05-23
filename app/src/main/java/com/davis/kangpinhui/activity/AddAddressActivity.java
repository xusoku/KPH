package com.davis.kangpinhui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;

public class AddAddressActivity extends BaseActivity {


    private TextView delete_address;
    private TextView add_des_address_text;
    private TextView add_address_people;
    private TextView add_address_phone;
    private TextView add_address_text;
    public static void jumpAddAddressActivity(Context cot){
        Intent it=new Intent(cot,AddAddressActivity.class);
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

        add_des_address_text=$(R.id.add_des_address_text);
        add_address_people=$(R.id.add_address_people);
        add_address_phone=$(R.id.add_address_phone);
        delete_address=$(R.id.delete_address);
        add_address_text=$(R.id.add_address_text);


        getRightTextButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    public void doClick(View view) {

    }
}
