package com.sky.detector;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import com.sky.detector.login.attempt.UserLoginAttemptFactory;
import com.sky.detector.policies.HackerDetectorPolicy;
import com.sky.detector.policies.UserLoginAttemptCountPolicy;

public class HackerDetectorFactory {

    public HackerDetector create(Integer maximumAttemptNumber, Integer periodOfTimeInMinutes) {
        HackerDetectorImpl instance = new HackerDetectorImpl();

        List<HackerDetectorPolicy> policies = new ArrayList<>();
        UserLoginAttemptCountPolicy policy = new UserLoginAttemptCountPolicy();
        policy.setMaximumNumberOfLoginAttempts(maximumAttemptNumber);
        policy.setPeriodOfTimeInMinutes(periodOfTimeInMinutes);
        policy.setCleanupTimeInMiliseconds(60 * 1000);
        Timer timer = new Timer(true);
        policy.setTimer(timer);
        policy.initCleanup();
        policies.add(policy);

        instance.setPolicies(policies);
        instance.setUserLoginFactory(new UserLoginAttemptFactory());

        return instance;

    }

}
