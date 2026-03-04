package org.firstinspires.ftc.teamcode.hardware.sensors;

import android.graphics.Color;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.hardware.subsystems.FidgetTech;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;

@Configurable
public class ArtifactSensor implements Subsystem {

    public static final ArtifactSensor INSTANCE = new ArtifactSensor();
    private ArtifactSensor() {}

    // ------------------------------------------------------------
    // HARDWARE
    // ------------------------------------------------------------

    private RevColorSensorV3 distanceSensor;
    private NormalizedColorSensor colorSensor;

    // ------------------------------------------------------------
    // STATE
    // ------------------------------------------------------------


    // ------------------------------------------------------------
    // INITIALIZATION (NEXT FTC STYLE)
    // ------------------------------------------------------------

    @Override
    public void initialize() {
        HardwareMap hw = ActiveOpMode.hardwareMap();

        distanceSensor = hw.get(RevColorSensorV3.class, "color");
        colorSensor = hw.get(NormalizedColorSensor.class, "color");

        colorSensor.setGain(10);
    }

    // ------------------------------------------------------------
    // ARTIFACT DETECTION LOGIC
    // ------------------------------------------------------------

    public FidgetTech.artifactColor getArtifact() {
        double dist = distanceSensor.getDistance(DistanceUnit.INCH);

        if (dist < 2 && (getDetectedColor() != FidgetTech.artifactColor.NONE)) {
            return getDetectedColor();

        }

        return FidgetTech.artifactColor.NONE;
    }

    public boolean fidgetBlocked() {
        double dist = distanceSensor.getDistance(DistanceUnit.INCH);
        return dist < 2;
    }

    // ------------------------------------------------------------
    // COLOR DETECTION LOGIC
    // ------------------------------------------------------------

    public FidgetTech.artifactColor getDetectedColor() {
        NormalizedRGBA colors = colorSensor.getNormalizedColors();

        float[] hsv = new float[3];
        Color.colorToHSV(colors.toColor(), hsv);

        float hue = hsv[0];

        if (hue >= 200 && hue <= 240) return FidgetTech.artifactColor.PURPLE;
        if (hue >= 150 && hue <= 163) return FidgetTech.artifactColor.GREEN;

        return FidgetTech.artifactColor.NONE;
    }


}