package com.davis.kangpinhui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.Model.Banner;
import com.davis.kangpinhui.Model.Index;
import com.davis.kangpinhui.Model.basemodel.BaseModel;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.api.ApiCallback;
import com.davis.kangpinhui.api.ApiInstant;
import com.davis.kangpinhui.fragment.base.BaseFragment;
import com.davis.kangpinhui.views.MySwipeRefreshLayout;
import com.davis.kangpinhui.views.loopbanner.LoopBanner;

import java.util.ArrayList;

import retrofit.Call;

/**
 * Created by davis on 16/5/18.
 */
public class IndexFragment extends BaseFragment {

    private LinearLayout index_rechange;
    private LinearLayout index_tuan;
    private LinearLayout index_classic;
    private LinearLayout index_linear_layout;
    private LoopBanner index_loopbanner;
    private ImageView index_cart;
    private ImageView index_search;
    private TextView index_local_select;
    private MySwipeRefreshLayout index_refresh;

    ArrayList<Banner> bannerList=new ArrayList<>();
    ArrayList<Index.Productlist> recommandList=new ArrayList<Index.Productlist>();


    @Override
    protected void initVariable() {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_index;
    }

    @Override
    protected void findViews(View view) {

        index_refresh=$(view,R.id.index_refresh);
        index_local_select=$(view,R.id.index_local_select);
        index_cart=$(view,R.id.index_cart);
        index_search=$(view,R.id.index_search);
        index_loopbanner=$(view,R.id.index_loopbanner);
        index_classic=$(view,R.id.index_classic);
        index_tuan=$(view,R.id.index_tuan);
        index_rechange=$(view,R.id.index_rechange);
        index_linear_layout=$(view,R.id.index_linear_layout);

        index_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
//                curPage = 0;
//                isRefreshOrLoad = true;
//                getShowFilmBannerData();
//                getShowFilmListData(curPage, limit);
            }
        });
    }

    @Override
    protected void initData() {

        Call<BaseModel<Index>> call=ApiInstant.getInstant().getIndex(AppApplication.apptype,"1","");

        call.enqueue(new ApiCallback<BaseModel<Index>>() {
            @Override
            public void onSucssce(BaseModel<Index> indexBaseModel) {

                Index index=indexBaseModel.object;
                bannerList.addAll(index.bannerList);
                recommandList.addAll(index.recommandList);

            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    protected void setListener() {

    }
}
