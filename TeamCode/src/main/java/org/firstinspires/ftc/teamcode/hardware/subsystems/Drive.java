package org.firstinspires.ftc.teamcode.hardware.subsystems;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.core.units.Angle;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.extensions.pedro.TurnBy;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.hardware.driving.DriverControlledCommand;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

public class Drive implements Subsystem {
    public static final Drive INSTANCE = new Drive();
    private Drive() {}
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

    public TurnBy turnToTarget(Pose target) {
        Pose currentPose = PedroComponent.follower().getPose();
        return new TurnBy(Angle.fromDeg(rotationToFacePoint(target)));
    }
    public static double rotationToFacePoint(Pose target) {
        double px = PedroComponent.follower().getPose().getX();
        double py = PedroComponent.follower().getPose().getY();
        double heading = PedroComponent.follower().getHeading();

        double fx = target.getX();
        double fy = target.getX();
        // Vector from moving point to fixed point
        double dx = fx - px;
        double dy = fy - py;

        // Angle from moving point to fixed point
        double targetAngle = Math.atan2(dy, dx);

        // Difference between target angle and current heading
        double delta = targetAngle - heading;

        // Normalize to [-π, π]
        delta = Math.atan2(Math.sin(delta), Math.cos(delta));

        return delta;
    }



}
