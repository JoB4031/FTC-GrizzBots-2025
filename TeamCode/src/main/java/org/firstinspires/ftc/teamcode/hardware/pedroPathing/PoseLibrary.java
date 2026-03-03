package org.firstinspires.ftc.teamcode.hardware.pedroPathing;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
import com.skeletonarmy.marrow.settings.Settings;
import com.skeletonarmy.marrow.zones.Point;
import org.firstinspires.ftc.teamcode.theKeep.TheKeepAuto;
import org.firstinspires.ftc.teamcode.tuners.SettingsSetter;

import dev.nextftc.core.subsystems.Subsystem;

@Configurable
public class PoseLibrary implements Subsystem {
    public static final PoseLibrary INSTANCE = new PoseLibrary();
    private PoseLibrary() {}

    public static Pose startPose;
    public static Pose trueStart;
    public Pose allianceGoalPose = new Pose(7.5, 140);

    public Pose baseZone = new Pose(100.2, 31.9, Math.toRadians(88));

    public Pose alliance1 = new Pose(65.4, 9, Math.toRadians(90));
    public Pose alliance2 = new Pose(44.8, 140.8, Math.toRadians(180));

    public Pose nearLaunchPose = new Pose(57, 91.5, Math.toRadians(135));
    public Pose farLaunchPose = new Pose(61, 18.6, Math.toRadians(111.5));
    public Pose notLaunchZoneNear = new Pose(30, 120, Math.toRadians(-135));
    public Pose notLaunchZoneFar = new Pose(40, 15, Math.toRadians(90));

    public Pose topArtifacts = new Pose(32, 91.5, Math.toRadians(180));
    public Pose middleArtifacts = new Pose(22, 67, Math.toRadians(180));
    public Pose bottomArtifacts = new Pose(22, 45, Math.toRadians(180));

    public Pose launchPose;
    public Pose firstArtifacts;
    public Pose firstArtifactControlPoint = new Pose(76.5,45);
    public Pose secondArtifacts;
    public Pose secondArtifactControlPoint = new Pose(76.5, 67);
    public Pose secondArtifactsShootStep1 = new Pose(60,67);
    public Pose thirdArtifacts;

    public static Point targetPoint = new Point(16, 131);

    public void assignStartPose() {
        startPose = (Settings.get("start position", SettingsSetter.startLocations.FAR) == SettingsSetter.startLocations.NEAR) ? alliance1 : alliance2;
        if (Settings.get("alliance", SettingsSetter.alliance.BLUE) == SettingsSetter.alliance.RED) {
            startPose = startPose.mirror();
        }
        trueStart = (Settings.get("start position", SettingsSetter.startLocations.FAR) == SettingsSetter.startLocations.NEAR) ? alliance1 : alliance2;
        if (Settings.get("alliance", SettingsSetter.alliance.BLUE) == SettingsSetter.alliance.RED) {
            trueStart = trueStart.mirror();
        }
    }
    public void configureAlliancePaths() {
        targetPoint = new Point(16, 131);
        if (Settings.get("start position", SettingsSetter.startLocations.NEAR) == SettingsSetter.startLocations.FAR) {
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

        if (Settings.get("alliance", SettingsSetter.alliance.BLUE) == SettingsSetter.alliance.RED) {
            launchPose = launchPose.mirror();
            nearLaunchPose = nearLaunchPose.mirror();

            allianceGoalPose = allianceGoalPose.mirror();
            notLaunchZoneNear = notLaunchZoneNear.mirror();
            notLaunchZoneFar = notLaunchZoneFar.mirror();
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

    @Override
    public void initialize() {
        configureAlliancePaths();
    }
}

