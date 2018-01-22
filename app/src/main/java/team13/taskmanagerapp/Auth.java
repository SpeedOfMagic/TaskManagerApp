package team13.taskmanagerapp;

/**
 * Created by ilyauyutov on 16.01.2018.
 */


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.security.PublicKey;

public class Auth extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

       // if (getIntent().hasExtra("authcode")) {

            WebView webView;
            webView = findViewById(R.id.authWV);

            String authcode = getIntent().getStringExtra("authcode");
            Log.d("MyLog", "HERE");
            Log.d("MyLog", authcode);
            String checkUrl = "https://www.myapp.com/oauth2_uri?code=<"+authcode+">";
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view,  String url) {
                    view.loadUrl(url);
                    Log.d("MyLog",url);
                    return true;
                }
            });
            webView.loadUrl(checkUrl);
            Log.d("MyLog",checkUrl);
            //https://www.myapp.com/oauth2_uri?code=<authorization_code>


     //   }
    }
}