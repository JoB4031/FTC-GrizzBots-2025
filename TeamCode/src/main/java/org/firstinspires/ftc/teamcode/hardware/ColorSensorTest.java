package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class ColorSensorTest extends OpMode {
    Sensors.ColorSensors colorSensor = new Sensors.ColorSensors();
    @Override
    public void init() {
        colorSensor.init(hardwareMap);
    }

    @Override
    public void loop(){
        colorSensor.getDetectedColor(telemetry);
    }
}
