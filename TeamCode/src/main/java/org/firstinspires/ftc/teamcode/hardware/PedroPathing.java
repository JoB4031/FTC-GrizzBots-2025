package org.firstinspires.ftc.teamcode.hardware;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.BezierPoint;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.skeletonarmy.marrow.zones.Point;
import com.skeletonarmy.marrow.zones.PolygonZone;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.theKeep.TheKeepAuto;
import java.util.function.Supplier;

public class PedroPathing {

    public boolean automatedDrive;
    public Follower follower;
    public Supplier<PathChain> turnToGoal;

    // Sets up a bunch of positions that the bot uses to create the start position - Jason
    public static Pose startPose;
    private final Pose redAlliance1 = new Pose(81,9,Math.toRadians(90));
    private final Pose redAlliance2 = new Pose(118.5,128.5,Math.toRadians(-135));
    private final Pose blueAlliance1 = new Pose(63,9,Math.toRadians(90));
    private final Pose blueAlliance2 = new Pose(25.6,128.5,Math.toRadians(-45));
    private final Pose redGoalPose = new Pose(130, 140);
    private final Pose blueGoalPose = new Pose(10,140);
    private static Pose allianceGoalPose;


    // Score Positions
    private final Pose blueNearLaunchPose = new Pose(57, 84, Math.toRadians(135)); // Scoring Pose of our robot. It is facing the goal at a 135 degree angle.
    private final Pose redNearLaunchPose = new Pose(87, 84, Math.toRadians(45));
    private final Pose blueFarLaunchPose = new Pose(57,21, Math.toRadians(113));
    private final Pose redFarLaunchPose = new Pose(87,21,Math.toRadians(70));
    private final Pose notLaunchZoneBlue = new Pose(45,60,Math.toRadians(180));
    private final Pose notLaunchZoneRed = new Pose(80,60,Math.toRadians(180));
    private Pose LaunchPose, notLaunchZone;


    // Blue alliance's top artifact positions
    private final Pose BTFArtifact = new Pose(36, 84, Math.toRadians(180));
    private final Pose BTMArtifact = new Pose(30, 84, Math.toRadians(180));
    private final Pose BTBArtifact = new Pose(24, 84, Math.toRadians(180));

    // Blue alliance's middle artifact positions
    private final Pose BMFArtifact = new Pose(36, 60, Math.toRadians(180));
    private final Pose BMMArtifact = new Pose(30, 60, Math.toRadians(180));
    private final Pose BMBArtifact = new Pose(24, 60, Math.toRadians(180));

    // Blue alliance's bottom artifact positions
    private final Pose BBFArtifact = new Pose(36, 36, Math.toRadians(180));
    private final Pose BBMArtifact = new Pose(30, 36, Math.toRadians(180));
    private final Pose BBBArtifact = new Pose(24, 36, Math.toRadians(180));


    // Red alliance's top artifact positions
    private final Pose RTFArtifact = new Pose(108, 84, Math.toRadians(0));
    private final Pose RTMArtifact = new Pose(114, 84, Math.toRadians(0));
    private final Pose RTBArtifact = new Pose(120, 84, Math.toRadians(0));

    // Red alliance's middle artifact positions
    private final Pose RMFArtifact = new Pose(108, 60, Math.toRadians(0));
    private final Pose RMMArtifact = new Pose(114, 60, Math.toRadians(0));
    private final Pose RMBArtifact = new Pose(120, 60, Math.toRadians(0));

    // Red alliance's bottom artifact positions
    private final Pose RBFArtifact = new Pose(108, 36, Math.toRadians(0));
    private final Pose RBMArtifact = new Pose(114, 36, Math.toRadians(0));
    private final Pose RBBArtifact = new Pose(120, 36, Math.toRadians(0));


    // The first set of artifacts picked up
    private Pose firstFrontArtifact;
    private Pose firstMiddleArtifact;
    private Pose firstBackArtifact;

    // The second set of artifacts picked up
    private Pose secondFrontArtifact;
    private Pose secondMiddleArtifact;
    private  Pose secondBackArtifact;


    public int pathState;
    public Path scoreArtifact;

    public PathChain grabFirstFront, grabFirstMiddle, grabFirstBack, scoreFirstArtifact;
    public PathChain grabSecondFront, grabSecondMiddle, grabSecondBack, scoreSecondArtifacts;
    public PathChain leaveLaunchZone;


    private final PolygonZone closeLaunchZone = new PolygonZone(new Point(144, 144), new Point(72, 72), new Point(0, 144));
    private final PolygonZone farLaunchZone = new PolygonZone(new Point(48, 0), new Point(72, 24), new Point(96, 0));
    private final PolygonZone robotZone = new PolygonZone(17.5, 17);
    public boolean isAllowedToShoot;

    // A method that sets up the follower for pedro pathing - Jason
    public void initFollower(HardwareMap hardwareMap) {

        // Sets up the follower based on the given position and tuned values from the constants class - Jason
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        follower.update();

        // Decides which goal the PathChain turnToGoal should face based on the chosen alliance - Jason
        turnToGoal = () -> follower.pathBuilder()
                .addPath(new BezierPoint(follower::getPose))
                .setHeadingInterpolation(HeadingInterpolator.facingPoint(allianceGoalPose))
                .build();
    }

