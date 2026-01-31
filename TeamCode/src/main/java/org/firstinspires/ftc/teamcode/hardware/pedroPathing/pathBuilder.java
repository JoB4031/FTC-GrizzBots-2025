package org.firstinspires.ftc.teamcode.hardware.pedroPathing;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.PathChain;
import org.firstinspires.ftc.teamcode.theKeep.TheKeepAuto;

public class pathBuilder {

    private Follower follower;
    private poseLibrary poses;
    public int pathState;

    public void initPathBuilder(Follower follower, poseLibrary poses) {
        this.follower = follower;
        this.poses = poses;
    }
    public void setPathState(int state) {
        pathState = state;
    }
    public PathChain scoreArtifact() {
        return follower.pathBuilder()
                .addPath(new BezierLine(poseLibrary.startPose, poses.launchPose))
                .setHeadingInterpolation(HeadingInterpolator.facingPoint(poses.allianceGoalPose))
                .build();
    }

    public PathChain grabFirstArtifacts() {
        return follower.pathBuilder()
                .addPath(new BezierLine(poses.launchPose, poses.firstArtifacts))
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
        Pose mid = (TheKeepAuto.alliance == TheKeepAuto.Alliance.BLUE)
                ? new Pose(76.5, 67)
                : new Pose(67.5, 67);

        return follower.pathBuilder()
                .addPath(new BezierCurve(poses.launchPose, mid, poses.secondArtifacts))
                .setConstantHeadingInterpolation(poses.secondArtifacts.getHeading())
                .build();
    }

    public PathChain scoreSecondArtifacts() {
        return follower.pathBuilder()
                .addPath(new BezierLine(poses.secondArtifacts, poses.nearLaunchPose))
                .setHeadingInterpolation(HeadingInterpolator.facingPoint(poses.allianceGoalPose))
                .build();
    }

    public PathChain leaveLaunchZone() {
        return follower.pathBuilder()
                .addPath(new BezierLine(poses.launchPose, poses.notLaunchZone))
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();
    }
}

