package com.gankaowangxiao.xzl;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gankaowangxiao.xzl.base.BaseActivity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by xiaotantan on 17/10/11.
 */

public class WebActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private RelativeLayout llBack;
    private ImageView ivHome;
    private ProgressWebView webView;
    private View viewBootoom;
    private WebSettings webSettings;
    private boolean needClearHistory = false;
    private String HomeUrl = "http://zhuli.gankao.com/go";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initWebView();
    }

    private void initWebView() {
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        initWeb();
    }

    @SuppressLint("JavascriptInterface")
    private void initWeb() {
        webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容
        webSettings.setJavaScriptEnabled(true);
        String ua = webSettings.getUserAgentString();
        webSettings.setUserAgentString(ua + " /gkagent" + getVersionName(this) + " ");
        webView.addJavascriptInterface(this, "JsBridgeApp");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setWebContentsDebuggingEnabled(true);
        }
        webView.loadUrl(HomeUrl);
    }

    @JavascriptInterface
    public boolean CopyToClipboard(String copyStr) {
        Log.i("TAG", copyStr + "-----剪切板复制内容---");
        ClipboardManager clipboardManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        //创建ClipData对象
        ClipData clipData = ClipData.newPlainText("simple text copy", copyStr);
        //添加ClipData对象到剪切板中
        clipboardManager.setPrimaryClip(clipData);
        return true;
    }

    /**
     * 获取版本名
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String name = "";
        try {
            name = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            0).versionName;
        } catch (PackageManager.NameNotFoundException ex) {
            name = "";
        }
        return name;
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        llBack = (RelativeLayout) findViewById(R.id.iv_back);
        ivHome = (ImageView) findViewById(R.id.iv_rigth);
        viewBootoom = findViewById(R.id.view);
        webView = (ProgressWebView) findViewById(R.id.web_view);
        llBack.setVisibility(GONE);
        llBack.setOnClickListener(this);
        ivHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                break;
            case R.id.iv_rigth:
                if (HomeUrl.equals(webView.getUrl())) {
                    return;
                }
                needClearHistory = true;
                webView.loadUrl(HomeUrl);
                break;
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
            if (needClearHistory) {
                needClearHistory = false;
                webView.clearHistory();//清除历史记录
            }
            if (webView != null) {
                if (webView.canGoBack()) {
                    llBack.setVisibility(VISIBLE);
                } else {
                    llBack.setVisibility(GONE);
                }
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String urls) {
            view.loadUrl(urls);

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }

    private class MyWebChromeClient extends WebChromeClient {

        public MyWebChromeClient() {
        }


        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                if (webView != null && webView.getProgressbar() != null)
                    webView.getProgressbar().setVisibility(GONE);
            } else {
                if (webView != null && webView.getProgressbar() != null) {
                    if (webView.getProgressbar().getVisibility() == GONE) {
                        webView.getProgressbar().setVisibility(VISIBLE);
                    }
                    webView.getProgressbar().setProgress(newProgress);
                }
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String name) {
            super.onReceivedTitle(view, name);
            if (TextUtils.isEmpty(name)) {
                name = "赶考小助手";
            }
            if (tvTitle != null)
                tvTitle.setText(name);
        }
    }

    private long firstTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 1000) {
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;
                    return true;
                } else {
                    System.exit(0);
                }

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
