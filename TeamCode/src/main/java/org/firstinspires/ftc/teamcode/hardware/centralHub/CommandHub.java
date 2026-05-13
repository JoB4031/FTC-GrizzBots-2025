package org.firstinspires.ftc.teamcode.hardware.centralHub;

import static org.firstinspires.ftc.teamcode.hardware.pedroPathing.LaunchTracker.LAUNCH_TRACKER;
import static org.firstinspires.ftc.teamcode.hardware.sensors.ArtifactSensor.ARTIFACT_SENSOR;
import static org.firstinspires.ftc.teamcode.hardware.subsystems.Ejector.EJECTOR;
import static org.firstinspires.ftc.teamcode.hardware.subsystems.FidgetTech.FIDGET_TECH;
import static org.firstinspires.ftc.teamcode.hardware.subsystems.Flywheel.FLYWHEEL;
import static org.firstinspires.ftc.teamcode.hardware.subsystems.Intake.INTAKE;
import org.firstinspires.ftc.teamcode.hardware.subsystems.FidgetTech;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Vision;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;

public class CommandHub implements Subsystem {
    public static final CommandHub COMMAND_HUB = new CommandHub();
    private CommandHub() {}

    private boolean automaticIntake = false;
    public Command intakeArtifacts() {
        return new Command() {

            @Override
            public void start() {
                INTAKE.setPower(1, 1).schedule();
                automaticIntake = true;
            }

            @Override
            public void update() {
                if (FidgetTech.artifactsHeld[FidgetTech.currentSlot] != FidgetTech.artifactColor.NONE && FidgetTech.artifactsHeld[FidgetTech.currentSlot] != FidgetTech.artifactColor.UNKNOWN) {
                    FIDGET_TECH.goToArtifact(FidgetTech.artifactColor.NONE).schedule();
                }
                if (FidgetTech.artifactsHeld[FidgetTech.currentSlot] != FidgetTech.artifactColor.NONE) {
                    INTAKE.internalPower(-0.1).schedule();
                } else INTAKE.internalPower(1).schedule();
            }

            @Override
            public boolean isDone() {
                return FIDGET_TECH.fidgetTechFull;
            }

            @Override
            public void stop(boolean interrupted) {
                automaticIntake = false;
                if (!interrupted) {
                    INTAKE.setPower(0,0).schedule();
                }
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
                    FLYWHEEL.setVelocity(FLYWHEEL.getRequiredVelocity(LAUNCH_TRACKER.shootDistance, LAUNCH_TRACKER.farLaunch))
                            .and(FIDGET_TECH.goToArtifact(FidgetTech.artifactColor.GREEN))
                            .then(EJECTOR.fire()),
                    FLYWHEEL.setVelocity(FLYWHEEL.getRequiredVelocity(LAUNCH_TRACKER.shootDistance, LAUNCH_TRACKER.farLaunch))
                            .and(FIDGET_TECH.goToArtifact(FidgetTech.artifactColor.PURPLE))
                            .then(EJECTOR.fire()),
                    FLYWHEEL.setVelocity(FLYWHEEL.getRequiredVelocity(LAUNCH_TRACKER.shootDistance, LAUNCH_TRACKER.farLaunch))
                            .and(FIDGET_TECH.goToArtifact(FidgetTech.artifactColor.PURPLE))
                            .then(EJECTOR.fire()),
                    FLYWHEEL.stopPower()
                    );
        } else if (Vision.pattern == Vision.BallPattern.PGP) {
            return new SequentialGroup(
                    FLYWHEEL.setVelocity(FLYWHEEL.getRequiredVelocity(LAUNCH_TRACKER.shootDistance, LAUNCH_TRACKER.farLaunch))
                            .and(FIDGET_TECH.goToArtifact(FidgetTech.artifactColor.PURPLE))
                            .then(EJECTOR.fire()),
                    FLYWHEEL.setVelocity(FLYWHEEL.getRequiredVelocity(LAUNCH_TRACKER.shootDistance, LAUNCH_TRACKER.farLaunch))
                            .and(FIDGET_TECH.goToArtifact(FidgetTech.artifactColor.GREEN))
                            .then(EJECTOR.fire()),
                    FLYWHEEL.setVelocity(FLYWHEEL.getRequiredVelocity(LAUNCH_TRACKER.shootDistance, LAUNCH_TRACKER.farLaunch))
                            .and(FIDGET_TECH.goToArtifact(FidgetTech.artifactColor.PURPLE))
                            .then(EJECTOR.fire()),
                    FLYWHEEL.stopPower()
            );
        } else {
            return new SequentialGroup(
                    FLYWHEEL.setVelocity(FLYWHEEL.getRequiredVelocity(LAUNCH_TRACKER.shootDistance, LAUNCH_TRACKER.farLaunch))
                            .and(FIDGET_TECH.goToArtifact(FidgetTech.artifactColor.PURPLE))
                            .then(EJECTOR.fire()),
                    FLYWHEEL.setVelocity(FLYWHEEL.getRequiredVelocity(LAUNCH_TRACKER.shootDistance, LAUNCH_TRACKER.farLaunch))
                            .and(FIDGET_TECH.goToArtifact(FidgetTech.artifactColor.PURPLE))
                            .then(EJECTOR.fire()),
                    FLYWHEEL.setVelocity(FLYWHEEL.getRequiredVelocity(LAUNCH_TRACKER.shootDistance, LAUNCH_TRACKER.farLaunch))
                            .and(FIDGET_TECH.goToArtifact(FidgetTech.artifactColor.GREEN))
                            .then(EJECTOR.fire()),
                    FLYWHEEL.stopPower()
            );
        }
    }

    @Override
    public void periodic(){
        if (FIDGET_TECH.spinComplete && ARTIFACT_SENSOR.currentArtifact == FidgetTech.artifactColor.NONE) {
            FidgetTech.artifactsHeld[FidgetTech.currentSlot] = ARTIFACT_SENSOR.currentArtifact;
        } else if (FIDGET_TECH.spinComplete && ARTIFACT_SENSOR.currentArtifact != FidgetTech.artifactColor.UNKNOWN) {
            FidgetTech.artifactsHeld[FidgetTech.currentSlot] = ARTIFACT_SENSOR.currentArtifact;
        }
        ActiveOpMode.telemetry().addData("Automatic Intake", automaticIntake);
        ActiveOpMode.telemetry().update();
    }

}
