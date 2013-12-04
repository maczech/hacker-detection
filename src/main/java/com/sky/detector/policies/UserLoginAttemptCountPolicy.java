package com.sky.detector.policies;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.sky.detector.login.attempt.UserLoginAttempt;

public class UserLoginAttemptCountPolicy implements HackerDetectorPolicy {

    private static final String MAXIMUM_NUMBER_OF_LOGIN_ATTEMPS_REACHED_MESSAGE = "Reached maximum number of login attempts during {0} minutes.";
    protected final Map<String, List<UserLoginAttempt>> userLoginAttempts = new ConcurrentHashMap<String, List<UserLoginAttempt>>();
    protected Integer maximumNumberOfLoginAttempts;
    protected Integer periodOfTimeInMinutes;
    protected Timer timer;
    protected Integer cleanupTimeInMiliseconds;


    @Override
    public synchronized HackerDetectorResult detect(UserLoginAttempt userLogin) {
        HackerDetectorResult result = new HackerDetectorResult();
        if (userLogin.getAction().equals(UserLoginAttempt.Action.SIGNIN_SUCCESS)) {
            result.setSucessfull(false);
            return result;
        }

        if (!userLoginAttempts.containsKey(userLogin.getIpAddress())) {
            List<UserLoginAttempt> attemptsList = new ArrayList<UserLoginAttempt>();
            userLoginAttempts.put(userLogin.getIpAddress(), attemptsList);
        }

        List<UserLoginAttempt> attemptsList = userLoginAttempts.get(userLogin.getIpAddress());
        if (attemptsList.isEmpty() || attemptsList.size() < maximumNumberOfLoginAttempts - 1) {
            attemptsList.add(userLogin);
            result.setSucessfull(false);
            return result;
        }

        Long periodToCheck = getPeriodToCheck();

        UserLoginAttempt lastAttempt = attemptsList.get(attemptsList.size() - 1);
        if (lastAttempt.getTimestamp().longValue() < periodToCheck.longValue()) {
            attemptsList.add(userLogin);
            result.setSucessfull(false);
            return result;
        }

        int attemptsCount = 0;
        attemptsList.add(userLogin);
        for (UserLoginAttempt attempt : attemptsList) {
            if (attempt.getTimestamp() >= periodToCheck) {
                attemptsCount++;
            }
            if (attemptsCount >= maximumNumberOfLoginAttempts) {
                result.setSucessfull(true);
                result.setMessage(MessageFormat.format(
                        MAXIMUM_NUMBER_OF_LOGIN_ATTEMPS_REACHED_MESSAGE, periodOfTimeInMinutes));
                return result;
            }
        }
        result.setSucessfull(false);
        return result;
    }

    public void initCleanup() {
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
               cleanup();

            }
        };
        timer.scheduleAtFixedRate(task, cleanupTimeInMiliseconds, cleanupTimeInMiliseconds);

    }

    protected synchronized void cleanup() {
        Long periodToCheck = getPeriodToCheck();
        for (Map.Entry<String, List<UserLoginAttempt>> entry : userLoginAttempts.entrySet()) {
            for (Iterator<UserLoginAttempt> iter = entry.getValue().iterator();iter.hasNext();) {
                UserLoginAttempt attempt = iter.next();
                if (attempt.getTimestamp() < periodToCheck) {
                    iter.remove();
                }
            }
        }

    }

    private Long getPeriodToCheck() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -periodOfTimeInMinutes);
        Long periodToCheck = calendar.getTimeInMillis();
        return periodToCheck;
    }

    public Integer getMaximumNumberOfLoginAttempts() {
        return maximumNumberOfLoginAttempts;
    }

    public void setMaximumNumberOfLoginAttempts(Integer maximumNumberOfLoginAttempts) {
        this.maximumNumberOfLoginAttempts = maximumNumberOfLoginAttempts;
    }

    public Integer getPeriodOfTimeInMinutes() {
        return periodOfTimeInMinutes;
    }

    public void setPeriodOfTimeInMinutes(Integer periodOfTimeInMinutes) {
        this.periodOfTimeInMinutes = periodOfTimeInMinutes;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public Integer getCleanupTimeInMiliseconds() {
        return cleanupTimeInMiliseconds;
    }

    public void setCleanupTimeInMiliseconds(Integer cleanupTimeInMiliseconds) {
        this.cleanupTimeInMiliseconds = cleanupTimeInMiliseconds;
    }





}
