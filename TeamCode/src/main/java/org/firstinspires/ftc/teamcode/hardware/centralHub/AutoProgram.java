package org.firstinspires.ftc.teamcode.hardware.centralHub;

import static org.firstinspires.ftc.teamcode.hardware.pedroPathing.LaunchTracker.LAUNCH_TRACKER;
import static org.firstinspires.ftc.teamcode.hardware.pedroPathing.PoseLibrary.POSE_LIBRARY;
import static org.firstinspires.ftc.teamcode.hardware.subsystems.Drive.DRIVE;
import static org.firstinspires.ftc.teamcode.hardware.subsystems.Flywheel.FLYWHEEL;
import dev.nextftc.core.commands.groups.SequentialGroup;

public class AutoProgram {
    public static final AutoProgram AUTO_PROGRAM = new AutoProgram();
    AutoProgram() {}



    public SequentialGroup farAuto = new SequentialGroup(
            DRIVE.goTo(POSE_LIBRARY.farLaunchPose, POSE_LIBRARY.goal)
                    .and(FLYWHEEL.setVelocity(FLYWHEEL.getRequiredVelocity(LAUNCH_TRACKER.shootDistance, LAUNCH_TRACKER.farLaunch))),
            CommandHub.COMMAND_HUB.firePattern(),
            (DRIVE.goTo(POSE_LIBRARY.firstArtifacts, POSE_LIBRARY.firstArtifactControlPoint, false)
                    .then((DRIVE.goTo(POSE_LIBRARY.farLaunchPose, POSE_LIBRARY.goal))
                            .and(FLYWHEEL.setVelocity(FLYWHEEL.getRequiredVelocity(LAUNCH_TRACKER.shootDistance, LAUNCH_TRACKER.farLaunch)))))
                    .and(CommandHub.COMMAND_HUB.intakeArtifacts()),
            CommandHub.COMMAND_HUB.firePattern(),
            (DRIVE.goTo(POSE_LIBRARY.secondArtifacts, POSE_LIBRARY.secondArtifactControlPoint, true)
                    .then((DRIVE.goTo(POSE_LIBRARY.nearLaunchPose, POSE_LIBRARY.goal))
                        .and(FLYWHEEL.setVelocity(FLYWHEEL.getRequiredVelocity(LAUNCH_TRACKER.shootDistance, LAUNCH_TRACKER.farLaunch)))))
                    .and(CommandHub.COMMAND_HUB.intakeArtifacts()),
            CommandHub.COMMAND_HUB.firePattern(),
            (DRIVE.goTo(POSE_LIBRARY.thirdArtifacts, POSE_LIBRARY.thirdArtifacts, false)
                    .then((DRIVE.goTo(POSE_LIBRARY.nearLaunchPose, POSE_LIBRARY.goal))
                        .and(FLYWHEEL.setVelocity(FLYWHEEL.getRequiredVelocity(LAUNCH_TRACKER.shootDistance, LAUNCH_TRACKER.farLaunch)))))
                    .and(CommandHub.COMMAND_HUB.intakeArtifacts()),
            CommandHub.COMMAND_HUB.firePattern(),
            DRIVE.goTo(POSE_LIBRARY.notLaunchZone)
    );
}