package com.wordpress.gatarblog.dzwonnik.Receivers;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * Test of calculation new volume value basing on stream maximum value
 */
public class RingtoneSwitcherTest {

    private static Class clazz;
    private static Method method;
    private static Object ringtoneSwitcherInstance;


    @BeforeClass
    public static void beforeAll() throws Exception {
        clazz = RingtoneSwitcher.class;
        method = clazz.getDeclaredMethod("calculateNewVolume",int.class, int.class);
        method.setAccessible(true);
        ringtoneSwitcherInstance = clazz.newInstance();
    }

    @Test
    public void maximumVolValueSeven() throws Exception {
        int volume = 2;
        int maxVol = 7;
        int expect = 2; // 2/7 * 7 = 2
        Object result = method.invoke(ringtoneSwitcherInstance,volume,maxVol);
        Assert.assertEquals(expect,result);
    }

    @Test
    public void maximumVolValueMultiplySeven() throws Exception{
        int volume = 2;
        int maxVol = 14;
        int expect = 4; // 14/7 * 2 = 4
        Object result = method.invoke(ringtoneSwitcherInstance,volume,maxVol);
        Assert.assertEquals(expect,result);
    }

    @Test
    public void maximumVolValueNonMultiplySeven() throws Exception{
        int volume = 2;
        int maxVol = 40;
        int expect = 11; // 40/7 * 2 = 11.42 --floor--> 11
        Object result = method.invoke(ringtoneSwitcherInstance,volume,maxVol);
        Assert.assertEquals(expect,result);
    }

    @Test
    public void newVolumeZero() throws Exception{
        int volume = 0;
        int maxVol = 15;
        int expect = 0; // 15/7 * 0 = 0
        Object result = method.invoke(ringtoneSwitcherInstance,volume,maxVol);
        Assert.assertEquals(expect,result);
    }

    @Test
    public void newVolumeMaximum() throws Exception{
        int volume = 7;
        int maxVol = 41;
        int expect = 41; // 41/7 * 7 = 41
        Object result = method.invoke(ringtoneSwitcherInstance,volume,maxVol);
        Assert.assertEquals(expect,result);
    }


}