package com.davis.kangpinhui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.Model.Banner;
import com.davis.kangpinhui.Model.ProductDetail;
import com.davis.kangpinhui.Model.basemodel.BaseModel;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;
import com.davis.kangpinhui.adapter.base.ViewHolder;
import com.davis.kangpinhui.api.ApiCallback;
import com.davis.kangpinhui.api.ApiInstant;
import com.davis.kangpinhui.util.LogUtils;
import com.davis.kangpinhui.views.AttachUtil;
import com.davis.kangpinhui.views.DragTopLayout;
import com.davis.kangpinhui.views.XWebView;
import com.davis.kangpinhui.views.loopbanner.LoopBanner;
import com.davis.kangpinhui.views.loopbanner.LoopPageAdapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import retrofit2.Call;

public class ProductDetailActivity extends BaseActivity {


    private String id = "";

    private LoopBanner product_detail_banner;
    private TextView
            product_detail_title,
            product_detail_price,
            product_detail_logo_text,
            product_detail_date,
            product_detail_save,
            product_detail_producter;
    private XWebView product_detail_xweb;
    private DragTopLayout product_detail_drag;
    private ScrollView product_detail_content_sv,product_detail_header_sv;

    public static void jumpProductDetailActivity(Context conx, String id) {
        Intent it = new Intent(conx, ProductDetailActivity.class);
        it.putExtra("id", id);
        conx.startActivity(it);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_product_detail;
    }

    @Override
    protected void initVariable() {
        id = getIntent().getStringExtra("id");
    }

    @Override
    protected void findViews() {
        setTranslucentStatusBarGone();
        product_detail_banner = $(R.id.product_detail_banner);
        product_detail_drag = $(R.id.product_detail_drag);
        product_detail_title = $(R.id.product_detail_title);
        product_detail_price = $(R.id.product_detail_price);
        product_detail_logo_text = $(R.id.product_detail_logo_text);
        product_detail_date = $(R.id.product_detail_date);
        product_detail_save = $(R.id.product_detail_save);
        product_detail_producter = $(R.id.product_detail_producter);
        product_detail_xweb = $(R.id.product_detail_xweb);
        product_detail_content_sv = $(R.id.product_detail_content_sv);
        product_detail_header_sv = $(R.id.product_detail_header_sv);


        //禁止下拉放大效果
        product_detail_drag.setOverDrag(false);
        //设置头部View可上拉显示ContentView
        product_detail_drag.setCollapseOffset(1);

        product_detail_drag.listener(new DragTopLayout.PanelListener() {
            @Override
            public void onPanelStateChanged(DragTopLayout.PanelState panelState) {
            }

            @Override
            public void onSliding(float ratio) {
                LogUtils.e(TAG,ratio+"");
            }

            @Override
            public void onRefresh() {
            }
        });

    }

    @Override
    protected void onActivityLoading() {
        super.onActivityLoading();
    }

    @Override
    protected void initData() {
        startActivityLoading();
        Call<BaseModel<ProductDetail>> call = ApiInstant.getInstant().getProductDetail(AppApplication.apptype,
                id, AppApplication.shopid);
        call.enqueue(new ApiCallback<BaseModel<ProductDetail>>() {
            @Override
            public void onSucssce(BaseModel<ProductDetail> productDetailBaseModel) {
                onActivityLoadingSuccess();
                ProductDetail productDetail = productDetailBaseModel.object;
                ArrayList<String> bannerList = productDetail.piclist;
                getBannerData(bannerList);
                setBindData(productDetail);
            }

            @Override
            public void onFailure() {
                onActivityLoadingFailed();
            }
        });
    }

    public void getBannerData(ArrayList<String> bannerList) {
        product_detail_banner.setPageAdapter(new LoopPageAdapter<String>(mContext, bannerList, R.layout.layout_main_banner_item) {

            @Override
            public void convert(ViewHolder holder, final String itemData, final int position) {
                // TODO Auto-generated method stub
                ImageView imageView = (ImageView) holder.getConvertView();
                String img = itemData;
                Glide.with(ProductDetailActivity.this).load(img).into(imageView);
//                        .placeholder(R.drawable.placeholder)
//                        .error(R.drawable.imagenotfound)
            }
        });
    }
    public void setBindData(ProductDetail productDetail) {
        product_detail_title.setText(productDetail.sphysicname);
        product_detail_price.setText(productDetail.fprice+"/"+productDetail.sstandard);
        product_detail_logo_text.setText(productDetail.sbrandname);
        product_detail_date.setText(productDetail.sshelflife);
        product_detail_save.setText(productDetail.sstorage);
        product_detail_producter.setText(productDetail.sfactoryname);

        product_detail_xweb.loadDataWithBaseURL(null, (productDetail.scontentinfo), "text/html", "utf-8", null);
    }

    @Override
    protected void setListener() {

        product_detail_content_sv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_MOVE:
                        int scrollY=view.getScrollY();
                        int height=view.getHeight();
                        int scrollViewMeasuredHeight=product_detail_content_sv.getChildAt(0).getMeasuredHeight();
                        if(scrollY==0){
                            System.out.println("滑动到了顶端 content");
                            product_detail_drag.setTouchMode(true);

                        }
                        if((scrollY+height)==scrollViewMeasuredHeight){
//                            product_detail_drag.setTouchMode(false);
                            System.out.println("滑动到了底部 content=");
                        }
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
        product_detail_header_sv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_MOVE:
                        int scrollY=view.getScrollY();
                        int height=view.getHeight();
                        int scrollViewMeasuredHeight=product_detail_header_sv.getChildAt(0).getMeasuredHeight();
                        if(scrollY==0){
                            System.out.println("滑动到了顶端 header");
//                            product_detail_drag.setTouchMode(false);

                        }
                        if((scrollY+height)==scrollViewMeasuredHeight){
                            product_detail_drag.setTouchMode(true);
                            System.out.println("滑动到了底部 header=");
                        }
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void doClick(View view) {

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
