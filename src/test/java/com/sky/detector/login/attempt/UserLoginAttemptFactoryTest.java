package com.sky.detector.login.attempt;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UserLoginAttemptFactoryTest {

    protected UserLoginAttemptFactory objectUnderTest = new UserLoginAttemptFactory();

    @Test
    public void shouldParseLogLine() {
        String logLine = "80.238.9.179,133612947,SIGNIN_SUCCESS,Dave.Branning";
        UserLoginAttempt result = objectUnderTest.create(logLine);

        assertEquals("80.238.9.179", result.getIpAddress());
        long timestamp = Long.parseLong("133612947");
        assertEquals(timestamp, result.getTimestamp().longValue());
        assertEquals(UserLoginAttempt.Action.SIGNIN_SUCCESS, result.getAction());
        assertEquals("Dave.Branning", result.getUserName());
    }

    @Test(expected=IllegalArgumentException.class)
    public void shouldThrowExceptionWhenMissingField() {
        String logLine = "80.238.9.179,133612947,SIGNIN_SUCCESS";
        objectUnderTest.create(logLine);
    }

    @Test(expected=IllegalArgumentException.class)
    public void shouldThrowExceptionWhenEmptyLine() {
        String logLine = "";
        objectUnderTest.create(logLine);
    }

    @Test(expected=IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNullLine() {
        objectUnderTest.create(null);
    }

    @Test(expected=IllegalStateException.class)
    public void shouldThrowExceptionWhenTimestampIsNotLong() {
        String logLine = "80.238.9.179,NOT_LONG,SIGNIN_SUCCESS,Dave.Branning";
        objectUnderTest.create(logLine);
    }

    @Test(expected=IllegalStateException.class)
    public void shouldThrowExceptionWhenWrongAction() {
        String logLine = "80.238.9.179,133612947,WRONG_ACTION,Dave.Branning";
        objectUnderTest.create(logLine);
    }

}
