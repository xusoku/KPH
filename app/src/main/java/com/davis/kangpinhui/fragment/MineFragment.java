package com.davis.kangpinhui.fragment;

import android.view.View;

import com.davis.kangpinhui.R;
import com.davis.kangpinhui.activity.FeedBackActivity;
import com.davis.kangpinhui.fragment.base.BaseFragment;
import com.davis.kangpinhui.util.ToastUitl;
import com.davis.kangpinhui.views.MineCustomLayout;

/**
 * Created by davis on 16/5/19.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    private MineCustomLayout mine_allorder;
    private MineCustomLayout mine_feedback_linear;
    @Override
    protected void initVariable() {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void findViews(View view) {

        mine_allorder=$(R.id.mine_allorder);
        mine_feedback_linear=$(R.id.mine_feedback_linear);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        mine_allorder.setOnClickListener(this);
        mine_feedback_linear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mine_allorder:
                ToastUitl.showToast("aaa");
                break;
            case R.id.mine_feedback_linear:
                FeedBackActivity.jumpFeedBackActivity(getActivity());
                break;
        }
    }
}
