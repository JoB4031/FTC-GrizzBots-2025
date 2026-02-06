package org.firstinspires.ftc.teamcode.tuners;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Fidget Tech Tuner", group="Tuners")
public class FidgetTechTuner extends OpMode {
    private Servo fidgetTech;
    private double position = .5;
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
        fidgetTech.setPosition(position);
    }
}
