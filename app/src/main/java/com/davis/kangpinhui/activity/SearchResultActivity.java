package com.davis.kangpinhui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.Model.Category;
import com.davis.kangpinhui.Model.Product;
import com.davis.kangpinhui.Model.basemodel.BaseModel;
import com.davis.kangpinhui.Model.basemodel.Page;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;
import com.davis.kangpinhui.adapter.base.CommonBaseAdapter;
import com.davis.kangpinhui.adapter.base.ViewHolder;
import com.davis.kangpinhui.adapter.recycleradapter.CommonRecyclerAdapter;
import com.davis.kangpinhui.api.ApiCallback;
import com.davis.kangpinhui.api.ApiInstant;
import com.davis.kangpinhui.util.CommonManager;
import com.davis.kangpinhui.util.ToastUitl;
import com.davis.kangpinhui.views.LoadMoreRecyclerView;
import com.davis.kangpinhui.views.MySwipeRefreshLayout;
import com.davis.kangpinhui.views.viewpagerindicator.scrollbar.ScrollBar;

import java.util.ArrayList;

import retrofit2.Call;

public class SearchResultActivity extends BaseActivity {

    private String key = "";
    private boolean isSearch=true;
    private LinearLayout search_back;
    private LinearLayout search_right_iv;
    private EditText search_et;
    private LoadMoreRecyclerView search_result_recycler;
    private MySwipeRefreshLayout search_result_myswipe;
    private LinearLayout search_all_classic;
    private LinearLayout search_all_sort;
    private TextView search_all_classic_text;
    private TextView search_all_sort_text;

    private int Page = 0;
    private int PageSize = 20;
    private int TotalPage = 0;
    private CommonRecyclerAdapter<Product> adapter;
    private ArrayList<Product> list;

    private boolean isLoadOrRefresh = false;
    private String sortid="0";
    private String rootid="0";
    private String classid="0";


    private PopupWindow classicpopupWindow;
    private PopupWindow sortpopupWindow;

    public static void jumpSearchResultActivity(Context con, String key,boolean isSearch) {
        Intent it = new Intent(con, SearchResultActivity.class);
        it.putExtra("key", key);
        it.putExtra("issearch", isSearch);
        con.startActivity(it);
    }
    public static void jumpSearchResultActivity(Context con, String key,boolean isSearch,String classid,String rootid) {
        Intent it = new Intent(con, SearchResultActivity.class);
        it.putExtra("key", key);
        it.putExtra("issearch", isSearch);
        it.putExtra("classid", classid);
        it.putExtra("rootid", rootid);
        con.startActivity(it);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_search_result;
    }

    @Override
    protected void initVariable() {

        key = getIntent().getStringExtra("key");
        isSearch = getIntent().getBooleanExtra("issearch", false);
        classid=getIntent().getStringExtra("classid");
        rootid=getIntent().getStringExtra("rootid");
        if(TextUtils.isEmpty(classid)){
            classid="0";
        }
        if(TextUtils.isEmpty(rootid)){
            rootid="0";
        }
        list = new ArrayList<>();
    }

