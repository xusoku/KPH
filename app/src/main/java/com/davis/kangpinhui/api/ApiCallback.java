package com.davis.kangpinhui.api;


import com.davis.kangpinhui.Model.basemodel.BaseModel;
import com.davis.kangpinhui.util.ToastUitl;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by davis on 16/5/17.
 */
public abstract  class ApiCallback<T> implements Callback<T> {

    public abstract void onSucssce(T t);
    public abstract void onFailure();
    @Override
    public void onResponse(Response<T> response, Retrofit retrofit) {
        if(response.code()==200){
            BaseModel t= (BaseModel) response.body();
            if(t.breturn){
                onSucssce(response.body());
            }else{
                ToastUitl.showToast(t.errorinfo);
            }

        }
    }

    @Override
    public void onFailure(Throwable t) {
        onFailure();

        ToastUitl.showToast("网络异常");
    }
}
