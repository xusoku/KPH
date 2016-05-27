package com.davis.kangpinhui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.Model.Order;
import com.davis.kangpinhui.Model.OrderDetail;
import com.davis.kangpinhui.Model.basemodel.BaseModel;
import com.davis.kangpinhui.Model.basemodel.Page;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.adapter.base.CommonBaseAdapter;
import com.davis.kangpinhui.adapter.base.ViewHolder;
import com.davis.kangpinhui.api.ApiCallback;
import com.davis.kangpinhui.api.ApiInstant;
import com.davis.kangpinhui.fragment.base.BaseFragment;
import com.davis.kangpinhui.views.LoadMoreListView;
import com.davis.kangpinhui.views.StretchedListView;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by davis on 16/5/27.
 */
public class CouponFragment extends BaseFragment {

    private LoadMoreListView mine_allorder_list;
    private int id = 0;

    private int iPage = 1;
    private int iPageSize = 10;
    private int TotalPage = 0;
    private CommonBaseAdapter<Order<ArrayList<OrderDetail>>> adapter;
    private ArrayList<Order<ArrayList<OrderDetail>>> list;

    public static CouponFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt("id", id);
        CouponFragment sampleFragment = new CouponFragment();
        sampleFragment.setArguments(args);
        return sampleFragment;
    }


    @Override
    protected void initVariable() {
        id = getArguments().getInt("id", 0);
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_allorder;
    }

    @Override
    protected void findViews(View view) {
        mine_allorder_list = $(view, R.id.mine_allorder_list);

        list = new ArrayList<>();
        adapter = new CommonBaseAdapter<Order<ArrayList<OrderDetail>>>(getActivity(), list, R.layout.fragment_allorder_item) {
            @Override
            public void convert(ViewHolder holder, Order<ArrayList<OrderDetail>> itemData, int position) {
//                Stype：  0：待配送  3：已付款成功  6：已关闭
                ArrayList<OrderDetail> orderDetails = itemData.list;
                holder.setText(R.id.allorder_item_number, itemData.sordernumber + "[详情]");
                if (itemData.stype.equals("0")) {
                    holder.setText(R.id.allorder_item_type, "待配送");

                } else if (itemData.stype.equals("3")) {
                    holder.setText(R.id.allorder_item_type, "配送中");

                } else if (itemData.stype.equals("6")) {
                    holder.setText(R.id.allorder_item_type, "已关闭");

                } else {
                    holder.setText(R.id.allorder_item_type,"未知");
                }

                LinearLayout linearLayout=holder.getView(R.id.allorder_item_linear);

                bindItemView(holder.<StretchedListView>getView(R.id.allorder_lst_item),orderDetails);

            }
        };
        mine_allorder_list.setAdapter(adapter);
    }

    private void bindItemView(StretchedListView listView,ArrayList<OrderDetail> orderDetails) {
        listView.setAdapter(new CommonBaseAdapter<OrderDetail>(getActivity(), orderDetails, R.layout.activity_order_item) {
            @Override
            public void convert(ViewHolder holder, OrderDetail itemData, int position) {
                holder.setImageByUrl(R.id.order_comfi_item_iv, itemData.picurl);
                holder.setText(R.id.order_comfi_item_title, itemData.sproductname);
                holder.setText(R.id.order_comfi_item_sstandent, itemData.sstandard);
                holder.setText(R.id.order_comfi_item_price, "¥" + itemData.fmoney);
                holder.setText(R.id.order_comfi_item_number, "数量:" + (int) Float.parseFloat(itemData.icount));
            }
        });
    }

    @Override
    protected void initData() {
        startFragmentLoading();
    }

    @Override
    protected void onFragmentLoading() {
        super.onFragmentLoading();
        getOrderList(iPage, iPageSize);
    }

    @Override
    protected void setListener() {

        mine_allorder_list.setOnLoadListener(new LoadMoreListView.OnLoadListener() {
            @Override
            public void onLoad(LoadMoreListView listView) {
                getOrderList(++iPage, iPageSize);
            }
        });
    }

    private void getOrderList(int ipage, int iPageSize) {

        Call<BaseModel<Page<ArrayList<Order<ArrayList<OrderDetail>>>>>> call = ApiInstant.getInstant().myOrderlist(AppApplication.apptype, ipage + "", iPageSize + "", AppApplication.token, id + "");
        call.enqueue(new ApiCallback<BaseModel<Page<ArrayList<Order<ArrayList<OrderDetail>>>>>>() {
            @Override
            public void onSucssce(BaseModel<Page<ArrayList<Order<ArrayList<OrderDetail>>>>> pageBaseModel) {

                onFragmentLoadingSuccess();

                Page<ArrayList<Order<ArrayList<OrderDetail>>>> page = pageBaseModel.object;

                TotalPage = page.iTotalPage;

                list.addAll(page.list);
                adapter.notifyDataSetChanged();

                if (list.size() == 0) {
                    onFragmentFirstLoadingNoData();
                    mine_allorder_list.onLoadUnavailable();
                } else if (TotalPage == iPage) {
                    mine_allorder_list.onLoadSucess(false);
                } else {
                    mine_allorder_list.onLoadSucess(true);
                }
            }

            @Override
            public void onFailure() {
                onFragmentLoadingFailed();
            }
        });
    }

}
