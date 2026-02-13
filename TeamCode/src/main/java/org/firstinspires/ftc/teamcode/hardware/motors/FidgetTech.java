package org.firstinspires.ftc.teamcode.hardware.motors;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.hardware.sensors.IntakeSensor;

public class FidgetTech {

    private Servo indexer;
    private static int snapPoint = 15;

    private final double[] positions = {
            0,0.04,0.08,0.12,0.15,0.19,0.22,0.26,0.3,0.33,0.37,0.41,
            0.45,0.48,0.52,0.56,0.61,0.64,0.68,0.72,0.75,0.79,0.83,
            0.86,0.9,0.93,0.97
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
                indexer.setPosition((positions[snapPoint])+0.01);
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
