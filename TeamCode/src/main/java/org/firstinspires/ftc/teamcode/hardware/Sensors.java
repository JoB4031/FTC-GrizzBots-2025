package org.firstinspires.ftc.teamcode.hardware;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.Telemetry;


@Configurable
public class Sensors {
    public static class ColorSensors {
        NormalizedColorSensor colorSensor;

        public enum DetectedColor {
            RED,
            BLUE,
            YELLOW,
            UNKNOWN,

        }

        public void init(HardwareMap hardwareMap) {
            NormalizedColorSensor colorSensor = hardwareMap.get(NormalizedColorSensor.class, "color");
            colorSensor.setGain(4);
        }

        public DetectedColor getDetectedColor(Telemetry telemetry) {
            NormalizedRGBA colors = colorSensor.getNormalizedColors();

            float normRed, normGreen, normBlue;
            normRed = colors.red / colors.alpha;
            normGreen = colors.green / colors.alpha;
            normBlue = colors.blue / colors.alpha;

            telemetry.addData("red", normRed);
            telemetry.addData("blue", normBlue);
            telemetry.addData("green", normGreen);

            return DetectedColor.UNKNOWN;
        }



    }
    public void update() {

    }
}