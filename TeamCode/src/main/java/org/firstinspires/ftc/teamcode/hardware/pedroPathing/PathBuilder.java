package org.firstinspires.ftc.teamcode.hardware.pedroPathing;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.BezierPoint;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.theKeep.TheKeepAuto;

public class PathBuilder {

    private Follower follower;
    private PoseLibrary poses;
    private LaunchZoneTracker tracker;
    public int pathState;

    public void initPathBuilder(Follower follower, PoseLibrary poses) {
        this.follower = follower;
        this.poses = poses;
    }
    public void setPathState(int state) {
        pathState = state;
    }
    public PathChain getAprilTag() {
        return follower.pathBuilder()
                .addPath(new BezierLine(PoseLibrary.startPose, poses.launchPose))
                .setLinearHeadingInterpolation(PoseLibrary.startPose.getHeading(), Math.toRadians(70))
                .build();
    }
    public PathChain scoreArtifact() {
        return follower.pathBuilder()
                .addPath(new BezierLine(follower.getPose(), poses.launchPose))
                .setHeadingInterpolation(HeadingInterpolator.facingPoint(poses.allianceGoalPose))
                .build();
    }
    public PathChain scoreFarArtifact() {
        return follower.pathBuilder()
                .addPath(new BezierLine(follower.getPose(), poses.farLaunchPose))
                .setHeadingInterpolation(HeadingInterpolator.facingPoint(poses.allianceGoalPose))
                .build();
    }
    public PathChain scoreNearArtifact() {
        return follower.pathBuilder()
                .addPath(new BezierLine(follower.getPose(), poses.nearLaunchPose))
                .setHeadingInterpolation(HeadingInterpolator.facingPoint(poses.allianceGoalPose))
                .build();
    }
    public PathChain grabFirstArtifacts() {
        return follower.pathBuilder()
                .addPath(new BezierCurve(poses.launchPose, poses.firstArtifactControlPoint, poses.firstArtifacts))
                .setConstantHeadingInterpolation(poses.firstArtifacts.getHeading())
                .build();
    }

    public PathChain scoreFirstArtifacts() {
        return follower.pathBuilder()
                .addPath(new BezierLine(poses.firstArtifacts, poses.launchPose))
                .setHeadingInterpolation(HeadingInterpolator.facingPoint(poses.allianceGoalPose))
                .build();
    }

    public PathChain grabSecondArtifacts() {
        return follower.pathBuilder()
                .addPath(new BezierCurve(poses.launchPose, poses.secondArtifactControlPoint, poses.secondArtifacts))
                .setConstantHeadingInterpolation(poses.secondArtifacts.getHeading())
                .build();
    }

    public PathChain scoreSecondArtifacts() {
        return follower.pathBuilder()
                .addPath(new BezierLine(poses.secondArtifacts, poses.secondArtifactsShootStep1))
                .setConstantHeadingInterpolation(poses.secondArtifacts.getHeading())
                .addPath(new BezierLine(poses.secondArtifactsShootStep1,poses.nearLaunchPose))
                .setHeadingInterpolation(HeadingInterpolator.facingPoint(poses.allianceGoalPose))
                .build();
    }

    public PathChain leaveLaunchZone() {
        if(TheKeepAuto.artifactsToCollect == 2 || TheKeepAuto.startLocation == 2) {
            return follower.pathBuilder()
                    .addPath(new BezierLine(follower.getPose(), poses.notLaunchZoneNear))
                    .setLinearHeadingInterpolation(follower.getHeading(), poses.notLaunchZoneNear.getHeading())
                    .build();
        } else  return follower.pathBuilder()
                .addPath(new BezierLine(follower.getPose(), poses.notLaunchZoneFar))
                .setLinearHeadingInterpolation(follower.getHeading(), poses.notLaunchZoneFar.getHeading())
                .build();
    }

    public PathChain turnToGoal() {
        return follower.pathBuilder()
                .addPath(new BezierLine(follower.getPose(), follower.getPose()))
                .setHeadingInterpolation(HeadingInterpolator.facingPoint(poses.allianceGoalPose))
                .build();
    }

    public PathChain moveToLaunch() {
        return follower.pathBuilder()
                .addPath(new BezierLine(follower.getPose(), tracker.closestPoseToZone(tracker.robotLaunchZone, poses.nearLaunchPose, poses.farLaunchPose)))
                .setHeadingInterpolation(HeadingInterpolator.facingPoint(poses.allianceGoalPose))
                .build();
    }

    public PathChain returnToBase() {
        return follower.pathBuilder()
                .addPath(new BezierLine(follower.getPose(), poses.baseZone))
                .setLinearHeadingInterpolation(follower.getHeading(), poses.baseZone.getHeading())
                .build();
    }
}

