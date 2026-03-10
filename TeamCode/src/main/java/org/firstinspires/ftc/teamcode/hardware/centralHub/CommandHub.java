package org.firstinspires.ftc.teamcode.hardware.centralHub;

import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.hardware.subsystems.Drive;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Ejector;
import org.firstinspires.ftc.teamcode.hardware.subsystems.FidgetTech;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Intake;
import org.firstinspires.ftc.teamcode.hardware.sensors.ArtifactSensor;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Vision;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.subsystems.Subsystem;

public class CommandHub implements Subsystem {
    public static final CommandHub INSTANCE = new CommandHub();
    CommandHub() {}
    
    private final Flywheel cannon = Flywheel.INSTANCE;
    private final FidgetTech sorter = FidgetTech.INSTANCE;
    private final Intake intake = Intake.INSTANCE;
    private final ArtifactSensor colorSensor = ArtifactSensor.INSTANCE;
    private final Ejector boot = Ejector.INSTANCE;
    private final Drive drive = Drive.INSTANCE;
    public Command waitForArtifact = new Command() {
        @Override
        public boolean isDone() {
            return (colorSensor.getArtifact() != FidgetTech.artifactColor.NONE);
        }
    };

    public SequentialGroup fireColor(FidgetTech.artifactColor color) {
        return new SequentialGroup(
        cannon.setVelocity(3500).and(sorter.shootArtifact(color)),
                boot.fire.thenWait(0.2),
                boot.reset

        );
    }

    public SequentialGroup firePattern() {
        if(Vision.pattern == Vision.BallPattern.PPG) {
            return new SequentialGroup(
                    fireColor(FidgetTech.artifactColor.PURPLE),
                    fireColor(FidgetTech.artifactColor.PURPLE),
                    fireColor(FidgetTech.artifactColor.GREEN)
            );
        }
        if(Vision.pattern == Vision.BallPattern.PGP) {
            return new SequentialGroup(
                    fireColor(FidgetTech.artifactColor.PURPLE),
                    fireColor(FidgetTech.artifactColor.GREEN),
                    fireColor(FidgetTech.artifactColor.PURPLE)
            );
        }
        if(Vision.pattern == Vision.BallPattern.GPP) {
            return new SequentialGroup(
                    fireColor(FidgetTech.artifactColor.GREEN),
                    fireColor(FidgetTech.artifactColor.PURPLE),
                    fireColor(FidgetTech.artifactColor.PURPLE)
            );
        }
        return new SequentialGroup(
                fireColor(FidgetTech.artifactColor.PURPLE),
                fireColor(FidgetTech.artifactColor.GREEN),
                fireColor(FidgetTech.artifactColor.PURPLE)
        );
    }

    public SequentialGroup intakeOneArtifact = new SequentialGroup(
            intake.setPower(1,1)
                    .and(sorter.goEmptyIntakeSlot)
                    .then(waitForArtifact)
    );

    public SequentialGroup followPath(Pose pose) {
        return new SequentialGroup(
                drive.goTo(pose),
                drive.teleOpDrive
        );
    }

}
