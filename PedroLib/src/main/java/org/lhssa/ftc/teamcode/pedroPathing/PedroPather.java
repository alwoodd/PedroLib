package org.lhssa.ftc.teamcode.pedroPathing;

import com.pedropathing.math.Pose;
import com.pedropathing.utils.Angle;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * This class creates and returns Paths created from passed Poses.
 */
public class PedroPather {
    private final boolean mustFlip;
    private final Function<Pose, Pose> flipFunction;

    private final Map<PathKey, PedroPathData> cache;

    /**
     * Constructor
     * The passed flipFunction can be the static
     * Pose FlipRoutineAlliancesOnSameSide(Pose) or FlipRoutineAlliancesOnOppositeSides(Pose),
     * or any other implementation that takes and returns a Pose.
     * @param canonicalColor The default AllianceColor
     * @param allianceColor  The current AllianceColor
     * @param flipFunction A {@code Function<Pose, Pose>} called to correctly flip a Pose.
     */
    public PedroPather(AllianceColor canonicalColor, AllianceColor allianceColor, Function<Pose,Pose> flipFunction) {
        this.mustFlip = allianceColor != canonicalColor;
        this.cache = new HashMap<>();
        this.flipFunction = flipFunction;
    }

    /**
     * createPath using the passed poses, and headingInterpolationType.
     * Paths are cached, so it is ok to call this method repeatedly.
     * @param poses Array of Poses
     * @param headingInterpolationType LINEAR, TANGENT, CONSTANT
     * @return PedroPathData
     */
    public PedroPathData pathBetween(HeadingInterpolationType headingInterpolationType, Pose... poses) {
        PathKey k = new PathKey(headingInterpolationType, poses);
        if (cache.containsKey(k)) {
            return cache.get(k);
        }
        else {
            PedroPathData newPath = createPath(headingInterpolationType, poses);
            cache.put(k, newPath);
            return newPath;
        }
    }

    /**
     * Create PedroPathData using the passed startPose, endPose, and headingInterpolationType.
     * Paths are cached, so it is ok to call this method repeatedly.
     * @param startPose start Pose of Line
     * @param endPose end Pose of Line
     * @param headingInterpolationType LINEAR, TANGENT, CONSTANT
     * @return PedroPathData
     */
    public PedroPathData pathBetween(Pose startPose, Pose endPose, HeadingInterpolationType headingInterpolationType) {
        return pathBetween(headingInterpolationType, startPose, endPose);
    }

    /**
     * Create PedroPathData using the passed startPose and endPose.
     * The heading interpolation is LINEAR.
     * Paths are cached, so it is ok to call this method repeatedly.
     * @param startPose start Pose of Line
     * @param endPose end Pose of Line
     * @return PedroPathData
     */
    public PedroPathData pathBetween(Pose startPose, Pose endPose) {
        return this.pathBetween(startPose, endPose, HeadingInterpolationType.LINEAR);
    }

    /**
     * Ensures that the passed pose is correct for the instantiated AllianceColor.
     * @param pose Pose to normalize
     * @return Pose
     */
    public Pose normalizePose(Pose pose) {
        Pose returnedPose = pose;

        if (mustFlip) {
            returnedPose = flipFunction.apply(pose);
        }

        return returnedPose;
    }

    /**
     * Create a PedroPathData.
     * The passed Poses are flipped as needed.
     * @param headingInterpolationType HeadingInterpolationType
     * @param poses two or more Poses
     * @return PedroPathData
     */
    private PedroPathData createPath(HeadingInterpolationType headingInterpolationType, Pose[] poses) {
        Pose[] workingPoses = new Pose[poses.length];

        for(int i = 0; i < poses.length; i++) {
            if (mustFlip) {
                workingPoses[i] = flipFunction.apply(poses[i]);
            }
            else {
                workingPoses[i] = poses[i];
            }
        }

        return new PedroPathData(headingInterpolationType, workingPoses);
    }

    /**
     * Pose flipper that can be used when RED and BLUE alliances are on the same
     * side of the field; i.e., the X coordinate is flipped, Y stays the same,
     * and the heading is subtracted from PI.
     * @param oldPose Pose to be flipped
     * @return new Pose
     */
    public static Pose FlipRoutineAlliancesOnSameSide(Pose oldPose) {
        return new Pose(
                144 - oldPose.x(),
                oldPose.y(),
                Angle.normalize(Math.PI - oldPose.heading()));
    }

    /**
     * Pose flipper that can be used when RED and BLUE alliances are on opposite
     * sides of the field; i.e., both the X and Y coordinates are flipped,
     * and the heading is added to PI.
     * @param oldPose Pose to be flipped
     * @return new Pose
     */public static Pose FlipRoutineAlliancesOnOppositeSides(Pose oldPose) {
        return new Pose(
                144 - oldPose.x(),
                144 - oldPose.y(),
                Angle.normalize((Math.PI + oldPose.heading())));
    }
}
