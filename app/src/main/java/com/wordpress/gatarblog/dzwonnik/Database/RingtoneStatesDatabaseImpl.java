package com.wordpress.gatarblog.dzwonnik.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wordpress.gatarblog.dzwonnik.RingtoneState;

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
        ContentValues values = fillNewRingtoneState(ringtoneState);

        long id = db.insert(TABLE_NAME,null,values);
        ringtoneState.setId(id);
        db.close();
    }

    @Override
    public void updateState(RingtoneState ringtoneState) {
        db = getWritableDatabase();
        String where = COLUMN_NAME_ID + "= ?";
        String [] whereArgs = new String[]{String.valueOf(ringtoneState.getId())};
        ContentValues values = fillUpdateRingtoneState(ringtoneState);
        System.out.println(ringtoneState.getId() + " ++++++++++++++++++++++++++++++++++++++++++++++   ");
        db.update(TABLE_NAME,values,where,whereArgs);
        db.close();
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

        db.close();
        return ringtoneStates;
    }

    @Override
    public void deleteState(RingtoneState ringtoneState) {
        db = this.getWritableDatabase();

        String where = COLUMN_NAME_ID + "= ?";
        String[] whereArgs = new String[]{String.valueOf(ringtoneState.getId())};

        db.delete(TABLE_NAME,where,whereArgs);
        db.close();
    }

    @Override
    public void cleanDatabase() {
        db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.delete(TABLE_NAME,null,null);
        db.close();
    }

    /**
     * Fill Content Values with RingtoneState data without id number (for create new database entity)
     * @param ringtoneState object for create Content Values
     * @return ready to save Content Values without ID number
     */
    private ContentValues fillNewRingtoneState(RingtoneState ringtoneState){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_VOLUME,ringtoneState.getVolumeValue());
        values.put(COLUMN_NAME_VIBRATION,parseBooleanToSQLiteInteger(ringtoneState.isVibration()));
        values.put(COLUMN_NAME_SILENT,parseBooleanToSQLiteInteger(ringtoneState.isSilent()));
        values.put(COLUMN_NAME_HOUR,ringtoneState.getHour());
        values.put(COLUMN_NAME_MINUTE,ringtoneState.getMinute());

        boolean[] ringtoneStateWeekDays = ringtoneState.getWeekDays();
        for(int i = 0; i < ringtoneStateWeekDays.length; i++){
            values.put(WEEK_DAYS[i],parseBooleanToSQLiteInteger(ringtoneStateWeekDays[i]));
        }
        return values;
    }

    /**
     * Fill Content Values with RingtoneState data with id number (for update database entity)
     * @param ringtoneState object for create Content Values
     * @return ready to save Content Values with ID number
     */
    private ContentValues fillUpdateRingtoneState(RingtoneState ringtoneState){
        ContentValues values = fillNewRingtoneState(ringtoneState);
        values.put(COLUMN_NAME_ID, ringtoneState.getId());
        return values;
    }

    private RingtoneState readRingtoneState(Cursor cursor){
        RingtoneState readState = new RingtoneState();

        readState.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_ID)));
        readState.setVolumeValue(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_VOLUME)));
        readState.setHour(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_HOUR)));
        readState.setMinute(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_MINUTE)));
        readState.setVibration(parseIntegerSQLiteToBoolean(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_VIBRATION))));
        readState.setSilent(parseIntegerSQLiteToBoolean(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_SILENT))));

        boolean[] ringtoneStateWeekDays = new boolean[7];
        for (int i = 0; i < ringtoneStateWeekDays.length; i++) {
            ringtoneStateWeekDays[i] = parseIntegerSQLiteToBoolean(cursor.getInt(cursor.getColumnIndex(WEEK_DAYS[i])));
        }
        readState.setWeekDays(ringtoneStateWeekDays);

        return readState;
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
