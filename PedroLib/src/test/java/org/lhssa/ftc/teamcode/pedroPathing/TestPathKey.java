package org.lhssa.ftc.teamcode.pedroPathing;



import static org.junit.jupiter.api.Assertions.*;

import com.pedropathing.math.Pose;

import org.junit.jupiter.api.Test;

public class TestPathKey {

    @Test
    public void testPathKeysEqual() {
        Pose pose1 = new Pose(10, 20, 30);
        Pose pose2 = new Pose(20, 30, 40);
        PathKey key1 = new PathKey(HeadingInterpolationType.TANGENT, pose1, pose2);
        PathKey key2 = new PathKey(HeadingInterpolationType.TANGENT, pose1, pose2);
        assertEquals(key1, key2);

        Pose pose3 = new Pose(30, 40, 50);
        key1 = new PathKey(HeadingInterpolationType.TANGENT, pose1, pose2, pose3);
        key2 = new PathKey(HeadingInterpolationType.TANGENT, pose1, pose2, pose3);
        assertEquals(key1, key2);
    }

    @Test
    public void testPathKeysNotEqual() {
        Pose pose1 = new Pose(10, 20, 30);
        Pose pose2 = new Pose(20, 30, 40);
        Pose pose3 = new Pose(30, 40, 50);
        PathKey key1 = new PathKey(HeadingInterpolationType.TANGENT, pose1, pose2, pose3);
        PathKey key2 = new PathKey(HeadingInterpolationType.TANGENT, pose2, pose1, pose3);
        assertNotEquals(key1, key2);

        key1 = new PathKey(HeadingInterpolationType.LINEAR, pose1, pose2, pose3);
        assertNotEquals(key1, key2);
    }
}
