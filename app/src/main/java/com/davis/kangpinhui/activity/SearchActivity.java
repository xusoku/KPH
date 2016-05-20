package com.davis.kangpinhui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.davis.kangpinhui.AppApplication;
import com.davis.kangpinhui.Model.basemodel.BaseModel;
import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.base.BaseActivity;
import com.davis.kangpinhui.api.ApiCallback;
import com.davis.kangpinhui.api.ApiInstant;
import com.davis.kangpinhui.util.CommonManager;
import com.davis.kangpinhui.util.ToastUitl;
import com.davis.kangpinhui.views.CustomTypefaceTextView;
import com.davis.kangpinhui.views.FlowLayout;

import java.util.ArrayList;

import retrofit2.Call;

public class SearchActivity extends BaseActivity {

    private FlowLayout mFlowLayout;
    @Override
    protected int setLayoutView() {
        return R.layout.activity_search;
    }


    public static void jumpSearchActivity(Context context,String key){
        Intent it=new Intent(context,SearchActivity.class);
        it.putExtra("key",key);
        context.startActivity(it);
    }
    @Override
    protected void initVariable() {

    }

    @Override
    protected void findViews() {
        mFlowLayout=$(R.id.search_flowlayout);
        mFlowLayout.removeAllViews();

    }

    @Override
    protected void initData() {

        Call<BaseModel<ArrayList<String>>> call= ApiInstant.getInstant().getSearchTag(AppApplication.apptype);

        call.enqueue(new ApiCallback<BaseModel<ArrayList<String>>>() {
            @Override
            public void onSucssce(BaseModel<ArrayList<String>> arrayListBaseModel) {
                ArrayList<String> list=arrayListBaseModel.object;
                if (list != null) {
                    for (String tag : list) {
                        mFlowLayout.addView(newFlowTagView(tag));
                    }
                    mFlowLayout.setVisibility(View.VISIBLE);
                }else{
                    mFlowLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure() {

            }
        });

    }

    @Override
    protected void setListener() {

    }

    @Override
    public void doClick(View view) {

    }


    public View newFlowTagView(final String tag)
    {
//        final CustomTypefaceTextView textView = (CustomTypefaceTextView) getLayoutInflater().inflate(R.layout.layout_flow_tag,mFlowLayout);
        final TextView textView = (TextView) View.inflate(mActivity, R.layout.layout_flow_tag, mFlowLayout);
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
        int dp = CommonManager.dpToPx(5);
        params.setMargins(dp, dp, dp, dp);
        textView.setLayoutParams(params);
        textView.setText(tag);
        textView.setBackgroundResource(R.drawable.bg_flow_tag_unselect);

        textView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ToastUitl.showToast("tag"+tag);
                CommonManager.dismissSoftInputMethod(view.getContext(), view.getWindowToken());
            }
        });
        return textView;
    }
}
