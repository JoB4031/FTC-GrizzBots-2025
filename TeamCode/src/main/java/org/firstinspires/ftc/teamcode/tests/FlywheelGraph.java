package org.firstinspires.ftc.teamcode.tests;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.hardware.motors.Flywheel;

@Configurable
@TeleOp(name="FlywheelGraph", group="Tests")
public class FlywheelGraph extends OpMode {
    public Flywheel shooter;
    public static double kP = 0.006;
    public static double kI = 0.0;
    public static double kD = 0.0;
    public static double kF = 0.0004;

    // Add these:
    public static double currentVelocity = 0;
    public static double targetVelocity = 1000;

    @Override
    public void init() {
        shooter = new Flywheel();
        shooter.initFlywheel(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.dpadLeftWasPressed()) {
            targetVelocity -= 100;
        }
        if (gamepad1.dpadDownWasPressed()) {
            shooter.controller.setSetPoint(0);
        }
        if (gamepad1.dpadRightWasPressed()) {
            targetVelocity += 100;
        }
        if (gamepad1.dpadUpWasPressed()) {
            shooter.controller.setSetPoint(targetVelocity);
        }
        shooter.controller.setPIDF(kP, kI, kD, kF);
        shooter.update();
        currentVelocity = shooter.getVelocity();  // You must implement this
        telemetry.addData("Target Velocity", targetVelocity);
        telemetry.addData("Flywheel Velocity", currentVelocity);
        telemetry.addData("Flywheel Power", shooter.rightFlywheel.getPower());
        telemetry.update();
    }
}
