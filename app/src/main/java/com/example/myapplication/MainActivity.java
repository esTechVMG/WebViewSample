package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public WebView webView;

    public EditText url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView=findViewById(R.id.webview);
        url=findViewById(R.id.urlBox);

        url.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    updatewebviewContent(url.getText().toString());
                }
                return false;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        updatewebviewContent("google.com");

    }
    public void updatewebviewContent(String url){
        if(url==null){
            Log.e("AppWebview","URL is NULL");
            url="";
        }else if(url.isEmpty()){
            Log.d("AppWebview","URL is empty");
        }else if(!url.startsWith("https://")) {
            if (url.startsWith("http://")) {
                String subUrl = url.substring(6);
                //Log.v("AppVars", "subURL:" + subUrl);//DEBUG INFO
                url = "http://" + subUrl;
            } else {
                url = "https://" + url;
            }
        }
        Log.v("AppVars","url:"+url);
        webView.loadUrl(url);
    }
    public void  updateWebviewContentCustom(String message){
        String customHtml = "<html><body><h1>UPS</h1><h2>" + message + "</h2></body></html>";
        webView.loadData(customHtml, "text/html", "UTF-8");
    }
}
