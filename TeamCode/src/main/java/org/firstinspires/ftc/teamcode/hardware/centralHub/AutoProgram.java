package org.firstinspires.ftc.teamcode.hardware.centralHub;

import org.firstinspires.ftc.teamcode.hardware.pedroPathing.LaunchTracker;
import org.firstinspires.ftc.teamcode.hardware.pedroPathing.PoseLibrary;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Drive;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Ejector;
import org.firstinspires.ftc.teamcode.hardware.subsystems.FidgetTech;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Intake;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.SequentialGroup;

public class AutoProgram {
    public static final AutoProgram INSTANCE = new AutoProgram();
    AutoProgram() {}



    public SequentialGroup farAuto = new SequentialGroup(
            Drive.INSTANCE.goTo(PoseLibrary.INSTANCE.farLaunchPose, PoseLibrary.INSTANCE.goal)
                    .and(Flywheel.INSTANCE.setVelocity(Flywheel.INSTANCE.getRequiredVelocity(LaunchTracker.INSTANCE.shootDistance, LaunchTracker.INSTANCE.farLaunch))),
            CommandHub.INSTANCE.firePattern(),
            (Drive.INSTANCE.goTo(PoseLibrary.INSTANCE.firstArtifacts, PoseLibrary.INSTANCE.firstArtifactControlPoint, false)
                    .then((Drive.INSTANCE.goTo(PoseLibrary.INSTANCE.farLaunchPose, PoseLibrary.INSTANCE.goal))
                            .and(Flywheel.INSTANCE.setVelocity(Flywheel.INSTANCE.getRequiredVelocity(LaunchTracker.INSTANCE.shootDistance, LaunchTracker.INSTANCE.farLaunch)))))
                    .and(CommandHub.INSTANCE.intakeArtifacts()),
            CommandHub.INSTANCE.firePattern(),
            (Drive.INSTANCE.goTo(PoseLibrary.INSTANCE.secondArtifacts, PoseLibrary.INSTANCE.secondArtifactControlPoint, true)
                    .then((Drive.INSTANCE.goTo(PoseLibrary.INSTANCE.nearLaunchPose, PoseLibrary.INSTANCE.goal))
                        .and(Flywheel.INSTANCE.setVelocity(Flywheel.INSTANCE.getRequiredVelocity(LaunchTracker.INSTANCE.shootDistance, LaunchTracker.INSTANCE.farLaunch)))))
                    .and(CommandHub.INSTANCE.intakeArtifacts()),
            CommandHub.INSTANCE.firePattern(),
            (Drive.INSTANCE.goTo(PoseLibrary.INSTANCE.thirdArtifacts, PoseLibrary.INSTANCE.thirdArtifacts, false)
                    .then((Drive.INSTANCE.goTo(PoseLibrary.INSTANCE.nearLaunchPose, PoseLibrary.INSTANCE.goal))
                        .and(Flywheel.INSTANCE.setVelocity(Flywheel.INSTANCE.getRequiredVelocity(LaunchTracker.INSTANCE.shootDistance, LaunchTracker.INSTANCE.farLaunch)))))
                    .and(CommandHub.INSTANCE.intakeArtifacts()),
            CommandHub.INSTANCE.firePattern(),
            Drive.INSTANCE.goTo(PoseLibrary.INSTANCE.notLaunchZone)
    );
}