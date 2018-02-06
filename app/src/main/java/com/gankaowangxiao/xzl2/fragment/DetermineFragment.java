package com.gankaowangxiao.xzl2.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gankaowangxiao.xzl2.R;
import com.gankaowangxiao.xzl2.UIchangeInterface;
import com.gankaowangxiao.xzl2.utils.Constance;
import com.gankaowangxiao.xzl2.utils.DimensUtils;
import com.gankaowangxiao.xzl2.utils.SoundPlayUtils;
import com.gankaowangxiao.xzl2.utils.Util;

/**
 * Created by xiaotantan on 17/10/24.
 */

public class DetermineFragment extends Fragment implements View.OnClickListener {

    private UIchangeInterface Interface;
    private String defuteStr;
    private static String KEY = "str";
    private ImageView ivCheck, ivClear;
    private TextView tvShow, tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv0, tvGone;
    private StringBuilder showText;
    private LinearLayout llCommit, llLeft, llRight, llNum;
    private int changePosition;
    private RelativeLayout rlBack;
    private TextView tvNum;

    public static DetermineFragment newInstance(String defulte) {
        DetermineFragment fragment = new DetermineFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY, defulte);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setUpdataListener(UIchangeInterface listener) {
        this.Interface = listener;
    }

    public void changeData(int position, String updateData) {
        changePosition = position;
        defuteStr = updateData;
        initShowText();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(KEY)) {
            defuteStr = getArguments().getString(KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_determine, container, false);
        rlBack = (RelativeLayout) view.findViewById(R.id.rl_back);
        tvNum = (TextView) view.findViewById(R.id.tv_num);
        ivClear = (ImageView) view.findViewById(R.id.iv_clear);
        tvShow = (TextView) view.findViewById(R.id.tv_show);
        tv0 = (TextView) view.findViewById(R.id.tv_0);
        tv1 = (TextView) view.findViewById(R.id.tv_1);
        tv2 = (TextView) view.findViewById(R.id.tv_2);
        tv3 = (TextView) view.findViewById(R.id.tv_3);
        tv4 = (TextView) view.findViewById(R.id.tv_4);
        tv5 = (TextView) view.findViewById(R.id.tv_5);
        tv6 = (TextView) view.findViewById(R.id.tv_6);
        tv7 = (TextView) view.findViewById(R.id.tv_7);
        tv8 = (TextView) view.findViewById(R.id.tv_8);
        tv9 = (TextView) view.findViewById(R.id.tv_9);
        tvGone = (TextView) view.findViewById(R.id.tv_gone);
        llCommit = (LinearLayout) view.findViewById(R.id.ll_commit);
        llLeft = (LinearLayout) view.findViewById(R.id.ll_left);
        llRight = (LinearLayout) view.findViewById(R.id.ll_right);
        llNum = (LinearLayout) view.findViewById(R.id.ll_num);
        ViewGroup.LayoutParams layoutParams = llNum.getLayoutParams();
        layoutParams.height = (int) ((Util.getScreenWidth(getContext()) - DimensUtils.dipToPx(getContext(), 20)) * 0.87);
        llNum.setLayoutParams(layoutParams);
        setOnClick();
        initShowText();
        return view;
    }

    //初始化
    private void initShowText() {
        showText = new StringBuilder(defuteStr);
        tvShow.setText(defuteStr);
        tvShow.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    tvGone.setVisibility(View.VISIBLE);
                } else {
                    tvGone.setVisibility(View.GONE);
                }
                if (s.toString().contains("-")) {
                    llLeft.setEnabled(false);
                } else {
                    llLeft.setEnabled(true);
                }
            }
        });
    }

    private void setOnClick() {
        tvNum.setText("修改录入");
        rlBack.setOnClickListener(this);
        tv0.setOnClickListener(this);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        tv5.setOnClickListener(this);
        tv6.setOnClickListener(this);
        tv7.setOnClickListener(this);
        tv8.setOnClickListener(this);
        tv9.setOnClickListener(this);
        llLeft.setOnClickListener(this);
        llRight.setOnClickListener(this);
        llCommit.setOnClickListener(this);
        ivClear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_0:
                SoundPlayUtils.play(Constance.ZERO);
                ChangeShowText(0);
                break;
            case R.id.tv_1:
                SoundPlayUtils.play(Constance.ONE);
                ChangeShowText(1);
                break;
            case R.id.tv_2:
                SoundPlayUtils.play(Constance.TWO);
                ChangeShowText(2);
                break;
            case R.id.tv_3:
                SoundPlayUtils.play(Constance.THREE);
                ChangeShowText(3);
                break;
            case R.id.tv_4:
                SoundPlayUtils.play(Constance.FOUR);
                ChangeShowText(4);
                break;
            case R.id.tv_5:
                SoundPlayUtils.play(Constance.FIVE);
                ChangeShowText(5);
                break;
            case R.id.tv_6:
                SoundPlayUtils.play(Constance.SIX);
                ChangeShowText(6);
                break;
            case R.id.tv_7:
                SoundPlayUtils.play(Constance.SEVEN);
                ChangeShowText(7);
                break;
            case R.id.tv_8:
                SoundPlayUtils.play(Constance.EIGHT);
                ChangeShowText(8);
                break;
            case R.id.tv_9:
                SoundPlayUtils.play(Constance.NINE);
                ChangeShowText(9);
                break;
            //提交
            case R.id.ll_commit:
                SoundPlayUtils.play(Constance.ENTER);
                if (isTrue(tvShow.getText().toString())) {
                    if (Interface != null) {
                        Interface.onDetermine(changePosition, tvShow.getText().toString());
                    }
                    Clean();
                }
                break;
            //-
            case R.id.ll_left:
                SoundPlayUtils.play(Constance.SPACE);
                ChangeShowText(10);
                break;
            //删除
            case R.id.ll_right:
                SoundPlayUtils.play(Constance.BACK);
                ChangeShowText(11);
                break;
            case R.id.iv_clear:
                Clean();
                break;
            case R.id.rl_back:
                if (Interface != null) {
                    Interface.backTwo();
                }
                break;
            default:
                break;
        }
    }

    //清空
    private void Clean() {
        llLeft.setEnabled(true);
        showText.delete(0, showText.length());
        tvShow.setText("");
    }

    private boolean isTrue(String str) {
        if (str.contains("-")) {
            String[] splitStr = str.split("-");
            if (splitStr.length < 2) {
                Util.makeText(getContext(), "请输入正确的卡号码段");
                return false;
            }
            if (splitStr[0].equals("") || splitStr[1].equals("")) {
                Util.makeText(getContext(), "请输入正确的卡号码段");
                return false;
            }
            long strOne = Long.parseLong(splitStr[0]);
            long strTwo = Long.parseLong(splitStr[1]);
            if (strTwo > strOne) {
                return true;
            } else {
                Util.makeText(getContext(), "卡段号码输入不正确");
                return false;
            }
        } else {
            if (str.trim().length() == 0) {
                Util.makeText(getContext(), "请先输入卡号");
                return false;
            }
            return true;
        }
    }

    //更改显示TextView
    private void ChangeShowText(int i) {
        switch (i) {
            case 10:
                showText.append("-");
                llLeft.setEnabled(false);
                break;
            case 11:
                if (showText.length() == 0) {
                    return;
                }
                showText.deleteCharAt(showText.length() - 1);
                break;
            default:
                showText.append(i + "");
                break;
        }
        tvShow.setText(showText.toString());
    }
}
