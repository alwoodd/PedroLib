package org.lhssa.ftc.teamcode.pedroActions;

import java.util.function.Supplier;

/**
 * This PedroAction runs the provided Supplier<Boolean> during update(),
 * and sets isComplete() to the boolean returned by that provided function.
 */
public class PedroActionRunUntilComplete implements PedroAction {
    private final String description;
    private final Supplier<Boolean> supplierFunction;
    private boolean isStarted = false;
    private boolean isComplete = false;

    public PedroActionRunUntilComplete(String description, Supplier<Boolean> supplierFunction) {
        this.description = description;
        this.supplierFunction = supplierFunction;
    }

    @Override
    public void update() {
        isComplete = supplierFunction.get();
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
        isStarted = false;
        isComplete = false;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void start() {
        isStarted = true;
    }
}
