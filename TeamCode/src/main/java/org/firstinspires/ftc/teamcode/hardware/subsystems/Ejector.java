package org.firstinspires.ftc.teamcode.hardware.subsystems;

import com.skeletonarmy.marrow.TimerEx;
import java.util.concurrent.TimeUnit;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;

public class Ejector implements Subsystem {
    public static final Ejector EJECTOR = new Ejector();
    private Ejector() {}

    private final TimerEx hitTime = new TimerEx(0.2, TimeUnit.SECONDS);
    private final ServoEx ejector = new ServoEx("ejector", 0.001);
    public boolean fireDone;

    public Command fire()  {
        return new Command() {
            @Override
            public void start() {
                hitTime.restart();
                ejector.setPosition(0.3);
            }
            @Override
            public void update() {
                if (hitTime.isDone()) {
                    hitTime.restart();
                    ejector.setPosition(0);
                }
            }
            @Override
            public boolean isDone() {
                return fireDone;
            }

        }.requires(this);
    }

    @Override
    public void initialize() {
        ejector.setPosition(0);
    }
}

