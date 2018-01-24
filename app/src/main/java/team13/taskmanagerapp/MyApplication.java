package team13.taskmanagerapp;

import android.app.Application;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

/**
 * Created by ilyauyutov on 23.01.18.
 */

public class MyApplication extends Application {

    public class Application extends android.app.Application {
        VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
            @Override
            public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
                if (newToken == null) {

                }
            }
        };
        @Override
        public void onCreate() {
            super.onCreate();
            vkAccessTokenTracker.startTracking();
            VKSdk.initialize(this);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(getApplicationContext());
    }
}
