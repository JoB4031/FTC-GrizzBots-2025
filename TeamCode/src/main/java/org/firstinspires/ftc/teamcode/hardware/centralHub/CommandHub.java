package org.firstinspires.ftc.teamcode.hardware.centralHub;

import org.firstinspires.ftc.teamcode.hardware.pedroPathing.LaunchTracker;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Ejector;
import org.firstinspires.ftc.teamcode.hardware.subsystems.FidgetTech;
import org.firstinspires.ftc.teamcode.hardware.sensors.ArtifactSensor;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Intake;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Vision;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.subsystems.Subsystem;

public class CommandHub implements Subsystem {
    public static final CommandHub INSTANCE = new CommandHub();
    private CommandHub() {}

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
    public SequentialGroup firePattern() {
        if (Vision.pattern == Vision.BallPattern.GPP) {
            return new SequentialGroup(
                    Flywheel.INSTANCE.setVelocity(Flywheel.INSTANCE.getRequiredVelocity(LaunchTracker.INSTANCE.shootDistance, LaunchTracker.INSTANCE.farLaunch))
                            .and(FidgetTech.INSTANCE.goToArtifact(FidgetTech.artifactColor.GREEN))
                            .then(Ejector.INSTANCE.fire()),
                    Flywheel.INSTANCE.setVelocity(Flywheel.INSTANCE.getRequiredVelocity(LaunchTracker.INSTANCE.shootDistance, LaunchTracker.INSTANCE.farLaunch))
                            .and(FidgetTech.INSTANCE.goToArtifact(FidgetTech.artifactColor.PURPLE))
                            .then(Ejector.INSTANCE.fire()),
                    Flywheel.INSTANCE.setVelocity(Flywheel.INSTANCE.getRequiredVelocity(LaunchTracker.INSTANCE.shootDistance, LaunchTracker.INSTANCE.farLaunch))
                            .and(FidgetTech.INSTANCE.goToArtifact(FidgetTech.artifactColor.PURPLE))
                            .then(Ejector.INSTANCE.fire()),
                    Flywheel.INSTANCE.stopPower()
                    );
        } else if (Vision.pattern == Vision.BallPattern.PGP) {
            return new SequentialGroup(
                    Flywheel.INSTANCE.setVelocity(Flywheel.INSTANCE.getRequiredVelocity(LaunchTracker.INSTANCE.shootDistance, LaunchTracker.INSTANCE.farLaunch))
                            .and(FidgetTech.INSTANCE.goToArtifact(FidgetTech.artifactColor.PURPLE))
                            .then(Ejector.INSTANCE.fire()),
                    Flywheel.INSTANCE.setVelocity(Flywheel.INSTANCE.getRequiredVelocity(LaunchTracker.INSTANCE.shootDistance, LaunchTracker.INSTANCE.farLaunch))
                            .and(FidgetTech.INSTANCE.goToArtifact(FidgetTech.artifactColor.GREEN))
                            .then(Ejector.INSTANCE.fire()),
                    Flywheel.INSTANCE.setVelocity(Flywheel.INSTANCE.getRequiredVelocity(LaunchTracker.INSTANCE.shootDistance, LaunchTracker.INSTANCE.farLaunch))
                            .and(FidgetTech.INSTANCE.goToArtifact(FidgetTech.artifactColor.PURPLE))
                            .then(Ejector.INSTANCE.fire()),
                    Flywheel.INSTANCE.stopPower()
            );
        } else {
            return new SequentialGroup(
                    Flywheel.INSTANCE.setVelocity(Flywheel.INSTANCE.getRequiredVelocity(LaunchTracker.INSTANCE.shootDistance, LaunchTracker.INSTANCE.farLaunch))
                            .and(FidgetTech.INSTANCE.goToArtifact(FidgetTech.artifactColor.PURPLE))
                            .then(Ejector.INSTANCE.fire()),
                    Flywheel.INSTANCE.setVelocity(Flywheel.INSTANCE.getRequiredVelocity(LaunchTracker.INSTANCE.shootDistance, LaunchTracker.INSTANCE.farLaunch))
                            .and(FidgetTech.INSTANCE.goToArtifact(FidgetTech.artifactColor.PURPLE))
                            .then(Ejector.INSTANCE.fire()),
                    Flywheel.INSTANCE.setVelocity(Flywheel.INSTANCE.getRequiredVelocity(LaunchTracker.INSTANCE.shootDistance, LaunchTracker.INSTANCE.farLaunch))
                            .and(FidgetTech.INSTANCE.goToArtifact(FidgetTech.artifactColor.GREEN))
                            .then(Ejector.INSTANCE.fire()),
                    Flywheel.INSTANCE.stopPower()
            );
        }
    }

    @Override
    public void periodic(){
        if(FidgetTech.INSTANCE.spinComplete && (ArtifactSensor.INSTANCE.currentArtifact != FidgetTech.artifactColor.NONE)) {
            FidgetTech.artifactsHeld[FidgetTech.currentSlot] = ArtifactSensor.INSTANCE.currentArtifact;
        }
    }

}
