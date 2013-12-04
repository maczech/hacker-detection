package com.sky.detector.policies;

import com.sky.detector.login.attempt.UserLoginAttempt;

public interface HackerDetectorPolicy {

    HackerDetectorResult detect(UserLoginAttempt userLogin);

}
