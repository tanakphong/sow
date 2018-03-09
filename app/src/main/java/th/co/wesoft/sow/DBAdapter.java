package th.co.wesoft.sow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by tanakorn.pho on 13/03/2560.
 */

public class DBAdapter {
    public SQLiteDatabase mDatabase;

    public DBAdapter(Context context) {
        String dbPath = AssetBundle.getAppPackagePath(context) + "/systemdb.sqlite";
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }
    public void insert(ConfigBean bn){
        ContentValues values = new ContentValues();
        values.put(ConfigBean.COLUMN_CLIENT,bn.getClient());
        values.put(ConfigBean.COLUMN_APP_VERSION,bn.getApp_version());
        values.put(ConfigBean.COLUMN_APP_CODE,bn.getApp_code());
        values.put(ConfigBean.COLUMN_APP_SIZE,bn.getApp_size());

        mDatabase.insert(ConfigBean.TABLE_NAME,null,values);
    }
    public ConfigBean query(int _client){
        String[] selectionAgrs = new String[]{String.valueOf(_client)};
        String[] columns=new String[]{ConfigBean.COLUMN_CLIENT,ConfigBean.COLUMN_APP_VERSION,ConfigBean.COLUMN_APP_CODE,ConfigBean.COLUMN_APP_SIZE};
        Cursor cursor = mDatabase.query(ConfigBean.TABLE_NAME, columns, "client = ?", selectionAgrs, null, null, "client asc");

        if(cursor.getCount()>0){
            cursor.moveToFirst();
            String _app_version = cursor.getString(cursor.getColumnIndex(ConfigBean.COLUMN_APP_VERSION));
            String _app_code = cursor.getString(cursor.getColumnIndex(ConfigBean.COLUMN_APP_CODE));
            long _app_size = cursor.getLong(cursor.getColumnIndex(ConfigBean.COLUMN_APP_SIZE));
            cursor.close();
            return new ConfigBean(_client,_app_version,_app_code,_app_size);
        }

        return null;
    }
    public void update(ConfigBean bn){
        ContentValues values = new ContentValues();
        values.put(ConfigBean.COLUMN_APP_VERSION,bn.getApp_version());
        values.put(ConfigBean.COLUMN_APP_CODE,bn.getApp_code());
        values.put(ConfigBean.COLUMN_APP_SIZE,bn.getApp_size());

        String whereClause ="client = ?";
        String[] whereAgrs=new String[]{String.valueOf(bn.getClient())};
        mDatabase.update(ConfigBean.TABLE_NAME,values,whereClause,whereAgrs);
    }

}
