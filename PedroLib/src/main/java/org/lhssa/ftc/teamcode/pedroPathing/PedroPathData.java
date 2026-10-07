package org.lhssa.ftc.teamcode.pedroPathing;

import com.pedropathing.api.Paths;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

/**
 * This class is a simple holder of Path Poses and the Path's heading interpolation.
 * It does not extend Path, and isn't constrained by any Path rules.
 */
public class PedroPathData {
    private final Pose[] poses;
    private final HeadingInterpolationType headingInterpolationType;

    /**
     * Construct using 0-to-many Poses. Note that passing <2 poses won't work right.
     * Poses are stored in the order they are provided.
     * @param headingInterpolationType HeadingInterpolationType
     * @param poses Provide at least 2 Poses
     */
    public PedroPathData(HeadingInterpolationType headingInterpolationType, Pose... poses) {
        this.poses = poses;
        this.headingInterpolationType = headingInterpolationType;
    }

    /**
     * Return the Pose at the passed index.
     * @param index index
     * @return Pose
     */
    public Pose getPose(int index) {
        return this.poses[index];
    }

    /**
     * @return the first Pose provided
     */
    public Pose getStartPose() {
        return getPose(0);
    }

    /**
     * @return the last Pose provided
     */
    public Pose getEndPose() {
        return getPose(poses.length - 1);
    }

    /**
     * Creates a genuine Path using this instance's path data.
     * @return Path
     */
    public Path toPath() {
        Path path;

        if (poses.length == 2) {
            path = Paths.line(getStartPose(), getEndPose());
        }
        else {
           path = Paths.curve(poses);
        }

        return setHeadingInterpolation(getStartPose(), getEndPose(), path, headingInterpolationType);
    }

    /**
     * Call appropriate heading interpolator function based on passed headingInterpolationType.
     * @param startPose startPose
     * @param endPose endPose
     * @param path Path we want to run interpolator function on.
     * @param headingInterpolationType HeadingInterpolationType
     * @return Path returned by the various interpolator functions.
     */
    private Path setHeadingInterpolation(Pose startPose, Pose endPose, Path path, HeadingInterpolationType headingInterpolationType) {
        Path returnPath = path; //Value of returnPath if no CASE applies.

        switch (headingInterpolationType) {
            case LINEAR:
                returnPath = path.linear(startPose, endPose);
                break;
            case TANGENT:
                returnPath = path.tangent();
                break;
            case CONSTANT:
                returnPath = path.constant(startPose);
                break;
        }

        return returnPath;
    }
}
