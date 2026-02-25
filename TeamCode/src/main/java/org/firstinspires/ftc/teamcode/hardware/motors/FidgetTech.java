package org.firstinspires.ftc.teamcode.hardware.motors;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.hardware.sensors.IntakeSensor;

public class FidgetTech {

    private Servo indexer;
    private static int snapPoint = 15;

    private final double[] positions = {0.0, 0.03, 0.07, 0.105, 0.145, 0.18, 0.22, 0.26,
            0.295, 0.325, 0.365, 0.405, 0.445, 0.485, 0.525, 0.565, 0.6, 0.64, 0.68,
            0.715, 0.755, 0.785, 0.825, 0.855, 0.89, 0.925, 0.96, 0.995
    };
    public static IntakeSensor.detectedColor[] artifactsLoaded = {
            IntakeSensor.detectedColor.NONE, IntakeSensor.detectedColor.NONE, IntakeSensor.detectedColor.NONE
    };

    public int colorSnapPoint;

    public void initFidgetTech(HardwareMap hw) {
        indexer = hw.get(Servo.class, "spinIndexer");
    }

    public void update(boolean flywheelRunning, boolean doNotSpin) {
        if (!flywheelRunning || doNotSpin) {
            if (snapPoint % 2 == 1) snapPoint += (snapPoint >= 15 ? -1 : 1);
        } else {
            if (snapPoint % 2 == 0) snapPoint += (snapPoint >= 15 ? -1 : 1);
        }
        try {
            if ((snapPoint % 2 == 0)) {
                indexer.setPosition(positions[snapPoint]);
            } else indexer.setPosition(positions[snapPoint]);

        } catch (ArrayIndexOutOfBoundsException e) {
            snapPoint = 15;
        }
    }

    public void next() {
        snapPoint += 2;
    }

    public void previous() {
        snapPoint -= 2;
    }

    public void setSnapPoint(int pos) {
        snapPoint = pos;
    }
    public int getSnapPoint() {
        return snapPoint;
    }
}
