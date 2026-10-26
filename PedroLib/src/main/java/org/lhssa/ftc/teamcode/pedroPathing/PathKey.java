package org.lhssa.ftc.teamcode.pedroPathing;


import com.pedropathing.math.Pose;

/**
 * Key object used by caching in TeamPaths.
 */
public class PathKey {
    private final Pose[] poses;
    private final HeadingInterpolationType headingInterpolationType;

    public PathKey(HeadingInterpolationType headingInterpolationType, Pose... poses) {
        this.headingInterpolationType = headingInterpolationType;
        this.poses = poses;
    }

    public Pose[] getPoses() {
        return poses;
    }

    public HeadingInterpolationType getHeadingInterpolationType() {
        return this.headingInterpolationType;
    }

    /**
     * @param o The other PathKey
     * @return true if the passed PathKey has the same start Pose, end Pose, and heading
     * interpolation values as this PathKey.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PathKey)) return false;
        PathKey other = (PathKey) o;

        boolean isEqual = true;
        if (getHeadingInterpolationType() != other.getHeadingInterpolationType()) {
            isEqual = false;
        }
        else {
            for (int i = 0; i < poses.length; i++) {
                if (!posesEqual(this.poses[i], other.poses[i]))
                {
                    isEqual = false;
                    break;
                }
            }
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        int poseHashSum = 0;

        for (Pose pose : poses) {
            poseHashSum += pose.hashCode();
        }

        return 31 * (poseHashSum + getHeadingInterpolationType().hashCode());
    }

    private boolean posesEqual(Pose pose1, Pose pose2) {
        return (pose1.x() == pose2.x() &&
                pose1.y() == pose2.y() &&
                pose1.heading() == pose2.heading());
    }
}
