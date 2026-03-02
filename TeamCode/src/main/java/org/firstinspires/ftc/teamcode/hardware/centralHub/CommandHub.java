package org.firstinspires.ftc.teamcode.hardware.centralHub;

import org.firstinspires.ftc.teamcode.hardware.motors.EjectorSubsystem;
import org.firstinspires.ftc.teamcode.hardware.motors.FlywheelSubsystem;
import org.firstinspires.ftc.teamcode.hardware.motors.IndexerSubsystem;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.SequentialGroup;

public class CommandHub {
    public SequentialGroup fire() {
        return new SequentialGroup(
        FlywheelSubsystem.INSTANCE.setVelocity(1500).and
                (IndexerSubsystem.INSTANCE.next),
                EjectorSubsystem.INSTANCE.fire

        );
    }
}
