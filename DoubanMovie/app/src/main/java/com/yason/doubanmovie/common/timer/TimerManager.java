package com.yason.doubanmovie.common.timer;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by yason on 2018/8/2.
 */
public class TimerManager {
    private static final int CORE_POOL_SIZE = 3;

    private ScheduledExecutorService mScheduledExecutorService;
    private HashMap<Integer, Future<Void>> mTasks;

    public static TimerManager getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 定时任务
     */
    public boolean schedule(final int timerId, int delay, final ITimeout iTimeout) {
        if (iTimeout == null) {
            throw new IllegalArgumentException();
        }

        checkServiceNotNull();
        if (mTasks.containsKey(timerId)) {
            return false;
        }
        Future<Void> task = (Future<Void>) mScheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                iTimeout.onTimeout(timerId);
                mTasks.remove(timerId);
            }
        }, delay, TimeUnit.MILLISECONDS);
        mTasks.put(timerId, task);

        return true;
    }

    private void checkServiceNotNull() {
        if (mScheduledExecutorService != null) {
            return;
        }

        /*
            corePoolSize = 3
            maximumPoolSize = Integer.MAX_VALUE
            keepAliveTime = 10 MILLISECONDS
            workQueue = new DelayedWorkQueue()
            defaultHandler = new AbortPolicy()
        */
        mScheduledExecutorService = Executors.newScheduledThreadPool(CORE_POOL_SIZE);
        mTasks = new HashMap<>();
    }

    public boolean scheduleWithFixedDelay(int timerId, int delay, int period, ITimeout iTimeout) {
        if (iTimeout == null) {
            throw new IllegalArgumentException();
        }
        return scheduleAtFixedRateInternal(timerId, delay, period, TimerRunnable.INFINITE, iTimeout);
    }

    public boolean scheduleAtFixedRate(int timerId, int delay, int period, ITimeout iTimeout) {
        if (iTimeout == null) {
            throw new IllegalArgumentException();
        }
        return scheduleAtFixedRateInternal(timerId, delay, period, TimerRunnable.INFINITE, iTimeout);
    }

    public boolean scheduleWithFixedDelay(int timerId, int delay, int period, int totalTimes, ITimeout iTimeout) {
        if (totalTimes <= 0 || iTimeout == null) {
            throw new IllegalArgumentException();
        }
        return scheduleWithFixedDelayInternal(timerId, delay, period, totalTimes, iTimeout);
    }

    public boolean scheduleAtFixedRate(int timerId, int delay, int period, int totalTimes, ITimeout iTimeout) {
        if (totalTimes <= 0 ||iTimeout == null){
            throw new IllegalArgumentException();
        }
        return scheduleAtFixedRateInternal(timerId, delay, period, totalTimes, iTimeout);
    }

    /**
     * 循环任务,以固定延时执行任务
     * 即上一次执行终止和下一次执行开始间隔 period
     */
    private boolean scheduleWithFixedDelayInternal(final int timerId, int delay, int period, int totalTimes, ITimeout iTimeout) {
        checkServiceNotNull();
        if (mTasks.containsKey(timerId)) {
            return false;
        }

        Runnable r = new TimerRunnable(timerId, totalTimes, iTimeout, new ITimesReached() {
            @Override
            public void onTimesReached() {
                cancelTimer(timerId, false);
            }
        });
        Future<Void> task = (Future<Void>) mScheduledExecutorService.scheduleWithFixedDelay(r, delay, period, TimeUnit.MILLISECONDS);
        mTasks.put(timerId, task);

        return true;
    }

    /**
     * 循环任务，以固定频率执行的任务
     * 即上一次执行开始和下一次执行开始间隔 period
     * 如果此任务的执行时间超过其周期，则后续执行可能会延迟，但不会同时执行
     */
    private boolean scheduleAtFixedRateInternal(final int timerId, int delay, int period, int totalTimes, ITimeout iTimeout) {
        checkServiceNotNull();
        if (mTasks.containsKey(timerId)) {
            return false;
        }

        Runnable r = new TimerRunnable(timerId, totalTimes, iTimeout, new ITimesReached() {
            @Override
            public void onTimesReached() {
                cancelTimer(timerId, false);
            }
        });
        Future<Void> task = (Future<Void>) mScheduledExecutorService.scheduleAtFixedRate(r, delay, period, TimeUnit.MILLISECONDS);
        mTasks.put(timerId, task);

        return true;
    }

    public boolean isShutdown() {
        return mScheduledExecutorService == null || mScheduledExecutorService.isShutdown();
    }

    public boolean cancelTimer(int timerId, boolean mayInterruptIfRunning) {
        Future task = mTasks.get(timerId);
        if (task == null || task.isCancelled()) {
            return false;
        }
        boolean isCancel = task.cancel(mayInterruptIfRunning);
        mTasks.remove(timerId);

        return isCancel;
    }

    public void shutdownNow() {
        if (mScheduledExecutorService == null) {
            return;
        }
        mScheduledExecutorService.shutdownNow();
        mScheduledExecutorService = null;

        mTasks.clear();
        mTasks = null;
    }

    private static final class Holder {
        private static final TimerManager INSTANCE = new TimerManager();

    }
}
