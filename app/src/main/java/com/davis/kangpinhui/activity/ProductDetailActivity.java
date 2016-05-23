package com.davis.kangpinhui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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
import com.davis.kangpinhui.views.loopbanner.LoopBanner;
import com.davis.kangpinhui.views.loopbanner.LoopPageAdapter;

import java.util.ArrayList;

import retrofit2.Call;

public class ProductDetailActivity extends BaseActivity {


    private String id="";

    private LoopBanner product_detail_banner;
    public static void jumpProductDetailActivity(Context conx,String id ){
        Intent it=new Intent(conx,ProductDetailActivity.class);
        it.putExtra("id",id);
        conx.startActivity(it);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_product_detail;
    }

    @Override
    protected void initVariable() {
        id=getIntent().getStringExtra("id");
    }

    @Override
    protected void findViews() {
        product_detail_banner=$(R.id.product_detail_banner);
    }

    @Override
    protected void onActivityLoading() {
        super.onActivityLoading();
    }

    @Override
    protected void initData() {
        startActivityLoading();
        Call<BaseModel<ProductDetail>> call= ApiInstant.getInstant().getProductDetail(AppApplication.apptype,
                id,AppApplication.shopid);
        call.enqueue(new ApiCallback<BaseModel<ProductDetail>>() {
            @Override
            public void onSucssce(BaseModel<ProductDetail> productDetailBaseModel) {
                onActivityLoadingSuccess();
                ProductDetail productDetail = productDetailBaseModel.object;
                ArrayList<String> bannerList=productDetail.piclist;
                getBannerData(bannerList);
            }

            @Override
            public void onFailure() {
                onActivityLoadingFailed();
            }
        });
    }

    public void getBannerData(ArrayList<String> bannerList){
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
    @Override
    protected void setListener() {

    }

    @Override
    public void doClick(View view) {

    }
}
