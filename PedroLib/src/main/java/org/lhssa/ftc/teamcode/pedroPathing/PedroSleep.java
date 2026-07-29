package org.lhssa.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;

/**
 * Make the current thread cyclically sleep then call follower.update().
 * The sleep() durations are controlled using the updateFrequency.
 * Using the default updateFrequency of 250 ms, and a sleep of 3000 ms,
 * follower.update() will be called after sleeping 250 ms. This cycle continues
 * until 3000 total ms have elapsed.
 *
 */
public class PedroSleep {
    private final Follower follower;
    private long updateFrequency = 250;

    /**
     * Construct using the default updateFrequency of 250 ms.
     * @param follower Instance of Follower
     */
    public PedroSleep(Follower follower) {
        this.follower = follower;
    }

    /***
     * Construct using a passed updateFrequency value.
     * @param follower Instance of Follower
     * @param updateFrequency Number of ms between calls to follower.update()
     */
    public PedroSleep(Follower follower, long updateFrequency) {
        this.follower = follower;
        this.updateFrequency = updateFrequency;
    }

    /**
     * Total length of time to sleep
     * @param milliseconds Total ms to sleep before returning control back to caller.
     */
    public void sleep(long milliseconds) {
        for (long m = 0; m < milliseconds; m += updateFrequency) {
            sleepImpl(updateFrequency);
            follower.update();
        }
    }

    private void sleepImpl(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
