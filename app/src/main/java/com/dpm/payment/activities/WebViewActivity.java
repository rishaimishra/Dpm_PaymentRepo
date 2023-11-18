package com.dpm.payment.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.payment.R;

public class WebViewActivity extends AppCompatActivity {


    WebView myWebView;

    private String URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        myWebView = (WebView) findViewById(R.id.webview);

       URL= getIntent().getStringExtra("url");
       // URL= "https://stackoverflow.com/questions/15156138/android-loading-webview-content-full-screen";


        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setPluginState(WebSettings.PluginState.ON);




        if (isOnline()) {
            final ProgressDialog progressDialog = new ProgressDialog(WebViewActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            myWebView.setWebViewClient(new WebViewClient() {

                public void onPageFinished(WebView view, String url) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

                public boolean shuldOverrideKeyEvent(WebView view, KeyEvent event) {
                    return true;
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {


                    return false;
                }

            });

            myWebView.loadUrl(URL);

        } else {
            Toast.makeText(this, "Please connect to internet connection...", Toast.LENGTH_LONG).show();
        }



    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onBackPressed() {

        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            //ShowAlertDialog();
            finish();
        }
    }

    public void ShowAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("WebView App")
                .setMessage("Do you really want to exit?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();

                        finish();

                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }
}

