package com.sky.detector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HackerDetectorIntegrationTest {

    @Test
    public void shouldDetectHacker() {
        String logLine = "80.238.9.179,"+System.currentTimeMillis()+",SIGNIN_FAILURE,Dave.Branning";
        HackerDetector objectUnderTest = new HackerDetectorFactory().create(2, 1);
        String result = objectUnderTest.parseLine(logLine);
        assertNull(result);
        result = objectUnderTest.parseLine(logLine);
        assertEquals("80.238.9.179",result);
    }

}
