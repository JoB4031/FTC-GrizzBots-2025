package org.firstinspires.ftc.teamcode.hardware.motors;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.hardware.sensors.IntakeSensor;

public class FidgetTech {

    private Servo indexer;
    private static int snapPoint = 15;

    private final double[] positions = {
            0,0.03,0.07,0.1,0.14,0.17,0.21,0.25,0.29,0.32,0.37,0.4,
            0.43,0.47,0.51,0.55,0.59,0.63,0.67,0.71,0.75,0.78,0.82,
            0.86,0.89,0.93,0.96,1
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
