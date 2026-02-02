package org.firstinspires.ftc.teamcode.hardware.motors;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Ejector {

    private Servo ejector;

    public void initEjector(HardwareMap hw) {
        ejector = hw.get(Servo.class, "ballEjector");
    }

    public void fire() {
        ejector.setPosition(0.3);
    }

    public void reset() {
        ejector.setPosition(0);
    }
}

