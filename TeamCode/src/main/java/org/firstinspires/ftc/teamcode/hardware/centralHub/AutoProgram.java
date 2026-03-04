package org.firstinspires.ftc.teamcode.hardware.centralHub;

import org.firstinspires.ftc.teamcode.hardware.pedroPathing.PoseLibrary;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Drive;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Flywheel;

import dev.nextftc.core.commands.groups.SequentialGroup;

public class AutoProgram {
    public static final AutoProgram INSTANCE = new AutoProgram();
    AutoProgram() {}
    Drive driveChain = Drive.INSTANCE;
    Flywheel cannon = Flywheel.INSTANCE;
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