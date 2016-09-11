package madri.me.vanilai.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by badri on 7/27/16.
 */
public class VanilaiSqlLiteHelper extends SQLiteOpenHelper {

    private static final String TABLE_VANILAI = "vanilai";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_LATITUDE = "zip";
    private static final String COLUMN_LONGITUDE = "country";
    private static final String DATABASE_NAME = "vanilai.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table "+TABLE_VANILAI+" " +
            "("+COLUMN_ID+" integer primary key autoincrement, "+COLUMN_CITY+" text not null, "+COLUMN_LATITUDE+" real not null, "+COLUMN_LONGITUDE+" real not null);";

    public VanilaiSqlLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(VanilaiSqlLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VANILAI);
        onCreate(db);
    }


}
