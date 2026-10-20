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
    private final AllianceColor definedPoseColor = AllianceColor.RED;
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
     * @param flipFunction A Function<Pose, Pose> called to correctly flip a Pose.
     */
    public PedroPather(AllianceColor canonicalColor, AllianceColor allianceColor, Function<Pose,Pose> flipFunction) {
        this.mustFlip = allianceColor != canonicalColor;
        this.cache = new HashMap<>();
        this.flipFunction = flipFunction;
    }

    /**
     * createPath using the passed startPose, endPose, and headingInterpolationType.
     * The headings come from the start and end Pose headings.
     * Paths are cached, so it is ok to call this method repeatedly.
     * @param startPose start Pose of Line
     * @param endPose end Pose of Line
     * @param headingInterpolationType LINEAR, TANGENT, CONSTANT
     * @return Path
     */
    public PedroPathData pathBetween(Pose startPose, Pose endPose, HeadingInterpolationType headingInterpolationType) {
        PathKey k = new PathKey(startPose, endPose, headingInterpolationType);
        if (cache.containsKey(k)) {
            return cache.get(k);
        }
        else {
            PedroPathData newPath = createPath(headingInterpolationType, startPose, endPose);
            //newPath = setHeadingInterpolation(startPose, endPose, newPath, headingInterpolationType);
            cache.put(k, newPath);
            return newPath;
        }
    }

    /**
     * createPath using the passed startPose and endPose.
     * The headings come from the start and end Pose headings,
     * and the heading interpolation is LINEAR.
     * Paths are cached, so it is ok to call this method repeatedly.
     * @param startPose start Pose of Line
     * @param endPose end Pose of Line
     * @return Path
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
     * Create a straight line Path using the passed start and end Poses.
     * The passed Poses are flipped as needed.
     * @param startPose start Pose of Line
     * @param endPose end Pose of Line
     * @return Path
     */
    private PedroPathData createPath(HeadingInterpolationType headingInterpolationType, Pose startPose, Pose endPose) {
        Pose workingStartPose;
        Pose workingEndPose;

        if (mustFlip) {
            workingStartPose = flipFunction.apply(startPose);
            workingEndPose = flipFunction.apply(endPose);
        }
        else {
            workingStartPose = startPose;
            workingEndPose = endPose;
        }

        return new PedroPathData(headingInterpolationType, workingStartPose, workingEndPose);
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
