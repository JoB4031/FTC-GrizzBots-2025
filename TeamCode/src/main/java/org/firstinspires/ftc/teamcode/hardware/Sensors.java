package org.firstinspires.ftc.teamcode.hardware;

    import com.bylazar.configurables.annotations.Configurable;
    import com.qualcomm.hardware.rev.RevColorSensorV3;
    import com.qualcomm.robotcore.hardware.HardwareMap;

    import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@Configurable
public class Sensors {
    private RevColorSensorV3 artifactedIntakeDetector;
    public boolean artifactLoaded;
    public void initSensors(HardwareMap hardwareMap) {
        artifactedIntakeDetector = hardwareMap.get(RevColorSensorV3.class, "artifactedIntakeDetector");

    }

    public void update() {
        artifactLoaded = (artifactedIntakeDetector.getDistance(DistanceUnit.INCH) < 6);
    }

}