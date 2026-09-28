package org.lhssa.ftc.teamcode.pedroPathing;

import com.pedropathing.api.Paths;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.utils.Angle;

import java.util.HashMap;
import java.util.Map;

/**
 * This class creates and returns Paths created from passed Poses.
 */
public class PedroPather {
    private final AllianceColor definedPoseColor = AllianceColor.RED;
    private final boolean mustFlip;

    private final Map<PathKey, Path> cache;

    /**
     * Constructor
     * @param canonicalColor The default AllianceColor
     * @param allianceColor  The current AllianceColor
     */
    public PedroPather(AllianceColor canonicalColor, AllianceColor allianceColor) {
        this.mustFlip = allianceColor != canonicalColor;
        this.cache = new HashMap<>();
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
    public Path pathBetween(Pose startPose, Pose endPose, HeadingInterpolationType headingInterpolationType) {
        PathKey k = new PathKey(startPose, endPose, headingInterpolationType);
        if (cache.containsKey(k)) {
            return cache.get(k);
        }
        else {
            Path newPath = createPath(startPose, endPose);
            newPath = setHeadingInterpolation(startPose, endPose, newPath, headingInterpolationType);
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
    public Path pathBetween(Pose startPose, Pose endPose) {
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
            returnedPose = flipPose(pose);
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
    private Path createPath(Pose startPose, Pose endPose) {
        Pose workingStartPose;
        Pose workingEndPose;

        if (mustFlip) {
            workingStartPose = flipPose(startPose);
            workingEndPose = flipPose(endPose);
        }
        else {
            workingStartPose = startPose;
            workingEndPose = endPose;
        }

        return Paths.line(workingStartPose, workingEndPose);
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

    private Pose flipPose(Pose oldPose) {
        return new Pose(
            144 - oldPose.x(),
            oldPose.y(),
                Angle.normalize(Math.PI - oldPose.heading()));
    }
}
