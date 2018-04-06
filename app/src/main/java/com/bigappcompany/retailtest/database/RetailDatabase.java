package com.bigappcompany.retailtest.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bigappcompany.retailtest.model.UserDetailsModel;

import java.util.ArrayList;

/**
 * Created by shankar on 6/4/18.
 */

public class RetailDatabase extends SQLiteOpenHelper {

    private static final String KEY_NAME = "_name";
    private static final String KEY_REPUTATION = "_reputation";
    private static final String KEY_IMAGE = "_image";
    private static final String KEY_PAGINATIONCOUNT = "_paginationcount";


    private static final int VERSION = 1;
    private static final String DATABASE = "retailUserDatabase";

    private static final String USER_TABLE = "user_table";

    public RetailDatabase(Context context) {
        super(context, DATABASE, null, VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        String chat_table_query = "create table " + USER_TABLE + " (" + KEY_NAME + " text, "+ KEY_IMAGE + " text, " + KEY_REPUTATION + " text, " +KEY_PAGINATIONCOUNT
                + " text " +")";


        db.execSQL(chat_table_query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }

    public void onUpdate(String date,String language) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = this.getWritableDatabase();


    }

    public void insertUserData(UserDetailsModel userDetailsModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, userDetailsModel.getName());
        values.put(KEY_REPUTATION, userDetailsModel.getReputation());
        values.put(KEY_IMAGE, userDetailsModel.getImage());
        values.put(KEY_PAGINATIONCOUNT, userDetailsModel.getPaginationCount());

        // Inserting Row
        try{
            db.insert(USER_TABLE, null, values);
        }catch(Exception e){
            e.printStackTrace();
        }
        db.close(); // Closing database connection
    }

    public ArrayList<UserDetailsModel> getUserDetaiks() {
        ArrayList<UserDetailsModel> msgModel = new ArrayList<UserDetailsModel>();
        SQLiteDatabase db = this.getReadableDatabase();

        String s = "Select * from "+ USER_TABLE ;


        msgModel.clear();
        Cursor c = db.rawQuery(s, null);

        if (c.moveToFirst()) {
            do {
                UserDetailsModel item = new UserDetailsModel(c.getString(0), c.getString(1), c.getString(2),c.getString(3));

                        msgModel.add(item);
            }
            while (c.moveToNext());
        }
        c.close();
        db.close();

        return msgModel;
    }


}