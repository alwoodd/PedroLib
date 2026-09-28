package org.lhssa.ftc.teamcode.pedroPathing;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

/**
 * This class manages how the Follower is used.
 */
public class PedroMotion {
    private final Follower follower;
    private final ForesightConfig foresightConfig;

    private final double EPSILON = .001;
    private Path priorPath = null;

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
     * Decides if a regular follow() should be called, a heading-only "turnTo"
     * (although this is implemented using followPath() as well), or holdPoint(), depending on what
     * ways the path's poses differ from each other.
     * This method can be called repeatedly, without regard to the Follower's state.
     * @param path Path to follow
     */
    public void goPath(Path path) {
//        RobotLog.ii("PedroMotion", "goPath() called");
        if (follower.isBusy() || pathsEqual(path, priorPath)) {
            return;
        }

        priorPath = path;

        //Path poses have different (X,Y).
        if (!posesHaveSameXY(path)) {
//            RobotLog.ii("PedroMotion", "follow() called");
            follower.follow(path);
        }
        //Path poses have same (X,Y), but different headings.
        else if (!posesHaveSameHeading(path)) {
//            RobotLog.ii("PedroMotion", "poses have different headings");
            Pose endPose = path.endPose();
            follower.setHeading(endPose.heading());
            follower.hold(endPose);
/*
            Path newPath = bumpEndY(path);
            follower.follow(newPath);
*/

            //NOTE: I couldn't get turnTo() to stop oscillating.
            //follower.turnTo(path.getLastControlPoint().getHeading());
        }
        //Path poses have same (X,Y), and same headings.
        else {
//            RobotLog.ii("PedroMotion", "hold() called");
            follower.hold(path.endPose());
        }
    }

    /**
     * Decides if a regular followPath() should be called, a heading-only "turnTo"
     * (although this is implemented using followPath() as well), or holdPoint(), depending on what
     * ways the path's poses differ from each other.
     * This method can be called repeatedly, without regard to the Follower's state.
     * @param path Path to follow
     * @param power Power for this path
     */
    public void goPath(Path path, double power) {
        //Yes, this is a test of reference equality.
        if (follower.isBusy() || pathsEqual(path, priorPath)) {
            return;
        }

        path = path.with(foresightConfig.maxPathSpeed.at(power));
        priorPath = path;

        if (!posesHaveSameXY(path)) {
            follower.follow(path);
        }
        else if (!posesHaveSameHeading(path)) {
            Pose endPose = path.endPose();
            follower.setHeading(endPose.heading());
            follower.hold(endPose);

/*
            path = bumpEndY(path);
            follower.follow(path);
*/
        }
        else {
            follower.hold(path.endPose());
        }
    }

    /**
     * An alternative to testing !follower.isBusy().
     * @return true if the robot is not currently following a path.
     */
    public boolean isPathComplete() {
        return !follower.isBusy();
    }

    /**
     * Make endPose's Y just a little different so followPath will move the robot.
     * @param path Path to bump
     * @return Path with to-Pose's Y value bumped
     */
/*
    private Path bumpEndY(Path path) {
        Pose startPose = path.getFirstControlPoint();
        Pose endPose = path.getLastControlPoint();
        //Make endPose's Y just a little different so followPath will move the robot.
        endPose = new Pose(endPose.getX(), endPose.getY() + EPSILON, endPose.getHeading());
        Path newPath = new Path(new BezierLine(startPose, endPose));
        newPath.setConstantHeadingInterpolation(endPose.getHeading());

        return newPath;
    }
*/

    /**
     * Evaluate if the passed path's poses have the same (X,Y) values.
     * @param path Path whose Poses are to be evaluated
     * @return true if the passed path's poses have the same (X,Y) values
     */
    boolean posesHaveSameXY(Path path) {
        Pose startPose = path.get(0);
        Pose endPose = path.get(1);

        return (startPose.x() == endPose.x() &&
                startPose.y() == endPose.y());
    }

    /**
     * Evaluate if the passed path's headings have the same value.
     * @param path Path whose Poses are to be evaluated
     * @return true if the passed path's headings have the same value
     */
    boolean posesHaveSameHeading(Path path) {
        Pose startPose = path.get(0);
        Pose endPose = path.get(1);

        return (startPose.heading() == endPose.heading());
    }

    /**
     * Compares two paths to see if their poses have the same X, Y, and headings.
     * @param pathA A Path to be compared
     * @param pathB A Path to be compared
     * @return true if the paths' poses have the same X, Y, and headings.
     */
    boolean pathsEqual(Path pathA, Path pathB) {
        if (pathA == null || pathB == null) {
            return false;
        }

        Pose pathAstartPose = pathA.get(0);
        Pose pathAendPose = pathA.get(1);
        Pose pathBstartPose = pathB.get(0);
        Pose pathBendPose = pathB.get(1);

        return (pathAstartPose.x() == pathBstartPose.x() &&
            pathAstartPose.y() == pathBstartPose.y() &&
            pathAstartPose.heading() == pathBstartPose.heading() &&
            pathAendPose.x() == pathBendPose.x() &&
            pathAendPose.y() == pathBendPose.y() &&
            pathAendPose.heading() == pathBendPose.heading());
    }
}
