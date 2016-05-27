package com.davis.kangpinhui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.Model.Order;
import com.davis.kangpinhui.Model.OrderDetail;
import com.davis.kangpinhui.Model.Product;
import com.davis.kangpinhui.Model.basemodel.BaseModel;
import com.davis.kangpinhui.Model.basemodel.Page;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.adapter.base.CommonBaseAdapter;
import com.davis.kangpinhui.adapter.base.ViewHolder;
import com.davis.kangpinhui.adapter.recycleradapter.CommonRecyclerAdapter;
import com.davis.kangpinhui.api.ApiCallback;
import com.davis.kangpinhui.api.ApiInstant;
import com.davis.kangpinhui.fragment.base.BaseFragment;
import com.davis.kangpinhui.views.LoadMoreListView;
import com.davis.kangpinhui.views.LoadMoreRecyclerView;


import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by davis on 16/5/27.
 */
public class AllorderFragment extends BaseFragment {

    private LoadMoreListView mine_allorder_list;
    private int id = 0;

    private int iPage = 1;
    private int iPageSize = 10;
    private int TotalPage = 0;
    private CommonBaseAdapter<Order<ArrayList<OrderDetail>>> adapter;
    private ArrayList<Order<ArrayList<OrderDetail>>> list;

    public static AllorderFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt("id", id);
        AllorderFragment sampleFragment = new AllorderFragment();
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

                ArrayList<OrderDetail> orderDetails = itemData.list;
            }
        };
        mine_allorder_list.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        startFragmentLoading();
    }

    @Override
    protected void onFragmentLoading() {
        super.onFragmentLoading();
        getOrderList(iPage,iPageSize);
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
