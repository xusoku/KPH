package com.davis.kangpinhui.adapter;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.davis.kangpinhui.Model.Cart;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.adapter.base.CommonBaseAdapter;
import com.davis.kangpinhui.adapter.base.ViewHolder;
import com.davis.kangpinhui.api.ApiService;

import java.util.ArrayList;

/**
 * Created by davis on 16/5/25.
 */
public class CartListAdapter extends CommonBaseAdapter<Cart> {


    public CartListAdapter(Context context, ArrayList<Cart> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }


    @Override
    public void convert(ViewHolder holder, final Cart itemData, int position) {

        holder.setImageByUrl(R.id.add_cart_item_iv, ApiService.picurl+itemData.picurl);
        holder.setText(R.id.add_cart_item_title, itemData.productName);
        holder.setText(R.id.add_cart_item_sstandent,"规格:"+itemData.sstandard);
        holder.setText(R.id.add_cart_item_price,itemData.iprice+"/"+itemData.sstandard);
        holder.setText(R.id.add_cart_item_add_center,itemData.inumber);

        CheckBox checkBox=holder.getView(R.id.add_cart_item_checkbox);
        ImageView ivDelete=holder.getView(R.id.add_cart_add_del);
        TextView addText=holder.getView(R.id.add_cart_item_add);
        TextView text=holder.getView(R.id.add_cart_item_add_center);
        TextView minsText=holder.getView(R.id.add_cart_item_add_mins);

        checkBox.setChecked(itemData.flag);


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                itemData.flag=isChecked;
            }
        });




    }
}
