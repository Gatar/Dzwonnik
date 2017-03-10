package com.wordpress.gatarblog.dzwonnik;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class RingtoneStatesDatabaseImpl extends SQLiteOpenHelper implements RingtoneStatesDatabase {

    private SQLiteDatabase db;

    public RingtoneStatesDatabaseImpl(Context context) {
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
    public void addState(RingtoneState ringtoneState) {
        db = this.getWritableDatabase();
        ContentValues values = fillContentStateValues(ringtoneState);

        long id = db.insert(TABLE_NAME,null,values);
        ringtoneState.setId(id);
    }

    @Override
    public void updateState(RingtoneState ringtoneState) {
        String where = COLUMN_NAME_ID + "= ?";
        String [] whereArgs = new String[]{String.valueOf(ringtoneState.getId())};
        ContentValues values = fillContentStateValues(ringtoneState);

        db.update(TABLE_NAME,values,where,whereArgs);
    }

    @Override
    public ArrayList<RingtoneState> getAllStates() {
        db = this.getReadableDatabase();

        ArrayList<RingtoneState> ringtoneStates = new ArrayList<>();

        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        while (cursor.moveToNext()){
            ringtoneStates.add(
                    readRingtoneState(cursor)
            );
        }

        return ringtoneStates;
    }

    @Override
    public void deleteState(RingtoneState ringtoneState) {
        db = this.getWritableDatabase();

        String where = COLUMN_NAME_ID + "= ?";
        String[] whereArgs = new String[]{String.valueOf(ringtoneState.getId())};

        db.delete(TABLE_NAME,where,whereArgs);
    }

    @Override
    public void cleanDatabase() {
        db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    private ContentValues fillContentStateValues(RingtoneState ringtoneState){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_VOLUME,ringtoneState.getVolumeValue());
        values.put(COLUMN_NAME_VIBRATION,parseBooleanToSQLiteInteger(ringtoneState.isVibration()));
        values.put(COLUMN_NAME_HOUR,ringtoneState.getHour());
        values.put(COLUMN_NAME_MINUTE,ringtoneState.getMinute());

        boolean[] ringtoneStateWeekDays = ringtoneState.getWeekDays();
        for(int i = 0; i < ringtoneStateWeekDays.length; i++){
            values.put(WEEK_DAYS[i],parseBooleanToSQLiteInteger(ringtoneStateWeekDays[i]));
        }
        return values;
    }

    private RingtoneState readRingtoneState(Cursor cursor){
        RingtoneState readedState = new RingtoneState();

        readedState.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_ID)));
        readedState.setVolumeValue(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_VOLUME)));
        readedState.setHour(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_HOUR)));
        readedState.setMinute(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_MINUTE)));
        readedState.setVibration(parseIntegerSQLiteToBoolean(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_VIBRATION))));

        boolean[] ringtoneStateWeekDays = new boolean[7];
        for (int i = 0; i < ringtoneStateWeekDays.length; i++) {
            ringtoneStateWeekDays[i] = parseIntegerSQLiteToBoolean(cursor.getInt(cursor.getColumnIndex(WEEK_DAYS[i])));
        }
        readedState.setWeekDays(ringtoneStateWeekDays);

        return readedState;
    }

    private int parseBooleanToSQLiteInteger(boolean value){
        return value ? 1 : 0;
    }

    private boolean parseIntegerSQLiteToBoolean(int valueFromDatabase){
        if(valueFromDatabase == 1) return  true;
        else if (valueFromDatabase == 0) return false;
        else throw new IllegalArgumentException("Boolean value saved in database as Numeric is incorrect! Value: " + valueFromDatabase);
    }

}
