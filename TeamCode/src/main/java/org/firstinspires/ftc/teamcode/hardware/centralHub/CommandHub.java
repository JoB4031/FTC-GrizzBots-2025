package org.firstinspires.ftc.teamcode.hardware.centralHub;

import org.firstinspires.ftc.teamcode.hardware.subsystems.FidgetTech;
import org.firstinspires.ftc.teamcode.hardware.sensors.ArtifactSensor;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Intake;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;

public class CommandHub implements Subsystem {
    public static final CommandHub INSTANCE = new CommandHub();
    CommandHub() {}

    public Command intakeArtifacts() {
        return new Command() {

            @Override
            public void start() {
                Intake.INSTANCE.setPower(1, 1);
            }

            @Override
            public void update() {
                if (FidgetTech.artifactsHeld[FidgetTech.currentSlot] != FidgetTech.artifactColor.NONE) {
                    FidgetTech.INSTANCE.goToArtifact(FidgetTech.artifactColor.NONE);
                }
            }

            @Override
            public boolean isDone() {
                return FidgetTech.INSTANCE.fidgetTechFull;
            }

            @Override
            public void stop(boolean interrupted) {
                FidgetTech.INSTANCE.goToArtifact(FidgetTech.artifactColor.PURPLE);
            }

        };
    }
    public Command stopIntakeArtifacts() {
        return new Command() {
            @Override
            public void start() {
                intakeArtifacts().cancel();
            }
            @Override
            public boolean isDone() {
                return true;
            }
        };
    }

    @Override
    public void periodic(){
        if(FidgetTech.INSTANCE.spinComplete && (ArtifactSensor.INSTANCE.currentArtifact != FidgetTech.artifactColor.NONE)) {
            FidgetTech.artifactsHeld[FidgetTech.currentSlot] = ArtifactSensor.INSTANCE.currentArtifact;
        }
    }

}
