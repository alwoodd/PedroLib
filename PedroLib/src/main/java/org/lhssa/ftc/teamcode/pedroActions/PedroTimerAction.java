package org.lhssa.ftc.teamcode.pedroActions;

import com.pedropathing.utils.Timer;

/**
 * This PedroAction runs a Timer, and sets isComplete to true when the passed
 * secondsUntilComplete has elapsed.
 */
public class PedroTimerAction implements PedroAction {
    private final String description;
    private final Timer timer;
    private final double secondsUntilComplete;
    private boolean isComplete = false;

    public PedroTimerAction(String description, double secondsUntilComplete) {
        this.description = description;
        this.timer = new Timer();
        this.secondsUntilComplete = secondsUntilComplete;
    }

    @Override
    public void update() {
        isComplete = timer.seconds() >= secondsUntilComplete;
    }

    @Override
    public boolean isComplete() {
        return isComplete;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void reset() {
        timer.reset();
    }
}
