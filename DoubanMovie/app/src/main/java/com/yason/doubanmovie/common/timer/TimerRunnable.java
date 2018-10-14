package com.yason.doubanmovie.common.timer;

/**
 * Created by yason on 2018/8/2.
 */
class TimerRunnable implements Runnable {

    public static final int INFINITE = -1;

    private final int mTimerId;
    private final int mTotalTimes;
    private final ITimesReached mITimesReached;
    private final ITimeout mITimeout;

    private int mCurrentTimes;

    TimerRunnable(int timeId, int totalTimes, ITimeout iTimeout, ITimesReached iTimesReached) {
        mTimerId = timeId;
        mTotalTimes = totalTimes;
        mITimeout = iTimeout;
        mITimesReached = iTimesReached;
        mCurrentTimes = 0;
    }

    @Override
    public void run() {
        mITimeout.onTimeout(mTimerId);
        checkTimesReached();
    }

    private void checkTimesReached() {
        if (mTotalTimes == INFINITE) {
            return;
        }
        mCurrentTimes++;
        if (mCurrentTimes >= mTotalTimes) {
            mITimesReached.onTimesReached();
        }
    }
}
