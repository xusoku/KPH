package com.davis.kangpinhui.adapter;

import android.content.Context;

import com.davis.kangpinhui.Model.Cart;
import com.davis.kangpinhui.adapter.base.CommonBaseAdapter;
import com.davis.kangpinhui.adapter.base.ViewHolder;

import java.util.ArrayList;

/**
 * Created by davis on 16/5/25.
 */
public class CartListAdapter extends CommonBaseAdapter<Cart> {


    public CartListAdapter(Context context, ArrayList<Cart> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }


    @Override
    public void convert(ViewHolder holder, Cart itemData, int position) {


    }
}
