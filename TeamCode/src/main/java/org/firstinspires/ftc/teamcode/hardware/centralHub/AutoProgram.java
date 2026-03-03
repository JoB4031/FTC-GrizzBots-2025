package org.firstinspires.ftc.teamcode.hardware.centralHub;

import org.firstinspires.ftc.teamcode.hardware.pedroPathing.PoseLibrary;
import org.firstinspires.ftc.teamcode.hardware.subsystems.DriveChainSubsystem;
import org.firstinspires.ftc.teamcode.hardware.subsystems.FlywheelSubsystem;

import dev.nextftc.core.commands.groups.SequentialGroup;

public class AutoProgram {
    public static final AutoProgram INSTANCE = new AutoProgram();
    AutoProgram() {}
    DriveChainSubsystem driveChain = DriveChainSubsystem.INSTANCE;
    FlywheelSubsystem cannon = FlywheelSubsystem.INSTANCE;
    PoseLibrary poses = PoseLibrary.INSTANCE;
    public SequentialGroup auto = new SequentialGroup(
            driveChain.goTo(poses.launchPose)
                    .and(cannon.setVelocity(3500)),
            CommandHub.INSTANCE.firePattern(),
            driveChain.goTo(poses.topArtifacts)
                    .and(CommandHub.INSTANCE.intakeOneArtifact
                            .then(CommandHub.INSTANCE.intakeOneArtifact)
                            .then(CommandHub.INSTANCE.intakeOneArtifact)),
            driveChain.goTo(poses.launchPose).and(cannon.setVelocity(3500)),
            CommandHub.INSTANCE.firePattern(),
            driveChain.goTo(poses.middleArtifacts)
                    .and(CommandHub.INSTANCE.intakeOneArtifact
                            .then(CommandHub.INSTANCE.intakeOneArtifact)
                            .then(CommandHub.INSTANCE.intakeOneArtifact)),
            driveChain.goTo(poses.launchPose).and(cannon.setVelocity(3500)),
            CommandHub.INSTANCE.firePattern()
    );
}