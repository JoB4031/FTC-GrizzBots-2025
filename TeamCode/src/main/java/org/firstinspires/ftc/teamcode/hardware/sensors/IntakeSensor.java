package org.firstinspires.ftc.teamcode.hardware.sensors;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class IntakeSensor {
    public RevColorSensorV3 artifactedIntakeDetector;
    public NormalizedColorSensor artifactColorDetector;
    public static enum  detectedColor {
        PURPLE, GREEN, NONE, UNKNOWN
    }
    private boolean artifactLoaded;
    private boolean nothingDetected;
    public void initIntakeSensor(HardwareMap hw) {
        artifactedIntakeDetector = hw.get(RevColorSensorV3.class, "color");
        artifactColorDetector = hw.get(NormalizedColorSensor.class, "color");

    }

    public boolean isArtifactLoaded() {
        if (artifactedIntakeDetector.getDistance(DistanceUnit.INCH) <= 2) {
            if (nothingDetected) {
                artifactLoaded = true;
                nothingDetected = false;
            } else artifactLoaded = false;
        } else if (artifactedIntakeDetector.getDistance(DistanceUnit.INCH) > 4) nothingDetected= true;
        return artifactLoaded;
    }

    public boolean isNothingDetected() {
        if (artifactedIntakeDetector.getDistance(DistanceUnit.INCH) <= 2) {
            if (nothingDetected) {
                artifactLoaded = true;
                nothingDetected = false;
            } else artifactLoaded = false;
        } else if (artifactedIntakeDetector.getDistance(DistanceUnit.INCH) > 4) nothingDetected= true;
        return nothingDetected;
    }
    public detectedColor getDetectedColor(Telemetry telemetry) {
        NormalizedRGBA colors = artifactColorDetector.getNormalizedColors();
        float normRed, normGreen, normBlue;
        normRed = colors.red / colors.alpha;
        normGreen = colors.green / colors.alpha;
        normBlue = colors.blue / colors.alpha;

        telemetry.addData("Red", normRed);
        telemetry.addData("Green", normGreen);
        telemetry.addData("Blue", normBlue);

        return detectedColor.NONE;
    }

}
