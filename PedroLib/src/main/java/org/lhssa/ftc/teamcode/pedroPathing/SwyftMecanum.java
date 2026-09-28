package org.lhssa.ftc.teamcode.pedroPathing;

import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Extends Mecanum and adds explicit setting of each motor's RunMode.
 * Swyft motors that use RUN_USING_ENCODER will randomly brake when power goes to 0.
 * Therefore we want them to explicitly be RUN_WITHOUT_ENCODER. Unfortunately, the Mecanum
 * class doesn't expose or set RunMode one way or the other, so this class extends Mecanum
 * with that ability.
 * Since we're using Pinpoint odometry, we don't need, or want, RUN_USING_ENCODER anyway.
 */
public class SwyftMecanum extends Mecanum {
    /**
     * Constructor
     * @param hardwareMap      this is the HardwareMap object that contains the motors and other hardware
     * @param mecanumConfig    this is used by the super class's constructor.
     */
    public SwyftMecanum(HardwareMap hardwareMap, MecanumConfig mecanumConfig) {
        super(hardwareMap, mecanumConfig);

        DcMotorEx[] motors;

        motors = new DcMotorEx[]{
                (hardwareMap.get(DcMotorEx.class, config.frontLeftName.get())),
                (hardwareMap.get(DcMotorEx.class, config.frontRightName.get())),
                (hardwareMap.get(DcMotorEx.class, config.backLeftName.get())),
                (hardwareMap.get(DcMotorEx.class, config.backRightName.get()))
        };

        for (DcMotor motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }
}
