package org.firstinspires.ftc.teamcode.hardware.motors;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {

    private DcMotor externalIntake, internalIntake;

    public void initIntake(HardwareMap hw) {
        externalIntake = hw.get(DcMotor.class, "intake");
        externalIntake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        internalIntake = hw.get(DcMotor.class, "internalIntake");
        internalIntake.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void setPower(double exteriorPower, double interiorPower) {
        externalIntake.setPower(exteriorPower);
        internalIntake.setPower(interiorPower);
    }

    public void on() {
        externalIntake.setPower(1);
    }

    public void off() {
        setPower(0,0);
    }

    public boolean isPowered() {
        return (externalIntake.getPower() > 0);
    }
}

