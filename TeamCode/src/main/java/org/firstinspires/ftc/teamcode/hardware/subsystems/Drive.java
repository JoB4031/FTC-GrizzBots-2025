package org.firstinspires.ftc.teamcode.hardware.subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.hardware.driving.DriverControlledCommand;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

public class Drive implements Subsystem {
    public static final Drive INSTANCE = new Drive();
    public boolean normalDrive = true;
    private Drive() {}
    public DriverControlledCommand normalTeleOpDrive() {
        normalDrive = true;
        return new PedroDriverControlled(
                Gamepads.gamepad1().leftStickY(),
                Gamepads.gamepad1().leftStickX(),
                Gamepads.gamepad1().rightStickX(),
                false
        );
    }
    public DriverControlledCommand slowTeleOpDrive() {
        normalDrive = false;
        return new PedroDriverControlled(
                () -> Gamepads.gamepad1().leftStickY().get() * 0.5,
                () -> Gamepads.gamepad1().leftStickX().get() * 0.5,
                () -> Gamepads.gamepad1().rightStickX().get() * 0.5,
                false
        );
    }
    public DriverControlledCommand resumeTeleOpDrive() {
        if(normalDrive) {
            return normalTeleOpDrive();
        } else return slowTeleOpDrive();
    }
    public Command facePointDrive(Pose target) {
        return new Command() {

            @Override
            public void update() {
                Pose robotPose = PedroComponent.follower().getPose();

                double dx = target.getX() - robotPose.getX();
                double dy = target.getY() - robotPose.getY();

                // Angle robot should face
                double targetAngle = Math.atan2(dy, dx);

                // Current robot heading
                double currentHeading = robotPose.getHeading();

                // Heading error wrapped to -π..π
                double error = wrapAngle(targetAngle - currentHeading);

                // Simple proportional controller for rotation
                double kP = 2.0; // tune this
                double rotationPower = kP * error;

                // Drive with joystick, rotation locked
                PedroComponent.follower().setTeleOpDrive(
                        Gamepads.gamepad1().leftStickY().get(),
                        Gamepads.gamepad1().leftStickX().get(),
                        rotationPower
                );
            }

            @Override
            public boolean isDone() {
                return false; // runs until canceled
            }
            private double wrapAngle(double angle) {
                while (angle > Math.PI) angle -= 2 * Math.PI;
                while (angle < -Math.PI) angle += 2 * Math.PI;
                return angle;
            }
        }.requires(this);
    }

    public FollowPath goTo(Pose pose) {
        Follower pedro = PedroComponent.follower();
        PathChain pathToFollow = pedro.pathBuilder()
                .addPath(new BezierLine(pedro.getPose(), pose.getPose()))
                .setLinearHeadingInterpolation(pedro.getHeading(), pose.getHeading())
                .build();
        return new FollowPath(pathToFollow);
    }
    public FollowPath goTo(Pose pose, Pose spline, boolean linearInterpolation) {
        Follower pedro = PedroComponent.follower();
        PathChain pathToFollow;
        if (linearInterpolation) {
            pathToFollow = pedro.pathBuilder()
                    .addPath(new BezierCurve(pedro.getPose(), spline, pose))
                    .setLinearHeadingInterpolation(pedro.getHeading(), pose.getHeading())
                    .build();
        } else {
            pathToFollow = pedro.pathBuilder()
                    .addPath(new BezierCurve(pedro.getPose(), spline, pose))
                    .setConstantHeadingInterpolation(pose.getHeading())
                    .build();
        }
        return new FollowPath(pathToFollow);
    }

}
