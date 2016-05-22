package com.davis.kangpinhui.api;


import com.davis.kangpinhui.Model.Address;
import com.davis.kangpinhui.Model.Cart;
import com.davis.kangpinhui.Model.Category;
import com.davis.kangpinhui.Model.Consume;
import com.davis.kangpinhui.Model.Extendedinfo;
import com.davis.kangpinhui.Model.Index;
import com.davis.kangpinhui.Model.Order;
import com.davis.kangpinhui.Model.OrderDetail;
import com.davis.kangpinhui.Model.Product;
import com.davis.kangpinhui.Model.ProductDetail;
import com.davis.kangpinhui.Model.Recharge;
import com.davis.kangpinhui.Model.Shop;
import com.davis.kangpinhui.Model.TakeGoodsdate;
import com.davis.kangpinhui.Model.Topic;
import com.davis.kangpinhui.Model.UserInfo;
import com.davis.kangpinhui.Model.basemodel.BaseModel;
import com.davis.kangpinhui.Model.basemodel.Page;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

//import retrofit.Call;
//import retrofit.http.Query;
//import retrofit.http.FormUrlEncoded;
//import retrofit.http.GET;
//import retrofit.http.POST;
//import retrofit.http.Query;

/**
 * Created by xusoku on 2016/4/5.
 */
public interface ApiService {

    public static String baseurl="http://m2.kangpinhui.com:8089";

        //http://www.tngou.net/tnfs/api/list?page=1&rows=10
//        @GET("tnfs/api/list")
//        Call<Grils> listGrils(@Query("id") int id,@Query("page") int page,@Query("rows") int rows);

//    @FormUrlEncoded
//    @POST("user/edit")
//    Call<User> getUser(@Query("name") String name, @Query("password") String password);

    //1、用户登陆
    @POST("user/login.do")
    Call<BaseModel<UserInfo>> userLogin(
            @Query("apptype") String apptype,
            @Query("phone") String phone,
            @Query("password") String password);

    //2、用户 注册
    @POST("user/register.do")
    Call<BaseModel> userRegister(
            @Query("apptype") String apptype,
            @Query("phone") String phone,
            @Query("password") String password,
            @Query("code") String code);

    //3、一级分类
    @GET("category/level1.do")
    Call<BaseModel<ArrayList<Category>>> categoryLevel1(
            @Query("apptype") String apptype);


    //4、二级分类
    @GET("category/level2.do")
    Call<BaseModel<ArrayList<Category>>> categoryLevel2(
            @Query("apptype") String apptype,
            @Query("id") String id);

    //5、商品分类列表
    @GET("product/list.do")
    Call<BaseModel<Page<ArrayList<Product>>>> getProductlist(
            @Query("apptype") String apptype,
            @Query("iordertype") String iordertype,
            @Query("rootid") String rootid,
            @Query("classid") String classid,
            @Query("shopid") String shopid,
            @Query("ipage") String ipage,
            @Query("ipagesize") String ipagesize
            );

    //6、商品搜索
    @FormUrlEncoded
    @POST("product/search.do")
    Call<BaseModel<Page<ArrayList<Product>>>> getSearchProductlist(
            @Query("apptype") String apptype,
            @Query("iordertype") String iordertype,
            @Query("shopid") String shopid,
            @Field("keyword") String keyword,
            @Query("ipage") String ipage,
            @Query("ipagesize") String ipagesize
            );

    //7、商品详情
    @GET("product/get.do")
    Call<BaseModel<ProductDetail>> getProductDetail(
            @Query("apptype") String apptype,
            @Query("id") String id,
            @Query("shopid") String shopid
    );

    //8.1、获取全部门店
    @GET("shop/list.do")
    Call<BaseModel<ArrayList<Shop>>> getShoplist(
            @Query("apptype") String apptype
    );

    //8.2、根据门店ID获取门店详情
    @GET("shop/get.do")
    Call<BaseModel<Shop>> getShopDetail(
            @Query("apptype") String apptype,
            @Query("shopid") String shopid
    );

