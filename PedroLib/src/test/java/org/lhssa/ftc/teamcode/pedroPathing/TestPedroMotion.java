package org.lhssa.ftc.teamcode.pedroPathing;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.graphics.Bitmap;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.config.ConfigVar;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestPedroMotion {
    private static PedroMotion pedroMotion;

    @BeforeClass
    public static void createStuff() {
        Follower follower = mock(Follower.class);

        ForesightConfig foresightConfig = new ForesightConfig(config -> {
            config.naturalForwardDeceleration.set(24.494434793868912);
            config.naturalStrafeDeceleration.set(30.686920911960016);
            config.maxPathSpeed.set(.6);
        });//mock(ForesightConfig.class);

/*
        ConfigVar<Double> maxPathSpeedConfigVar = ConfigVar.of(1.0);//mock(ConfigVar.class);
        when(foresightConfig.maxPathSpeed).thenReturn(maxPathSpeedConfigVar);
        ConfigVar<Double> naturalForwardAcceleration = ConfigVar.of(25.0);
        when(foresightConfig.naturalForwardDeceleration).thenReturn(naturalForwardAcceleration);
        ConfigVar<Double> naturalStrafeDeceleration = ConfigVar.of(30.0);
        when(foresightConfig.naturalStrafeDeceleration).thenReturn(naturalStrafeDeceleration);
*/

        Foresight algorithm = new Foresight(foresightConfig);
        when(follower.algorithm()).thenReturn(algorithm);


        pedroMotion = new PedroMotion(follower);
    }

    @Test
    public void testcasePosesHaveSameXY() {
        Pose startPose = new Pose(10, 20, 0);
        Pose endPose = new Pose(10, 20, 0);

        PedroPathData path = new PedroPathData(HeadingInterpolationType.CONSTANT, startPose, endPose);
        assertTrue(pedroMotion.posesHaveSameXY(path));

        endPose = new Pose(20, 10, 1);
        path = new PedroPathData(HeadingInterpolationType.CONSTANT, startPose, endPose);
        assertFalse(pedroMotion.posesHaveSameXY(path));
    }

    @Test
    public void testcasePosesHaveSameHeading() {
        Pose startPose = new Pose(10, 20, 0);
        Pose endPose = new Pose(10, 20, 0);

        PedroPathData path = new PedroPathData(HeadingInterpolationType.CONSTANT, startPose, endPose);
        assertTrue(pedroMotion.posesHaveSameHeading(path));

        endPose = new Pose(20, 10, 1);
        path = new PedroPathData(HeadingInterpolationType.CONSTANT, startPose, endPose);
        assertFalse(pedroMotion.posesHaveSameHeading(path));
    }

    @Test
    public void testcasePathsEqual() {
        Pose startPose = new Pose(10, 20, 0);
        Pose endPose = new Pose(10, 20, 0);

        PedroPathData path1 = new PedroPathData(HeadingInterpolationType.CONSTANT, startPose, endPose);
        PedroPathData path2 = new PedroPathData(HeadingInterpolationType.CONSTANT, startPose, endPose);
        assertTrue(pedroMotion.pathsEqual(path1, path2));

        path2 = new PedroPathData(HeadingInterpolationType.CONSTANT, startPose,new Pose(10,20,1));
        assertFalse(pedroMotion.pathsEqual(path1, path2));

        assertFalse(pedroMotion.pathsEqual(path1, null));
    }
}