package com.davis.kangpinhui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.Model.Extendedinfo;
import com.davis.kangpinhui.Model.ProductDetail;
import com.davis.kangpinhui.Model.UserInfo;
import com.davis.kangpinhui.Model.basemodel.BaseModel;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;
import com.davis.kangpinhui.adapter.base.ViewHolder;
import com.davis.kangpinhui.api.ApiCallback;
import com.davis.kangpinhui.api.ApiInstant;
import com.davis.kangpinhui.util.DisplayMetricsUtils;
import com.davis.kangpinhui.util.LogUtils;
import com.davis.kangpinhui.util.ToastUitl;
import com.davis.kangpinhui.views.BadgeView;
import com.davis.kangpinhui.views.dargview.DavisWebView;
import com.davis.kangpinhui.views.dargview.DivasScrollViewPageOne;
import com.davis.kangpinhui.views.dargview.DragLayout;
import com.davis.kangpinhui.views.loopbanner.LoopBanner;
import com.davis.kangpinhui.views.loopbanner.LoopPageAdapter;

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
    private DavisWebView product_detail_xweb;
    private DragLayout product_detail_drag;
    private DivasScrollViewPageOne product_detail_header_sv;
    private TextView product_detail_title_text;
    private LinearLayout product_detail_title_text_linear, add_cart_number_linear;

    private PopupWindow addpopupWindow;
    private ImageView add_cart_icon;

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
        add_cart_number_linear = $(R.id.add_cart_number_linear);
        product_detail_drag = $(R.id.product_detail_drag);
        product_detail_title = $(R.id.product_detail_title);
        product_detail_price = $(R.id.product_detail_price);
        product_detail_logo_text = $(R.id.product_detail_logo_text);
        product_detail_date = $(R.id.product_detail_date);
        product_detail_save = $(R.id.product_detail_save);
        product_detail_producter = $(R.id.product_detail_producter);
        product_detail_xweb = $(R.id.product_detail_xweb);
        product_detail_header_sv = $(R.id.product_detail_header_sv);
        product_detail_title_text = $(R.id.product_detail_title_text);
        product_detail_title_text_linear = $(R.id.product_detail_title_text_linear);
        add_cart_icon = $(R.id.add_cart_icon);


        backgroundDefaultBadge = new BadgeView(this);
        backgroundDefaultBadge.setTargetView(add_cart_icon);
        setcartNumber();
        initPopupWindow();
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
                setBindPopData(productDetail);
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
        product_detail_price.setText(productDetail.fprice + "/" + productDetail.sstandard);
        product_detail_logo_text.setText(productDetail.sbrandname);
        product_detail_date.setText(productDetail.sshelflife);
        product_detail_save.setText(productDetail.sstorage);
        product_detail_producter.setText(productDetail.sfactoryname);

        product_detail_xweb.loadDataWithBaseURL(null, (productDetail.scontentinfo), "text/html", "utf-8", null);
    }

    private void setBindPopData(final ProductDetail productDetail) {
        Glide.with(this).load(productDetail.spicurl).into(add_cart_image);

        add_cart_text_vip_price.setText("会员价" + productDetail.fvipprice + "/" + productDetail.sstandard);
        add_cart_text_price.setText("康品价" + productDetail.fprice + "/" + productDetail.sstandard);

        add_cart_add_center.setText("1");


        add_cart_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AppApplication.isLogin(ProductDetailActivity.this)) {

                    String num=add_cart_add_center.getText().toString().trim();
                    Call<BaseModel> call = ApiInstant.getInstant().addCart(AppApplication.apptype, AppApplication.shopid, num, productDetail.iproductid, productDetail.srequire, AppApplication.token);

                    call.enqueue(new ApiCallback<BaseModel>() {
                        @Override
                        public void onSucssce(BaseModel baseModel) {
                            ToastUitl.showToast("添加成功");
                            EventBus.getDefault().post(new Extendedinfo());

                            String number = AppApplication.getCartcount();
                            if (!TextUtils.isEmpty(number) &&number.equals("0.0")){
                                number="0";
                            }
                            int n = Integer.valueOf(number);
                            n++;
                            backgroundDefaultBadge.setText(n+"");

                            addpopupWindow.dismiss();
                        }

                        @Override
                        public void onFailure() {

                        }
                    });
                }

            }
        });
        add_cart_guige.setText(productDetail.sstandard);
        add_cart_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = add_cart_add_center.getText().toString().trim();

                int n = Integer.valueOf(number);
                n++;
                add_cart_add_center.setText(n + "");
            }
        });

        add_cart_add_mins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = add_cart_add_center.getText().toString().trim();

                int n = Integer.valueOf(number);

                if (n <= 1) {
                    n = 1;
                } else {
                    n--;
                }
                add_cart_add_center.setText(n + "");
            }
        });


        if (!TextUtils.isEmpty(productDetail.srequire)) {
            add_cart_sp.setText(productDetail.srequire);
            add_cart_add_linear_sp.setVisibility(View.VISIBLE);
        } else {
            add_cart_add_linear_sp.setVisibility(View.GONE);
        }

    }

    @Override
    protected void setListener() {
        product_detail_title_text_linear.setAlpha(0);
        product_detail_drag.setChange(new DragLayout.Change() {
            @Override
            public void onScrollChange(int t) {
                float alpha = 0.0f;
                if (t > 0) {
                    alpha = (float) ((t - 100) / 100.00);
                } else {
                    alpha = 0;
                }
                product_detail_title_text_linear.setAlpha(alpha);
            }
        });

    }

    @Override
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.product_detail_back:
                finish();
                break;
            case R.id.add_cart_addlinear:
                addpopupWindow.showAtLocation(product_detail_drag, Gravity.NO_GRAVITY, 0, 0);
                break;
        }

    }


    private ImageView add_cart_image, add_cart_image_close;

    private TextView add_cart_text_vip_price, add_cart_text_price, add_cart_guige, add_cart_sp,
            add_cart_add_mins, add_cart_add, add_cart_add_center, add_cart_add_btn;

    private LinearLayout add_cart_add_linear_sp;

    private View add_cart_pop_view;

    private BadgeView backgroundDefaultBadge;

    //初始化分类
    private void initPopupWindow() {
        // TODO Auto-generated method stub
        View view = getLayoutInflater().inflate(R.layout.activity_product_detail_add_cart, null);

        add_cart_pop_view = $(view, R.id.add_cart_pop_view);
        add_cart_add_linear_sp = $(view, R.id.add_cart_add_linear_sp);
        add_cart_image = $(view, R.id.add_cart_image);
        add_cart_image_close = $(view, R.id.add_cart_image_close);
        add_cart_text_vip_price = $(view, R.id.add_cart_text_vip_price);
        add_cart_text_price = $(view, R.id.add_cart_text_price);
        add_cart_sp = $(view, R.id.add_cart_sp);
        add_cart_add_mins = $(view, R.id.add_cart_add_mins);
        add_cart_add_center = $(view, R.id.add_cart_add_center);
        add_cart_add_btn = $(view, R.id.add_cart_add_btn);
        add_cart_guige = $(view, R.id.add_cart_guige);
        add_cart_add = $(view, R.id.add_cart_add);
        addpopupWindow = new PopupWindow(view,
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        addpopupWindow.setFocusable(true);
        addpopupWindow.setOutsideTouchable(true);
        addpopupWindow.setAnimationStyle(R.style.popwin_add_cart_anim_style);
        addpopupWindow.setOnDismissListener(new popupWindowclickListener());
        addpopupWindow.setBackgroundDrawable(new BitmapDrawable());


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addpopupWindow != null && addpopupWindow.isShowing()) {
                    addpopupWindow.dismiss();
                }
            }
        });

        add_cart_pop_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addpopupWindow != null && addpopupWindow.isShowing()) {
                    addpopupWindow.dismiss();
                }
            }
        });
        add_cart_image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (addpopupWindow != null && addpopupWindow.isShowing()) {
                    addpopupWindow.dismiss();
                }
            }
        });
    }

    class popupWindowclickListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            if (add_cart_add_center != null) {
                add_cart_add_center.setText("1");
            }
        }
    }

    public void setcartNumber() {
        String number = AppApplication.getCartcount();
        if (!TextUtils.isEmpty(number) && !number.equals("0") && !number.equals("0.0"))
            backgroundDefaultBadge.setText(number);
    }
}
