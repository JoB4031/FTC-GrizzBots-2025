package org.firstinspires.ftc.teamcode.hardware.subsystems;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.hardware.driving.DriverControlledCommand;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

public class DriveChainSubsystem implements Subsystem {
    public static final DriveChainSubsystem INSTANCE = new DriveChainSubsystem();
    private DriveChainSubsystem() {}
    public DriverControlledCommand driverControlled = new PedroDriverControlled(
            Gamepads.gamepad1().leftStickY(),
            Gamepads.gamepad1().leftStickX(),
            Gamepads.gamepad1().rightStickX(),
            false
    );

    public FollowPath goTo(Pose pose) {
        Follower pedro = PedroComponent.follower();
        PathChain pathToFollow = pedro.pathBuilder()
                .addPath(new BezierLine(pedro.getPose(), pose.getPose()))
                .setLinearHeadingInterpolation(pedro.getHeading(), pose.getHeading())
                .build();
        return new FollowPath(pathToFollow);
    }

}
