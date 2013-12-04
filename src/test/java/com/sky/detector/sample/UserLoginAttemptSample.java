package com.sky.detector.sample;

import com.sky.detector.login.attempt.UserLoginAttempt;

public class UserLoginAttemptSample {
    private final UserLoginAttempt result = new UserLoginAttempt();

    public UserLoginAttemptSample() {
        result.setIpAddress("80.238.9.179");
        result.setTimestamp(System.currentTimeMillis());
        result.setAction(UserLoginAttempt.Action.SIGNIN_SUCCESS);
        result.setUserName("Dave.Branning");
    }

    public UserLoginAttemptSample withAction(UserLoginAttempt.Action action) {
        result.setAction(action);
        return this;
    }
    public UserLoginAttempt build() {
        return result;
    }

    public UserLoginAttemptSample withTimestamp(long timestamp) {
        result.setTimestamp(timestamp);
        return this;
    }

}
