package org.firstinspires.ftc.teamcode.hardware.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.BezierPoint;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.function.Supplier;

public class PathFollower {

    public Follower follower;
    public Supplier<PathChain> turnToGoal;
    public Supplier<PathChain> returnToBase;
    public Supplier<PathChain> returnToStart;

    public void init(HardwareMap map, PoseLibrary poses) {
        follower = Constants.createFollower(map);
        follower.setStartingPose(PoseLibrary.startPose);
        follower.update();

        turnToGoal = () -> follower.pathBuilder()
                .addPath(new BezierPoint(follower::getPose))
                .setHeadingInterpolation(HeadingInterpolator.facingPoint(poses.allianceGoalPose))
                .build();

        returnToBase = () -> follower.pathBuilder()
                .addPath(new BezierLine(follower::getPose, poses.baseZone))
                .setLinearHeadingInterpolation(follower.getHeading(), poses.baseZone.getHeading())
                .build();

        returnToStart = () -> follower.pathBuilder()
                .addPath(new BezierLine(follower::getPose, PoseLibrary.trueStart))
                .setLinearHeadingInterpolation(follower.getHeading(), PoseLibrary.trueStart.getHeading())
                .build();
    }
    public void update() {
        follower.update();
    }
    public Pose getPosition() {
        return follower.getPose();
    }
}
