package org.firstinspires.ftc.teamcode.hardware.centralHub;

import static org.firstinspires.ftc.teamcode.hardware.subsystems.FidgetTech.artifactsHeld;

import org.firstinspires.ftc.teamcode.hardware.subsystems.Drive;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Ejector;
import org.firstinspires.ftc.teamcode.hardware.subsystems.FidgetTech;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Intake;
import org.firstinspires.ftc.teamcode.hardware.sensors.ArtifactSensor;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Vision;
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

    public SequentialGroup fireColor(FidgetTech.artifactColor color) {
        boolean artifactFound= false;
        for (FidgetTech.artifactColor artifact : artifactsHeld)
            if (artifact == color) {
                artifactFound = true;
                break;
            }
        if (artifactFound) {
            return new SequentialGroup(
                    cannon.setVelocity(3500).and(sorter.goToArtifact(color)),
                    boot.fire,
                    boot.reset,
                    cannon.stopPower()

            );
        } else return new SequentialGroup(cannon.setVelocity(3500));
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
                    .then(colorSensor.findArtifact)
    );

    @Override
    public void periodic(){
        if(sorter.spinComplete && (colorSensor.getArtifact() != FidgetTech.artifactColor.NONE)) {
            artifactsHeld[sorter.currentSlot] = colorSensor.getArtifact();
        }
    }

}
