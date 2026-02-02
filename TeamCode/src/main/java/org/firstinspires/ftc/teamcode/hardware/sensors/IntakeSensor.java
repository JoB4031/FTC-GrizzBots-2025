package org.firstinspires.ftc.teamcode.hardware.sensors;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class IntakeSensor {
    private RevColorSensorV3 artifactedIntakeDetector;
    private boolean artifactLoaded;
    private boolean nothingDetected;
    public void initIntakeSensor(HardwareMap hw) {
        artifactedIntakeDetector = hw.get(RevColorSensorV3.class, "color");

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

}
