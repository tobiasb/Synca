package tbits.com.synca;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;

public class DropboxHelper {

    public static DropboxAPI<AndroidAuthSession> initializeDropBox(){
        Context context = MyApp.context;
        AppKeyPair appKeys = new AppKeyPair(context.getString(R.string.APP_KEY), context.getString(R.string.APP_SECRET));
        AndroidAuthSession authSession;

        String accessToken = SharedPreferencesHelper.getDropboxToken();
        if(accessToken == null){
            authSession = new AndroidAuthSession(appKeys);
        } else {
            authSession = new AndroidAuthSession(appKeys, accessToken);
        }
        return new DropboxAPI<AndroidAuthSession>(authSession);
    }
}
