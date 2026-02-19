package org.firstinspires.ftc.teamcode.tuners;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.hardware.sensors.IntakeSensor;
@TeleOp(name="Color Sensor Tuner", group="Tuners")
public class ColorSensorCalibration extends OpMode {
    public static float gain = 1;
    IntakeSensor intakeSensor;
    @Override
    public void init() {
        intakeSensor = new IntakeSensor();
        intakeSensor.initIntakeSensor(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.rightBumperWasPressed()) gain += 1;
        if (gamepad1.rightBumperWasPressed()) gain -= 1;
        intakeSensor.getDetectedColor(telemetry);
        telemetry.addData("Artifact Detected", intakeSensor.artifactedIntakeDetector.getDistance(DistanceUnit.INCH));
        telemetry.addData("Artifact Color", intakeSensor.getDetectedColor(telemetry));

    }
}
