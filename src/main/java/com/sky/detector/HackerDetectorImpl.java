package com.sky.detector;

import java.util.List;

import com.sky.detector.login.attempt.UserLoginAttempt;
import com.sky.detector.login.attempt.UserLoginAttemptFactory;
import com.sky.detector.policies.HackerDetectorPolicy;
import com.sky.detector.policies.HackerDetectorResult;

public class HackerDetectorImpl implements HackerDetector {

    protected UserLoginAttemptFactory userLoginFactory;
    protected List<HackerDetectorPolicy> policies;

    @Override
    public String parseLine(String line) {
        UserLoginAttempt userLogin = userLoginFactory.create(line);
        for (HackerDetectorPolicy policy : policies) {
            HackerDetectorResult result = policy.detect(userLogin);
            if (result.isSucessfull()) {
                return userLogin.getIpAddress();
            }
        }

        return null;
    }

    public UserLoginAttemptFactory getUserLoginFactory() {
        return userLoginFactory;
    }

    public void setUserLoginFactory(UserLoginAttemptFactory userLoginFactory) {
        this.userLoginFactory = userLoginFactory;
    }

    public List<HackerDetectorPolicy> getPolicies() {
        return policies;
    }

    public void setPolicies(List<HackerDetectorPolicy> policies) {
        this.policies = policies;
    }

}
