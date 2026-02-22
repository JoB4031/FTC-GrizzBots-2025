package org.firstinspires.ftc.teamcode.hardware.pedroPathing;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
import com.skeletonarmy.marrow.zones.Point;
import org.firstinspires.ftc.teamcode.theKeep.TheKeepAuto;
@Configurable
public class PoseLibrary {

    public static Pose startPose;
    public static Pose trueStart;
    public static Pose allianceGoalPose = new Pose(5, 150);
    public Pose baseZone = new Pose(100.2, 31.9, Math.toRadians(88));

    public Pose alliance1 = new Pose(63, 9, Math.toRadians(90));
    public Pose alliance2 = new Pose(44.8, 140.8, Math.toRadians(180));

    public Pose nearLaunchPose = new Pose(57, 91.5, Math.toRadians(135));
    public Pose farLaunchPose = new Pose(61, 18.6, Math.toRadians(111.5));
    public Pose notLaunchZone = new Pose(30, 120, Math.toRadians(-135));

    public Pose nearLaunch1Pose = new Pose(57,91.5);
    public Pose nearLaunch2Pose = new Pose(57, 91.5);
    public Pose nearLaunch3Pose = new Pose(57, 91.5);
    public Pose nearLaunch4Pose = new Pose(57, 91.5);
    public Pose nearLaunch5Pose = new Pose(57, 91.5);
    public Pose nearLaunch6Pose = nearLaunch1Pose.mirror();
    public Pose nearLaunch7Pose = nearLaunch2Pose.mirror();
    public Pose nearLaunch8Pose = nearLaunch3Pose.mirror();
    public Pose nearLaunch9Pose = nearLaunch4Pose.mirror();
    public Pose nearLaunch10Pose = nearLaunch5Pose.mirror();

    public Pose topArtifacts = new Pose(32, 91.5, Math.toRadians(180));
    public Pose middleArtifacts = new Pose(20, 67, Math.toRadians(180));
    public Pose bottomArtifacts = new Pose(20, 40, Math.toRadians(180));

    public Pose launchPose;
    public Pose firstArtifacts;
    public Pose firstArtifactControlPoint = new Pose(76.5,35);
    public Pose secondArtifacts;
    public Pose secondArtifactControlPoint = new Pose(76.5, 67);
    public Pose secondArtifactsShootStep1 = new Pose(65,67);
    public Pose thirdArtifacts;

    public Point targetPoint = new Point(16, 131);

    public void assignStartPose() {
        startPose = (TheKeepAuto.startLocation == 1) ? alliance1 : alliance2;
        if (TheKeepAuto.alliance == TheKeepAuto.Alliance.RED) {
            startPose = startPose.mirror();
        }
        trueStart = (TheKeepAuto.startLocation == 1) ? alliance1 : alliance2;
        if (TheKeepAuto.alliance == TheKeepAuto.Alliance.RED) {
            trueStart = trueStart.mirror();
        }
    }
    public void saveStartPose(PathFollower pathFollower) {
        startPose = pathFollower.getPosition();
    }
    public void configureAlliancePaths() {
        if (TheKeepAuto.startLocation == 1) {
            launchPose = farLaunchPose;
            firstArtifacts = bottomArtifacts;
            thirdArtifacts = topArtifacts;
        } else {
            launchPose = nearLaunchPose;
            firstArtifacts = topArtifacts;
            firstArtifactControlPoint = new Pose(firstArtifacts.getX(), firstArtifacts.getY());
            thirdArtifacts = bottomArtifacts;
        }

        secondArtifacts = middleArtifacts;

        if (TheKeepAuto.alliance == TheKeepAuto.Alliance.RED) {
            launchPose = launchPose.mirror();
            nearLaunchPose = nearLaunchPose.mirror();
            nearLaunch1Pose = nearLaunch1Pose.mirror();
            nearLaunch2Pose = nearLaunch2Pose.mirror();
            nearLaunch3Pose = nearLaunch3Pose.mirror();
            nearLaunch4Pose = nearLaunch4Pose.mirror();
            nearLaunch5Pose = nearLaunch5Pose.mirror();
            nearLaunch6Pose = nearLaunch6Pose.mirror();
            nearLaunch7Pose = nearLaunch7Pose.mirror();
            nearLaunch8Pose = nearLaunch8Pose.mirror();
            nearLaunch9Pose = nearLaunch9Pose.mirror();
            nearLaunch10Pose = nearLaunch10Pose.mirror();

            allianceGoalPose = allianceGoalPose.mirror();
            notLaunchZone = notLaunchZone.mirror();
            firstArtifacts = firstArtifacts.mirror();
            firstArtifactControlPoint = firstArtifactControlPoint.mirror();
            secondArtifacts = secondArtifacts.mirror();
            secondArtifactControlPoint = secondArtifactControlPoint.mirror();
            secondArtifactsShootStep1 = secondArtifactsShootStep1.mirror();
            thirdArtifacts = thirdArtifacts.mirror();

            baseZone = baseZone.mirror();
            targetPoint = new Point(((72 - targetPoint.getX()) + 72), targetPoint.getY());
        }
    }
}

