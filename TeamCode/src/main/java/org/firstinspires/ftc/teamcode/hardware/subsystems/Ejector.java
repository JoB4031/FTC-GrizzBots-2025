package org.firstinspires.ftc.teamcode.hardware.subsystems;

import com.skeletonarmy.marrow.TimerEx;

import java.util.concurrent.TimeUnit;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class Ejector implements Subsystem {
    public static final Ejector INSTANCE = new Ejector();
    private Ejector() { }
    private final TimerEx hitTime = new TimerEx(0.2, TimeUnit.SECONDS);
    private final ServoEx ejector = new ServoEx("ejector", 0.001);

    public Command fire = new Command() {

        @Override
        public void start() {
            hitTime.restart();
            if(!FidgetTech.inIntakePosition && FidgetTech.INSTANCE.spinComplete) {
                ejector.setPosition(0.3);
            }
        }

        @Override
        public boolean isDone() {
            return hitTime.isDone();
        }

    };

    public Command reset = new Command() {

        @Override
        public void start() {
            hitTime.restart();
            ejector.setPosition(0);
        }

        @Override
        public boolean isDone() {
            return hitTime.isDone();
        }

    };
}

