package org.firstinspires.ftc.teamcode.hardware.motors;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class fidgetTech {

    private Servo indexer;
    private static int spinPosition = 15;

    private final double[] positions = {
            0,0,0.04,0.08,0.12,0.15,0.19,0.22,0.26,0.3,0.33,0.37,0.41,
            0.45,0.48,0.52,0.56,0.61,0.64,0.68,0.72,0.75,0.79,0.83,
            0.86,0.9,0.93,0.97
    };

    public void initFidgetTech(HardwareMap hw) {
        indexer = hw.get(Servo.class, "spinIndexer");
    }

    public void update(boolean flywheelRunning, boolean doNotSpin) {
        if (!flywheelRunning || doNotSpin) {
            if (spinPosition % 2 == 1) spinPosition += (spinPosition >= 15 ? -1 : 1);
        } else {
            if (spinPosition % 2 == 0) spinPosition += (spinPosition >= 15 ? -1 : 1);
        }

        indexer.setPosition(positions[spinPosition]);
    }

    public void advance() {
        spinPosition += 2;
    }
    public void reset() {
        spinPosition = 15;
    }
    public void setPosition(int pos) {
        spinPosition = pos;
    }
    public int getPosition() {
        return spinPosition;
    }
}
