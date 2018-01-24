package team13.taskmanagerapp;

/**
 * Created by ilyauyutov on 23.01.2018.
 */

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_log);

        VKSdk.login(this, scope);


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
                startActivity(intent);
            }

            @Override
            public void onError(VKError error) {

                Intent intent = new Intent(WebLog.this, AuthErrorActivity.class);
                startActivity(intent);

            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}