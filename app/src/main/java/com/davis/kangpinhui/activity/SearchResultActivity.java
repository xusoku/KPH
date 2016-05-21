package com.davis.kangpinhui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.Model.Product;
import com.davis.kangpinhui.Model.basemodel.BaseModel;
import com.davis.kangpinhui.Model.basemodel.Page;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;
import com.davis.kangpinhui.adapter.recycleradapter.CommonRecyclerAdapter;
import com.davis.kangpinhui.api.ApiCallback;
import com.davis.kangpinhui.api.ApiInstant;
import com.davis.kangpinhui.util.ToastUitl;
import com.davis.kangpinhui.views.LoadMoreRecyclerView;

import java.util.ArrayList;

import retrofit2.Call;

public class SearchResultActivity extends BaseActivity {

    private String key = "";
    private LinearLayout search_back;
    private LinearLayout search_right_iv;
    private EditText search_et;
    private LoadMoreRecyclerView search_result_recycler;

    private int Page = 0;
    private int PageSize = 20;
    private int TotalPage=0;
    private CommonRecyclerAdapter<Product> adapter;
    private ArrayList<Product> list;

    public static void jumpSearchResultActivity(Context con, String key) {
        Intent it = new Intent(con, SearchResultActivity.class);
        it.putExtra("key", key);
        con.startActivity(it);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_search_result;
    }

    @Override
    protected void initVariable() {

        key = getIntent().getStringExtra("key");
        list=new ArrayList<>();
    }

    @Override
    protected void findViews() {
        search_back = $(R.id.search_back);
        search_right_iv = $(R.id.search_right_iv);
        search_et = $(R.id.search_et);
        search_result_recycler = $(R.id.search_result_recycler);

        search_et.setText(key);



        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        search_result_recycler.setLayoutManager(gridLayoutManager);

        adapter= new CommonRecyclerAdapter<Product>(this,list,R.layout.activity_search_result_item){
            @Override
            public void convert(BaseViewHolder holder, Product itemData, int position) {

                ImageView iv=holder.getView(R.id.search_result_item_iv);
                Glide.with(mActivity).load(itemData.picurl).into(iv);

                TextView tv_name=holder.getView(R.id.search_result_item_name);
                tv_name.setText(itemData.productname);

                TextView tv_price=holder.getView(R.id.search_result_item_price);
                tv_price.setText(itemData.fprice);
            }
        };


        search_result_recycler.setAdapter(adapter);
        adapter.setOnItemClickLitener(new CommonRecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View itemView, int position) {
                ToastUitl.showToast("" + position);
            }

            @Override
            public void onItemLongClick(View itemView, int position) {

            }
        });
    }

    @Override
    protected void initData() {

        Call<BaseModel<Page<ArrayList<Product>>>> call = ApiInstant.getInstant().getSearchProductlist(AppApplication.apptype, "0",
                AppApplication.shopid, key, Page + "", PageSize + "");

        call.enqueue(new ApiCallback<BaseModel<com.davis.kangpinhui.Model.basemodel.Page<ArrayList<Product>>>>() {
            @Override
            public void onSucssce(BaseModel<Page<ArrayList<Product>>> pageBaseModel) {

                Page<ArrayList<Product>> page=pageBaseModel.object;

                TotalPage=page.iTotalPage;

                list.addAll(page.list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void doClick(View view) {

        switch (view.getId()) {
            case R.id.search_back:
                break;
            case R.id.search_right_iv:
                break;
        }
    }
}
