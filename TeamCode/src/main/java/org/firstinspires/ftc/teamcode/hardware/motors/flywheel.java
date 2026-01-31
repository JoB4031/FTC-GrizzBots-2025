package org.firstinspires.ftc.teamcode.hardware.motors;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class flywheel {

    private DcMotorEx leftFlywheel, rightFlywheel;
    private PIDFController controller;

    public static double kP = 0.0003;
    public static double kI = 0.0;
    public static double kD = 0.0;
    public static double kF = 0.00025;

    public void initFlywheel(HardwareMap hw) {
        rightFlywheel = hw.get(DcMotorEx.class, "rightFlywheel");
        leftFlywheel  = hw.get(DcMotorEx.class, "leftFlywheel");

        rightFlywheel.setDirection(DcMotorEx.Direction.FORWARD);
        leftFlywheel.setDirection(DcMotorEx.Direction.REVERSE);

        rightFlywheel.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        leftFlywheel.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

        controller = new PIDFController(kP, kI, kD, kF);
        controller.setSetPoint(0);
    }

    public void setFlywheelFireDistance(double distance) {
        controller.setSetPoint((6.3 * Math.sqrt(398210 * distance)
                - 900 * Math.sqrt(distance)
                + (Math.ceil(distance / 5) * 400)) * (28.0 / 60.0));
    }
    public void turnOff() {
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

    public boolean isAtSpeed() {
        return Math.abs(controller.getVelocityError()) < 100;
    }
}

