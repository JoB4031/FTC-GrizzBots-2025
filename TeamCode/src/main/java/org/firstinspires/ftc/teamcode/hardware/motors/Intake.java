package org.firstinspires.ftc.teamcode.hardware.motors;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {

    private DcMotor intake;
    private Servo leftIntake, rightIntake;

    public void initIntake(HardwareMap hw) {
        intake = hw.get(DcMotor.class, "intake");
        leftIntake = hw.get(Servo.class, "leftIntake");
        rightIntake = hw.get(Servo.class, "rightIntake");
    }

    public void setPower(double power) {
        intake.setPower(power);

        if (power <= 0) {
            leftIntake.setPosition(0.5);
            rightIntake.setPosition(0.5);
        } else {
            leftIntake.setPosition(0);
            rightIntake.setPosition(1);
        }
    }
    public boolean isPowered() {
        return (intake.getPower() > 0);
    }
}

