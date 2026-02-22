package org.firstinspires.ftc.teamcode.tuners;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@TeleOp(name="Fidget Tech Tuner", group="Tuners")
public class FidgetTechTuner extends OpMode {
    private Servo fidgetTech;
    private double position = 0;
    List<Double> list = new ArrayList<>(Arrays.asList(
    ));
    private TelemetryManager telemetryM;

    @Override
    public void init() {
        fidgetTech = hardwareMap.get(Servo.class, "spinIndexer");
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
    }

    @Override
    public void loop() {
        if (gamepad1.dpadRightWasPressed()) {
            position += 0.01;
        }
        if (gamepad1.dpadLeftWasPressed()) {
            position -= 0.01;
        }
        if (gamepad1.dpadUpWasPressed()) {
            position += 0.005;
        }
        if (gamepad1.dpadDownWasPressed()) {
            position -= 0.005;
        }
        if (gamepad1.rightBumperWasPressed()) {
            position += 0.001;
        }
        if (gamepad1.leftBumperWasPressed()) {
            position -= 0.001;
        }
        double roundedPosition = ((double) Math.round(position * 1000) /1000);
        if (gamepad1.circleWasPressed()) list.add(roundedPosition);
        fidgetTech.setPosition(position);
        telemetryM.addData("Position", fidgetTech.getPosition());
        telemetryM.addData("Positions", list);
        telemetryM.update(telemetry);
    }
}
