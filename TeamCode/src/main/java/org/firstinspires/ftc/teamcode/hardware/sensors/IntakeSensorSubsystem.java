package org.firstinspires.ftc.teamcode.hardware.sensors;

import android.graphics.Color;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import dev.nextftc.core.components.Component;
import dev.nextftc.core.subsystems.Subsystem;

@Configurable
public class IntakeSensorSubsystem implements Subsystem {

    public static final IntakeSensorSubsystem INSTANCE = new IntakeSensorSubsystem();
    private IntakeSensorSubsystem() {}

    // ------------------------------------------------------------
    // HARDWARE
    // ------------------------------------------------------------

    private RevColorSensorV3 distanceSensor;
    private NormalizedColorSensor colorSensor;

    // ------------------------------------------------------------
    // STATE
    // ------------------------------------------------------------

    public enum detectedColor {
        PURPLE, GREEN, NONE, UNKNOWN
    }

    private boolean artifactLoaded = false;
    private boolean nothingDetected = true;
    private final ElapsedTime artifactTime = new ElapsedTime();

    // ------------------------------------------------------------
    // INITIALIZATION (NEXT FTC STYLE)
    // ------------------------------------------------------------

    @Override
    public void initialize() {
        distanceSensor = hw.get(RevColorSensorV3.class, "color");
        colorSensor = hw.get(NormalizedColorSensor.class, "color");
        colorSensor.setGain(10);
    }

    // ------------------------------------------------------------
    // ARTIFACT DETECTION LOGIC
    // ------------------------------------------------------------

    /** Returns true if an artifact is detected and color is valid. */
    public boolean isArtifactLoaded() {

        double dist = distanceSensor.getDistance(DistanceUnit.INCH);

        if (dist < 2) {
            if ((nothingDetected || artifactTime.seconds() > 0.5)
                    && getDetectedColor() != detectedColor.NONE) {

                artifactLoaded = true;
                artifactTime.reset();
                nothingDetected = false;

            } else {
                artifactLoaded = false;
            }

        } else if (dist > 2.5) {
            nothingDetected = true;
            artifactTime.reset();
        }

        return artifactLoaded;
    }

    /** Returns true if nothing is detected in the intake. */
    public boolean isNothingDetected() {

        double dist = distanceSensor.getDistance(DistanceUnit.INCH);

        if (dist < 2.5) {
            if (nothingDetected) {
                artifactLoaded = true;
                nothingDetected = false;
            } else {
                artifactLoaded = false;
            }

        } else if (dist > 2.5) {
            nothingDetected = true;
        }

        return nothingDetected;
    }

    // ------------------------------------------------------------
    // COLOR DETECTION LOGIC
    // ------------------------------------------------------------

    public detectedColor getDetectedColor() {

        NormalizedRGBA colors = colorSensor.getNormalizedColors();

        float[] hsv = new float[3];
        Color.colorToHSV(colors.toColor(), hsv);

        float hue = hsv[0];

        if (hue >= 200 && hue <= 240) return detectedColor.PURPLE;
        if (hue >= 150 && hue <= 163) return detectedColor.GREEN;

        return detectedColor.NONE;
    }

    // ------------------------------------------------------------
    // ACCESSORS
    // ------------------------------------------------------------

    public double getDistanceInches() {
        return distanceSensor.getDistance(DistanceUnit.INCH);
    }

    public boolean hasArtifact() {
        return isArtifactLoaded();
    }

    public detectedColor getColor() {
        return getDetectedColor();
    }
}