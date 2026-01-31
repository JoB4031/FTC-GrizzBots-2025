package org.firstinspires.ftc.teamcode.hardware.pedroPathing;

import com.pedropathing.geometry.Pose;
import com.skeletonarmy.marrow.zones.Point;
import org.firstinspires.ftc.teamcode.theKeep.TheKeepAuto;

public class poseLibrary {

    public static Pose startPose;
    public Pose allianceGoalPose = new Pose(10, 140);
    public Pose baseZone = new Pose(39, 33.8, Math.toRadians(90));

    public Pose alliance1 = new Pose(63, 9, Math.toRadians(90));
    public Pose alliance2 = new Pose(44.8, 140.8, Math.toRadians(180));

    public Pose nearLaunchPose = new Pose(57, 91.5, Math.toRadians(135));
    public Pose farLaunchPose = new Pose(61, 18.6, Math.toRadians(111.5));
    public Pose notLaunchZone = new Pose(45, 60, Math.toRadians(180));

    public Pose topArtifacts = new Pose(35, 91.5, Math.toRadians(180));
    public Pose middleArtifacts = new Pose(35, 67, Math.toRadians(180));
    public Pose bottomArtifacts = new Pose(35, 40, Math.toRadians(180));

    public Pose launchPose;
    public Pose firstArtifacts;
    public Pose secondArtifacts;

    public Point targetPoint = new Point(16, 131);

    public void assignStartPose() {
        startPose = (TheKeepAuto.startLocation == 1) ? alliance1 : alliance2;
        if (TheKeepAuto.alliance == TheKeepAuto.Alliance.RED) {
            startPose = startPose.mirror();
        }
    }
    public void saveStartPose(pathFollower pathFollower) {
        startPose = pathFollower.getPosition();
    }
    public void configureAlliancePaths() {
        if (TheKeepAuto.startLocation == 1) {
            launchPose = farLaunchPose;
            firstArtifacts = bottomArtifacts;
        } else {
            launchPose = nearLaunchPose;
            firstArtifacts = topArtifacts;
        }

        secondArtifacts = middleArtifacts;

        if (TheKeepAuto.alliance == TheKeepAuto.Alliance.RED) {
            launchPose = launchPose.mirror();
            nearLaunchPose = nearLaunchPose.mirror();
            allianceGoalPose = allianceGoalPose.mirror();
            notLaunchZone = notLaunchZone.mirror();
            firstArtifacts = firstArtifacts.mirror();
            secondArtifacts = secondArtifacts.mirror();
            baseZone = baseZone.mirror();

            targetPoint = new Point(((72 - targetPoint.getX()) + 72), targetPoint.getY());
        }
    }
}

