package com.sky.detector.login.attempt;

import java.text.MessageFormat;

public class UserLoginAttemptFactory {
    public UserLoginAttempt create(String logLine) {
        if (logLine == null) {
            throw new IllegalArgumentException("Log line cannot be null");
        }
        String[] logLineFields = logLine.split(",");
        if (logLineFields.length < 4) {
            throw new IllegalArgumentException(MessageFormat.format(
                    "Cannot parse log line {0}, line should contains 4 fields but has {1}",
                    logLine, logLineFields.length));
        }

        UserLoginAttempt userLogin = new UserLoginAttempt();
        userLogin.setIpAddress(logLineFields[0]);

        try {
            Long timestamp = Long.valueOf(logLineFields[1]);
            userLogin.setTimestamp(timestamp);
        } catch (NumberFormatException e) {
            throw new IllegalStateException(MessageFormat.format(
                    "Timestamp must be a valid long number, but was {0}", logLineFields[1]));
        }
        try {
            UserLoginAttempt.Action action = UserLoginAttempt.Action.valueOf(logLineFields[2]);
            userLogin.setAction(action);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(MessageFormat.format("Unknown user login action:{0}",
                    logLineFields[2]));
        }

        userLogin.setUserName(logLineFields[3]);
        return userLogin;
    }
}
