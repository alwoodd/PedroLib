//package org.firstinspires.ftc.teamcode.test;
package org.lhssa.ftc.teamcode.pedroPathing;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestPedroMotion {
    private static PedroMotion pedroMotion;

    @BeforeClass
    public static void createStuff() {
        Follower follower = mock(Follower.class);
        pedroMotion = new PedroMotion(follower);
    }

    @Test
    public void testcasePosesHaveSameXY() {
        Pose startPose = new Pose(10, 20, 0);
        Pose endPose = new Pose(10, 20, 0);

        Path path = new Path(new BezierLine(startPose, endPose));
        assertTrue(pedroMotion.posesHaveSameXY(path));

        endPose = new Pose(20, 10, 1);
        path = new Path(new BezierLine(startPose, endPose));
        assertFalse(pedroMotion.posesHaveSameXY(path));
    }

    @Test
    public void testcasePosesHaveSameHeading() {
        Pose startPose = new Pose(10, 20, 0);
        Pose endPose = new Pose(10, 20, 0);

        Path path = new Path(new BezierLine(startPose, endPose));
        assertTrue(pedroMotion.posesHaveSameHeading(path));

        endPose = new Pose(20, 10, 1);
        path = new Path(new BezierLine(startPose, endPose));
        assertFalse(pedroMotion.posesHaveSameHeading(path));
    }

    @Test
    public void testcasePathsEqual() {
        Pose startPose = new Pose(10, 20, 0);
        Pose endPose = new Pose(10, 20, 0);

        Path path1 = new Path(new BezierLine(startPose, endPose));
        Path path2 = new Path(new BezierLine(startPose, endPose));
        assertTrue(pedroMotion.pathsEqual(path1, path2));

        path2 = new Path(new BezierLine(startPose, new Pose(10,20,1)));
        assertFalse(pedroMotion.pathsEqual(path1, path2));

        assertFalse(pedroMotion.pathsEqual(path1, null));
    }
}