package org.firstinspires.ftc.teamcode.hardware.centralHub;

import org.firstinspires.ftc.teamcode.hardware.subsystems.EjectorSubsystem;
import org.firstinspires.ftc.teamcode.hardware.subsystems.FlywheelSubsystem;
import org.firstinspires.ftc.teamcode.hardware.subsystems.FidgetTechSubsystem;
import org.firstinspires.ftc.teamcode.hardware.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.hardware.sensors.IntakeSensorSubsystem;
import org.firstinspires.ftc.teamcode.hardware.vision.Vision;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;

public class CommandHub {
    public static final CommandHub INSTANCE = new CommandHub();
    CommandHub() {}
    private final FlywheelSubsystem cannon = FlywheelSubsystem.INSTANCE;
    private final FidgetTechSubsystem sorter = FidgetTechSubsystem.INSTANCE;
    private final IntakeSubsystem intake = IntakeSubsystem.INSTANCE;
    private final IntakeSensorSubsystem colorSensor = IntakeSensorSubsystem.INSTANCE;
    private final EjectorSubsystem boot = EjectorSubsystem.INSTANCE;
    public Command waitForArtifact = new Command() {
        @Override
        public boolean isDone() {
            return (colorSensor.getArtifact() != FidgetTechSubsystem.artifactColor.NONE);
        }
    };

    public SequentialGroup fireColor(FidgetTechSubsystem.artifactColor color) {
        return new SequentialGroup(
        cannon.setVelocity(3500).and(sorter.goToColorArtifact(color)),
                boot.fire.thenWait(0.2),
                boot.reset

        );
    }

    public SequentialGroup firePattern() {
        if(Vision.pattern == Vision.BallPattern.PPG) {
            return new SequentialGroup(
                    fireColor(FidgetTechSubsystem.artifactColor.PURPLE),
                    fireColor(FidgetTechSubsystem.artifactColor.PURPLE),
                    fireColor(FidgetTechSubsystem.artifactColor.GREEN)
            );
        }
        if(Vision.pattern == Vision.BallPattern.PGP) {
            return new SequentialGroup(
                    fireColor(FidgetTechSubsystem.artifactColor.PURPLE),
                    fireColor(FidgetTechSubsystem.artifactColor.GREEN),
                    fireColor(FidgetTechSubsystem.artifactColor.PURPLE)
            );
        }
        if(Vision.pattern == Vision.BallPattern.GPP) {
            return new SequentialGroup(
                    fireColor(FidgetTechSubsystem.artifactColor.GREEN),
                    fireColor(FidgetTechSubsystem.artifactColor.PURPLE),
                    fireColor(FidgetTechSubsystem.artifactColor.PURPLE)
            );
        }
        return new SequentialGroup(
                fireColor(FidgetTechSubsystem.artifactColor.PURPLE),
                fireColor(FidgetTechSubsystem.artifactColor.GREEN),
                fireColor(FidgetTechSubsystem.artifactColor.PURPLE)
        );
    }

    public SequentialGroup intakeOneArtifact = new SequentialGroup(
            intake.setPower(1,1)
                    .and(sorter.goToIntake(sorter.findEmptySlot()))
                    .then(waitForArtifact)
    );

}
