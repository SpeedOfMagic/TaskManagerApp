package team13.taskmanagerapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.*;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebLog extends AppCompatActivity {

    public String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_log);
        WebView webView;
        webView = findViewById(R.id.webLogin);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view,  String url) {
                //android.util.Log.d("MyLog", url);
                view.loadUrl(url);
                if(url.substring(18,22).equals("code")){
                     code = url.substring(23);
                    Log.d("MyLog",code);
                    Intent intent = new Intent(WebLog.this, Auth.class);
                    intent.putExtra("authcode",code);
                    startActivity(intent);
                }
                return true;
            }
        });
        //webView.loadUrl("https://google.com");
        webView.loadUrl("https://www.wrike.com/oauth2/authorize?client_id=01a00I4c&response_type=code");
    }
}