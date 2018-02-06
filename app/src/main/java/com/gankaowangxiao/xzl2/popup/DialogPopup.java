package com.gankaowangxiao.xzl2.popup;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;

import com.gankaowangxiao.xzl2.R;
import com.gankaowangxiao.xzl2.UIchangeInterface;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by xiaotantan on 17/10/25.
 * 提示框
 */

public class DialogPopup extends BasePopupWindow implements View.OnClickListener {
    private Button ok;
    private Button cancel;
    private UIchangeInterface Interface;


    public DialogPopup(Context context) {
        super(context);
        ok = (Button) findViewById(R.id.bt_determine);
        cancel = (Button) findViewById(R.id.bt_cance);

        setViewClickListener(this, ok, cancel);
    }

    public void setUpdataListener(UIchangeInterface listener) {
        this.Interface = listener;
    }

    @Override
    protected Animation initShowAnimation() {
        return getTranslateAnimation(250 * 2, 0, 300);
    }

    @Override
    public View getClickToDismissView() {
        return null;
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.popup_dialog);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.ll_content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_determine:
                if (Interface != null) {
                    Interface.dissMiss();
                }
                dismiss();
                break;
            case R.id.bt_cance:
                dismiss();
                break;
            default:
                break;
        }

    }
}
