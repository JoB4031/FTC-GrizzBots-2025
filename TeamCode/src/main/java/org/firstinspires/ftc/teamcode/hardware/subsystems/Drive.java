package org.firstinspires.ftc.teamcode.hardware.subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.ftc.Gamepads;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.PathChain;

public class Drive implements Subsystem {
    public static final Drive INSTANCE = new Drive();
    private Drive() {}

    public Command normalTeleOpDrive() {
        return new Command() {
            final PedroDriverControlled teleOpDrive = new PedroDriverControlled(
                    Gamepads.gamepad1().leftStickY().negate(),
                    Gamepads.gamepad1().leftStickX().negate(),
                    Gamepads.gamepad1().rightStickX().negate(),
                    true
            );
            @Override
            public void start() {

                teleOpDrive.start();
            }

            @Override
            public void update() {
                teleOpDrive.update();
            }

            @Override
            public boolean isDone() {
                return false;
            }
        }.requires(this);
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
                        Gamepads.gamepad1().leftStickY().get()*-1,
                        Gamepads.gamepad1().leftStickX().get()*-1,
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
        PathChain pathToFollow = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(PedroComponent.follower().getPose(), pose.getPose()))
                .setLinearHeadingInterpolation(PedroComponent.follower().getHeading(), pose.getHeading())
                .build();
        return new FollowPath(pathToFollow);
    }
    public FollowPath goTo(Pose pose, Pose target) {
        PathChain pathToFollow;
            pathToFollow = PedroComponent.follower().pathBuilder()
                    .addPath(new BezierCurve(PedroComponent.follower().getPose(), pose))
                    .setHeadingInterpolation(HeadingInterpolator.facingPoint(target))
                    .build();

        return new FollowPath(pathToFollow);
    }
    public FollowPath goTo(Pose pose, Pose spline, boolean linearInterpolation) {
        PathChain pathToFollow;
        if (linearInterpolation) {
            pathToFollow = PedroComponent.follower().pathBuilder()
                    .addPath(new BezierCurve(PedroComponent.follower().getPose(), spline, pose))
                    .setLinearHeadingInterpolation(PedroComponent.follower().getHeading(), pose.getHeading())
                    .build();
        } else {
            pathToFollow = PedroComponent.follower().pathBuilder()
                    .addPath(new BezierCurve(PedroComponent.follower().getPose(), spline, pose))
                    .setConstantHeadingInterpolation(pose.getHeading())
                    .build();
        }
        return new FollowPath(pathToFollow);
    }

    @Override
    public void periodic() {
        ActiveOpMode.telemetry().addData("Position", PedroComponent.follower().getPose());
    }

}
