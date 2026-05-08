
package org.firstinspires.ftc.teamcode;


import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.Telemetry;
@TeleOp(name = "Color Sensor", group = "Test")
public class ColorSensors extends OpMode {
    public NormalizedColorSensor colorSensor;

    public enum DetectedColor {
        PURPLE,
        GREEN,
        UNKNOWN,

    }

    @Override
    public void init() {

            colorSensor = hardwareMap.get(NormalizedColorSensor.class, "color");
            colorSensor.setGain(4);


    }

    @Override
    public void loop() {
        getDetectedColor(telemetry);

            /*
                red, green, blue

                Purple =

                Green =
        */




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


