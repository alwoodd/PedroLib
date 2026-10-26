package org.lhssa.ftc.teamcode.pedroActions;

import com.qualcomm.robotcore.util.RobotLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manages a List of PedroActions, and a way to iterate them.
 */
public class PedroActionManager {
    private final List<PedroAction> actionSteps;
    private Iterator<PedroAction> actionStep;
    private boolean ignoreRemainingActions = false;

    public PedroActionManager() {
        actionSteps = new ArrayList<>();
    }

    /**
     * Add a PedroAction to the list
     * @param action PedroAction
     */
    public void add(PedroAction action) {
        actionSteps.add(action);
    }

    /**
     * Returns the next PedroAction. If the underlying list is empty, PedroNoAction is returned.
     * @return PedroAction
     */
    public PedroAction next() {
        if (actionSteps.isEmpty()) {
            return new PedroNoAction();
        }
        else if (actionStep == null) {
            actionStep = actionSteps.iterator();
        }

        //Call reset() on the currentAction before returning it.
        PedroAction currentAction = actionStep.next();
        currentAction.reset();
        return currentAction;
    }

    /**
     * @return true if at least PedroAction remains to be returned using next().
     */
    public boolean hasNext() {
        if (actionStep == null || ignoreRemainingActions) {
            return false;
        }

        return actionStep.hasNext();
    }

    /**
     * Once called, all calls to hasNext() return false.
     */
    public void ignoreRemainingActions() {
        ignoreRemainingActions = true;
    }
}
