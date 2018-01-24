package team13.taskmanagerapp;

/**
 * Created by ilyauyutov on 23.01.2018.
 */

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.WebView;
import com.vk.sdk.api.VKError;

import java.util.Arrays;

public class WebLog extends AppCompatActivity {

    private String[] scope = new String[]{VKScope.MESSAGES, VKScope.FRIENDS, VKScope.WALL};


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_log);

        VKSdk.login(this, scope);

        if(isOnline() == false) {
            Toast.makeText(getApplicationContext(), "Оффлайн режим.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(WebLog.this, MainActivity.class);
            intent.putExtra("checkAuth","O");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        }

        //String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        //System.out.println("FINGERPRIIIINTTT");
        //System.out.println(Arrays.asList(fingerprints));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {

                Toast.makeText(getApplicationContext(), "Вы успешно авторизовались!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(WebLog.this, MainActivity.class);
                intent.putExtra("checkAuth","O");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                WebLog.this.finish();
            }

            @Override
            public void onError(VKError error) {
                Intent intent = new Intent(WebLog.this, AuthErrorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                WebLog.this.finish();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}