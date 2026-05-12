package org.firstinspires.ftc.teamcode.hardware.pedroPathing;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.skeletonarmy.marrow.settings.Settings;
import org.firstinspires.ftc.teamcode.tuners.SettingsSetter;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.ActiveOpMode;

public class PoseLibrary implements Subsystem {
    public static final PoseLibrary INSTANCE = new PoseLibrary();
    private PoseLibrary() {}

    public static Pose startPose;
    public Pose blueFarStart = new Pose(65.4, 9, Math.toRadians(90));
    public Pose blueNearStart = new Pose(44.8, 140.8, Math.toRadians(180));

    public Pose farLaunchPose;
    public Pose nearLaunchPose;
    public Pose notLaunchZone;
    public Pose blueNearLaunchPose = new Pose(57, 91.5, Math.toRadians(135));
    public Pose blueFarLaunchPose = new Pose(61, 18.6, Math.toRadians(111.5));
    public Pose blueNotLaunchZoneNear = new Pose(30, 120, Math.toRadians(-135));
    public Pose blueNotLaunchZoneFar = new Pose(40, 15, Math.toRadians(90));

    public Pose blueTopArtifacts = new Pose(32, 91.5, Math.toRadians(180));
    public Pose blueMiddleArtifacts = new Pose(22, 67, Math.toRadians(180));
    public Pose blueBottomArtifacts = new Pose(22, 45, Math.toRadians(180));
    public Pose firstArtifacts;
    public Pose firstArtifactControlPoint = new Pose(76.5,45);
    public Pose secondArtifacts;
    public Pose secondArtifactControlPoint = new Pose(76.5, 67);
    public Pose thirdArtifacts;

    public Pose blueGoal = new Pose(16, 131);
    public Pose goal = new Pose(16, 131);

    public void assignStartPose() {
        boolean isAuto = ActiveOpMode.INSTANCE.getClass().isAnnotationPresent(Autonomous.class);
        if (isAuto) {
            startPose = (Settings.get("start position", SettingsSetter.startLocations.FAR) == SettingsSetter.startLocations.NEAR) ? blueNearStart : blueFarStart;
            if (Settings.get("alliance", SettingsSetter.alliance.BLUE) == SettingsSetter.alliance.RED) {
                startPose = startPose.mirror();
            }
        } else {
            startPose = PedroComponent.follower().getPose();
        }
    }

    public void configureAlliancePaths() {
        goal = blueGoal;
        farLaunchPose = blueFarLaunchPose;
        nearLaunchPose = blueNearLaunchPose;
        secondArtifacts = blueMiddleArtifacts;

        if (Settings.get("start position", SettingsSetter.startLocations.NEAR) == SettingsSetter.startLocations.FAR) {
            firstArtifacts = blueBottomArtifacts;
            thirdArtifacts = blueTopArtifacts;
            notLaunchZone = blueNotLaunchZoneFar;
        } else {
            firstArtifacts = blueTopArtifacts;
            firstArtifactControlPoint = new Pose(firstArtifacts.getX(), firstArtifacts.getY());
            thirdArtifacts = blueBottomArtifacts;
            notLaunchZone = blueNotLaunchZoneNear;
        }

        if (Settings.get("alliance", SettingsSetter.alliance.BLUE) == SettingsSetter.alliance.RED) {
            farLaunchPose = farLaunchPose.mirror();
            nearLaunchPose = nearLaunchPose.mirror();
            notLaunchZone = notLaunchZone.mirror();

            firstArtifacts = firstArtifacts.mirror();
            firstArtifactControlPoint = firstArtifactControlPoint.mirror();
            secondArtifacts = secondArtifacts.mirror();
            secondArtifactControlPoint = secondArtifactControlPoint.mirror();
            thirdArtifacts = thirdArtifacts.mirror();

            goal = blueGoal.mirror();
        }
    }

    @Override
    public void initialize() {
        configureAlliancePaths();
        assignStartPose();
        ActiveOpMode.telemetry().addData("Alliance", Settings.get("alliance", SettingsSetter.alliance.BLUE));
        ActiveOpMode.telemetry().addData("Start Position", Settings.get("start position", SettingsSetter.startLocations.NEAR));
    }
}

