package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.hardware.Motors;

@TeleOp(name= "Fidget Tech Tuning", group= "Tests")
public class fidgetTechTuning extends OpMode {

    private Motors motors;
    private double fidgetTechPosition = 0.5;
    @Override
    public void init() {
        motors = new Motors();
        motors.initMotors(hardwareMap);
        motors.update();
    }

    @Override
    public void loop() {
        motors.fidgetTech.setPosition(fidgetTechPosition);
        if(gamepad1.leftBumperWasPressed()) fidgetTechPosition -= 0.01;
        if(gamepad1.rightBumperWasPressed()) fidgetTechPosition += 0.01;
        telemetry.addData("Fidget Tech Position",motors.fidgetTech.getPosition());
        telemetry.update();
    }
}
