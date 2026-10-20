package org.lhssa.ftc.teamcode.pedroPathing;

import com.pedropathing.math.Pose;
import com.pedropathing.utils.Angle;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

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
}
