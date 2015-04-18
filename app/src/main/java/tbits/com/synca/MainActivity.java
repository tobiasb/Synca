package tbits.com.synca;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;

import java.util.List;


public class MainActivity extends ActionBarActivity {


    private DropboxAPI<AndroidAuthSession> dbApi;
    private static final String TAG = "MainActivity";
    public final static String EXTRA_MESSAGE = "tbits.com.synca.MESSAGE";
    private boolean startedDropboxAuth = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbApi = DropboxHelper.initializeDropBox();

        Log.i(TAG, "MainActivity created");

        if(SharedPreferencesHelper.getDropboxToken() == null) {
            startedDropboxAuth = true;
            dbApi.getSession().startOAuth2Authentication(MainActivity.this);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        if(startedDropboxAuth && dbApi.getSession().authenticationSuccessful()){
            try{
                dbApi.getSession().finishAuthentication();
                String accessToken = dbApi.getSession().getOAuth2AccessToken();

                SharedPreferencesHelper.setDropboxAccessToken(accessToken);
                startedDropboxAuth = false;
            }catch(IllegalStateException ex){
                Log.i(TAG, "Error authenticating Dropbox", ex);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            openSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openSettings(){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void sendMessage(View view){
        new SyncDataHelper(getApplicationContext()).deleteAll();
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText)findViewById(R.id.edit_message);
        intent.putExtra(EXTRA_MESSAGE, editText.getText().toString());
        startActivity(intent);
    }

    public void showDatabaseContent(View view){
        List<SyncDataContract.SyncData> all = new SyncDataHelper(getApplicationContext()).getAll();
        String result = "";

        for(SyncDataContract.SyncData syncData : all){
            result += syncData.FilePath + "\n";
        }

        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText)findViewById(R.id.edit_message);
        intent.putExtra(EXTRA_MESSAGE, result);
        startActivity(intent);
    }
}
