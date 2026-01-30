package org.firstinspires.ftc.teamcode.hardware;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@Configurable
public class Sensors {
    public RevColorSensorV3 artifactedIntakeDetector;
    public boolean artifactLoaded;
    public boolean nothingDetected;
    public void initSensors(HardwareMap hardwareMap) {
        artifactedIntakeDetector = hardwareMap.get(RevColorSensorV3.class, "color");

    }

    public void update() {

        if (artifactedIntakeDetector.getDistance(DistanceUnit.INCH) <= 2) {
            if (nothingDetected) {
                artifactLoaded = true;
                nothingDetected = false;
            } else artifactLoaded = false;
        } else if (artifactedIntakeDetector.getDistance(DistanceUnit.INCH) > 4) nothingDetected= true;
    }

}