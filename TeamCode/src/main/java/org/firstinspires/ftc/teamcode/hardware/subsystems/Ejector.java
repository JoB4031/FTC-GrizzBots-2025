package org.firstinspires.ftc.teamcode.hardware.subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class Ejector implements Subsystem {
    public static final Ejector INSTANCE = new Ejector();
    private Ejector() { }
    private final ServoEx ejector = new ServoEx("ejector", 0.001);

    public Command fire = new SetPosition(ejector, 0.35).requires(this);

    public Command reset = new SetPosition(ejector, 0).requires(this);
}

