package com.diligroup.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

import com.diligroup.R;

public class MyLoadingDialog extends Dialog {
    AnimationDrawable animationDrawable;
    ImageView imageView;

    public MyLoadingDialog(Context context) {
        super(context, R.style.DialogStyle);
        init();
    }

    private void init() {
        setContentView(R.layout.webview_loading_content);
        imageView = (ImageView) findViewById(R.id.iv_loading);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
    }

    @Override
    public void show() {
        super.show();
        animationDrawable.start();
    }

    @Override
    public void dismiss() {
        if (null != animationDrawable) {
            if (animationDrawable.isRunning()) {
                animationDrawable.stop();
            }
        }
        super.dismiss();
    }
}
