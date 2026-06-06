package org.firstinspires.ftc.teamcode.hardware.centralHub;

import static org.firstinspires.ftc.teamcode.hardware.pedroPathing.PoseLibrary.POSE_LIBRARY;
import static org.firstinspires.ftc.teamcode.hardware.subsystems.Drive.DRIVE;
import static org.firstinspires.ftc.teamcode.hardware.subsystems.Flywheel.FLYWHEEL;
import com.skeletonarmy.marrow.settings.Settings;
import org.firstinspires.ftc.teamcode.tuners.SettingsSetter;
import dev.nextftc.core.commands.groups.SequentialGroup;

public class AutoProgram {
    public static final AutoProgram AUTO_PROGRAM = new AutoProgram();
    AutoProgram() {}

    public SequentialGroup auto() {
        if (Settings.get("start position", SettingsSetter.startLocations.NEAR) == SettingsSetter.startLocations.FAR) {
            if (Settings.get("artifact sets", 2) == 0) {
                return new SequentialGroup(
                        DRIVE.goTo(POSE_LIBRARY.farLaunchPose, POSE_LIBRARY.goal)
                                .and(FLYWHEEL.setVelocity()),
                        FLYWHEEL.waitTillReady(),
                        CommandHub.COMMAND_HUB.firePattern(),
                        DRIVE.goTo(POSE_LIBRARY.notLaunchZone)
                );
            } else if (Settings.get("artifact sets", 2) == 1) {
                return new SequentialGroup(
                        DRIVE.goTo(POSE_LIBRARY.farLaunchPose, POSE_LIBRARY.goal)
                                .and(FLYWHEEL.setVelocity()),
                        FLYWHEEL.waitTillReady(),
                        CommandHub.COMMAND_HUB.firePattern(),
                        (DRIVE.goTo(POSE_LIBRARY.firstArtifacts, POSE_LIBRARY.firstArtifactControlPoint, false)
                                .then((DRIVE.goTo(POSE_LIBRARY.farLaunchPose, POSE_LIBRARY.goal))
                                        .and(FLYWHEEL.setVelocity())))
                                .and(CommandHub.COMMAND_HUB.intakeArtifacts()),
                        FLYWHEEL.waitTillReady(),
                        CommandHub.COMMAND_HUB.firePattern(),
                        DRIVE.goTo(POSE_LIBRARY.notLaunchZone)
                );
            } else {
                return new SequentialGroup(
                        DRIVE.goTo(POSE_LIBRARY.farLaunchPose, POSE_LIBRARY.goal)
                                .and(FLYWHEEL.setVelocity()),
                        FLYWHEEL.waitTillReady(),
                        CommandHub.COMMAND_HUB.firePattern(),
                        (DRIVE.goTo(POSE_LIBRARY.firstArtifacts, POSE_LIBRARY.firstArtifactControlPoint, false)
                                .then((DRIVE.goTo(POSE_LIBRARY.farLaunchPose, POSE_LIBRARY.goal))
                                        .and(FLYWHEEL.setVelocity())))
                                .and(CommandHub.COMMAND_HUB.intakeArtifacts()),
                        FLYWHEEL.waitTillReady(),
                        CommandHub.COMMAND_HUB.firePattern(),
                        (DRIVE.goTo(POSE_LIBRARY.secondArtifacts, POSE_LIBRARY.secondArtifactControlPoint, true)
                                .then((DRIVE.goTo(POSE_LIBRARY.nearLaunchPose, POSE_LIBRARY.goal))
                                        .and(FLYWHEEL.setVelocity())))
                                .and(CommandHub.COMMAND_HUB.intakeArtifacts()),
                        FLYWHEEL.waitTillReady(),
                        CommandHub.COMMAND_HUB.firePattern(),
                        DRIVE.goTo(POSE_LIBRARY.notLaunchZone)
                );
            }
        } else {
            if (Settings.get("artifact sets", 2) == 0) {
                return new SequentialGroup(DRIVE.goTo(POSE_LIBRARY.nearLaunchPose, POSE_LIBRARY.goal)
                        .and(FLYWHEEL.setVelocity()),
                        FLYWHEEL.waitTillReady(),
                        CommandHub.COMMAND_HUB.firePattern(),
                        DRIVE.goTo(POSE_LIBRARY.notLaunchZone)
                );
            } else if (Settings.get("artifact sets", 2) == 1) {
                return new SequentialGroup(DRIVE.goTo(POSE_LIBRARY.nearLaunchPose, POSE_LIBRARY.goal)
                        .and(FLYWHEEL.setVelocity()),
                        FLYWHEEL.waitTillReady(),
                        CommandHub.COMMAND_HUB.firePattern(),
                        (DRIVE.goTo(POSE_LIBRARY.firstArtifacts, POSE_LIBRARY.firstArtifactControlPoint, false)
                                .then((DRIVE.goTo(POSE_LIBRARY.nearLaunchPose, POSE_LIBRARY.goal))
                                        .and(FLYWHEEL.setVelocity())))
                                .and(CommandHub.COMMAND_HUB.intakeArtifacts()),
                        FLYWHEEL.waitTillReady(),
                        CommandHub.COMMAND_HUB.firePattern(),
                        DRIVE.goTo(POSE_LIBRARY.notLaunchZone)
                );
            } else {
                return new SequentialGroup(DRIVE.goTo(POSE_LIBRARY.nearLaunchPose, POSE_LIBRARY.goal)
                        .and(FLYWHEEL.setVelocity()),
                        FLYWHEEL.waitTillReady(),
                        CommandHub.COMMAND_HUB.firePattern(),
                        (DRIVE.goTo(POSE_LIBRARY.firstArtifacts, POSE_LIBRARY.firstArtifactControlPoint, false)
                                .then((DRIVE.goTo(POSE_LIBRARY.nearLaunchPose, POSE_LIBRARY.goal))
                                        .and(FLYWHEEL.setVelocity())))
                                .and(CommandHub.COMMAND_HUB.intakeArtifacts()),
                        FLYWHEEL.waitTillReady(),
                        CommandHub.COMMAND_HUB.firePattern(),
                        (DRIVE.goTo(POSE_LIBRARY.secondArtifacts, POSE_LIBRARY.secondArtifactControlPoint, true)
                                .then((DRIVE.goTo(POSE_LIBRARY.nearLaunchPose, POSE_LIBRARY.goal))
                                        .and(FLYWHEEL.setVelocity())))
                                .and(CommandHub.COMMAND_HUB.intakeArtifacts()),
                        FLYWHEEL.waitTillReady(),
                        CommandHub.COMMAND_HUB.firePattern(),
                        DRIVE.goTo(POSE_LIBRARY.notLaunchZone)
                );
            }
        }
    }
}