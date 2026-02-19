package org.firstinspires.ftc.teamcode.hardware.motors;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.hardware.sensors.IntakeSensor;

public class FidgetTech {

    private Servo indexer;
    private static int snapPoint = 15;

    private final double[] positions = {
            0,0.005,0.035,0.075,0.110,0.145,0.185,0.22,0.265,0.295,0.330,0.370,0.405,0.450,
            0.480,0.520,0.560,0.6,0.64,0.675,0.715,0.750,0.790,0.825,0.860,
            0.905,0.935,0.970,1
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
