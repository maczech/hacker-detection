package com.sky.detector.policies;

import java.util.Timer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sky.detector.login.attempt.UserLoginAttempt;
import com.sky.detector.sample.UserLoginAttemptSample;

public class UserLoginAttemptCountPolicyTest {
    @Mock
    Timer timer;

    @InjectMocks
    UserLoginAttemptCountPolicy objectUnderTest = new UserLoginAttemptCountPolicy();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        objectUnderTest.setCleanupTimeInMiliseconds(2000);
        objectUnderTest.setMaximumNumberOfLoginAttempts(5);
        objectUnderTest.setPeriodOfTimeInMinutes(2);
    }

    @Test
    public void shouldDetectWhenLimitExceeded() {
        UserLoginAttempt loginAttempt = new UserLoginAttemptSample().withAction(
                UserLoginAttempt.Action.SIGNIN_FAILURE).build();
        HackerDetectorResult result = null;
        for (int i = 0; i < 4; i++) {
            result = objectUnderTest.detect(loginAttempt);
            Assert.assertFalse(result.isSucessfull());
        }
        result = objectUnderTest.detect(loginAttempt);
        Assert.assertTrue(result.isSucessfull());
    }

    @Test
    public void shouldNotDetectWhenAllOlderThanPeriod() {
        UserLoginAttempt loginAttempt = new UserLoginAttemptSample()
                .withAction(UserLoginAttempt.Action.SIGNIN_FAILURE).withTimestamp(1336129471L)
                .build();
        HackerDetectorResult result = null;
        for (int i = 0; i < 4; i++) {
            result = objectUnderTest.detect(loginAttempt);
            Assert.assertFalse(result.isSucessfull());
        }
        result = objectUnderTest.detect(loginAttempt);
        Assert.assertFalse(result.isSucessfull());
    }

    @Test
    public void shouldNotDetectWhenActionSuccess() {
        UserLoginAttempt loginAttempt = new UserLoginAttemptSample().withAction(
                UserLoginAttempt.Action.SIGNIN_FAILURE).build();
        HackerDetectorResult result = null;
        for (int i = 0; i < 4; i++) {
            result = objectUnderTest.detect(loginAttempt);
            Assert.assertFalse(result.isSucessfull());
        }

        loginAttempt = new UserLoginAttemptSample().build();
        result = objectUnderTest.detect(loginAttempt);
        Assert.assertFalse(result.isSucessfull());
    }

    @Test
    public void shouldNotCountOlderThanPeriod() {
        UserLoginAttempt loginAttempt = new UserLoginAttemptSample()
                .withAction(UserLoginAttempt.Action.SIGNIN_FAILURE).withTimestamp(1336129471L)
                .build();
        HackerDetectorResult result = null;
        for (int i = 0; i < 4; i++) {
            result = objectUnderTest.detect(loginAttempt);
            Assert.assertFalse(result.isSucessfull());
        }
        loginAttempt = new UserLoginAttemptSample().withAction(
                UserLoginAttempt.Action.SIGNIN_FAILURE).build();
        for (int i = 0; i < 4; i++) {
            result = objectUnderTest.detect(loginAttempt);
            Assert.assertFalse(result.isSucessfull());
        }
        result = objectUnderTest.detect(loginAttempt);
        Assert.assertTrue(result.isSucessfull());
    }

    @Test
    public void shouldNotCountActionSuccess() {
        UserLoginAttempt loginAttempt = new UserLoginAttemptSample().build();
        HackerDetectorResult result = null;
        for (int i = 0; i < 4; i++) {
            result = objectUnderTest.detect(loginAttempt);
            Assert.assertFalse(result.isSucessfull());
        }
        loginAttempt = new UserLoginAttemptSample().withAction(
                UserLoginAttempt.Action.SIGNIN_FAILURE).build();
        for (int i = 0; i < 4; i++) {
            result = objectUnderTest.detect(loginAttempt);
            Assert.assertFalse(result.isSucessfull());
        }
        result = objectUnderTest.detect(loginAttempt);
        Assert.assertTrue(result.isSucessfull());
    }

    @Test
    public void shouldCleanupOlderThanPeriod() {
        UserLoginAttempt loginAttempt = new UserLoginAttemptSample()
                .withAction(UserLoginAttempt.Action.SIGNIN_FAILURE).withTimestamp(1336129471L)
                .build();

        objectUnderTest.detect(loginAttempt);

        loginAttempt = new UserLoginAttemptSample()
        .withAction(UserLoginAttempt.Action.SIGNIN_FAILURE)
        .build();

        objectUnderTest.detect(loginAttempt);
        Assert.assertEquals(2, objectUnderTest.userLoginAttempts.get(loginAttempt.getIpAddress()).size());

        objectUnderTest.cleanup();
        Assert.assertEquals(1, objectUnderTest.userLoginAttempts.get(loginAttempt.getIpAddress()).size());
    }

}
