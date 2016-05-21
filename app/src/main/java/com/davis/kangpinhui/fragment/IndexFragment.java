package com.davis.kangpinhui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.MainActivity;
import com.davis.kangpinhui.Model.Banner;
import com.davis.kangpinhui.Model.Index;
import com.davis.kangpinhui.Model.Product;
import com.davis.kangpinhui.Model.basemodel.BaseModel;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.SearchActivity;
import com.davis.kangpinhui.activity.ShopActivity;
import com.davis.kangpinhui.adapter.base.CommonBaseAdapter;
import com.davis.kangpinhui.adapter.base.ViewHolder;
import com.davis.kangpinhui.adapter.recycleradapter.CommonRecyclerAdapter;
import com.davis.kangpinhui.api.ApiCallback;
import com.davis.kangpinhui.api.ApiInstant;
import com.davis.kangpinhui.fragment.base.BaseFragment;
import com.davis.kangpinhui.util.CommonManager;
import com.davis.kangpinhui.util.LogUtils;
import com.davis.kangpinhui.util.ToastUitl;
import com.davis.kangpinhui.views.MySwipeRefreshLayout;
import com.davis.kangpinhui.views.loopbanner.LoopBanner;
import com.davis.kangpinhui.views.loopbanner.LoopPageAdapter;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by davis on 16/5/18.
 */
public class IndexFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout index_rechange;
    private LinearLayout index_tuan;
    private LinearLayout index_classic;
    private LoopBanner index_loopbanner;
    private LinearLayout headerView;

    private ListView content;
    private LinearLayout index_cart;
    private LinearLayout index_search;
    private TextView index_local_select;
    private LinearLayout index_local_select_linear;
    private MySwipeRefreshLayout index_refresh;

    private boolean isRefreshOrLoad=false;

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

        headerView= (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_index_header,null);

        index_loopbanner=$(headerView,R.id.index_loopbanner);
        index_classic=$(headerView,R.id.index_classic);
        index_tuan=$(headerView,R.id.index_tuan);
        index_rechange=$(headerView,R.id.index_rechange);


        index_refresh=$(view,R.id.index_refresh);
        index_local_select=$(view,R.id.index_local_select);
        index_local_select_linear=$(view,R.id.index_local_select_linear);
        index_cart=$(view,R.id.index_cart);
        index_search=$(view,R.id.index_search);

        content=$(view,R.id.content);
        content.addHeaderView(headerView);
        index_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                curPage = 0;
                isRefreshOrLoad = true;
                LogUtils.e(TAG, "isRefreshOrLoad");
                getDate();
//                getShowFilmBannerData();
//                getShowFilmListData(curPage, limit);
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
        LogUtils.e(TAG, "onFragmentLoading");
        isRefreshOrLoad=true;
        getDate();
//        CommonManager.setRefreshingState(index_refresh, true);
    }

    private void getDate() {
        Call<BaseModel<Index>> call= ApiInstant.getInstant().getIndex(AppApplication.apptype,AppApplication.shopid,"");
        call.enqueue(new ApiCallback<BaseModel<Index>>() {
            @Override
            public void onSucssce(BaseModel<Index> indexBaseModel) {
                onFragmentLoadingSuccess();//显示内容区
                CommonManager.setRefreshingState(index_refresh, false);//隐藏下拉刷新
                if (isRefreshOrLoad) {
                    bannerList.clear();
                    recommandList.clear();
                }
                Index index = indexBaseModel.object;
                bannerList.addAll(index.bannerList);
                recommandList.addAll(index.recommandList);
                getBannerData();
                getContentData();
            }

            @Override
            public void onFailure() {
                if (isRefreshOrLoad) {
                    onFragmentLoadingFailed();//显示刷新失败网络异常
                    CommonManager.setRefreshingState(index_refresh, false);//隐藏下拉刷新
                } else {
//                    listShowFilm.onLoadFailed();//加载失败
                }
            }
        });
    }

    public void getBannerData(){
        index_loopbanner.setPageAdapter(new LoopPageAdapter<Banner>(mContext, bannerList, R.layout.layout_main_banner_item) {

            @Override
            public void convert(ViewHolder holder, final Banner itemData, final int position) {
                // TODO Auto-generated method stub
                ImageView imageView = (ImageView) holder.getConvertView();
                String img = itemData.picurl;
                Glide.with(getActivity()).load(img).into(imageView);
//                        .placeholder(R.drawable.placeholder)
//                        .error(R.drawable.imagenotfound)

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = itemData.picurl;
//                        CommonManager.processBannerClick(mContext, url);
                    }
                });
            }
        });
    }


    public void getContentData(){
        content.setAdapter(new CommonBaseAdapter<Index.Productlist>(getActivity(), recommandList, R.layout.fragment_index_item_layout) {
            @Override
            public void convert(ViewHolder holder, Index.Productlist itemData, int position) {

                String image = itemData.picurl;
                ArrayList<Product> list = itemData.list;
                LogUtils.e(TAG, list.toString());

                holder.setImageByUrl(R.id.fragment_index_item_image, image);

                    getContentitemData((RecyclerView) holder.getView(R.id.fragment_index_item_recycler), list);
            }
        });
    }

    private CommonRecyclerAdapter<Product> adapter;
    public void getContentitemData(RecyclerView loadrecycler,final ArrayList<Product> list){


        adapter= new CommonRecyclerAdapter<Product>(getActivity(),list,R.layout.fragment_index_item_layout_item){
            @Override
            public void convert(BaseViewHolder holder, Product itemData, int position) {

                ImageView iv=holder.getView(R.id.fragment_index_item_image_item);
                Glide.with(getActivity()).load(itemData.picurl).into(iv);

                TextView tv_name=holder.getView(R.id.fragment_index_item_name);
                tv_name.setText(itemData.productname);

                TextView tv_price=holder.getView(R.id.fragment_index_item_price);
                tv_price.setText(itemData.fprice);
            }
        };

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        loadrecycler.setLayoutManager(linearLayoutManager);

        loadrecycler.setAdapter(adapter);
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
    protected void setListener() {

        index_local_select_linear.setOnClickListener(this);
        index_classic.setOnClickListener(this);
        index_rechange.setOnClickListener(this);
        index_tuan.setOnClickListener(this);
        index_cart.setOnClickListener(this);
        index_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.index_local_select_linear:
                Intent it=new Intent(getActivity(), ShopActivity.class);
                startActivityForResult(it,0);
                break;
            case R.id.index_classic:
                MainActivity activity= (MainActivity) getActivity();
                activity.change2();
                break;
            case R.id.index_rechange:
                break;
            case R.id.index_tuan:

                break;
            case R.id.index_cart:

                break;
            case R.id.index_search:
                SearchActivity.jumpSearchActivity(getActivity(), "");
                break;
        }
    }
}
