package org.firstinspires.ftc.teamcode.hardware.sensors;

import android.graphics.Color;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.hardware.motors.FidgetTech;
import org.firstinspires.ftc.teamcode.tuners.ColorSensorCalibration;

@Configurable
public class IntakeSensor {
    public RevColorSensorV3 artifactedIntakeDetector;
    public NormalizedColorSensor artifactColorDetector;
    public enum  detectedColor {
        PURPLE, GREEN, NONE, UNKNOWN
    }
    private boolean artifactLoaded;
    private boolean nothingDetected;
    public final ElapsedTime artifactTime = new ElapsedTime();
    public static double greenBallRedLessThan = 0.03;
    public static double greenBallGreenLessThan = 0.1;
    public static double greenBallBlueLessThan = 0.1;
    public static double greenBallRedGreaterThan = 0.1;
    public static double greenBallGreenGreaterThan = 0.1;
    public static double greenBallBlueGreaterThan = 0.1;

    public static double purpleBallRedLessThan = 0.18;
    public static double purpleBallGreenLessThan = 0.14;
    public static double purpleBallBlueLessThan = 0.11;
    public static double purpleBallRedGreaterThan = 0.05;
    public static double purpleBallGreenGreaterThan = 0.25;
    public static double purpleBallBlueGreaterThan = 0.45;
    public void initIntakeSensor(HardwareMap hw) {
        artifactedIntakeDetector = hw.get(RevColorSensorV3.class, "color");
        artifactColorDetector = hw.get(NormalizedColorSensor.class, "color");
        artifactColorDetector.setGain(10);

    }

    public boolean isArtifactLoaded() {
        if (artifactedIntakeDetector.getDistance(DistanceUnit.INCH) < 2) {
            if ((nothingDetected || artifactTime.seconds() > 0.5) && getDetectedColor() != detectedColor.NONE) {
                artifactLoaded = true;
                artifactTime.reset();
                nothingDetected = false;
            } else {
                artifactLoaded = false;
            }
        } else if (artifactedIntakeDetector.getDistance(DistanceUnit.INCH) > 2.5) {
            nothingDetected= true;
            artifactTime.reset();
        }
        return artifactLoaded;
    }

    public boolean isNothingDetected() {
        if (artifactedIntakeDetector.getDistance(DistanceUnit.INCH) < 2.5) {
            if (nothingDetected) {
                artifactLoaded = true;
                nothingDetected = false;
            } else artifactLoaded = false;
        } else if (artifactedIntakeDetector.getDistance(DistanceUnit.INCH) > 2.5) nothingDetected= true;
        return nothingDetected;
    }
    public detectedColor getDetectedColor() {
        // Read raw sensor values
        NormalizedRGBA colors = artifactColorDetector.getNormalizedColors();

// Convert to HSV and store in an array
        float[] hsvValues = new float[3];
        Color.colorToHSV(colors.toColor(), hsvValues);
        float hue = hsvValues[0];
        if(hue >= 200 && hue <= 240) {
            return detectedColor.PURPLE;
        }
        if(hue >= 150 && hue <= 163) {
            return detectedColor.GREEN;
        }
        return detectedColor.NONE;
    }

}
