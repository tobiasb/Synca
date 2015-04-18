package tbits.com.synca;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobias.Boehm on 14/04/2015.
 */
public class SyncDataHelper extends SQLiteOpenHelper {
    public static final int DatabaseVersion = 3;
    public static final String DatabaseName = "SyncData.db";

    public SyncDataHelper(Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + SyncDataContract.SyncDataEntry.TableName
                + " (" +
                SyncDataContract.SyncDataEntry._ID + " INTEGER PRIMARY KEY" +
                "," + SyncDataContract.SyncDataEntry.FilePath + " TEXT" +
                "," + SyncDataContract.SyncDataEntry.RelativePath + " TEXT" +
                "," + SyncDataContract.SyncDataEntry.Synced + " INTEGER"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SyncDataContract.SyncDataEntry.TableName);
        onCreate(db);
    }

    public List<SyncDataContract.SyncData> getAll(){
        List<SyncDataContract.SyncData> resultList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {
                SyncDataContract.SyncDataEntry._ID,
                SyncDataContract.SyncDataEntry.FilePath,
                SyncDataContract.SyncDataEntry.RelativePath,
                SyncDataContract.SyncDataEntry.Synced
        };
        Cursor c = db.query(
                SyncDataContract.SyncDataEntry.TableName,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        if(c.moveToFirst()){
            do {
                SyncDataContract.SyncData data = new SyncDataContract.SyncData();
                data.Id = c.getInt(0);
                data.FilePath = c.getString(1);
                data.RelativePath = c.getString(2);
                data.Synced = c.getInt(3) == 1 ? true : false;
                resultList.add(data);
            }while(c.moveToNext());
        }

        return resultList;
    }

    public List<SyncDataContract.SyncData> getAllUnsynced(){
        List<SyncDataContract.SyncData> resultList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {
                SyncDataContract.SyncDataEntry._ID,
                SyncDataContract.SyncDataEntry.FilePath,
                SyncDataContract.SyncDataEntry.RelativePath,
                SyncDataContract.SyncDataEntry.Synced
        };
        Cursor c = db.query(
                SyncDataContract.SyncDataEntry.TableName,
                columns,
                "Synced = 0",
                null,
                null,
                null,
                null
        );

        if(c.moveToFirst()){
            do {
                SyncDataContract.SyncData data = new SyncDataContract.SyncData();
                data.Id = c.getInt(0);
                data.FilePath = c.getString(1);
                data.RelativePath = c.getString(2);
                data.Synced = c.getInt(3) == 1 ? true : false;
                resultList.add(data);
            }while(c.moveToNext());
        }

        return resultList;
    }

    public boolean exists(String filePath){
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {
                SyncDataContract.SyncDataEntry._ID,
                SyncDataContract.SyncDataEntry.FilePath,
                SyncDataContract.SyncDataEntry.RelativePath,
                SyncDataContract.SyncDataEntry.Synced
        };
        String[] whereArgs = {filePath};
        Cursor c = db.query(
                SyncDataContract.SyncDataEntry.TableName,
                columns,
                "FilePath = ?",
                whereArgs,
                null,
                null,
                null
        );

        return c.getCount() > 0;
    }

    public void insert(SyncDataContract.SyncData syncData){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SyncDataContract.SyncDataEntry.FilePath, syncData.FilePath);
        values.put(SyncDataContract.SyncDataEntry.RelativePath, syncData.RelativePath);
        values.put(SyncDataContract.SyncDataEntry.Synced, syncData.Synced ? 1 : 0);
        db.insert(SyncDataContract.SyncDataEntry.TableName, null, values);
    }

    public void deleteAll(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(SyncDataContract.SyncDataEntry.TableName, null, null);
    }

    public void setSynced(int id){
        SQLiteDatabase db = getWritableDatabase();
        String strFilter = SyncDataContract.SyncDataEntry._ID + "=" + id;
        ContentValues args = new ContentValues();
        args.put(SyncDataContract.SyncDataEntry.Synced, 1);
        db.update(SyncDataContract.SyncDataEntry.TableName, args, strFilter, null);
    }
}
