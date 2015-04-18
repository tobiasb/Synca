package tbits.com.synca;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        SchedulerManager.getInstance().startScheduler();
        Log.i(TAG, "onReceive");
    }
}
