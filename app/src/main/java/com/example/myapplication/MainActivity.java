package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public WebView webView;
    public Button reloadButton,backButton,forButton;
    public EditText urlText;
    public ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Back/Forward Buttons
        backButton=findViewById(R.id.backButton);
        forButton=findViewById(R.id.forwardButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(webView.canGoBack()){
                    webView.goBack();
                }
            }
        });
        forButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(webView.canGoForward()){
                    webView.goForward();
                }
            }
        });

        //Progress Bar
        progressBar=findViewById(R.id.progressBar);

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
                if(reloadButton.getText().equals(getString(R.string.reload))){
                    webView.reload();
                }else{
                    webView.stopLoading();
                }

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

    public boolean isHttp(String url) {
        boolean http = url.startsWith("http://");
        Log.v("isHTTP", "isHTTP:" + http);
        return http;
    }

    public void buttonsUpdate(){
        backButton.setEnabled(webView.canGoBack());
        forButton.setEnabled(webView.canGoForward());
    }

    class WebClient extends WebViewClient{
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            setTittle();
            reloadButton.setText(getString(R.string.reload));
            progressBar.setProgress(100);
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            reloadButton.setText(getString(R.string.stop));
            progressBar.setProgress(50);
            buttonsUpdate();
        }
    }
}