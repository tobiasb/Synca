package tbits.com.synca;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
    }

    @Override
    public void onResume(){
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if(key.equals("sync_enabled")){
            boolean value = sharedPreferences.getBoolean(key, false);

            Log.i("Settings", " changed " + key + " to " + value);

            if(value == true){
                enableBootReceiver();
                SchedulerManager.getInstance().startScheduler();
            } else {
                disableBootReceiver();
                SchedulerManager.getInstance().stopScheduler();
            }
        }


    }

    private void enableBootReceiver(){
        changeBootReceiverState(PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
    }

    private void disableBootReceiver(){
        changeBootReceiverState(PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
    }

    private void changeBootReceiverState(int state){
        Context context = getActivity();
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver, state, PackageManager.DONT_KILL_APP);
    }
}