    @Override
    protected void findViews() {
        search_back = $(R.id.search_back);
        search_right_iv = $(R.id.search_right_iv);
        search_et = $(R.id.search_et);
        search_result_recycler = $(R.id.search_result_recycler);
        search_result_myswipe = $(R.id.content);
        search_all_classic = $(R.id.search_all_classic);
        search_all_sort = $(R.id.search_all_sort);
        search_all_classic_text = $(R.id.search_all_classic_text);
        search_all_sort_text = $(R.id.search_all_sort_text);

        search_et.setText(key);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        search_result_recycler.setLayoutManager(gridLayoutManager);

        adapter = new CommonRecyclerAdapter<Product>(this, list, R.layout.activity_search_result_item) {
            @Override
            public void convert(BaseViewHolder holder, Product itemData, int position) {

                ImageView iv = holder.getView(R.id.search_result_item_iv);
                Glide.with(mActivity).load(itemData.picurl).into(iv);

                TextView tv_name = holder.getView(R.id.search_result_item_name);
                tv_name.setText(itemData.productname);

                TextView tv_price = holder.getView(R.id.search_result_item_price);
                tv_price.setText(itemData.fprice);
            }
        };
        View footerView = mInflater.inflate(R.layout.layout_load_more_footer, null);
        adapter.addFooterView(footerView);
        search_result_recycler.setAdapter(adapter);
        search_result_myswipe.setDistanceToTriggerSync(CommonManager.dpToPx(200));
        search_result_myswipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Page = 1;
                isLoadOrRefresh = true;
                if(!isSearch){
                    getProductList(Page,PageSize);
                }else {
                    getSearchProductList(Page, PageSize);
                }

            }
        });

    }

    @Override
    protected void onActivityLoading() {
        super.onActivityLoading();
        Page = 1;
        isLoadOrRefresh = true;
        if(isSearch){
            getSearchProductList(Page, PageSize);
        }else{
        getProductList(Page, PageSize);}
    }

    @Override
    protected void initData() {
        startActivityLoading();
        initPopupClassicWindow();
        initPopupSortWindow();
    }

    @Override
    protected void setListener() {

        adapter.setOnItemClickLitener(new CommonRecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View itemView, int position) {
                ToastUitl.showToast("" + position);
            }

            @Override
            public void onItemLongClick(View itemView, int position) {

            }
        });
        search_result_recycler.setOnLoadListener(new LoadMoreRecyclerView.OnLoadListener() {
            @Override
            public void onLoad(LoadMoreRecyclerView recyclerView) {

                if(isSearch){
                    getSearchProductList(++Page, PageSize);
                }else{
                   getProductList(++Page, PageSize);
                }
                isLoadOrRefresh = false;
            }


        });
    }

    /**
     * 搜索list
     *
     * @param page
     * @param pagesize
     */
    private void getSearchProductList(int page, int pagesize) {
        Call<BaseModel<Page<ArrayList<Product>>>> call = ApiInstant.getInstant().getSearchProductlist(AppApplication.apptype, sortid,
                AppApplication.shopid, key, page + "", pagesize + "");

        call.enqueue(new ApiCallback<BaseModel<com.davis.kangpinhui.Model.basemodel.Page<ArrayList<Product>>>>() {
            @Override
            public void onSucssce(BaseModel<Page<ArrayList<Product>>> pageBaseModel) {

                CommonManager.setRefreshingState(search_result_myswipe, false);//隐藏下拉刷新
                onActivityLoadingSuccess();
                if (isLoadOrRefresh) {
                    list.clear();
                }
                Page<ArrayList<Product>> page = pageBaseModel.object;

                TotalPage = page.iTotalPage;

                list.addAll(page.list);
                adapter.notifyDataSetChanged();

                if (list.size() == 0) {
                    onActivityFirstLoadingNoData();
                    search_result_recycler.onLoadUnavailable();
                } else if (TotalPage == Page ) {
                    search_result_recycler.onLoadSucess(false);
                } else {
                    search_result_recycler.onLoadSucess(true);
                }
            }

            @Override
            public void onFailure() {
                CommonManager.setRefreshingState(search_result_myswipe, false);//隐藏下拉刷新
                onActivityLoadingFailed();
                if (!isLoadOrRefresh) {
                    search_result_recycler.onLoadFailed();
                }
            }
        });
    }

    /**
     * 商品list
     *
     * @param page
     * @param pagesize
     */
    private void getProductList(int page, int pagesize) {
        Call<BaseModel<Page<ArrayList<Product>>>> call = ApiInstant.getInstant().getProductlist(AppApplication.apptype, sortid,
                rootid,classid,AppApplication.shopid, page + "", pagesize + "");

        call.enqueue(new ApiCallback<BaseModel<com.davis.kangpinhui.Model.basemodel.Page<ArrayList<Product>>>>() {
            @Override
            public void onSucssce(BaseModel<Page<ArrayList<Product>>> pageBaseModel) {

                CommonManager.setRefreshingState(search_result_myswipe, false);//隐藏下拉刷新
                onActivityLoadingSuccess();
                if (isLoadOrRefresh) {
                    list.clear();
                }
                Page<ArrayList<Product>> page = pageBaseModel.object;

                TotalPage = page.iTotalPage;

                list.addAll(page.list);
                adapter.notifyDataSetChanged();

                if (list.size() == 0) {
                    onActivityFirstLoadingNoData();
                    search_result_recycler.onLoadUnavailable();
                } else if (TotalPage != Page ) {
                    search_result_recycler.onLoadSucess(true);
                } else {
                    search_result_recycler.onLoadSucess(false);
                }
            }

            @Override
            public void onFailure() {
                CommonManager.setRefreshingState(search_result_myswipe, false);//隐藏下拉刷新
                onActivityLoadingFailed();
                if (!isLoadOrRefresh) {
                    search_result_recycler.onLoadFailed();
                }
            }
        });
    }

    @Override
    public void doClick(View view) {

        switch (view.getId()) {
            case R.id.search_back:
                break;
            case R.id.search_all_sort:
                sortpopupWindow.showAsDropDown(search_all_classic,0,5);
                break;
            case R.id.search_all_classic:
                classicpopupWindow.showAsDropDown(search_all_classic,0,5);
                break;
            case R.id.search_right_iv:
                key = search_et.getText().toString().trim();
                if (!TextUtils.isEmpty(key)) {
                    CommonManager.dismissSoftInputMethod(this, view.getWindowToken());
                    isSearch=true;
                    startActivityLoading();
                }
                break;
        }
    }


    private ListView pop_list_classic_main;
    private ListView pop_list_classic;

    //初始化分类
    private void initPopupClassicWindow() {
        // TODO Auto-generated method stub
        View view = getLayoutInflater().inflate(R.layout.activity_search_result_pop_classic, null);
        pop_list_classic_main=$(view,R.id.pop_list_classic_main);
        pop_list_classic=$(view,R.id.pop_list_classic);
        classicpopupWindow = new PopupWindow(view,
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        classicpopupWindow.setFocusable(true);
        classicpopupWindow.setOutsideTouchable(true);
        classicpopupWindow.setAnimationStyle(R.style.popwin_recent_anim_style);
        classicpopupWindow.setBackgroundDrawable(new BitmapDrawable());
        classicpopupWindow.setOnDismissListener(new popupWindowclickListener());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classicpopupWindow != null && classicpopupWindow.isShowing()) {
                    classicpopupWindow.dismiss();
                }
            }
        });
        getClassicData();
    }

    private ListView pop_list_classic_sort;
    //初始化排序
    private void initPopupSortWindow() {
        // TODO Auto-generated method stub
        View view = getLayoutInflater().inflate(R.layout.activity_search_result_pop_classic, null);
        ListView pop_list_classic_main=$(view, R.id.pop_list_classic_main);
        pop_list_classic_main.setVisibility(View.GONE);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        pop_list_classic_sort=$(view,R.id.pop_list_classic);
        pop_list_classic_sort.setLayoutParams(params);
        sortpopupWindow = new PopupWindow(view,
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        sortpopupWindow.setFocusable(true);
        sortpopupWindow.setOutsideTouchable(true);
        sortpopupWindow.setAnimationStyle(R.style.popwin_recent_anim_style);
        sortpopupWindow.setBackgroundDrawable(new BitmapDrawable());
        sortpopupWindow.setOnDismissListener(new popupWindowclickListener());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortpopupWindow != null && sortpopupWindow.isShowing()) {
                    sortpopupWindow.dismiss();
                }
            }
        });
        getSortData();
    }

    private void getSortData() {
        final ArrayList<String> list=new ArrayList<>();
        list.add("销量排序");
        list.add("价格排序");
        list.add("最新上线");
        pop_list_classic_sort.setAdapter(new CommonBaseAdapter<String>(this, list, R.layout.fragment_classic_left_item) {
            @Override
            public void convert(ViewHolder holder, String itemData, int position) {
                TextView textView = holder.getView(R.id.classic_rootid_list_item);
                textView.setTextColor(getResources().getColor(R.color.black));
                textView.setText(itemData);
            }
        });
        pop_list_classic_sort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    sortid = "0";
                } else if (position == 1) {
                    sortid = "2";
                } else {
                    sortid = "3";
                }
                closePopuw();
                Page = 1;
                isLoadOrRefresh = true;
                if(isSearch){
                    getSearchProductList(Page,PageSize);
                }else{
                    getProductList(Page,PageSize);
                }
                search_all_sort_text.setText(list.get(position));
            }
        });
    }


    private void getClassicData(){
        if(AppApplication.classiclist.size()>0){
            if(!AppApplication.classiclist.get(0).name.equals("全部分类")) {
                Category category = new Category();
                category.name = "全部分类";
                category.id = "0";
                Category category1 = new Category();
                category1.name = "全部";
                category1.id = "0";
                category.clist.add(category1);
                AppApplication.classiclist.add(0, category);
            }
            AppApplication.classiclist.get(0).isOnclick=true;
            bindClassicView();
        }else {
            Call<BaseModel<ArrayList<Category>>> call = ApiInstant.getInstant().categoryLevel2(AppApplication.apptype, "");
            call.enqueue(new ApiCallback<BaseModel<ArrayList<Category>>>() {
                @Override
                public void onSucssce(BaseModel<ArrayList<Category>> arrayListBaseModel) {
                    AppApplication.classiclist.addAll(arrayListBaseModel.object);
                    getClassicData();
                }
                @Override
                public void onFailure() {
                }
            });
        }
    }

    private void bindClassicView(){
        final  CommonBaseAdapter adapter=new CommonBaseAdapter<Category>(this,AppApplication.classiclist,R.layout.fragment_classic_left_item) {
            @Override
            public void convert(ViewHolder holder, Category itemData, int position) {
                TextView textView=holder.getView(R.id.classic_rootid_list_item);
                textView.setText(itemData.name);
                if(itemData.isOnclick){

                    textView.setTextColor(getResources().getColor(R.color.colormain));
                }else {
                    textView.setTextColor(getResources().getColor(R.color.black));
                }
            }
        };
        pop_list_classic_main.setAdapter(adapter);
        pop_list_classic.setAdapter(new CommonBaseAdapter<Category>(SearchResultActivity.this,AppApplication.classiclist.get(0).clist,R.layout.fragment_classic_left_item) {
            @Override
            public void convert(ViewHolder holder, Category itemData, int position) {
                TextView textView=holder.getView(R.id.classic_rootid_list_item);
                textView.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
                textView.setTextColor(getResources().getColor(R.color.black));
                textView.setText(itemData.name);
            }
        });
        pop_list_classic_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for (Category category : AppApplication.classiclist) {
                    category.isOnclick = false;
                }
                AppApplication.classiclist.get(position).isOnclick = true;
                rootid = AppApplication.classiclist.get(position).id;
                adapter.notifyDataSetChanged();
                pop_list_classic.setAdapter(new CommonBaseAdapter<Category>(SearchResultActivity.this, AppApplication.classiclist.get(position).clist, R.layout.fragment_classic_left_item) {
                    @Override
                    public void convert(ViewHolder holder, Category itemData, int position) {
                        TextView textView = holder.getView(R.id.classic_rootid_list_item);
                        textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                        textView.setTextColor(getResources().getColor(R.color.black));
                        textView.setText(itemData.name);
                    }
                });
            }
        });

        pop_list_classic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long ids) {
                Category category=null;
                for(Category category1 : AppApplication.classiclist){
                    if(category1.id.equals(rootid)){
                        category=category1;
                    }
                }
                if(category!=null)
                classid= category.clist.get(position).id;
                Page = 0;
                isLoadOrRefresh = true;
                isSearch=false;
                getProductList(Page++,PageSize);
                closePopuw();
                search_et.setText("");
                search_all_classic_text.setText(category.clist.get(position).name);
            }
        });
    }

    private void closePopuw(){
        if (sortpopupWindow != null && sortpopupWindow.isShowing()) {
            sortpopupWindow.dismiss();
        }
        if (classicpopupWindow != null && classicpopupWindow.isShowing()) {
            classicpopupWindow.dismiss();
        }
    }
    class popupWindowclickListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
//            cinema_expandable_image_city.setImageResource(R.drawable.price_expandable_close);
//            cinema_expandable_image_region.setImageResource(R.drawable.price_expandable_close);
        }
    }
}
