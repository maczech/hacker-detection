package com.sky.detector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sky.detector.login.attempt.UserLoginAttempt;
import com.sky.detector.login.attempt.UserLoginAttemptFactory;
import com.sky.detector.policies.HackerDetectorPolicy;
import com.sky.detector.policies.HackerDetectorResult;
import com.sky.detector.sample.UserLoginAttemptSample;

public class HackerDetectorTest {
    @Mock
    private UserLoginAttemptFactory userLoginFactory;
    @Mock
    private List<HackerDetectorPolicy> policies;
    @Mock
    private HackerDetectorPolicy policy;
    @InjectMocks
    HackerDetector objectUnderTest = new HackerDetectorImpl();

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnNullWhenNotDetected() {
        String logLine = "80.238.9.179,133612947,SIGNIN_SUCCESS,Dave.Branning";
        UserLoginAttempt loginAttempt = new UserLoginAttemptSample().build();
        when(userLoginFactory.create(logLine)).thenReturn(loginAttempt);

        Iterator<HackerDetectorPolicy> mockIter = mock(Iterator.class);

        when(policies.iterator()).thenReturn(mockIter);
        when(mockIter.hasNext()).thenReturn(true).thenReturn(false);
        when(mockIter.next()).thenReturn(policy);
        HackerDetectorResult detectorResult = new HackerDetectorResult();
        detectorResult.setSucessfull(false);
        when(policy.detect(loginAttempt)).thenReturn(detectorResult);

        String result = objectUnderTest.parseLine(logLine);

        assertNull(result);
    }

    @Test
    public void shouldReturnIpAddressWhenDetectedWhenDetected() {
        String logLine = "80.238.9.179,133612947,SIGNIN_SUCCESS,Dave.Branning";
        UserLoginAttempt loginAttempt = new UserLoginAttemptSample().build();

        when(userLoginFactory.create(logLine)).thenReturn(loginAttempt);

        Iterator<HackerDetectorPolicy> mockIter = mock(Iterator.class);

        when(policies.iterator()).thenReturn(mockIter);
        when(mockIter.hasNext()).thenReturn(true).thenReturn(false);
        when(mockIter.next()).thenReturn(policy);
        HackerDetectorResult detectorResult = new HackerDetectorResult();
        detectorResult.setSucessfull(true);
        when(policy.detect(loginAttempt)).thenReturn(detectorResult);

        String result = objectUnderTest.parseLine(logLine);

        assertEquals("80.238.9.179",result);
    }

}
