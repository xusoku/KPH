package com.davis.kangpinhui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
import com.davis.kangpinhui.db.SearchHistroy;
import com.davis.kangpinhui.db.SearchHistroyDao;
import com.davis.kangpinhui.model.Category;
import com.davis.kangpinhui.model.Extendedinfo;
import com.davis.kangpinhui.model.Index;
import com.davis.kangpinhui.model.Product;
import com.davis.kangpinhui.model.Topic;
import com.davis.kangpinhui.model.basemodel.BaseModel;
import com.davis.kangpinhui.model.basemodel.Page;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;
import com.davis.kangpinhui.adapter.base.CommonBaseAdapter;
import com.davis.kangpinhui.adapter.base.ViewHolder;
import com.davis.kangpinhui.adapter.recycleradapter.CommonRecyclerAdapter;
import com.davis.kangpinhui.api.ApiCallback;
import com.davis.kangpinhui.api.ApiInstant;
import com.davis.kangpinhui.util.CommonManager;
import com.davis.kangpinhui.util.DisplayMetricsUtils;
import com.davis.kangpinhui.util.LogUtils;
import com.davis.kangpinhui.util.UtilText;
import com.davis.kangpinhui.views.BadgeView;
import com.davis.kangpinhui.views.LoadMoreRecyclerView;
import com.davis.kangpinhui.views.MySwipeRefreshLayout;

import java.util.ArrayList;
import java.util.logging.Handler;

import de.greenrobot.event.EventBus;
import retrofit2.Call;

public class SearchResultActivity extends BaseActivity {

    private String key = "";
    private LinearLayout search_back;
    private LinearLayout search_right_iv;
    private EditText search_et;
    private LoadMoreRecyclerView search_result_recycler;
    private MySwipeRefreshLayout search_result_myswipe;
    private LinearLayout search_all_classic;
    private LinearLayout search_all_sort;
    private TextView search_all_classic_text;
    private TextView search_all_sort_text;
    private TextView search_et_text;
    private int Page = 0;

    private int PageSize = 20;
    private int TotalPage = 0;
    private CommonRecyclerAdapter<Product> adapter;
    private ArrayList<Product> list;
    private boolean isLoadOrRefresh = false;

    private String sortid = "0";
    private String rootid = "0";
    private String classid = "0";

    //判断是不是从搜索进来的
    private boolean isSearch = true;
    //判断是不是从搜索进来的
    private boolean isProductlist = true;

    //专题数据判断
    private boolean type = false;
    private String activid = "";

    private PopupWindow classicpopupWindow;
    private PopupWindow sortpopupWindow;

    private CardView search_result_card;
    private LinearLayout search_result_title_linear;
    private AddCartPopuWindow addCartPopuWindow;

    private BadgeView backgroundDefaultBadge;

    /**
     * 搜索
     */
    public static void jumpSearchResultActivity(Context con, String key, boolean isSearch) {
        Intent it = new Intent(con, SearchResultActivity.class);
        it.putExtra("key", key);
        it.putExtra("issearch", isSearch);
        con.startActivity(it);
    }

    /**
     * 专题进入
     */
    public static void jumpSearchResultActivity(Context con, String title, boolean type, String activid) {
        Intent it = new Intent(con, SearchResultActivity.class);
        it.putExtra("key", title);
        it.putExtra("type", type);
        it.putExtra("activid", activid);
        con.startActivity(it);
    }

    /**
     * 分类
     */
    public static void jumpSearchResultActivity(Context con, String key, boolean isSearch, String classid, String rootid) {
        Intent it = new Intent(con, SearchResultActivity.class);
        it.putExtra("key", key);
        it.putExtra("issearch", isSearch);
        it.putExtra("classid", classid);
        it.putExtra("rootid", rootid);
        con.startActivity(it);
    }
    /**
     *
     */
    public static void jumpSearchResultActivity(Context con, String key,boolean isSearch, boolean isProductlist, String classid, String rootid) {
        Intent it = new Intent(con, SearchResultActivity.class);
        it.putExtra("key", key);
        it.putExtra("isProductlist", isProductlist);
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
        type = getIntent().getBooleanExtra("type", false);
        isProductlist = getIntent().getBooleanExtra("isProductlist", false);
        activid = getIntent().getStringExtra("activid");
        if(TextUtils.isEmpty(activid)){
            activid="";
        }
        if (!type) {
            isSearch = getIntent().getBooleanExtra("issearch", false);
            classid = getIntent().getStringExtra("classid");
            rootid = getIntent().getStringExtra("rootid");
            if (TextUtils.isEmpty(classid)) {
                classid = "0";
            }
            if (TextUtils.isEmpty(rootid)) {
                rootid = "0";
            }
        }

        list = new ArrayList<>();
    }

