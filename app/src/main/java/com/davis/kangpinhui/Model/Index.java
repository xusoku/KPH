package com.davis.kangpinhui.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by davis on 16/5/18.
 */
public class Index implements Serializable {
    public ArrayList<Banner> bannerList=new ArrayList<>();
    public ArrayList<Productlist> recommandList=new ArrayList<Productlist>();
    public ArrayList<Product> productList=new ArrayList<Product>();
    public ArrayList<Banner> bannerListAd=new ArrayList<Banner>();
    public ArrayList<Banner> iconBannerList=new ArrayList<Banner>();

    public static class Productlist extends  Banner{

        public String id;
        public ArrayList<Product> list=new ArrayList<>();
    }
}
