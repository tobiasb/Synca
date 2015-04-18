package tbits.com.synca;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MediaDiscoveryService extends BroadcastReceiver {
    private static final String TAG = "MediaDiscoveryService";

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "tbits.com.synca.action.FOO";
    private static final String ACTION_BAZ = "tbits.com.synca.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "tbits.com.synca.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "tbits.com.synca.extra.PARAM2";


    private static final String WhatsAppFolderName = "WhatsApp";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
            }
        }

        File whatsAppDir = getWhatsAppDirectory();

        if(whatsAppDir != null){
            ArrayList<File> files = getAllFilesIn(whatsAppDir);
            handleFiles(files);
        }
    }

    private ArrayList<File> getAllFilesIn(File root){
        ArrayList<File> list =  new ArrayList<>();

        if(root == null) return list;

        if(root.isFile()){
            list.add(root);
            return list;
        }

        for(File file : root.listFiles()){
            list.addAll(getAllFilesIn(file));
        }

        return list;
    }

    private void handleFiles(ArrayList<File> files){
        SyncDataHelper syncDataHelper = new SyncDataHelper(MyApp.context);
        handleFiles(files, syncDataHelper);
    }

    private void handleFiles(ArrayList<File> files, SyncDataHelper db){
        for(File file : files){
            SyncDataContract.SyncData syncData = new SyncDataContract.SyncData();
            syncData.FilePath = file.getAbsolutePath();
            syncData.RelativePath = getRelativeFilePath(file);

            if(!isRelevantFileType(file) || db.exists(syncData.FilePath)){
                continue;
            }

            db.insert(syncData);
        }
    }

    private boolean isRelevantFileType(File file){
        String fileName = file.getName();
        return !(
                    fileName.endsWith(".nomedia") ||
                    fileName.endsWith(".aac") ||
                    fileName.endsWith(".mp4") ||
                    fileName.endsWith(".db.crypt8")
                );
    }

    private String getRelativeFilePath(File file){
        int startIndex = file.getAbsolutePath().indexOf(WhatsAppFolderName) + WhatsAppFolderName.length();
        return file.getAbsolutePath().substring(startIndex);
    }

    private File getWhatsAppDirectory(){
        if(!isExternalStorageReadable()){
            Log.i(TAG, "External storage not readable");
            return null;
        }

        File rootDir = Environment.getExternalStorageDirectory();

        if(rootDir != null && rootDir.canRead()){
            File whatsAppDir = new File(rootDir, WhatsAppFolderName);
            if(whatsAppDir != null && whatsAppDir.exists() && whatsAppDir.canRead()){
                return whatsAppDir;
            }
        }

        return null;
    }

    /* Checks if external storage is available to at least read */
    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
