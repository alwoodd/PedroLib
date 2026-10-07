package org.lhssa.ftc.teamcode.pedroPathing;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.utils.Angle;

/**
 * This class manages how the Follower is used.
 */
public class PedroMotion {
    private final Follower follower;
    private final ForesightConfig foresightConfig;

    private final double EPSILON_RADIANS = Math.toRadians(2);
    private PedroPathData priorPath = null;
    private double targetHeading;
    private boolean isFollowing = false;

    public PedroMotion(Follower follower) {
        this.follower = follower;
        /*
         * Get a reference to the ForesightConfig.
         * The follower's algorithm refers to the Foresight it was constructed with.
         * The Foresite's config is the ForesightConfig that *it* was constructed with.
         * Are we loving Pedro 3.0's architecture yet?
         */
        Foresight foresight = (Foresight)follower.algorithm();
        this.foresightConfig = foresight.config;
    }

    /**
     * Decides if follow() or hold() should be called
     * This method can be called repeatedly, without regard to the Follower's state.
     * @param path Path to follow
     */
    public void goPath(PedroPathData path) {
        if (follower.isBusy() || pathsEqual(path, priorPath)) {
            return;
        }

        priorPath = path;

        /*
         * We follow the path if either the path's pose's X or Y differ.
         * Otherwise, we just hold the path's end pose. Holding will turn
         * the robot if its current heading is different.
         */
        if (!posesHaveSameXY(path)) {
            follower.follow(path.toPath());
            isFollowing = true;
        }
        else {
            Pose endPose = path.getEndPose();
            follower.hold(endPose);
            isFollowing = false;
            targetHeading = endPose.heading();
        }
    }

    /**
     * Decides if follow() or hold() should be called
     * This method can be called repeatedly, without regard to the Follower's state.
     * @param path Path to follow
     * @param power Power for this path
     */
    public void goPath(PedroPathData path, double power) {
        if (follower.isBusy() || pathsEqual(path, priorPath)) {
            return;
        }

        priorPath = path;

        /*
         * We follow the path if either the path's pose's X or Y differ.
         * Otherwise, we just hold the path's end pose. Holding will turn
         * the robot if its current heading is different.
         */
        if (!posesHaveSameXY(path)) {
            follower.follow(path.toPath().with(foresightConfig.maxPathSpeed.at(power)));
            isFollowing = true;
        }
        else {
            Pose endPose = path.getEndPose();
            follower.hold(endPose);
            isFollowing = false;
            targetHeading = endPose.heading();
        }
    }

    /**
     * @return true if the robot is finished moving.
     */
    public boolean isPathComplete() {
        boolean pathComplete;

        if (isFollowing) {
            pathComplete = !follower.isBusy();
        }
        else {
            pathComplete = isAtTargetHeading();
        }

        return pathComplete;
    }

    /**
     * @return true if the robot's heading is withing EPSILON_RADIAN of its target.
     */
    boolean isAtTargetHeading() {
        double delta = Angle.normalize(follower.pose().heading() - targetHeading);

        return (Math.abs(delta) < Math.toRadians(EPSILON_RADIANS));
    }

    /**
     * Evaluate if the passed path's poses have the same (X,Y) values.
     * @param path Path whose Poses are to be evaluated
     * @return true if the passed path's poses have the same (X,Y) values
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean posesHaveSameXY(PedroPathData path) {
        Pose startPose = path.getStartPose();
        Pose endPose = path.getEndPose();

        return (startPose.x() == endPose.x() &&
                startPose.y() == endPose.y());
    }

    /**
     * Evaluate if the passed path's headings have the same value.
     * @param path Path whose Poses are to be evaluated
     * @return true if the passed path's headings have the same value
     */
    boolean posesHaveSameHeading(PedroPathData path) {
        Pose startPose = path.getStartPose();
        Pose endPose = path.getEndPose();

        return (startPose.heading() == endPose.heading());
    }

    /**
     * Compares two paths to see if their poses have the same X, Y, and headings.
     * @param pathA A Path to be compared
     * @param pathB A Path to be compared
     * @return true if the paths' poses have the same X, Y, and headings.
     */
    boolean pathsEqual(PedroPathData pathA, PedroPathData pathB) {
        if (pathA == null || pathB == null) {
            return false;
        }

        Pose pathAstartPose = pathA.getStartPose();
        Pose pathAendPose = pathA.getEndPose();
        Pose pathBstartPose = pathB.getStartPose();
        Pose pathBendPose = pathB.getEndPose();

        return (pathAstartPose.x() == pathBstartPose.x() &&
            pathAstartPose.y() == pathBstartPose.y() &&
            pathAstartPose.heading() == pathBstartPose.heading() &&
            pathAendPose.x() == pathBendPose.x() &&
            pathAendPose.y() == pathBendPose.y() &&
            pathAendPose.heading() == pathBendPose.heading());
    }
}
