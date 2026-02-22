package org.firstinspires.ftc.teamcode.hardware.sensors;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class LED {
    private Servo LED;
    public void initLED(HardwareMap hw) {
        LED = hw.get(Servo.class, "LED");
    }
    public void red() {
        LED.setPosition(0.277);
    }
    public void orange() {
        LED.setPosition(0.333);
    }
    public void yellow() {
        LED.setPosition(0.388);
    }
    public void green() {
        LED.setPosition(0.500);
    }
    public void blue() {
        LED.setPosition(0.611);
    }
    public void purple() {
        LED.setPosition(0.722);
    }
    public void white() {
        LED.setPosition(1);
    }
    public void off() {
        LED.setPosition(0);
    }
}