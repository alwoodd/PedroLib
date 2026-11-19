package org.lhssa.ftc.teamcode.pedroActions;

/**
 * Interface that all concrete PedroActions must implement.
 */
public interface PedroAction {
    void update();
    boolean isComplete();
    String getDescription();

    /**
     * Called by PedroActionManager
     */
    void reset();
}
