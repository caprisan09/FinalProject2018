package com.example.hunar_parneet.finalproject2018;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ActivityNewsWebPage extends AppCompatActivity {

    private static final String EXTRA_ID = "com.example.hunar_parneet.finalproject2018";

    private WebView myWebView;
    private ProgressBar progressBar;
    private TextView titletextview;
    private String urlNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_web_page);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);
        titletextview = (TextView) findViewById(R.id.title);

        urlNews = getIntent().getStringExtra(EXTRA_ID);

        setup();
    }

    protected void setup(){
        myWebView = (WebView) findViewById(R.id.webView);


        // display the whole webpage in the window
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);

        // enable zooming
        myWebView.getSettings().setBuiltInZoomControls(true);

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true); // enable JavaScript

        myWebView.setWebViewClient(new WebViewClient()); // keep navigation in the app

        myWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webView, int progress) {
                if (progress == 100) {
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(progress);
                }
            }

            public void onReceivedTitle(WebView webView, String title) {
                titletextview.setText(title);
            }
        });


        myWebView.loadUrl(urlNews);
    } //setup

    // use the device back button for browser history
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    public static Intent newIntent(Context context, String newsUrl) {
        Intent intent = new Intent(context, ActivityNewsWebPage.class);
        intent.putExtra(EXTRA_ID, newsUrl);
        return intent;

    }
}

