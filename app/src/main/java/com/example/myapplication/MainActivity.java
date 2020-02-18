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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public WebView webView;
    public Button reloadButton;
    public EditText urlText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("WebViewVer","WebView Version:");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //WEBVIEW
        webView=findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebClient());

        //RELOAD BUTTON
        reloadButton=findViewById(R.id.reload);
        reloadButton.setOnClickListener(new View.OnClickListener() {//STUB IMPLEMENTATION
            @Override
            public void onClick(View v) {
                Log.v("Reload","Reloading URL:" + webView.getOriginalUrl());
                webView.reload();
            }
        });

        //URL BOX CODE
        urlText=findViewById(R.id.urlBox);
        urlText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {//Input action listener
                if(actionId== EditorInfo.IME_ACTION_DONE){//Checks for IME_ACTION_DONE
                    updatewebviewContent(urlText.getText().toString());// Updates Webview on IME DONE
                }
                return false;
            }
        });
        urlText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    urlText.setText(webView.getOriginalUrl());
                    urlText.selectAll();
                }else{
                    setTittle();
                }
            }
        });
        updatewebviewContent("google.com");
    }


    //BACK BUTTON
    //Default 0
    // One Click to exit 1
    public int exitCount=0;
    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else if(exitCount==0){
            Toast.makeText(MainActivity.this,"Press again to exit",
                    Toast.LENGTH_LONG).show();
            exitCount++;
        }else if(exitCount>=1){
            finish();
        }
    }

    public void updatewebviewContent(String url){
        exitCount=0;//Restarts exit count
        urlText.clearFocus();//Ensures that loses focus the url box
        if(isValidURL(url)){//Loads specific URL
            url=httpsStrict(url);
            Log.v("WebViewDebug","url:" + url);
            webView.loadUrl(url);
        }else{//Google Search in case that no url is provided
            Log.v("WebViewDebug","Making Google Search");
            url="https://www.google.com/search?q=" + url.replace(" ","+");
            webView.loadUrl(url);
            Log.d("URL","URL Loaded:" + webView.getOriginalUrl());
        }
    }
    public boolean isValidURL(String url){
        boolean validUrl=url.contains(".") && url!=null && !url.isEmpty()|hasSpaces(url);
        Log.v("ValidURL","isValid:"+ validUrl);
        return validUrl;
    }
    public void setTittle(){
        Log.v("TittleUpdate","Setting Tittle to " + webView.getTitle() + " from URL " + webView.getOriginalUrl());
        urlText.setText(webView.getTitle());
    }
    boolean hasSpaces(String url){
        boolean ret=url.contains(" ");
        Log.d("HasSpaces","HasSpaces:"+ ret);
        return  ret;
    }

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
    class WebClient extends WebViewClient{
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            setTittle();
        }
    }
}