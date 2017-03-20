package com.wordpress.gatarblog.dzwonnik;

import android.content.Context;

import com.wordpress.gatarblog.dzwonnik.Database.RingtoneStatesDatabase;
import com.wordpress.gatarblog.dzwonnik.Database.RingtoneStatesDatabaseImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Test for basic database functionalities for RingtoneState: read, save, update and delete.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class RingtoneStatesDatabaseTest {


    private Context context;
    private RingtoneStatesDatabase database;

    private RingtoneState stateFirst = createAndFill(20, 10, 30, true, false);
    private RingtoneState stateSecond = createAndFill(80, 15, 0, false, true);

    @Before
    public void setUp() throws Exception {
        context = RuntimeEnvironment.application;
        context.deleteDatabase(RingtoneStatesDatabase.DATABASE_NAME);
        database = new RingtoneStatesDatabaseImpl(context);
    }

    @After
    public void tearDown() throws Exception {
        database.cleanDatabase();
    }

    @Test
    public void testDatabase() throws Exception{
        getEmptyAllStates();
        addState();
        updateState();
        deleteState();
        cleanDatabase();
    }

    @Test
    public void testContext() throws Exception{
        assertNotNull(context);
    }

    public void getEmptyAllStates() throws Exception {
        assertEquals(0,database.getAllStates().size());
    }

    public void addState() throws Exception {
        database.addState(stateFirst);
        database.addState(stateSecond);

        ArrayList<RingtoneState> loadedList = database.getAllStates();
        assertEquals(2,loadedList.size());
        assertEquals(stateFirst,loadedList.get(0));
        assertEquals(stateSecond,loadedList.get(1));
    }

    public void updateState() throws Exception {
        //given
        stateFirst.setVolumeValue(0);
        stateFirst.setHour(5);
        stateFirst.setMinute(9);
        stateFirst.setVibration(false);
        stateFirst.getWeekDays()[0] = true;
        stateFirst.getWeekDays()[2] = true;
        stateFirst.getWeekDays()[6] = true;

        //when
        database.updateState(stateFirst);

        //then
        ArrayList<RingtoneState> loadedList = database.getAllStates();
        assertEquals(2,loadedList.size());
        assertEquals(stateFirst,loadedList.get(0));
        assertEquals(stateSecond,loadedList.get(1));
    }

    public void deleteState() throws Exception {
        database.deleteState(stateFirst);

        ArrayList<RingtoneState> loadedList = database.getAllStates();
        assertEquals(1,loadedList.size());
        assertEquals(stateSecond,loadedList.get(0));
    }

    public void cleanDatabase() throws Exception {
        database.cleanDatabase();
    }

    private RingtoneState createAndFill(int volume, int hour, int minute, boolean vibra, boolean weekdays){
        RingtoneState ringtoneState = new RingtoneState(volume,vibra,hour,minute);
        boolean[] week = new boolean[7];
        for (int i = 0; i < week.length; i++) {
            week[i] = weekdays;
        }
        ringtoneState.setWeekDays(week);
        return ringtoneState;
    }

}