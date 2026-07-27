package org.lhssa.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;

/**
 * This class manages how the Follower is used.
 */
public class PedroMotion {
    private final Follower follower;
    private final double EPSILON = .001;
    private Path priorPath = null;

    public PedroMotion(Follower follower) {
        this.follower = follower;
    }

    /**
     * Decides if a regular followPath() should be called, a heading-only "turnTo"
     * (although this is implemented using followPath() as well), or holdPoint(), depending in what
     * ways the path's poses differ from each other.
     * This method can be called repeatedly, without regard to the Follower's state.
     * @param path Path to follow
     */
    public void goPath(Path path) {
        if (follower.isBusy() || pathsEqual(path, priorPath)) {
            return;
        }

        priorPath = path;

        //Path poses have different (X,Y).
        if (!posesHaveSameXY(path)) {
            follower.followPath(path);
        }
        //Path poses have same (X,Y), but different headings.
        else if (!posesHaveSameHeading(path)) {
            Path newPath = bumpEndY(path);
            follower.followPath(newPath);

            //NOTE: I couldn't get turnTo() to stop oscillating.
            //follower.turnTo(path.getLastControlPoint().getHeading());
        }
        //Path poses have same (X,Y), and same headings.
        else {
            follower.holdPoint(path.getLastControlPoint());
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

        priorPath = path;

        if (!posesHaveSameXY(path)) {
            follower.followPath(new PathChain(path), power, true);
        }
        else if (!posesHaveSameHeading(path)) {
            Path newPath = bumpEndY(path);
            follower.followPath(new PathChain(newPath), power, true);
        }
        else {
            follower.holdPoint(path.getLastControlPoint());
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
    private Path bumpEndY(Path path) {
        Pose startPose = path.getFirstControlPoint();
        Pose endPose = path.getLastControlPoint();
        //Make endPose's Y just a little different so followPath will move the robot.
        endPose = new Pose(endPose.getX(), endPose.getY() + EPSILON, endPose.getHeading());
        Path newPath = new Path(new BezierLine(startPose, endPose));
        newPath.setConstantHeadingInterpolation(endPose.getHeading());

        return newPath;
    }

    /**
     * Evaluate if the passed path's poses have the same (X,Y) values.
     * @param path Path whose Poses are to be evaluated
     * @return true if the passed path's poses have the same (X,Y) values
     */
    boolean posesHaveSameXY(Path path) {
        Pose startPose = path.getFirstControlPoint();
        Pose endPose = path.getLastControlPoint();

        return (startPose.getX() == endPose.getX() &&
                startPose.getY() == endPose.getY());
    }

    /**
     * Evaluate if the passed path's headings have the same value.
     * @param path Path whose Poses are to be evaluated
     * @return true if the passed path's headings have the same value
     */
    boolean posesHaveSameHeading(Path path) {
        Pose startPose = path.getFirstControlPoint();
        Pose endPose = path.getLastControlPoint();

        return (startPose.getHeading() == endPose.getHeading());
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

        Pose pathAstartPose = pathA.getFirstControlPoint();
        Pose pathAendPose = pathA.getLastControlPoint();
        Pose pathBstartPose = pathB.getFirstControlPoint();
        Pose pathBendPose = pathB.getLastControlPoint();

        return (pathAstartPose.getX() == pathBstartPose.getX() &&
            pathAstartPose.getY() == pathBstartPose.getY() &&
            pathAstartPose.getHeading() == pathBstartPose.getHeading() &&
            pathAendPose.getX() == pathBendPose.getX() &&
            pathAendPose.getY() == pathBendPose.getY() &&
            pathAendPose.getHeading() == pathBendPose.getHeading());
    }
}
