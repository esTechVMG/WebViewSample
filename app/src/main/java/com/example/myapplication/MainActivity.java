package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public WebView webView;
    public Button reloadButton;
    public EditText url;
    public boolean isWebviewLoading=true;

    public void updateWebviewLoading(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //WEBVIEW
        webView=findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        //RELOAD BUTTON
        reloadButton=findViewById(R.id.reload);
        reloadButton.setOnClickListener(new View.OnClickListener() {//STUB IMPLEMENTATION
            @Override
            public void onClick(View v) {
                webView.reload();
            }
        });

        //URL BOX CODE
        url=findViewById(R.id.urlBox);
        url.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {//Input action listener
                if(actionId== EditorInfo.IME_ACTION_DONE){//Checks for IME_ACTION_DONE
                    updatewebviewContent(url.getText().toString());// Updates Webview on IME DONE
                }
                return false;
            }
        });
        updatewebviewContent("google.com");
    }

    public void updatewebviewContent(String url){
        if(isValidURL(url)){
            url=httpsStrict(url);
            Log.v("WebViewDebug","url:" + url);
            webView.loadUrl(url);
        }

    }
    public boolean isValidURL(String url){
        boolean validUrl=url.contains(".") && url!=null && !url.isEmpty();
        Log.v("ValidURL","isValid:"+ validUrl);
        return validUrl;
    }

    //!(isHttp(url)|url.startsWith("https://")|url.startsWith("ftp://")
    public String httpsStrict(String url){
        if(isHttp(url)){
            String subUrl = url.substring(6);
            return  "https://" + subUrl;
        }else{
            return "https://" + url;
        }
    }
    public boolean isHttp(String url){
        boolean http=url.startsWith("http://");
        Log.v("isHTTP","isHTTP:"+http);
        return http;
    }

    public void  updateWebviewContentCustom(String message){
        String customHtml = "<html><body><h1>UPS</h1><h2>" + message + "</h2></body></html>";
        webView.loadData(customHtml, "text/html", "UTF-8");
    }
}
