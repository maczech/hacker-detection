package com.sky.detector;


public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        HackerDetector detector = new HackerDetectorFactory().create(3, 2);
        String logLine = "80.238.9.179,"+System.currentTimeMillis()+",SIGNIN_FAILURE,Dave.Branning";
        detector.parseLine(logLine);
        detector.parseLine(logLine);
        String result = detector.parseLine(logLine);

        try {
            Thread.sleep(2*(60*1000));
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        result.toString();

    }

}
