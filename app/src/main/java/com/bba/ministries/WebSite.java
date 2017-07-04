package com.bba.ministries;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rey.material.widget.ProgressView;

/**
 * Created by Prakash on 12/10/2016.
 */

public class WebSite extends AppCompatActivity {
    WebView webView;
    Toolbar toolbar;
    ProgressView progress;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);
        webView=(WebView)findViewById(R.id.webview);
        progress=(ProgressView) findViewById(R.id.progress);


        Intent i=getIntent();
        String title=i.getStringExtra("title");
        String url=i.getStringExtra("url");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.logos);
        getSupportActionBar().setTitle("  "+title);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });


        startWebView(url);


    }
    private void startWebView(String url) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);

                return true;
            }

            //Show loader on url load
            public void onLoadResource (WebView view, String url) {
                    // in standard case YourActivity.this


            }
            public void onPageFinished(WebView view, String url) {

                progress.setVisibility(View.INVISIBLE);
            }

        });

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);

        // Other webview options
        /*
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);
        */

        /*
         String summary = "<html><body>You scored <b>192</b> points.</body></html>";
         webview.loadData(summary, "text/html", null);
         */

        //Load url in webview
        webView.loadUrl(url);



    }

    // Open previous opened link from history on webview when back button pressed

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        /*if(webView.canGoBack()) {
            webView.goBack();
        } else {
            // Let the system handle the back button
            super.onBackPressed();
        }*/
        super.onBackPressed();

        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        finish();
    }
}
