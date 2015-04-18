package tbits.com.synca;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Tobias.Boehm on 16/04/2015.
 */
public class SharedPreferencesHelper {
    public static String getDropboxToken(){
        Context context = MyApp.context;
        SharedPreferences sharedPref =context.getSharedPreferences(context.getString(R.string.preferences), Context.MODE_PRIVATE);
        String accessToken = sharedPref.getString(context.getString(R.string.dropbox_token), null);
        return accessToken;
    }

    public static void setDropboxAccessToken(String accessToken){
        Context context = MyApp.context;
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.dropbox_token), accessToken);
        editor.commit();
    }
}