    //9	用户收货地址列表接口
    @GET("address/list.do")
    Call<BaseModel<ArrayList<Address>>> getAddresslist(
            @Query("apptype") String apptype,
            @Query("token") String token
    );

    //10 用户收货地址根据ID获取接口
    @GET("address/get.do")
    Call<BaseModel<Address>> getAddressById(
            @Query("apptype") String apptype,
            @Query("token") String token,
            @Query("id") String id
    );

    //11.1 添加用户收货地址接口
    @GET("address/save.do")
    Call<BaseModel<Address>> Addaddress(
            @Query("apptype") String apptype,
            @Query("token") String token,
            @Query("iuseraddressid") String iuseraddressid,
            @Query("saddressname") String saddressname,
            @Query("saddress") String saddress,
            @Query("smobile") String smobile,
            @Query("shopid") String shopid,
            @Query("saddressperfix") String saddressperfix
    );

    //11.2 修改用户收货地址接口
    @GET("address/update.do")
    Call<BaseModel<Address>> updateAddress(
            @Query("apptype") String apptype,
            @Query("token") String token,
            @Query("iuseraddressid") String iuseraddressid,
            @Query("saddressname") String saddressname,
            @Query("saddress") String saddress,
            @Query("smobile") String smobile,
            @Query("shopid") String shopid,
            @Query("saddressperfix") String saddressperfix
    );

    //11.3 删除用户收货地址接口
    @GET("address/delete.do")
    Call<BaseModel<Address>> deleteAddress(
            @Query("apptype") String apptype,
            @Query("token") String token,
            @Query("id") String iuseraddressid
    );

    //12 我的订单
    @GET("order/list.do")
    Call<BaseModel<Page<Order<OrderDetail>>>> myOrderlist(
            @Query("apptype") String apptype,
            @Query("ipage") String ipage,
            @Query("ipagesize") String ipagesize,
            @Query("token") String token,
            @Query("orderstatus") String orderstatus
    );

    //12.1 我的订单详情
    @GET("order/get.do")
    Call<BaseModel<Order<OrderDetail>>> myOrderDetail(
            @Query("apptype") String apptype,
            @Query("ordernum") String ordernum,
            @Query("token") String token
    );

    //13 用户消费信息接口
    @GET("moneyinfo/list.do")
    Call<BaseModel<Page<Consume>>> getConsumelist(
            @Query("apptype") String apptype,
            @Query("ordernum") String ordernum,
            @Query("ipage") String ipage,
            @Query("ipagesize") String ipagesize,
            @Query("token") String token
    );

    //14 意见反馈接口
    @GET("feedback/save.do")
    Call<BaseModel> feedback(
            @Query("apptype") String apptype,
            @Query("version") String version,
            @Query("scontent") String scontent,
            @Query("token") String token
    );

    //15 加入购物车
    @GET("cart/add.do")
    Call<BaseModel> addCart(
            @Query("apptype") String apptype,
            @Query("shopid") String shopid,
            @Query("number") String number,
            @Query("iproductid") String iproductid,
            @Query("token") String token
    );
    //15.1 删除购物车
    @GET("cart/delete.do")
    Call<BaseModel> deleteCart(
            @Query("apptype") String apptype,
            @Query("shopid") String shopid,
            @Query("ids") String ids,
            @Query("token") String token
    );
    //15.2 获取购物车
    @GET("cart/list.do")
    Call<BaseModel<ArrayList<Cart>>> getCartlist(
            @Query("apptype") String apptype,
            @Query("shopid") String shopid,
            @Query("ids") String ids,
            @Query("token") String token
    );

    //16 获取收货日期
    @GET("common/sendtime.do")
    Call<BaseModel<ArrayList<TakeGoodsdate>>> getTakegoodtimelist(
            @Query("apptype") String apptype,
            @Query("shopid") String shopid,
            @Query("ids") String ids,
            @Query("token") String token
    );