    @Override
    protected void findViews() {

        EventBus.getDefault().register(this);

        search_result_title_linear = $(R.id.search_result_title_linear);
        search_result_card = $(R.id.search_result_card);


        search_back = $(R.id.search_back);
        search_right_iv = $(R.id.search_right_iv);
        search_et = $(R.id.search_et);
        search_et_text = $(R.id.search_et_text);
        search_result_recycler = $(R.id.search_result_recycler);
        search_result_myswipe = $(R.id.content);
        search_all_classic = $(R.id.search_all_classic);
        search_all_sort = $(R.id.search_all_sort);
        search_all_classic_text = $(R.id.search_all_classic_text);
        search_all_sort_text = $(R.id.search_all_sort_text);

        if (type||isProductlist) {
            search_et_text.setVisibility(View.VISIBLE);
            search_et.setVisibility(View.GONE);
            search_result_card.setVisibility(View.GONE);
            search_et_text.setText(key);
        } else {
            search_et_text.setVisibility(View.GONE);
            search_et.setVisibility(View.VISIBLE);
            search_et.setText(key);
        }


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        search_result_recycler.setLayoutManager(gridLayoutManager);

        adapter = new CommonRecyclerAdapter<Product>(this, list, R.layout.activity_search_result_item) {
            @Override
            public void convert(final BaseViewHolder holder, final Product itemData, int position) {

                ImageView iv = holder.getView(R.id.search_result_item_iv);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) DisplayMetricsUtils.getWidth() / 2 - 30, (int) DisplayMetricsUtils.getWidth() / 2 - 30);
                iv.setLayoutParams(layoutParams);
                Glide.with(mActivity).load(itemData.picurl)
                        .placeholder(R.mipmap.img_defualt_bg)
                        .error(R.mipmap.img_defualt_bg)
                        .into(iv);

                TextView tv_name = holder.getView(R.id.search_result_item_name);
                tv_name.setText(itemData.productname);
                LinearLayout search_cart_iv = holder.getView(R.id.search_cart_iv);
                TextView tv_price = holder.getView(R.id.search_result_item_price);
                LinearLayout search_result_item_linear = holder.getView(R.id.search_result_item_linear);
                if (type && activid.equals("jifen")) {
                    tv_price.setText("");
                    tv_price.append(UtilText.getBigProductDetail(itemData.score + ""));
                    tv_price.append("积分");
                    tv_price.append("/" + itemData.sstandard);
                    search_cart_iv.setVisibility(View.GONE);
                    search_result_item_linear.setVisibility(View.VISIBLE);

                    holder.getView(R.id.search_result_item_jifen).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                             TextView numbertv = holder.getView(R.id.add_cart_add_center);
                             String number = numbertv.getText().toString().trim();
                            OrderActivity.jumpOrderActivity(SearchResultActivity.this, itemData.iproductid, itemData, true, number);
                        }
                    });

                    holder.getView(R.id.add_cart_add).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String number =((TextView)holder.getView(R.id.add_cart_add_center)).getText().toString().trim();
                            int n = Integer.valueOf(number);
                            n++;
                            ((TextView)holder.getView(R.id.add_cart_add_center)).setText((n + ""));
                        }
                    });

                    holder.getView(R.id.add_cart_add_mins).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String number = ((TextView)holder.getView(R.id.add_cart_add_center)).getText().toString().trim();
                            int n = Integer.valueOf(number);
                            if (n <= 1) {
                                n = 1;
                            } else {
                                n--;
                                if (n == 0) {
                                    n = 1;
                                }
                            }
                            ((TextView)holder.getView(R.id.add_cart_add_center)).setText((n + ""));
                        }
                    });

                } else {
                    tv_price.setText("");
                    tv_price.append(UtilText.getIndexPrice("¥"));
                    tv_price.append(UtilText.getBigProductDetail(itemData.fprice));
                    tv_price.append("/" + itemData.sstandard);
                    search_cart_iv.setVisibility(View.VISIBLE);
                    search_result_item_linear.setVisibility(View.GONE);
                }

                TextView textView = holder.getView(R.id.search_result_item_name_tv);
                String text = itemData.prostate;
                if (TextUtils.isEmpty(text)) {
                    textView.setVisibility(View.GONE);
                } else {
                    textView.setText(text);
                    textView.setVisibility(View.VISIBLE);
                }


                holder.getView(R.id.search_cart_iv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addCartPopuWindow.setBindPopData(itemData);
                        if (addCartPopuWindow.isShowPW(itemData)) {
                            addCartPopuWindow.addpopupWindow.showAtLocation(search_result_title_linear, Gravity.NO_GRAVITY, 0, 0);
                        } else {
                            addCartPopuWindow.onClickAdd();
                        }
                    }
                });
            }
        };
        if (!type) {
            View footerView = mInflater.inflate(R.layout.layout_load_more_footer, null);
            adapter.addFooterView(footerView);
        }
        search_result_recycler.setAdapter(adapter);
        search_result_myswipe.setDistanceToTriggerSync(CommonManager.dpToPx(200));
        search_result_myswipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Page = 1;
                isLoadOrRefresh = true;

                if (type) {
                    if (activid.equals("jifen")) {
                        getMScorelist(Page, PageSize);
                    } else {
                        getActivedlist();
                    }
                } else {
                    if (isSearch) {
                        getSearchProductList(Page, PageSize);
                    } else {
                        if(isProductlist){
                            getProductListbyids(Page, PageSize);
                        }else{
                            getProductList(Page, PageSize);
                        }
                    }
                }

            }
        });

        addCartPopuWindow = new AddCartPopuWindow(this);
        backgroundDefaultBadge = new BadgeView(this);
        backgroundDefaultBadge.setTargetView(search_right_iv);
    }

    @Override
    protected void onActivityLoading() {
        super.onActivityLoading();
        Page = 1;
        isLoadOrRefresh = true;

        if (type) {
            if (activid.equals("jifen")) {
                getMScorelist(Page, PageSize);
            } else {
                getActivedlist();
            }
        } else {
            if (isSearch) {
                getSearchProductList(Page, PageSize);
            } else {
                if(isProductlist){
                    getProductListbyids(Page, PageSize);
                }else {
                    getProductList(Page, PageSize);
                }
            }
        }
    }

    @Override
    protected void initData() {
        startActivityLoading();
        initPopupClassicWindow();
        initPopupSortWindow();
        setcartNumber();
    }

    //添加成功购物车 需要调用
    public void setUIOrder() {
        Call<BaseModel<Extendedinfo>> call = ApiInstant.getInstant().getExtendedInfo(AppApplication.apptype,
                AppApplication.shopid, AppApplication.token);

        call.enqueue(new ApiCallback<BaseModel<Extendedinfo>>() {
            @Override
            public void onSucssce(BaseModel<Extendedinfo> extendedinfoBaseModel) {

                AppApplication.extendedinfo = extendedinfoBaseModel.object;
                EventBus.getDefault().post(new Extendedinfo());
                setcartNumber();
            }

            @Override
            public void onFailure() {

            }
        });
    }


    public void setcartNumber() {
        String number = AppApplication.getCartcount();
        if (!TextUtils.isEmpty(number) && !number.equals("0") && !number.equals("0.0") && backgroundDefaultBadge != null) {
            backgroundDefaultBadge.setVisibility(View.VISIBLE);
            backgroundDefaultBadge.setText((int) Float.parseFloat(number) + "");
        } else {
            setcartNumberLoginout();
        }
    }

    public void setcartNumberLoginout() {
        if (backgroundDefaultBadge != null)
            backgroundDefaultBadge.setVisibility(View.GONE);
    }

    @Override
    protected void setListener() {

        adapter.setOnItemClickLitener(new CommonRecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View itemView, int position) {
                ProductDetailActivity.jumpProductDetailActivity(SearchResultActivity.this, list.get(position).iproductid);
            }

            @Override
            public void onItemLongClick(View itemView, int position) {

            }
        });
        if (type) {
            if (activid.equals("jifen")) {
                search_result_recycler.setOnLoadListener(new LoadMoreRecyclerView.OnLoadListener() {
                    @Override
                    public void onLoad(LoadMoreRecyclerView recyclerView) {
                        getMScorelist(++Page, PageSize);
                        isLoadOrRefresh = false;
                    }
                });
            }
        } else {
            search_result_recycler.setOnLoadListener(new LoadMoreRecyclerView.OnLoadListener() {
                @Override
                public void onLoad(LoadMoreRecyclerView recyclerView) {

                    if (isSearch) {
                        getSearchProductList(++Page, PageSize);
                    } else {
                        if(isProductlist){
                            getProductListbyids(++Page, PageSize);
                        }else {
                            getProductList(++Page, PageSize);
                        }
                    }
                    isLoadOrRefresh = false;
                }
            });
        }

        search_et.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int arg1,
                                          KeyEvent arg2) {
                if (arg1 == EditorInfo.IME_ACTION_SEARCH
                        || arg1 == EditorInfo.IME_ACTION_GO
                        || arg1 == EditorInfo.IME_ACTION_NEXT
                        || arg1 == EditorInfo.IME_ACTION_SEND
                        || arg1 == EditorInfo.IME_ACTION_DONE
                        || arg1 == EditorInfo.IME_NULL) {
                    String keyu = search_et.getText().toString()
                            .trim();
                    // 去除空格
                    if (!TextUtils.isEmpty(keyu)) {
                        key = keyu;
                        CommonManager.dismissSoftInputMethod(SearchResultActivity.this, search_et.getWindowToken());
                        isSearch = true;
                        startActivityLoading();
                        SearchHistroy histroy = new SearchHistroy();
                        histroy.setKey(key);
                        new SearchHistroyDao(SearchResultActivity.this).add(histroy);
                    } else {
                        return false;
                    }
                }
                return true;
            }
        });
    }

    /**
     * 专题数据
     */
    private void getActivedlist() {

        if (activid.equals("youlike")) {
            getYouLikelist();
            return;
        }
        if (activid.equals("index_tuan")) {
            getIndexTuanlist();
            return;
        }
        Call<BaseModel<Topic<ArrayList<Product>>>> call = ApiInstant.getInstant().getActivelist(AppApplication.apptype, AppApplication.shopid, activid);

        call.enqueue(new ApiCallback<BaseModel<Topic<ArrayList<Product>>>>() {
            @Override
            public void onSucssce(BaseModel<Topic<ArrayList<Product>>> topicBaseModel) {
                CommonManager.setRefreshingState(search_result_myswipe, false);//隐藏下拉刷新
                onActivityLoadingSuccess();
                if (isLoadOrRefresh) {
                    list.clear();
                }
                list.addAll(topicBaseModel.object.list);
                adapter.notifyDataSetChanged();
                if (list.size() == 0) {
                    onActivityFirstLoadingNoData();
                }
                search_result_recycler.onLoadUnavailable();
            }

            @Override
            public void onFailure() {
                CommonManager.setRefreshingState(search_result_myswipe, false);//隐藏下拉刷新
                onActivityLoadingFailed();
            }
        });
    }

    /**
     * 积分兑换
     *
     * @param page
     * @param pagesize
     */
    private void getMScorelist(int page, int pagesize) {
        Call<BaseModel<Page<ArrayList<Product>>>> call = ApiInstant.getInstant().getScoreList(AppApplication.apptype, page + "", pagesize + "", "0", AppApplication.token);

        call.enqueue(new ApiCallback<BaseModel<com.davis.kangpinhui.model.basemodel.Page<ArrayList<Product>>>>() {
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
                } else if (TotalPage != Page) {
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

    /**
     * 猜你喜欢
     */
    private void getYouLikelist() {

        Call<BaseModel<ArrayList<Product>>> call = ApiInstant.getInstant().getYoulikelist(AppApplication.apptype, AppApplication.shopid, AppApplication.token, "10");

        call.enqueue(new ApiCallback<BaseModel<ArrayList<Product>>>() {
            @Override
            public void onSucssce(BaseModel<ArrayList<Product>> topicBaseModel) {
                CommonManager.setRefreshingState(search_result_myswipe, false);//隐藏下拉刷新
                onActivityLoadingSuccess();
                if (isLoadOrRefresh) {
                    list.clear();
                }
                list.addAll(topicBaseModel.object);
                adapter.notifyDataSetChanged();
                if (list.size() == 0) {
                    onActivityFirstLoadingNoData();
                }
                search_result_recycler.onLoadUnavailable();
            }

            @Override
            public void onFailure() {
                CommonManager.setRefreshingState(search_result_myswipe, false);//隐藏下拉刷新
                onActivityLoadingFailed();
            }
        });
    }

    /**
     * 团购
     */
    private void getIndexTuanlist() {

        Call<BaseModel<ArrayList<Product>>> call = ApiInstant.getInstant().getTuanlist(AppApplication.apptype, AppApplication.shopid);

        call.enqueue(new ApiCallback<BaseModel<ArrayList<Product>>>() {
            @Override
            public void onSucssce(BaseModel<ArrayList<Product>> topicBaseModel) {
                CommonManager.setRefreshingState(search_result_myswipe, false);//隐藏下拉刷新
                onActivityLoadingSuccess();
                if (isLoadOrRefresh) {
                    list.clear();
                }
                list.addAll(topicBaseModel.object);
                adapter.notifyDataSetChanged();
                if (list.size() == 0) {
                    onActivityFirstLoadingNoData();
                }
                search_result_recycler.onLoadUnavailable();
            }

            @Override
            public void onFailure() {
                CommonManager.setRefreshingState(search_result_myswipe, false);//隐藏下拉刷新
                onActivityLoadingFailed();
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

        call.enqueue(new ApiCallback<BaseModel<com.davis.kangpinhui.model.basemodel.Page<ArrayList<Product>>>>() {
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
                } else if (TotalPage == Page) {
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
                rootid, classid, AppApplication.shopid, page + "", pagesize + "");

        call.enqueue(new ApiCallback<BaseModel<com.davis.kangpinhui.model.basemodel.Page<ArrayList<Product>>>>() {
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
                } else if (TotalPage != Page) {
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

    /**
     * 商品list
     *
     * @param page
     * @param pagesize
     */
    private void getProductListbyids(int page, int pagesize) {
        Call<BaseModel<Page<ArrayList<Product>>>> call = ApiInstant.getInstant().getProductlistbyids(AppApplication.apptype, "0",
                rootid, AppApplication.shopid, page + "", pagesize + "", AppApplication.token);

        call.enqueue(new ApiCallback<BaseModel<com.davis.kangpinhui.model.basemodel.Page<ArrayList<Product>>>>() {
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
                } else if (TotalPage != Page) {
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
                finish();
                break;
            case R.id.search_all_sort:
                sortpopupWindow.showAsDropDown(search_all_classic, 0, 5);
                break;
            case R.id.search_all_classic:
                classicpopupWindow.showAsDropDown(search_all_classic, 0, 5);
                break;
            case R.id.search_right_iv:
                CartListActivity.jumpCartListActivity(this);
                break;
        }
    }


    private ListView pop_list_classic_main;
    private ListView pop_list_classic;

    //初始化分类
    private void initPopupClassicWindow() {
        // TODO Auto-generated method stub
        View view = getLayoutInflater().inflate(R.layout.activity_search_result_pop_classic, null);
        pop_list_classic_main = $(view, R.id.pop_list_classic_main);
        pop_list_classic = $(view, R.id.pop_list_classic);
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
        ListView pop_list_classic_main = $(view, R.id.pop_list_classic_main);
        pop_list_classic_main.setVisibility(View.GONE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pop_list_classic_sort = $(view, R.id.pop_list_classic);
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
        final ArrayList<String> list = new ArrayList<>();
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
                search_all_sort_text.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        search_result_recycler.scrollTo(0, 0);
                    }
                }, 100);
                if (isSearch) {
                    getSearchProductList(Page, PageSize);
                } else {
                    getProductList(Page, PageSize);
                }
                search_all_sort_text.setText(list.get(position));
            }
        });
    }


    private void getClassicData() {
        if (AppApplication.classiclist.size() > 0) {

            for (Category cat : AppApplication.classiclist) {
                if (cat.clist.get(0).id.equals("0")) {
                    continue;
                }
                Category category2 = new Category();
                category2.name = "全部" + cat.name;
                category2.id = "0";
                cat.clist.add(0, category2);
            }


            if (!AppApplication.classiclist.get(0).id.equals("0")) {
                Category category = new Category();
                category.name = "全部分类";
                category.id = "0";
                Category category1 = new Category();
                category1.name = "全部";
                category1.id = "0";
                category.clist.add(0, category1);
                AppApplication.classiclist.add(0, category);
            }


            for (Category category : AppApplication.classiclist) {
                category.isOnclick = false;
                if (rootid.equals(category.id)) {
                    category.isOnclick = true;
                    bindClassRightView(category.clist);
                    for (Category category1 : category.clist) {
                        if (category1.id.equals(classid)) {
                            search_all_classic_text.setText(category1.name);
                            break;
                        }
                    }
                }
            }

            bindClassicView();
        } else {
            Call<BaseModel<ArrayList<Category>>> call = ApiInstant.getInstant().categoryLevel2(AppApplication.apptype, "");
            call.enqueue(new ApiCallback<BaseModel<ArrayList<Category>>>() {
                @Override
                public void onSucssce(BaseModel<ArrayList<Category>> arrayListBaseModel) {
                    AppApplication.classiclist.clear();
                    AppApplication.classiclist.addAll(arrayListBaseModel.object);
                    getClassicData();
                }

                @Override
                public void onFailure() {
                }
            });
        }
    }

    private void bindClassicView() {
        final CommonBaseAdapter adapter = new CommonBaseAdapter<Category>(this, AppApplication.classiclist, R.layout.fragment_classic_left_item) {
            @Override
            public void convert(ViewHolder holder, Category itemData, int position) {
                TextView textView = holder.getView(R.id.classic_rootid_list_item);
                textView.setText(itemData.name);
                if (itemData.isOnclick) {

                    textView.setTextColor(getResources().getColor(R.color.colormain));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.black));
                }
            }
        };
        pop_list_classic_main.setAdapter(adapter);

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
                Category category = null;
                for (Category category1 : AppApplication.classiclist) {
                    if (category1.id.equals(rootid)) {
                        category = category1;
                    }
                }
                if (category != null)
                    classid = category.clist.get(position).id;
                Page = 0;
                isLoadOrRefresh = true;
                search_all_sort_text.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        search_result_recycler.scrollTo(0, 0);
                    }
                }, 100);
                isSearch = false;
                getProductList(Page++, PageSize);
                closePopuw();
                search_et.setText("");
                search_all_classic_text.setText(category.clist.get(position).name);
            }
        });
    }

    private void bindClassRightView(ArrayList<Category> list) {
        pop_list_classic.setAdapter(new CommonBaseAdapter<Category>(SearchResultActivity.this, list, R.layout.fragment_classic_left_item) {
            @Override
            public void convert(ViewHolder holder, Category itemData, int position) {
                TextView textView = holder.getView(R.id.classic_rootid_list_item);
                textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                textView.setTextColor(getResources().getColor(R.color.black));
                textView.setText(itemData.name);
            }
        });
    }

    private void closePopuw() {
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

    public void onEvent(Index index) {
        setcartNumber();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
