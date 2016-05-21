package com.davis.kangpinhui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.Model.Product;
import com.davis.kangpinhui.Model.basemodel.BaseModel;
import com.davis.kangpinhui.Model.basemodel.Page;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;
import com.davis.kangpinhui.api.ApiCallback;
import com.davis.kangpinhui.api.ApiInstant;
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

    }

    @Override
    protected void findViews() {
        search_back = $(R.id.search_back);
        search_right_iv = $(R.id.search_right_iv);
        search_et = $(R.id.search_et);
        search_result_recycler = $(R.id.search_result_recycler);

        search_et.setText(key);
    }

    @Override
    protected void initData() {

        Call<BaseModel<Page<ArrayList<Product>>>> call = ApiInstant.getInstant().getSearchProductlist(AppApplication.apptype, "0",
                AppApplication.shopid, key, Page + "", PageSize + "");

        call.enqueue(new ApiCallback<BaseModel<com.davis.kangpinhui.Model.basemodel.Page<ArrayList<Product>>>>() {
            @Override
            public void onSucssce(BaseModel<Page<ArrayList<Product>>> pageBaseModel) {

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