    // A method that simply sets the start position based on what was the chosen alliance and position - Jason
    public void setStartPose(boolean atPromptEnd) {
        if (atPromptEnd) {
            if (TheKeepAuto.alliance == TheKeepAuto.Alliance.RED) {
                if (TheKeepAuto.startLocation == 1) {
                    startPose = redAlliance1;
                } else startPose = redAlliance2;
            } else {
                if (TheKeepAuto.startLocation == 1) {
                    startPose = blueAlliance1;
                } else startPose = blueAlliance2;
            }
        } else {
            startPose = follower.getPose();
        }

    }

    // method to update all the pathing function
    public void update() {

        follower.update();

        robotZone.setPosition(follower.getPose().getX(), follower.getPose().getY());
        robotZone.setRotation(follower.getPose().getHeading());
        isAllowedToShoot = robotZone.isInside(closeLaunchZone) || robotZone.isInside(farLaunchZone);
    }

    public void setArtifact() {
        if(TheKeepAuto.alliance == TheKeepAuto.Alliance.BLUE) {
            // Sets the near launch based on the alliance
            if (TheKeepAuto.startLocation == 1) {
                LaunchPose = blueFarLaunchPose;

                firstFrontArtifact = BBFArtifact;
                firstMiddleArtifact = BBMArtifact;
                firstBackArtifact = BBBArtifact;
            } else {
                LaunchPose = blueNearLaunchPose;

                firstFrontArtifact = BTFArtifact;
                firstMiddleArtifact = BTMArtifact;
                firstBackArtifact = BTBArtifact;
            }
            allianceGoalPose = blueGoalPose;
            notLaunchZone = notLaunchZoneBlue;
            // Sets the middle artifact pose based on alliance
            secondFrontArtifact = BMFArtifact;
            secondMiddleArtifact = BMMArtifact;
            secondBackArtifact = BMBArtifact;


        } else{
            // Sets the near launch based on the alliance
            if (TheKeepAuto.startLocation == 1) {
                LaunchPose = redFarLaunchPose;

                firstFrontArtifact = RBFArtifact;
                firstMiddleArtifact = RBMArtifact;
                firstBackArtifact = RBBArtifact;
            } else {
                LaunchPose = redNearLaunchPose;

                firstFrontArtifact = RTFArtifact;
                firstMiddleArtifact = RTMArtifact;
                firstBackArtifact = RTBArtifact;
            }
            allianceGoalPose = redGoalPose;
            notLaunchZone = notLaunchZoneRed;
            // Sets the middle artifact pose based on alliance
            secondFrontArtifact = RMFArtifact;
            secondMiddleArtifact = RMMArtifact;
            secondBackArtifact = RMBArtifact;
        }
    }

    public void buildPaths() {
        /* This is our scorePreload path. We are using a BezierLine, which is a straight line. */
        scoreArtifact = new Path(new BezierLine(startPose, LaunchPose));
        scoreArtifact.setHeadingInterpolation(HeadingInterpolator.facingPoint(allianceGoalPose));

    /* Here is an example for Constant Interpolation
    scorePreload.setConstantInterpolation(startPose.getHeading()); */
            /* This is our grabPickup1 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        grabFirstFront = follower.pathBuilder()
                .addPath(new BezierLine(LaunchPose, firstFrontArtifact))
                .setLinearHeadingInterpolation(LaunchPose.getHeading(), firstFrontArtifact.getHeading())
                .build();
        grabFirstMiddle = follower.pathBuilder()
                .addPath(new BezierLine(firstFrontArtifact, firstMiddleArtifact))
                .setTangentHeadingInterpolation()
                .build();
        grabFirstBack = follower.pathBuilder()
                .addPath(new BezierLine(firstMiddleArtifact, firstBackArtifact))
                .setTangentHeadingInterpolation()
                .build();
            /* This is our scorePickup1 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        scoreFirstArtifact = follower.pathBuilder()
                .addPath(new BezierLine(firstBackArtifact, LaunchPose))
                .setHeadingInterpolation(HeadingInterpolator.facingPoint(allianceGoalPose))
                .build();


        grabSecondFront = follower.pathBuilder()
                .addPath(new BezierLine(LaunchPose, secondFrontArtifact))
                .setLinearHeadingInterpolation(LaunchPose.getHeading(), secondFrontArtifact.getHeading())
                .build();
        grabSecondMiddle = follower.pathBuilder()
                .addPath(new BezierLine(secondFrontArtifact, secondMiddleArtifact))
                .setTangentHeadingInterpolation()
                .build();
        grabSecondBack = follower.pathBuilder()
                .addPath(new BezierLine(secondMiddleArtifact, secondBackArtifact))
                .setTangentHeadingInterpolation()
                .build();
            /* This is our scorePickup1 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        scoreSecondArtifacts = follower.pathBuilder()
                .addPath(new BezierLine(secondBackArtifact, LaunchPose))
                .setHeadingInterpolation(HeadingInterpolator.facingPoint(allianceGoalPose))
                .build();
        leaveLaunchZone = follower.pathBuilder()
                .addPath(new BezierLine(LaunchPose, notLaunchZone))
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();
    }


    public void setPathState(int pState) {
        pathState = pState;
    }


}