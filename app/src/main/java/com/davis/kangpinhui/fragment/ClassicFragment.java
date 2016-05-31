package com.davis.kangpinhui.fragment;import android.support.v4.widget.SwipeRefreshLayout;import android.text.TextUtils;import android.view.View;import android.widget.AdapterView;import android.widget.EditText;import android.widget.LinearLayout;import android.widget.ListView;import android.widget.TextView;import com.davis.kangpinhui.AppApplication;import com.davis.kangpinhui.activity.PoiKeywordSearchActivity;import com.davis.kangpinhui.activity.PolygonActivity;import com.davis.kangpinhui.activity.ShopActivity;import com.davis.kangpinhui.model.Category;import com.davis.kangpinhui.model.basemodel.BaseModel;import com.davis.kangpinhui.R;import com.davis.kangpinhui.activity.SearchResultActivity;import com.davis.kangpinhui.adapter.ClassicGroupSelectAdapter;import com.davis.kangpinhui.adapter.base.CommonBaseAdapter;import com.davis.kangpinhui.adapter.base.ViewHolder;import com.davis.kangpinhui.api.ApiCallback;import com.davis.kangpinhui.api.ApiInstant;import com.davis.kangpinhui.fragment.base.BaseFragment;import com.davis.kangpinhui.util.CommonManager;import com.davis.kangpinhui.util.ToastUitl;import com.davis.kangpinhui.views.MySwipeRefreshLayout;import com.davis.kangpinhui.views.PinnedHeaderListView;import java.util.ArrayList;import java.util.HashMap;import retrofit2.Call;/** * Created by davis on 16/5/19. */public class ClassicFragment extends BaseFragment {    private ListView listView;    private PinnedHeaderListView pinnedHeaderListView;    private ClassicGroupSelectAdapter classicGroupSelectAdapter;    private MySwipeRefreshLayout classic_swipe;    private LinearLayout classic_rootid_search_linear;    private LinearLayout no_linear_shopid;    private TextView no_shopid_add_address;    private TextView no_shopid_see_sending;    private EditText classic_rootid_et;    private String id="";    @Override    protected void initVariable() {    }    @Override    protected int setContentView() {        return R.layout.fragment_classic;    }    @Override    protected void findViews(View view) {        listView = $(view,R.id.classic_rootid_list);        no_linear_shopid = $(view,R.id.no_linear_shopid);        classic_rootid_search_linear = $(view,R.id.classic_rootid_search_linear);        classic_rootid_et = $(view,R.id.classic_rootid_et);        classic_swipe = $(view,R.id.classic_swipe);        no_shopid_add_address=$(view,R.id.no_shopid_add_address);        no_shopid_see_sending=$(view,R.id.no_shopid_see_sending);        pinnedHeaderListView = $(view,R.id.classic_rootid_pinnlist);        classic_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {            @Override            public void onRefresh() {                getRightData(id, true);            }        });    }    @Override    protected void onFragmentLoading() {        super.onFragmentLoading();        getLeftData();    }    @Override    protected void initData() {        if(!TextUtils.isEmpty(AppApplication.shopid)){            no_linear_shopid.setVisibility(View.GONE);            startFragmentLoading();        }else{            no_linear_shopid.setVisibility(View.VISIBLE);        }    }    public void iniData(){        initData();    }    private void getRightData(final String id,boolean isRefrush) {        if(AppApplication.classiclist.size()>0){            getBindRightView(AppApplication.classiclist, id);            return;        }        CommonManager.setRefreshingState(classic_swipe,true);        Call<BaseModel<ArrayList<Category>>> call = ApiInstant.getInstant().categoryLevel2(AppApplication.apptype, "");        call.enqueue(new ApiCallback<BaseModel<ArrayList<Category>>>() {            @Override            public void onSucssce(BaseModel<ArrayList<Category>> arrayListBaseModel) {                CommonManager.setRefreshingState(classic_swipe, false);                ArrayList<Category> classiclist = arrayListBaseModel.object;                AppApplication.classiclist.clear();                AppApplication.classiclist.addAll(classiclist);                getBindRightView(classiclist, id);            }            @Override            public void onFailure() {                CommonManager.setRefreshingState(classic_swipe, false);            }        });    }    private void getLeftData() {        Call<BaseModel<ArrayList<Category>>> call = ApiInstant.getInstant().categoryLevel1(AppApplication.apptype);        call.enqueue(new ApiCallback<BaseModel<ArrayList<Category>>>() {            @Override            public void onSucssce(BaseModel<ArrayList<Category>> arrayListBaseModel) {                onFragmentLoadingSuccess();                ArrayList<Category> list = arrayListBaseModel.object;                getBindLeftView(list);            }            @Override            public void onFailure() {                onFragmentLoadingFailed();            }        });    }    private void getBindRightView(final ArrayList<Category> list,String id){        ArrayList<String> s=new ArrayList<>();        if(id.contains(",")){            String [] ss=id.split(",");            for(String sss: ss){                s.add(sss);            }        }else{            s.add(id);        }        ArrayList<Category> list1=new ArrayList<>();        for(Category category:list){            for(String sid : s){                if(category.id.equals(sid)){                    list1.add(category);                }            }        }        classicGroupSelectAdapter = new ClassicGroupSelectAdapter(mContext, list1);        pinnedHeaderListView.setAdapter(classicGroupSelectAdapter);    }    private void getBindLeftView(final ArrayList<Category> list) {        list.get(0).isOnclick = true;        getRightData(list.get(0).id, false);        final CommonBaseAdapter<Category> adapter = new CommonBaseAdapter<Category>(getActivity(), list, R.layout.fragment_classic_left_item) {            @Override            public void convert(ViewHolder holder, Category itemData, int position) {                TextView textView = holder.getView(R.id.classic_rootid_list_item);                textView.setText(itemData.name);                LinearLayout linearLayout = holder.getView(R.id.classic_rootid_list_item_bg);                if (itemData.isOnclick) {                    linearLayout.setVisibility(View.VISIBLE);                    textView.setBackgroundColor(getResources().getColor(R.color.white));                } else {                    linearLayout.setVisibility(View.GONE);                    textView.setBackgroundColor(getResources().getColor(R.color.colorgray));                }            }        };        listView.setAdapter(adapter);        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {            @Override            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {                for (Category c : list) {                    c.isOnclick = false;                }                list.get(position).isOnclick = true;                adapter.notifyDataSetChanged();                getRightData(list.get(position).id, false);            }        });    }    @Override    protected void setListener() {        classic_rootid_search_linear.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                String str = classic_rootid_et.getText().toString().trim();                if (!TextUtils.isEmpty(str)) {                    SearchResultActivity.jumpSearchResultActivity(getActivity(), str, true);                }            }        });        no_shopid_add_address.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                ShopActivity.jumpShopActivity(getActivity());            }        });        no_shopid_see_sending.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                if(AppApplication.shoplist.size()>0)                PolygonActivity.jumpPolygonActivity(getActivity(),AppApplication.shoplist);                else                    ToastUitl.showToast("暂无数据");            }        });    }    public boolean getInit(){        return getisInit();    }}