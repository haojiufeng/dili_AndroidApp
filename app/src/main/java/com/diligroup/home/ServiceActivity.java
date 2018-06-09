package com.diligroup.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.diligroup.R;
import com.diligroup.base.BaseActivity;
import com.diligroup.base.Constant;
import com.diligroup.bean.CommonBean;
import com.diligroup.net.AppAction.Action;
import com.diligroup.net.Api;
import com.diligroup.utils.DateUtils;
import com.diligroup.utils.LogUtils;
import com.diligroup.utils.NetUtils;
import com.diligroup.utils.ToastUtil;
import com.diligroup.utils.UserManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.techery.properratingbar.ProperRatingBar;
import okhttp3.Request;

/**
 * 服务评价页面
 */
public class ServiceActivity extends BaseActivity {

    @Bind(R.id.service_evaluation)
    ProperRatingBar serviceEvaluation;
    @Bind(R.id.input_service_evaluation)
    EditText inputServiceEvaluation;
    @Bind(R.id.service_commit)
    Button serviceCommit;
    private String mealType;
    private String date;
    @Bind(R.id.comm_title)
    TextView tv_title;
    @Bind(R.id.iv_back)
    ImageView ivBack;

    String dateStr;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_service;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    protected void initViewAndData() {
        mealType = getIntent().getStringExtra("mealType");

        date = getIntent().getStringExtra("date");//2016-09-09
        if(!TextUtils.isEmpty(date)){
          dateStr= DateUtils.dateFormatChanged_2(DateUtils.dateFormatChagee(date));
        }
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        serviceCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(serviceEvaluation.getRating()==0){
                    ToastUtil.showLong(ServiceActivity.this,"亲:您还没有给评星呢");
                    return;
                }
                if(TextUtils.isEmpty(inputServiceEvaluation.getText().toString())){
                    ToastUtil.showLong(ServiceActivity.this,"亲：评价内容不能为空！");
                    return;
                }
                //1:菜品评价 2:服务评价)
                 String content=inputServiceEvaluation.getText().toString();
                int serviceStar=serviceEvaluation.getRating();
                Api.dishVarietyEvaluate(UserManager.getInstance().getUserId(),UserManager.getInstance().getStoreId(),"2","",dateStr,mealType,content,"","",serviceStar+"",ServiceActivity.this);
            }
        });
        inputServiceEvaluation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length()>=120){
                    ToastUtil.showLong(ServiceActivity.this,"亲：超过字数限制啦");
                    return;
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initDate();
    }

    private void initDate() {
        tv_title.setText("服务评价");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onError(Request request, Action action, Exception e) {

    }

    @Override
    public void onResponse(Request request, Action action, Object object) {
        if(action==Action.DISEVALUATE  && object!=null){
            CommonBean bean= (CommonBean) object;
            if(bean.getCode().equals(Constant.RESULT_SUCESS)){
                LogUtils.i("服务餐别评价接口调用成功");
                ToastUtil.showLong(this,"恭喜你！评价成功");
                finish();
            }else{

            }
        }
    }
}
