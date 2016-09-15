package me.madri.vanilai.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by badri on 7/27/16.
 */
public class VanilaiSqlLiteHelper extends SQLiteOpenHelper {

    private static final String TABLE_VANILAI = "vanilai";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
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

    public void insertRecord(String city, double latitude, double longitude) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CITY, city);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        database.insert(TABLE_VANILAI, null, values);
        database.close();
    }

    public List<Map<String, Object>> getAllCities() {
        List<Map<String, Object>> cities = new ArrayList<>();
        String query = "SELECT * FROM "+TABLE_VANILAI;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            do {
                Map<String, Object> citiesMap = new HashMap<>();
                citiesMap.put("city", cursor.getString(1));
                citiesMap.put("latitude", cursor.getDouble(2));
                citiesMap.put("longitude", cursor.getDouble(3));
                cities.add(citiesMap);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cities;
    }

    public boolean removeCity(String city) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(TABLE_VANILAI, COLUMN_CITY + " = '"+city+"'", null) > 0;
    }
}
