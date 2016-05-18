package com.davis.kangpinhui.api;


import com.davis.kangpinhui.Model.Address;
import com.davis.kangpinhui.Model.Cart;
import com.davis.kangpinhui.Model.Category;
import com.davis.kangpinhui.Model.Consume;
import com.davis.kangpinhui.Model.Order;
import com.davis.kangpinhui.Model.OrderDetail;
import com.davis.kangpinhui.Model.Product;
import com.davis.kangpinhui.Model.ProductDetail;
import com.davis.kangpinhui.Model.Recharge;
import com.davis.kangpinhui.Model.Shop;
import com.davis.kangpinhui.Model.TakeGoodsdate;
import com.davis.kangpinhui.Model.UserInfo;
import com.davis.kangpinhui.Model.basemodel.BaseModel;
import com.davis.kangpinhui.Model.basemodel.Page;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by xusoku on 2016/4/5.
 */
public interface ApiService {

    public static String baseurl="http://m2.kangpinhui.com:8089";

    public static String token="ED82DDC119CDD9E1F056C46C85C7D7EB";

        //http://www.tngou.net/tnfs/api/list?page=1&rows=10
//        @GET("tnfs/api/list")
//        Call<Grils> listGrils(@Query("id") int id,@Query("page") int page,@Query("rows") int rows);

//    @FormUrlEncoded
//    @POST("user/edit")
//    Call<User> getUser(@Field("name") String name, @Field("password") String password);

    //1、用户登陆
    @POST("user/login.do")
    Call<BaseModel<UserInfo>> userLogin(
            @Field("apptype") String apptype,
            @Field("phone") String phone,
            @Field("password") String password);

    //2、用户登陆
    @POST("user/register.do")
    Call<BaseModel<UserInfo>> userRegister(
            @Field("apptype") String apptype,
            @Field("phone") String phone,
            @Field("password") String password,
            @Field("code") String code);

    //3、一级分类
    @GET("category/level1.do")
    Call<BaseModel<ArrayList<Category>>> categoryLevel1(
            @Field("apptype") String apptype);


    //4、二级分类
    @GET("category/level2.do")
    Call<BaseModel<ArrayList<Category>>> categoryLevel2(
            @Field("apptype") String apptype,
            @Field("id") String id);

    //5、商品分类列表
    @GET("product/list.do")
    Call<BaseModel<Page<ArrayList<Product>>>> getProductlist(
            @Field("apptype") String apptype,
            @Field("iordertype") String iordertype,
            @Field("shopid") String shopid,
            @Field("classid") String classid,
            @Field("ipage") String ipage,
            @Field("ipagesize") String ipagesize
            );

    //6、商品搜索
    @FormUrlEncoded
    @GET("product/search.do")
    Call<BaseModel<Page<ArrayList<Product>>>> getSearchProductlist(
            @Field("apptype") String apptype,
            @Field("iordertype") String iordertype,
            @Field("shopid") String shopid,
            @Field("keyword") String keyword,
            @Field("ipage") String ipage,
            @Field("ipagesize") String ipagesize
            );

    //7、商品详情
    @GET("product/get.do")
    Call<BaseModel<ProductDetail>> getProductDetail(
            @Field("apptype") String apptype,
            @Field("id") String id,
            @Field("shopid") String shopid
    );

    //8.1、获取全部门店
    @GET("shop/list.do")
    Call<BaseModel<ArrayList<Shop>>> getShoplist(
            @Field("apptype") String apptype
    );

    //8.2、根据门店ID获取门店详情
    @GET("shop/get.do")
    Call<BaseModel<Shop>> getShopDetail(
            @Field("apptype") String apptype,
            @Field("shopid") String shopid
    );

    //9	用户收货地址列表接口
    @GET("address/list.do")
    Call<BaseModel<ArrayList<Address>>> getAddresslist(
            @Field("apptype") String apptype,
            @Field("token") String token
    );

    //10 用户收货地址根据ID获取接口
    @GET("address/get.do")
    Call<BaseModel<Address>> getAddressById(
            @Field("apptype") String apptype,
            @Field("token") String token,
            @Field("id") String id
    );

    //11.1 添加用户收货地址接口
    @GET("address/save.do")
    Call<BaseModel<Address>> Addaddress(
            @Field("apptype") String apptype,
            @Field("token") String token,
            @Field("iuseraddressid") String iuseraddressid,
            @Field("saddressname") String saddressname,
            @Field("saddress") String saddress,
            @Field("smobile") String smobile,
            @Field("shopid") String shopid,
            @Field("saddressperfix") String saddressperfix
    );

