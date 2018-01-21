package team13.taskmanagerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebLog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_log);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Браузер");

        WebView webView;
        webView = findViewById(R.id.webLogin);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view,  String url) {
            view.loadUrl(url);
            return true;
            }
        });
        //webView.loadUrl("https://google.com");
        webView.loadUrl("https://www.wrike.com/oauth2/authorize?client_id=01a00I4c&response_type=code");
    }
}
