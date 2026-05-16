package org.firstinspires.ftc.teamcode.hardware.subsystems;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.PathChain;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.ftc.Gamepads;

public class Drive implements Subsystem {

    public static final Drive DRIVE = new Drive();
    private Drive() {}

    // -----------------------------
    //  STATE FLAGS
    // -----------------------------
    private boolean slowMode = false;
    private boolean autoAim = false;
    private Pose autoAimTarget = null;
    private double forward = 0;
    private double strafe = 0;
    private double rotate = 0;
    public Command teleOpDrive() {
        return new Command() {
            @Override
            public void update() {
                PedroComponent.follower().setTeleOpDrive(forward, strafe, rotate);
            }

            @Override
            public boolean isDone() {
                return false; // runs forever
            }
        }.requires(this);
    }

    // -----------------------------
    //  SLOW MODE COMMANDS
    // -----------------------------
    public Command slowTeleOpDrive() {
        return new Command() {
            @Override
            public void start() {
                slowMode = true;
            }
            @Override
            public boolean isDone() { return true; }
        };
    }

    public Command normalTeleOpDrive() {
        return new Command() {
            @Override
            public void start() {
                slowMode = false;
            }
            @Override
            public boolean isDone() { return true; }
        };
    }

    // -----------------------------
    //  AUTO-AIM COMMAND
    // -----------------------------
    public Command facePointDrive(Pose target) {
        return new Command() {
            @Override
            public void start() {
                autoAim = true;
                autoAimTarget = target;
            }

            @Override
            public void stop(boolean interrupted) {
                autoAim = false;
                autoAimTarget = null;
            }

            @Override
            public boolean isDone() { return false; }
        };
    }

    // -----------------------------
    //  AUTONOMOUS PATH COMMANDS
    // -----------------------------
    public Command goTo(Pose pose) {
        PathChain pathToFollow = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(PedroComponent.follower().getPose(), pose.getPose()))
                .setLinearHeadingInterpolation(PedroComponent.follower().getHeading(), pose.getHeading())
                .build();
        return new FollowPath(pathToFollow).requires(this);
    }

    public Command goTo(Pose pose, Pose target) {
        PathChain pathToFollow = PedroComponent.follower().pathBuilder()
                .addPath(new BezierCurve(PedroComponent.follower().getPose(), pose))
                .setHeadingInterpolation(HeadingInterpolator.facingPoint(target))
                .build();
        return new FollowPath(pathToFollow).requires(this);
    }

    public Command goTo(Pose pose, Pose spline, boolean linearInterpolation) {
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
        return new FollowPath(pathToFollow).requires(this);
    }

    // -----------------------------
    //  MAIN DRIVE LOOP
    // -----------------------------
    @Override
    public void periodic() {

        forward = Gamepads.gamepad1().leftStickY().negate().get();
        strafe  = Gamepads.gamepad1().leftStickX().negate().get();
        rotate  = Gamepads.gamepad1().rightStickX().negate().get();

        // Apply slow mode
        if (slowMode) {
            forward *= 0.4;
            strafe  *= 0.4;
            rotate  *= 0.4;
        }

        // Auto-aim overrides rotation
        if (autoAim && autoAimTarget != null) {

            Pose robotPose = PedroComponent.follower().getPose();

            double dx = autoAimTarget.getX() - robotPose.getX();
            double dy = autoAimTarget.getY() - robotPose.getY();

            double targetAngle = Math.atan2(dy, dx);
            double currentHeading = robotPose.getHeading();

            double error = wrapAngle(targetAngle - currentHeading);

            double kP = 2.0;
            rotate = kP * error;
        }

        ActiveOpMode.telemetry().addData("Position", PedroComponent.follower().getPose());
    }

    private double wrapAngle(double angle) {
        while (angle > Math.PI) angle -= 2 * Math.PI;
        while (angle < -Math.PI) angle += 2 * Math.PI;
        return angle;
    }
}
