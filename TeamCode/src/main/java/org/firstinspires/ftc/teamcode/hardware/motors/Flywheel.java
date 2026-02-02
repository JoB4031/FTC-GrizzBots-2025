package org.firstinspires.ftc.teamcode.hardware.motors;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Configurable
public class Flywheel {

    public DcMotorEx leftFlywheel, rightFlywheel;
    public PIDFController controller;

    public static double kP = 0.006;
    public static double kI = 0.0;
    public static double kD = 0.0;
    public static double kF = 0.0004;

    public void initFlywheel(HardwareMap hw) {
        rightFlywheel = hw.get(DcMotorEx.class, "rightFlywheel");
        leftFlywheel  = hw.get(DcMotorEx.class, "leftFlywheel");

        rightFlywheel.setDirection(DcMotorEx.Direction.FORWARD);
        leftFlywheel.setDirection(DcMotorEx.Direction.REVERSE);

        rightFlywheel.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        leftFlywheel.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        controller = new PIDFController(kP,kI,kD,kF);
        controller.setSetPoint(0);
    }

    public void setFlywheelFireDistance(double distance) {
        controller.setSetPoint(RPMToVelocity(6.3 * Math.sqrt(398210 * distance)
                - 900 * Math.sqrt(distance)
                + (Math.ceil(distance / 5) * 400)));
        if (controller.getSetPoint() > 2460) controller.setSetPoint(2460);
    }
    public void off() {
        setFlywheelFireDistance(0);
    }

    public void update() {
        double current = rightFlywheel.getVelocity();
        double power = controller.calculate(current);
        if (controller.getSetPoint() <= 0) {
            leftFlywheel.setPower(0);
            rightFlywheel.setPower(0);
        } else {
            leftFlywheel.setPower(power);
            rightFlywheel.setPower(power);
        }
    }
    public double RPMToVelocity(double RPM) {
        return RPM*0.41;
    }
    public double getVelocity() {
        return rightFlywheel.getVelocity();
    }
    public double getRPM() {
        return rightFlywheel.getVelocity()*2.439;
    }
    public boolean isPowered() {
        return rightFlywheel.getPower() > 0;
    }
    public boolean isAtSpeed() {
       return (Math.abs(controller.getSetPoint() - rightFlywheel.getVelocity()) < 30);
    }
}

