package com.gankaowangxiao.xzl.popup;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import com.gankaowangxiao.xzl.R;
import com.gankaowangxiao.xzl.adapter.TabPagerAdapter;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by xiaotantan on 17/10/20.
 */

public class SlideFromBottomPopup extends BasePopupWindow implements View.OnClickListener {
    private View popupView;
    private ViewPager viewPager;


    public SlideFromBottomPopup(Context context, TabPagerAdapter tabPagerAdapter) {
        super(context);
        bindEvent();
//        initView();
        viewPager.setAdapter(tabPagerAdapter);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        if (popupView != null) {
            viewPager = (ViewPager) popupView.findViewById(R.id.view_page);
        }
    }

    /**
     * 添加点击事件
     */
    private void bindEvent() {
        if (popupView != null) {
            popupView.findViewById(R.id.iv_back).setOnClickListener(this);
            popupView.findViewById(R.id.iv_rigth).setOnClickListener(this);
        }
    }

    @Override
    protected Animation initShowAnimation() {
        return getTranslateAnimation(250 * 2, 0, 300);
    }

    @Override
    protected Animation initExitAnimation() {
        return getTranslateAnimation(0, 250 * 2, 300);
    }

    @Override
    public View getClickToDismissView() {
        return null;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.ll_content);
    }

    @Override
    public View onCreatePopupView() {
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.layout_popupwindow, null);
        viewPager = (ViewPager) popupView.findViewById(R.id.view_page);
        return popupView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.iv_rigth:
                break;
            default:
                break;
        }
    }
//
//    class TabPagerAdapter extends FragmentStatePagerAdapter {
//        private List<Fragment> mFragments;
//
//        public TabPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragments) {
//            super(fragmentManager);
//            this.mFragments = fragments;
//        }
//
//        @Override
//        public int getCount() {
//            return mFragments.size();
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return mFragments.get(position);
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return false;
//        }
//    }
}
