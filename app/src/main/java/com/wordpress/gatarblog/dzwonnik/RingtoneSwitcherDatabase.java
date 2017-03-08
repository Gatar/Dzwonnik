package com.wordpress.gatarblog.dzwonnik;

import java.util.ArrayList;

/**
 * Created by bgata on 08.03.2017.
 */

public interface RingtoneSwitcherDatabase {

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

    String SQL_CREATE_TABLE_SWITCHER = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_NAME_ID + "          INTEGER   PRIMARY KEY AUTOINCREMENT" +
            COLUMN_NAME_VOLUME + "      INTEGER NOT NULL" +
            COLUMN_NAME_VIBRATION + "   NUMERIC NOT NULL" +
            COLUMN_NAME_HOUR + "        INTEGER NOT NULL" +
            COLUMN_NAME_MINUTE + "      INTEGER NOT NULL" +
            COLUMN_NAME_TUESDAY + "      NUMERIC NOT NULL" +
            COLUMN_NAME_WEDNESDAY + "      NUMERIC NOT NULL" +
            COLUMN_NAME_THURSDAY + "      NUMERIC NOT NULL" +
            COLUMN_NAME_FRIDAY + "      NUMERIC NOT NULL" +
            COLUMN_NAME_SATURDAY + "      NUMERIC NOT NULL" +
            COLUMN_NAME_SUNDAY + "      NUMERIC NOT NULL" +
            COLUMN_NAME_MONDAY + "      NUMERIC NOT NULL );";

    /**
     * Add switcher to database.
     * @param switcher RingtoneSwitcher object to add
     */
    void addSwitcher(RingtoneState switcher);

    /**
     * Update existing switcher in database.
     * @param switcher RingtoneSwitcher object to update
     */
    void updateSwitcher(RingtoneState switcher);

    /**
     * Get all switchers saved in database.
     * @return List of all Switchers
     */
    ArrayList<RingtoneState> getAllSwitchers();

    /**
     * Delete single switcher from database.
     * @param switcher RingtoneSwitcher object to delete
     */
    void deleteSwitcher(RingtoneState switcher);

    /**
     * Delete all database entries.
     */
    void cleanDatabase();
}
