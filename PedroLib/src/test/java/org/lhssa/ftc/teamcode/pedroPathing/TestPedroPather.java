package org.lhssa.ftc.teamcode.pedroPathing;

import static org.junit.jupiter.api.Assertions.*;

import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.curves.Curve;
import com.pedropathing.paths.curves.Line;
import com.pedropathing.paths.curves.bezier.BezierCurve;
import com.pedropathing.utils.Angle;

import org.junit.jupiter.api.Test;

public class TestPedroPather {
    private PedroPather pedroPather;

    @Test
    public void testUsingFlipRoutineAlliancesOnSameSide() {
        pedroPather = new PedroPather(AllianceColor.BLUE, AllianceColor.RED, PedroPather::FlipRoutineAlliancesOnSameSide);
        Pose oldPose = new Pose(10, 20, Math.toRadians(45));
        Pose newPose = pedroPather.normalizePose(oldPose);

        assertEquals(newPose.x(), 144 -oldPose.x(), 0);
        assertEquals(newPose.y(), oldPose.y(), 0);
        assertEquals(newPose.heading(), Angle.normalize(Math.toRadians(135)), 0);
    }

    @Test
    public void testUsingFlipRoutineAlliancesOnOppositeSides() {
        pedroPather = new PedroPather(AllianceColor.BLUE, AllianceColor.RED, PedroPather::FlipRoutineAlliancesOnOppositeSides);
        Pose oldPose = new Pose(10, 20, Math.toRadians(45));
        Pose newPose = pedroPather.normalizePose(oldPose);

        assertEquals(newPose.x(), 144 -oldPose.x(), 0);
        assertEquals(newPose.y(), 144 -oldPose.y(), 0);
        assertEquals(newPose.heading(), Angle.normalize(Math.toRadians(-135)), 0);
    }

    @Test
    public void testPathBetweens() {
        PedroPathData pedroPathData;
        Path path;

        pedroPather = new PedroPather(AllianceColor.BLUE, AllianceColor.BLUE, PedroPather::FlipRoutineAlliancesOnOppositeSides);
        Pose pointA = new Pose(10, 20, Math.toRadians(45));
        Pose pointB = new Pose(20, 30, Math.toRadians(45));

        pedroPathData = pedroPather.pathBetween(HeadingInterpolationType.TANGENT, pointA, pointB);
        path = pedroPathData.toPath();

        //A Path has a Curve. The Curve can either be a Line or a BezierCurve.
        assertInstanceOf(Line.class, path.curve);

        Pose midPoint = new Pose(15, 25, 0);
        pedroPathData = pedroPather.pathBetween(HeadingInterpolationType.CONSTANT, pointA, midPoint, pointB);
        path = pedroPathData.toPath();
        assertInstanceOf(BezierCurve.class, path.curve);
    }
}
