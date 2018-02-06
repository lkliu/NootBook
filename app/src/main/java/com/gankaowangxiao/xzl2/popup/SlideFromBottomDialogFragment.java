package com.gankaowangxiao.xzl2.popup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.gankaowangxiao.xzl2.R;
import com.gankaowangxiao.xzl2.UIchangeInterface;
import com.gankaowangxiao.xzl2.WebActivity;
import com.gankaowangxiao.xzl2.WrapContentHeightViewPager;
import com.gankaowangxiao.xzl2.adapter.TabPagerAdapter;
import com.gankaowangxiao.xzl2.fragment.DetermineFragment;
import com.gankaowangxiao.xzl2.fragment.ListsFrragment;
import com.gankaowangxiao.xzl2.fragment.WriterFrragment;
import com.gankaowangxiao.xzl2.onDisMissListener;
import com.gankaowangxiao.xzl2.utils.DimensUtils;
import com.gankaowangxiao.xzl2.utils.Util;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by xiaotantan on 17/10/23.
 * 卡号录入
 */

@SuppressLint("ValidFragment")
public class SlideFromBottomDialogFragment extends DialogFragment implements UIchangeInterface {
    private UIchangeInterface uIchangeInterface;
    private WriterFrragment frragmentWriter;
    private ListsFrragment frragmentLists;
    private DetermineFragment determineFragment;
    private WrapContentHeightViewPager viewPager;
    private View view1;
    private onDisMissListener disMissListener;

    private String showStr;
    private Activity activity;
    private View popupWindow;


    public SlideFromBottomDialogFragment(String showStr, Activity activity) {
        super();
        this.showStr = showStr;
        this.activity = activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        int currentItem = viewPager.getCurrentItem();
                        if (currentItem > 0) {
                            switch (currentItem) {
                                case 2:
                                    viewPager.setCurrentItem(1);
                                    break;
                                case 1:
                                    viewPager.setCurrentItem(0);
                                    break;
                                default:
                                    break;
                            }
                            return true;
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void setDissMissListener(onDisMissListener dissMissListener) {
        this.disMissListener = dissMissListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.style_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_popupwindow, container);

        viewPager = (WrapContentHeightViewPager) view.findViewById(R.id.view_page);
        ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
        layoutParams.height = (int) (((Util.getScreenWidth(getContext()) - DimensUtils.dipToPx(getContext(), 20)) * 0.87) + DimensUtils.dipToPx(getContext(), 230));
        viewPager.setLayoutParams(layoutParams);
        view1 = view.findViewById(R.id.view);
        return view;
    }

    private List getFragments() {
        List fList = new ArrayList();
        uIchangeInterface = this;
        frragmentWriter = WriterFrragment.newInstance(showStr);
        frragmentWriter.setUpdataListener(uIchangeInterface);
        fList.add(frragmentWriter);
        frragmentLists = ListsFrragment.newInstance(new ArrayList<String>());
        frragmentLists.setUpdataListener(uIchangeInterface);
        fList.add(frragmentLists);
        determineFragment = DetermineFragment.newInstance("");
        determineFragment.setUpdataListener(uIchangeInterface);
        fList.add(determineFragment);
        return fList;
    }

    @Override
    public void UpdataLists(ArrayList<String> strings) {
        if (frragmentLists != null) {
            frragmentLists.ChangeData(strings);
        }
        viewPager.setCurrentItem(1);
    }

    @Override
    public void UpdataItem(int postion, String str) {
        if (determineFragment != null) {
            determineFragment.changeData(postion, str);
        }
        viewPager.setCurrentItem(2);
    }

    @Override
    public void Back(ArrayList<String> strings) {
        if (frragmentWriter != null) {
            frragmentWriter.ChangeData(strings);
        }
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onDetermine(int position, String str) {
        if (frragmentLists != null) {
            frragmentLists.onUpdataPosition(position, str);
        }
        viewPager.setCurrentItem(1);
    }

    @Override
    public void backTwo() {
        viewPager.setCurrentItem(1);
    }

    @Override
    public void dissMiss() {
    }

    @Override
    public void onClose() {
        //提示是否关闭
        DialogPopup popup = new DialogPopup(activity);
        popup.setUpdataListener(uIchangeInterface);
        popup.showPopupWindow();
    }

    @Override
    public void onCommitData(ArrayList<String> datas) {
        RecyclerViewPopup recyclerViewPopup = new RecyclerViewPopup(activity, datas);
        recyclerViewPopup.setUpdataListener(uIchangeInterface);
//        recyclerViewPopup.setCommitListener(disMissListener);
        recyclerViewPopup.showPopupWindow();
    }
}
