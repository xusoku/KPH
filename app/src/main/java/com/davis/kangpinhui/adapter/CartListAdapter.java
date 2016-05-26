package com.davis.kangpinhui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.Model.Cart;
import com.davis.kangpinhui.Model.basemodel.BaseModel;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.adapter.base.CommonBaseAdapter;
import com.davis.kangpinhui.adapter.base.ViewHolder;
import com.davis.kangpinhui.api.ApiCallback;
import com.davis.kangpinhui.api.ApiInstant;
import com.davis.kangpinhui.api.ApiService;
import com.davis.kangpinhui.util.ToastUitl;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by davis on 16/5/25.
 */
public class CartListAdapter extends CommonBaseAdapter<Cart> {

    private OnPriceChange onPriceChange;


    private Context contxt;

    public void setOnPriceChange(OnPriceChange onPriceChange) {
        this.onPriceChange = onPriceChange;
    }

    public CartListAdapter(Context context, ArrayList<Cart> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
        this.contxt = context;
    }


    @Override
    public void convert(ViewHolder holder, final Cart itemData, int position) {

        holder.setImageByUrl(R.id.add_cart_item_iv, ApiService.picurl + itemData.picurl);
        holder.setText(R.id.add_cart_item_title, itemData.productName);
        holder.setText(R.id.add_cart_item_sstandent, "规格:" + itemData.sstandard);
        holder.setText(R.id.add_cart_item_price, itemData.iprice + "/" + itemData.sstandard);
        holder.setText(R.id.add_cart_item_add_center, (int) Float.parseFloat(itemData.inumber) + "");

        CheckBox checkBox = holder.getView(R.id.add_cart_item_checkbox);
        ImageView ivDelete = holder.getView(R.id.add_cart_add_del);
        TextView addText = holder.getView(R.id.add_cart_item_add);
        final TextView text = holder.getView(R.id.add_cart_item_add_center);
        TextView minsText = holder.getView(R.id.add_cart_item_add_mins);

        checkBox.setChecked(itemData.flag);


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                itemData.flag = isChecked;
                notifyDataSetChanged();
                onPriceChange.priceChange();
            }
        });


        addText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = text.getText().toString().trim();

                int n = (int) Float.parseFloat(number);
                n++;
                itemData.inumber = n + "";
                notifyDataSetChanged();
                onPriceChange.priceChange();
            }
        });

        minsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = text.getText().toString().trim();
                int n = (int) Float.parseFloat(number);

                if (n <= 1) {
                    n = 1;
                } else {
                    n--;
                }
                itemData.inumber = n + "";
                notifyDataSetChanged();
                onPriceChange.priceChange();
            }
        });


        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(contxt).setTitle("确定要删除吗？").setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Call<BaseModel> call = ApiInstant.getInstant().deleteCart(AppApplication.apptype,
                                        AppApplication.shopid, itemData.iproductid, AppApplication.token);
                                call.enqueue(new ApiCallback<BaseModel>() {
                                    @Override
                                    public void onSucssce(BaseModel baseModel) {
                                        onPriceChange.listChange();
                                        ToastUitl.showToast("删除成功");
                                    }

                                    @Override
                                    public void onFailure() {

                                    }
                                });
                            }
                        }).show();

            }
        });

    }

    public interface OnPriceChange {
        public void priceChange();
        public void listChange();
    }
}
