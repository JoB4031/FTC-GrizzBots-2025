package org.firstinspires.ftc.teamcode.tuners;

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

    @Override
    public void init() {
        fidgetTech = hardwareMap.get(Servo.class, "spinIndexer");
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
        if (gamepad1.circleWasPressed()) list.add(position);
        fidgetTech.setPosition(position);
        telemetry.addData("Position", fidgetTech.getPosition());
        telemetry.addData("Positions", list);
    }
}
