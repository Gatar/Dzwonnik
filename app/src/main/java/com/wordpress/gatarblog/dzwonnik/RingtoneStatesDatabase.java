package com.wordpress.gatarblog.dzwonnik;

import java.util.ArrayList;

/**
 * Internal SQLite database class for remember RingtoneStates.
 */
public interface RingtoneStatesDatabase {

    String DATABASE_NAME = "switcherDatabase.db";
    String TABLE_NAME = "switchers";

    String COLUMN_NAME_ID = "id";
    String COLUMN_NAME_VOLUME = "volume";
    String COLUMN_NAME_VIBRATION = "vibration";
    String COLUMN_NAME_HOUR = "hour";
    String COLUMN_NAME_MINUTE = "minute";
    String COLUMN_NAME_MONDAY = "monday";
    String COLUMN_NAME_TUESDAY = "tuesday";
    String COLUMN_NAME_WEDNESDAY = "wednesday";
    String COLUMN_NAME_THURSDAY = "thursday";
    String COLUMN_NAME_FRIDAY = "friday";
    String COLUMN_NAME_SATURDAY = "saturday";
    String COLUMN_NAME_SUNDAY = "sunday";
    String [] WEEK_DAYS  = {COLUMN_NAME_MONDAY,COLUMN_NAME_TUESDAY,COLUMN_NAME_WEDNESDAY, COLUMN_NAME_THURSDAY,COLUMN_NAME_FRIDAY,COLUMN_NAME_SATURDAY,COLUMN_NAME_SUNDAY};

    String SQL_CREATE_TABLE_SWITCHER = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_NAME_ID + "          INTEGER   PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME_VOLUME + "      INTEGER NOT NULL, " +
            COLUMN_NAME_VIBRATION + "   INTEGER NOT NULL, " +
            COLUMN_NAME_HOUR + "        INTEGER NOT NULL, " +
            COLUMN_NAME_MINUTE + "      INTEGER NOT NULL, " +
            COLUMN_NAME_TUESDAY + "      INTEGER NOT NULL, " +
            COLUMN_NAME_WEDNESDAY + "      INTEGER NOT NULL, " +
            COLUMN_NAME_THURSDAY + "      INTEGER NOT NULL, " +
            COLUMN_NAME_FRIDAY + "      INTEGER NOT NULL, " +
            COLUMN_NAME_SATURDAY + "      INTEGER NOT NULL, " +
            COLUMN_NAME_SUNDAY + "      INTEGER NOT NULL, " +
            COLUMN_NAME_MONDAY + "      INTEGER NOT NULL );";

    /**
     * Add ringtoneState to database.
     * @param ringtoneState RingtoneSwitcher object to add
     */
    void addState(RingtoneState ringtoneState);

    /**
     * Update existing ringtoneState in database.
     * @param ringtoneState RingtoneSwitcher object to update
     */
    void updateState(RingtoneState ringtoneState);

    /**
     * Get all switchers saved in database.
     * @return List of all Switchers
     */
    ArrayList<RingtoneState> getAllStates();

    /**
     * Delete single ringtoneState from database.
     * @param ringtoneState RingtoneSwitcher object to delete
     */
    void deleteState(RingtoneState ringtoneState);

    /**
     * Delete all database entries.
     */
    void cleanDatabase();
}
