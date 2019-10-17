package com.holike.crm.activity.web;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyApplication;

import butterknife.BindView;

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;
    @BindView(R.id.webView)
    WebView mWebView;

    public static void open(BaseActivity<?, ?> activity, String url) {
        open(activity, url, null);
    }

    public static void open(BaseActivity<?, ?> activity, String url, String title) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        activity.openActivity(intent);
    }

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        setTitle(title);
        initWebSettings();
        initWebClient();
        if (!TextUtils.isEmpty(url)) {
            mWebView.loadUrl(url);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebSettings() {
        WebSettings settings = mWebView.getSettings();
        // 缓存(cache)
        settings.setAppCacheEnabled(true);      // 默认值 false
        settings.setAppCachePath(MyApplication.getInstance().getCacheDir().getAbsolutePath());
        // 存储(storage)
        settings.setDomStorageEnabled(true);    // 默认值 false
        settings.setDatabaseEnabled(true);      // 默认值 false
        // 是否支持viewport属性，默认值 false
        // 页面通过`<meta name="viewport" ... />`自适应手机屏幕
        settings.setUseWideViewPort(true);
        // 是否使用overview mode加载页面，默认值 false
        // 当页面宽度大于WebView宽度时，缩小使页面宽度等于WebView宽度
        settings.setLoadWithOverviewMode(true);
        // 是否支持Javascript，默认值false
        settings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 5.0以上允许加载http和https混合的页面(5.0以下默认允许，5.0+默认禁止)
            settings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    private void initWebClient() {
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setProgress(newProgress);
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                setTitle(title);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mWebView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler handler, SslError sslError) {
                super.onReceivedSslError(webView, handler, sslError);
                handler.proceed();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                onError();
            }
        });
        mWebView.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } catch (Exception ignored) {
            }
        });
    }

    private void onError() {
        mWebView.setVisibility(View.INVISIBLE);
        noNetwork();
    }

    @Override
    public void reload() {
        mWebView.reload();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        mWebView.resumeTimers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
        mWebView.pauseTimers();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        mWebView.stopLoading();
        mWebView.clearHistory();
        mWebView.destroy();
        super.onDestroy();
    }
}
