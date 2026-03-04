package org.firstinspires.ftc.teamcode.hardware.sensors;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;

public class LEDIndicator implements Subsystem {
    public static final LEDIndicator INSTANCE = new LEDIndicator();
    private LEDIndicator() {}
    private final ServoEx led = new ServoEx("LED");
    public void red() {
        led.setPosition(0.277);
    }
    public void orange() {
        led.setPosition(0.333);
    }
    public void yellow() {
        led.setPosition(0.388);
    }
    public void green() {
        led.setPosition(0.500);
    }
    public void blue() {
        led.setPosition(0.611);
    }
    public void purple() {
        led.setPosition(0.722);
    }
    public void white() {
        led.setPosition(1);
    }
    public void off() {
        led.setPosition(0);
    }
}