    //17 购物验证
    @GET("order/check.do")
    Call<BaseModel> getcheck(
            @Query("apptype") String apptype,
            @Query("shopid") String shopid,
            @Query("ids") String ids,
            @Query("token") String token
    );

    //17.1 去结算、形成订单
    @GET("order/save.do")
    Call<BaseModel> orderSave(
            @Query("apptype") String apptype,
            @Query("shopid") String shopid,
            @Query("ids") String ids,
            @Query("addressid") String addressid,
            @Query("paytype") String paytype,
            @Query("sendtime") String sendtime,
            @Query("sremark") String sremark,
            @Query("couponid") String couponid,
            @Query("token") String token
    );

    //18 充值列表
    @GET("chongzhi/list.do")
    Call<BaseModel<Page<Recharge>>> getRechargelist(
            @Query("apptype") String apptype,
            @Query("token") String token
    );

    //18.1 添加充值订单
    @GET("chongzhi/save.do")
    Call<BaseModel<Recharge>> saveRecharge(
            @Query("apptype") String apptype,
            @Query("sinvoice") String sinvoice,
            @Query("srechargetype") String srechargetype,
            @Query("fmoney") String fmoney,
            @Query("sremark") String sremark,
            @Query("sconsignee") String sconsignee,
            @Query("smobile") String smobile,
            @Query("saddress") String saddress,
            @Query("token") String token
    );

    //18.2 获取充值订单
    @GET("chongzhi/get.do")
    Call<BaseModel<Recharge>> getRecharge(
            @Query("apptype") String apptype,
            @Query("ordernum") String ordernum,
            @Query("token") String token
    );

    //19 首页
    @GET("common/index.do")
    Call<BaseModel<Index>> getIndex(
            @Query("apptype") String apptype,
            @Query("shopid") String shopid,
            @Query("token") String token
    );

    //20 搜索标签
    @GET("common/keyword.do")
    Call<BaseModel<ArrayList<String>>> getSearchTag(
            @Query("apptype") String apptype
    );

    //21 手机注册验证码注册
    @GET("user/sendmsg.do")
    Call<BaseModel> sendMsgRegister(
            @Query("apptype") String apptype,
            @Query("phone") String phone
    );

    //22	提货券入库，验证提货code
    @GET("product/checkquancode.do")
    Call<BaseModel> checkquancode(
            @Query("apptype") String apptype,
            @Query("shopid") String shopid,
            @Query("code") String code,
            @Query("token") String token
    );

    //23根据提货券，获取商品数据
    @GET("product/getbyquancode.do")
    Call<BaseModel> getProductByCode(
            @Query("apptype") String apptype,
            @Query("shopid") String shopid,
            @Query("code") String code,
            @Query("token") String token
    );

    //24 创建提货券
    @GET("order/tihuoquan/save.do")
    Call<BaseModel> saveProductCode(
            @Query("apptype") String apptype,
            @Query("shopid") String shopid,
            @Query("addressid") String addressid,
            @Query("sendtime") String sendtime,
            @Query("sremark") String sremark,
            @Query("code") String code,
            @Query("token") String token
    );

    //25 专题数据获取
    @GET("product/active.do")
    Call<BaseModel<Topic>> getActivelist(
            @Query("apptype") String apptype,
            @Query("shopid") String shopid,
            @Query("activeid") String activeid
    );

    //27 提交订单时获取用户的优惠券信息
    @GET("coupon/getbyuid.do")
    Call<BaseModel<Topic>> getCouponByUid(
            @Query("apptype") String apptype,
            @Query("token") String token
    );

    //28 获取优惠券列表信息
    @GET("coupon/list.do")
    Call<BaseModel<Page<Topic>>> getCouponlist(
            @Query("apptype") String apptype,
            @Query("token") String token
    );

    //29 获取用户扩展信息
    @GET("user/extended.do")
    Call<BaseModel<Extendedinfo>> getExtendedInfo(
            @Query("apptype") String apptype,
            @Query("shopid") String shopid,
            @Query("token") String token
    );













}

