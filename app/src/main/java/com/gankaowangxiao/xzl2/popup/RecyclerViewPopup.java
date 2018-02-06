package com.gankaowangxiao.xzl2.popup;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;

import com.gankaowangxiao.xzl2.R;
import com.gankaowangxiao.xzl2.UIchangeInterface;
import com.gankaowangxiao.xzl2.WebActivity;
import com.gankaowangxiao.xzl2.adapter.DataListAdapter;
import com.gankaowangxiao.xzl2.onDisMissListener;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by xiaotantan on 17/10/25.
 */

public class RecyclerViewPopup extends BasePopupWindow implements View.OnClickListener {
    private Button ok;
    private Button cancel;
    private RecyclerView dataRecy;
    private DataListAdapter dataListAdapter;
    private UIchangeInterface Interface;
    public onDisMissListener disMissListener;
    private List<String> dates = new ArrayList<>();

    public RecyclerViewPopup(Context context, ArrayList<String> datas) {
        super(context);
        ok = (Button) findViewById(R.id.bt_determine);
        cancel = (Button) findViewById(R.id.bt_cance);
        dataRecy = (RecyclerView) findViewById(R.id.recy_data);
        dates.addAll(datas);
        dataListAdapter = new DataListAdapter(dates);
        dataRecy.setLayoutManager(new LinearLayoutManager(context));
        dataRecy.setAdapter(dataListAdapter);
        setViewClickListener(this, ok, cancel);
    }

    public void setUpdataListener(UIchangeInterface listener) {
        this.Interface = listener;
    }

    public void setCommitListener(onDisMissListener listener) {
        this.disMissListener = listener;
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
        return createPopupById(R.layout.popup_recyclerview);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.ll_content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_determine:
//                if (Interface != null) {
//                    Interface.dissMiss();
//                }
                CommitDate();
                dismiss();
                break;
            case R.id.bt_cance:
                dismiss();
                break;
            default:
                break;
        }
    }

    /**
     * 上传数据
     */
    private void CommitDate() {
        StringBuilder sb = new StringBuilder();
        for (String s : dates) {//aa就是你想转化的集合
            sb.append(s + ",");
        }
        String str = sb.toString();
        String string = str.subSequence(0, str.length() - 1).toString();
        if (disMissListener != null) {
            disMissListener.onCommitDate(string);
        }
    }


}
