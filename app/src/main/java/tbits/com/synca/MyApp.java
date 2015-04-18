package tbits.com.synca;

import android.app.Application;
import android.content.Context;

/**
 * Created by Tobias.Boehm on 03/31/2015.
 */
public class MyApp extends Application {
    public static Context context;

    @Override public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
