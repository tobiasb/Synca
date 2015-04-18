package tbits.com.synca;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MediaUploadService extends BroadcastReceiver {
    private static final String TAG = "MediaUploadService";
    private DropboxAPI<AndroidAuthSession> dbApi;

    public MediaUploadService(){
        dbApi = DropboxHelper.initializeDropBox();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(/*!shouldUpload(context) ||*/ !canUpload()){
            return;
        }

        SyncDataHelper syncDataHelper = new SyncDataHelper(MyApp.context);

        for(SyncDataContract.SyncData unsyncedEntry : syncDataHelper.getAllUnsynced()){
            TaskParameters param = new TaskParameters();
            param.unsyncedEntry = unsyncedEntry;
            param.dbApi = dbApi;
            param.syncDataHelper = syncDataHelper;

            new AsyncUploader().execute(param);
        }
    }

    private boolean shouldUpload(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
               activeNetwork.isConnected() &&
               activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
    }

    private boolean canUpload(){
        return dbApi.getSession().isLinked();
    }

    private class TaskParameters{
        public SyncDataContract.SyncData unsyncedEntry;
        public DropboxAPI<AndroidAuthSession> dbApi;
        SyncDataHelper syncDataHelper;
    }

    private class AsyncUploader extends AsyncTask<TaskParameters, Void, Void> {
        @Override
        protected Void doInBackground(TaskParameters... params) {
            TaskParameters param = params[0];

            try {
                File file = new File(param.unsyncedEntry.FilePath);
                FileInputStream fis = new FileInputStream(file);
                DropboxAPI.Entry response = param.dbApi.putFile(param.unsyncedEntry.RelativePath, fis, file.length(), null, null);
                param.syncDataHelper.setSynced(param.unsyncedEntry.Id);
            } catch (FileNotFoundException e) {
                Log.e(TAG, "Could not create fileStream", e);
            } catch (DropboxException e) {
                Log.e(TAG, "Could not upload", e);
            }

            return null;
        }
    }
}
