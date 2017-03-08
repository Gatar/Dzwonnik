package com.wordpress.gatarblog.dzwonnik;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by bgata on 08.03.2017.
 */

public class RingtoneSwitcherDatabaseImpl extends SQLiteOpenHelper implements RingtoneSwitcherDatabase{

    private SQLiteDatabase db;

    public RingtoneSwitcherDatabaseImpl(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_SWITCHER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void addSwitcher(RingtoneState switcher) {

    }

    @Override
    public void updateSwitcher(RingtoneState switcher) {

    }

    @Override
    public ArrayList<RingtoneState> getAllSwitchers() {
        return null;
    }

    @Override
    public void deleteSwitcher(RingtoneState switcher) {

    }

    @Override
    public void cleanDatabase() {

    }
}
