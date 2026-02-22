package org.firstinspires.ftc.teamcode.hardware.motors;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.hardware.sensors.IntakeSensor;

public class FidgetTech {

    private Servo indexer;
    private static int snapPoint = 15;

    private final double[] positions = {
            0.04, 0.075, 0.115, 0.15, 0.185, 0.225, 0.26, 0.3, 0.33, 0.37,
            0.405, 0.445, 0.485, 0.525, 0.565, 0.605, 0.645, 0.675, 0.715,
            0.755, 0.79, 0.83, 0.86, 0.9, 0.94, 0.975, 1
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
