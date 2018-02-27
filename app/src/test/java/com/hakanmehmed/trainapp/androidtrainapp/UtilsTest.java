package com.hakanmehmed.trainapp.androidtrainapp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by hakanmehmed on 26/02/2018.
 * Utils class unit tests
 */

public class UtilsTest {
    @Test
    public void formatTimeTest() {
        String time = "2018-02-21T18:33:00+00:00";
        String result = Utils.formatTime(time);
        String expectedResult = "18:33";

        String notExpectedResult = "18:00";

        assertEquals(expectedResult, result);
        assertNotEquals(notExpectedResult, result);
    }

    @Test
    public void formatDateTest() {
        String time = "2018-02-21T18:33:00+00:00";
        String result = Utils.formatDate(time);
        String expectedResult = "2018-02-21";

        String notExpectedResult = "2018-03-21";
        String notExpectedResult2 = "2018-02-22";

        assertEquals(expectedResult, result);
        assertNotEquals(notExpectedResult, result);
        assertNotEquals(notExpectedResult2, result);
    }

    @Test
    public void getMillisecondsTest() {
        String time = "2018-02-21T18:33:00+00:00";
        long result = Utils.getSeconds(time);
        long expectedResult = 1519237980;
        long notExpectedResult = 151923798;

        assertEquals(expectedResult, result);
        assertNotEquals(notExpectedResult, result);
    }

    @Test
    public void getTimeDifferenceTest() {
        String time1 = "2018-02-21T18:33:00+00:00";
        String time2 = "2018-02-21T18:33:00+00:00";
        String result = Utils.getTimeDifference(time1, time2, true);
        String expectedResult = "";
        assertEquals(expectedResult.trim(), result.trim());

        time1 = "2018-02-21T18:23:00+00:00";
        time2 = "2018-02-21T18:33:00+00:00";
        result = Utils.getTimeDifference(time1, time2, true);
        expectedResult = "10m";
        assertEquals(expectedResult.trim(), result.trim());

        time1 = "2018-02-21T18:23:00+00:00";
        time2 = "2018-02-21T19:33:00+00:00";
        result = Utils.getTimeDifference(time1, time2, true);
        expectedResult = "1h 10m";
        assertEquals(expectedResult.trim(), result.trim());

        time1 = "2018-02-21T19:00:00+00:00";
        time2 = "2018-02-21T18:33:00+00:00";
        result = Utils.getTimeDifference(time1, time2, true);
        expectedResult = "27m";
        assertEquals(expectedResult.trim(), result.trim());

        time1 = "2018-02-21T18:33:00+00:00";
        time2 = "2018-02-21T18:33:00+00:00";
        result = Utils.getTimeDifference(time1, time2, false);
        expectedResult = "";
        assertEquals(expectedResult.trim(), result.trim());

        time1 = "2018-02-21T18:23:00+00:00";
        time2 = "2018-02-21T18:33:00+00:00";
        result = Utils.getTimeDifference(time1, time2, false);
        expectedResult = "10 minutes";
        assertEquals(expectedResult.trim(), result.trim());

        time1 = "2018-02-21T18:23:00+00:00";
        time2 = "2018-02-21T19:33:00+00:00";
        result = Utils.getTimeDifference(time1, time2, false);
        expectedResult = "1 hour 10 minutes";
        assertEquals(expectedResult.trim(), result.trim());

        time1 = "2018-02-21T19:00:00+00:00";
        time2 = "2018-02-21T18:33:00+00:00";
        result = Utils.getTimeDifference(time1, time2, false);
        expectedResult = "27 minutes";
        assertEquals(expectedResult.trim(), result.trim());
    }

    @Test
    public void isSameTimeTest() {
        String time1 = "2018-02-21T18:33:00+00:00";
        String time2 = "2018-02-21T18:33:00+00:00";
        boolean result = Utils.isSameTime(time1, time2);
        assertEquals(true, result);


        time1 = "2018-02-21T18:33:00+00:00";
        time2 = "2018-02-21T18:34:00+00:00";
        result = Utils.isSameTime(time1, time2);
        assertEquals(false, result);
    }

    @Test
    public void isDateBetweenTest(){
        String date = "2018-02-21T08:20:00+00:00";
        String before = "2018-02-21T07:33:00+00:00";
        String after = "2018-02-21T08:21:00+00:00";
        boolean result = Utils.isDateBetween(date, before, after);
        assertEquals(true, result);

        after = "2018-02-21T08:19:00+00:00";
        result = Utils.isDateBetween(date, before, after);
        assertEquals(false, result);

        before = "2018-02-21T08:20:00+00:00";
        after = "2018-02-21T08:21:00+00:00";
        result = Utils.isDateBetween(date, before, after);
        assertEquals(true, result);

    }
}
