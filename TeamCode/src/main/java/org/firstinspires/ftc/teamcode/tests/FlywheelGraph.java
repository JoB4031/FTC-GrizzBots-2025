package org.firstinspires.ftc.teamcode.tests;

import com.bylazar.configurables.annotations.Configurable;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.hardware.motors.Flywheel;
import org.firstinspires.ftc.teamcode.hardware.sensors.IntakeSensor;

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
    private IntakeSensor color;
    static TelemetryManager telemetryM;


    @Override
    public void init() {
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        shooter = new Flywheel();
        color = new IntakeSensor();
        color.initIntakeSensor(hardwareMap);
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

        telemetryM.addData("Target Velocity", targetVelocity);
        telemetryM.addData("Flywheel Velocity", currentVelocity);
        telemetryM.addData("Flywheel RPM", shooter.getRPM());
        telemetryM.addData("Flywheel Power", shooter.rightFlywheel.getPower());
        telemetryM.update();
        telemetryM.update(telemetry);
    }
}
