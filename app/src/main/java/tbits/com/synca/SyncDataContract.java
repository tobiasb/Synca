package tbits.com.synca;

import android.provider.BaseColumns;

/**
 * Created by Tobias.Boehm on 14/04/2015.
 */
public final class SyncDataContract {
    public SyncDataContract(){}

    public static abstract class SyncDataEntry implements BaseColumns{
        public static final String TableName = "SyncData";

        public static final String FilePath = "FilePath";
        public static final String RelativePath = "RelativePath";
        public static final String Synced = "Synced";
    }

    public static class SyncData{
        public int Id;
        public String FilePath;
        public String RelativePath;
        public boolean Synced;
    }
}
