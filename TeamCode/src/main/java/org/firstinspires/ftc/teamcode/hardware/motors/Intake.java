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

    public void setPower(double exteriorPower, double interiorPower) {
        intake.setPower(exteriorPower);

        leftIntake.setPosition((-interiorPower/2)+0.5);
        rightIntake.setPosition((interiorPower/2)+0.5);
    }

    public void on() {
        setPower(1,1);
    }

    public void off() {
        setPower(0,0);
    }

    public boolean isPowered() {
        return (intake.getPower() > 0);
    }
}

