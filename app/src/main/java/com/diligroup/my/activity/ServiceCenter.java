package com.diligroup.my.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.base.BaseActivity;
import com.diligroup.home.HomeActivity;
import com.diligroup.net.AppAction.Action;
import com.diligroup.utils.NetUtils;

import butterknife.Bind;
import okhttp3.Request;

/**
 * 服务中心
 */
public class ServiceCenter extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.comm_title)
    TextView tv_title;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.my_help_center)
    RelativeLayout my_help_center;
    @Bind(R.id.about_us)
    RelativeLayout aboutus;
    @Bind(R.id.current_steps)
    Button current_steps;//当日步数

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_service_center;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }


    @Override
    protected void initViewAndData() {
     ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(this);
        tv_title.setText("服务中心");
        my_help_center.setOnClickListener(this);
        aboutus.setOnClickListener(this);
        current_steps.setOnClickListener(this);
    }
    @Override
    public void onError(Request request, Action action, Exception e) {

    }

    @Override
    public void onResponse(Request request, Action action, Object object) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.my_help_center:
                startActivity(new Intent(this,InstructionsActivity.class));
                break;
            case R.id.about_us:
                startActivity(new Intent(this,AboutUsActivity.class));
                break;
            case R.id.current_steps:
                startActivity(new Intent(this,CurrentStepActivity.class));
                break;
            case R.id.iv_back:
                startActivity(new Intent(this, HomeActivity.class));
                break;
        }
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HomeActivity.class));
        super.onBackPressed();
    }
}
