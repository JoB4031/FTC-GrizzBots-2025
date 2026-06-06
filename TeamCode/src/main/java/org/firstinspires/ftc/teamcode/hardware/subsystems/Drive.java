package org.firstinspires.ftc.teamcode.hardware.subsystems;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.PathChain;
import java.util.function.Supplier;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.ActiveOpMode;

public class Drive implements Subsystem {
    public static final Drive DRIVE = new Drive();
    private Drive() {}

    private Pose autoAimTarget = null;
    private double rotate = 0;
    private final Supplier<Double> rotateSupplier = () -> rotate;

    private final Supplier<Double> normalForward = () -> (double) (ActiveOpMode.gamepad1().left_stick_y);
    private final Supplier<Double> normalStrafe = () -> (double) (ActiveOpMode.gamepad1().left_stick_x);
    private final Supplier<Double> normalRotate = () -> (double) (ActiveOpMode.gamepad1().right_stick_x);

    private final Supplier<Double> slowForward = () -> (double) (ActiveOpMode.gamepad1().left_stick_y*0.5);
    private final Supplier<Double> slowStrafe = () -> (double) (ActiveOpMode.gamepad1().left_stick_x*0.5);
    private final Supplier<Double> slowRotate = () -> (double) (ActiveOpMode.gamepad1().right_stick_x*0.5);

    public PedroDriverControlled normalTeleOpDrive() {
        return new PedroDriverControlled(normalForward, normalStrafe, normalRotate);
    }
    public PedroDriverControlled slowTeleOpDrive() {
        return new PedroDriverControlled(slowForward, slowStrafe, slowRotate);
    }
    public PedroDriverControlled facePointDrive(Pose target) {
        autoAimTarget = target;
        return new PedroDriverControlled(normalForward, normalStrafe, rotateSupplier);
    }

    public FollowPath goTo(Pose pose) {
        PathChain pathToFollow = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(PedroComponent.follower().getPose(), pose.getPose()))
                .setLinearHeadingInterpolation(PedroComponent.follower().getHeading(), pose.getHeading())
                .build();
        return new FollowPath(pathToFollow);
    }
    public FollowPath goTo(Pose pose, Pose target) {
        PathChain pathToFollow = PedroComponent.follower().pathBuilder()
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
        if (autoAimTarget != null) {
            Pose robotPose = PedroComponent.follower().getPose();
            double dx = autoAimTarget.getX() - robotPose.getX();
            double dy = autoAimTarget.getY() - robotPose.getY();
            double targetAngle = Math.atan2(dy, dx);
            double currentHeading = robotPose.getHeading();
            double error = wrapAngle(targetAngle - currentHeading);
            double kP = 2.0;
            rotate = kP * error;
        } else rotate = 0;
        ActiveOpMode.telemetry().addData("Position", PedroComponent.follower().getPose());
    }

    private double wrapAngle(double angle) {
        while (angle > Math.PI) angle -= 2 * Math.PI;
        while (angle < -Math.PI) angle += 2 * Math.PI;
        return angle;
    }
}
