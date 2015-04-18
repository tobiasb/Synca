package tbits.com.synca;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * Created by Tobias.Boehm on 03/31/2015.
 */
public class SchedulerManager {
    private static final long INTERVAL_TEN_SECONDS = 10 * 1000;

    private static SchedulerManager instance = null;

    private static PendingIntent[] pi;

    private SchedulerManager(){}

    public static SchedulerManager getInstance(){
        if(instance == null){
            instance = new SchedulerManager();
        }

        return instance;
    }

    public void startScheduler(){
        if(pi != null) return;

        pi = new PendingIntent[]{
                startScheduler(MediaDiscoveryService.class, INTERVAL_TEN_SECONDS),
                startScheduler(MediaUploadService.class, INTERVAL_TEN_SECONDS)
        };
    }

    private PendingIntent startScheduler(Class clazz, long interval){
        Context context = MyApp.context;
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, clazz);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC,
                System.currentTimeMillis() + interval,//AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                interval,//AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                pi);

        return pi;
    }

    public void stopScheduler(){
        if(pi == null) return;

        Context context = MyApp.context;
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        for(PendingIntent pendingIntent : pi){
            alarmManager.cancel(pendingIntent);
        }
    }
}