    //11.2 修改用户收货地址接口
    @GET("address/update.do")
    Call<BaseModel<Address>> updateAddress(
            @Field("apptype") String apptype,
            @Field("token") String token,
            @Field("iuseraddressid") String iuseraddressid,
            @Field("saddressname") String saddressname,
            @Field("saddress") String saddress,
            @Field("smobile") String smobile,
            @Field("shopid") String shopid,
            @Field("saddressperfix") String saddressperfix
    );

    //11.3 删除用户收货地址接口
    @GET("address/delete.do")
    Call<BaseModel<Address>> deleteAddress(
            @Field("apptype") String apptype,
            @Field("token") String token,
            @Field("id") String iuseraddressid
    );

    //12 我的订单
    @GET("order/list.do")
    Call<BaseModel<Page<Order<OrderDetail>>>> myOrderlist(
            @Field("apptype") String apptype,
            @Field("ipage") String ipage,
            @Field("ipagesize") String ipagesize,
            @Field("token") String token,
            @Field("orderstatus") String orderstatus
    );

    //12.1 我的订单详情
    @GET("order/get.do")
    Call<BaseModel<Order<OrderDetail>>> myOrderDetail(
            @Field("apptype") String apptype,
            @Field("ordernum") String ordernum,
            @Field("token") String token
    );

    //13 用户消费信息接口
    @GET("moneyinfo/list.do")
    Call<BaseModel<Page<Consume>>> getConsumelist(
            @Field("apptype") String apptype,
            @Field("ordernum") String ordernum,
            @Field("ipage") String ipage,
            @Field("ipagesize") String ipagesize,
            @Field("token") String token
    );

    //14 意见反馈接口
    @GET("feedback/save.do")
    Call<BaseModel> feedback(
            @Field("apptype") String apptype,
            @Field("version") String version,
            @Field("scontent") String scontent,
            @Field("token") String token
    );

    //15 加入购物车
    @GET("cart/add.do")
    Call<BaseModel> addCart(
            @Field("apptype") String apptype,
            @Field("shopid") String shopid,
            @Field("number") String number,
            @Field("iproductid") String iproductid,
            @Field("token") String token
    );
    //15.1 删除购物车
    @GET("cart/delete.do")
    Call<BaseModel> deleteCart(
            @Field("apptype") String apptype,
            @Field("shopid") String shopid,
            @Field("ids") String ids,
            @Field("token") String token
    );
    //15.2 获取购物车
    @GET("cart/list.do")
    Call<BaseModel<ArrayList<Cart>>> getCartlist(
            @Field("apptype") String apptype,
            @Field("shopid") String shopid,
            @Field("ids") String ids,
            @Field("token") String token
    );

    //16 获取收货日期
    @GET("common/sendtime.do")
    Call<BaseModel<ArrayList<TakeGoodsdate>>> getTakegoodtimelist(
            @Field("apptype") String apptype,
            @Field("shopid") String shopid,
            @Field("ids") String ids,
            @Field("token") String token
    );

    //17 购物验证
    @GET("order/check.do")
    Call<BaseModel> getcheck(
            @Field("apptype") String apptype,
            @Field("shopid") String shopid,
            @Field("ids") String ids,
            @Field("token") String token
    );

    //17.1 去结算、形成订单
    @GET("order/save.do")
    Call<BaseModel> orderSave(
            @Field("apptype") String apptype,
            @Field("shopid") String shopid,
            @Field("ids") String ids,
            @Field("addressid") String addressid,
            @Field("paytype") String paytype,
            @Field("sendtime") String sendtime,
            @Field("sremark") String sremark,
            @Field("couponid") String couponid,
            @Field("token") String token
    );

    //18 充值列表
    @GET("chongzhi/list.do")
    Call<BaseModel<Page<Recharge>>> getRecharge(
            @Field("apptype") String apptype,
            @Field("token") String token
    );

    //18.1 添加订单
    @GET("chongzhi/save.do")
    Call<BaseModel<Recharge>> saveRecharge(
            @Field("apptype") String apptype,
            @Field("sinvoice") String sinvoice,
            @Field("srechargetype") String srechargetype,
            @Field("fmoney") String fmoney,
            @Field("sremark") String sremark,
            @Field("sconsignee") String sconsignee,
            @Field("smobile") String smobile,
            @Field("saddress") String saddress,
            @Field("token") String token
    );

    //18.2 获取充值订单
    @GET("chongzhi/get.do")
    Call<BaseModel<Recharge>> saveRecharge(
            @Field("apptype") String apptype,
            @Field("ordernum") String ordernum,
            @Field("token") String token
    );











}

