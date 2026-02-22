package org.firstinspires.ftc.teamcode.hardware.motors;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

@Configurable
public class Flywheel {

    public DcMotorEx leftFlywheel, rightFlywheel;
    public PIDFController controller;

    public static double kP = 0.006;
    public static double kI = 0.0;
    public static double kD = 0.0;
    public static double kF = 0.0004;
    public static double oneMeterPower = 3500;
    public static double onePointTwoMeterPower = 3750;
    public static double onePointFourMeterPower = 4000;
    public static double onePointSixMeterPower = 4250;
    public static double onePointEightMeterPower = 4500;
    public static double twoMeterPower = 4750;
    public static double twoPointTwoMeterPower = 3500;
    public static double twoPointFourMeterPower = 3750;
    public static double twoPointSixMeterPower = 4000;
    public static double twoPointEightMeterPower = 4250;
    public static double threeMeterPower = 4500;

    public final ElapsedTime flywheelStable = new ElapsedTime();

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

    public void setFlywheelNearFire(double distance) {
        controller.setSetPoint(RPMToVelocity(6.1 * Math.sqrt(398210 * distance)
                - 900 * Math.sqrt(distance) + (-667*distance)
                + (Math.ceil(distance / 5) * (1433))));
        if (controller.getSetPoint() > 2460) controller.setSetPoint(2460);
    }
    public void off() {
        setFlywheelNearFire(0);
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
    public void setToRPM(double rpm) {
        controller.setSetPoint(RPMToVelocity(rpm));
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
        if (Math.abs(controller.getSetPoint() - rightFlywheel.getVelocity()) < 60) {
            if (flywheelStable.seconds() > 0.25) {
                return true;
            }
        } else {
            flywheelStable.reset();
        }
        return false;
    }
}

