package com.diligroup.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.diligroup.R;
import com.diligroup.bean.MyItemClickListener;

/**
 * Created by hjf on 2016/8/4.
 */
public class SwitchIpDialog extends Dialog implements  RadioGroup.OnCheckedChangeListener {
    Context mContext;
    MyItemClickListener listener;
    private  RadioGroup rg_switch_ip;
    String currentHost;//当前的host

    public SwitchIpDialog(Context mContext, String currentHost, MyItemClickListener listener){
        super(mContext,R.style.DialogStyle);
        this.mContext = mContext;
        this.listener=listener;
        this.currentHost=currentHost;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.switch_ip);
        rg_switch_ip = (RadioGroup)findViewById(R.id.switch_ip_rg);

        rg_switch_ip.setOnCheckedChangeListener(this);
        if(currentHost.contains(".67")){
            rg_switch_ip.check(R.id.ip_test);
        }else if(currentHost.contains(".101")){
            rg_switch_ip.check(R.id.ip_yewu);
        }else{
            rg_switch_ip.check(R.id.ip_test);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.ip_test:
                listener.onItemClick(group,checkedId);
                this.dismiss();
                break;
            case R.id.ip_yewu:
                listener.onItemClick(group,checkedId);
                this.dismiss();
                break;
                default:
                    break;
        }
    }
}
