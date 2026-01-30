package org.firstinspires.ftc.teamcode.hardware;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
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
    private final Pose redAlliance2 = new Pose(99.2,140.8,Math.toRadians(0));
    private final Pose blueAlliance1 = new Pose(63,9,Math.toRadians(90));
    private final Pose blueAlliance2 = new Pose(44.8,140.8,Math.toRadians(180));
    private final Pose redGoalPose = new Pose(130, 140);
    private final Pose blueGoalPose = new Pose(10,140);
    private static Pose allianceGoalPose;


    // Score Positions
    private final Pose blueNearLaunchPose = new Pose(57, 91.5, Math.toRadians(135)); // Scoring Pose of our robot. It is facing the goal at a 135 degree angle.
    private final Pose redNearLaunchPose = new Pose(87, 91.5, Math.toRadians(45));
    private final Pose blueFarLaunchPose = new Pose(61,18.6, Math.toRadians(111.5));
    private final Pose redFarLaunchPose = new Pose(82,17,Math.toRadians(70));
    private final Pose notLaunchZoneBlue = new Pose(45,60,Math.toRadians(180));
    private final Pose notLaunchZoneRed = new Pose(99,60,Math.toRadians(180));
    private Pose LaunchPose, nearLaunchPose, notLaunchZone;


    // Blue alliance's top artifact positions
    private final Pose blueTopArtifacts = new Pose(35, 91.5, Math.toRadians(180));
    // Blue alliance's middle artifact positions
    private final Pose blueMiddleArtifacts = new Pose(35, 67, Math.toRadians(180));

    // Blue alliance's bottom artifact positions
    private final Pose blueBottomArtifacts = new Pose(35, 40, Math.toRadians(180));


    // Red alliance's top artifact positions
    private final Pose redTopArtifacts = new Pose(109, 91.5, Math.toRadians(0));
    // Red alliance's middle artifact positions
    private final Pose redMiddleArtifacts = new Pose(109, 67, Math.toRadians(0));

    // Red alliance's bottom artifact positions
    private final Pose redBottomArtifacts = new Pose(109, 40, Math.toRadians(0));

    // The first set of artifacts picked up
    private Pose firstArtifacts;
    // The second set of artifacts picked up
    private Pose secondArtifacts;


    public int pathState;
    public Path scoreArtifact;

    public PathChain grabFirstArtifacts, scoreFirstArtifacts;
    public PathChain grabSecondArtifacts, scoreSecondArtifacts;
    public PathChain leaveLaunchZone;


    private final PolygonZone closeLaunchArea = new PolygonZone(new Point(144, 144), new Point(72, 72), new Point(0, 144));
    private final PolygonZone farLaunchArea = new PolygonZone(new Point(48, 0), new Point(72, 24), new Point(96, 0));
    private final PolygonZone robotToGoalZone = new PolygonZone(1, 1);
    private final PolygonZone robotLaunchZone = new PolygonZone(17,17.5);
    public boolean robotInRange;
    private final Point blueGoalTargetPoint = new Point(16,131);
    private final Point redGoalTargetPoint = new Point(128,131);
    private Point allianceGoalZone;
    public double shootDistance;
    public boolean farLaunch;

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
        robotToGoalZone.setPosition(follower.getPose().getX(), follower.getPose().getY());
        robotToGoalZone.setRotation(follower.getPose().getHeading());
        robotLaunchZone.setPosition(follower.getPose().getX(), follower.getPose().getY());
        robotLaunchZone.setRotation(follower.getPose().getHeading());
        follower.update();
        shootDistance = ((robotToGoalZone.distanceTo(allianceGoalZone)*0.0254)-0.2);
        robotInRange = ((robotLaunchZone.isInside(closeLaunchArea) || robotLaunchZone.isInside(farLaunchArea)) && shootDistance >= 0.9);
        farLaunch = robotLaunchZone.isInside(farLaunchArea);
    }

    public void setArtifact() {
        if(TheKeepAuto.alliance == TheKeepAuto.Alliance.BLUE) {
            // Sets the near launch based on the alliance
            if (TheKeepAuto.startLocation == 1) {
                LaunchPose = blueFarLaunchPose;
                firstArtifacts = blueBottomArtifacts;
            } else {
                LaunchPose = blueNearLaunchPose;
                firstArtifacts = blueTopArtifacts;
            }
            nearLaunchPose = blueNearLaunchPose;
            allianceGoalPose = blueGoalPose;
            allianceGoalZone = blueGoalTargetPoint;
            notLaunchZone = notLaunchZoneBlue;
            // Sets the middle artifact pose based on alliance
            secondArtifacts = blueMiddleArtifacts;


        } else{
            // Sets the near launch based on the alliance
            if (TheKeepAuto.startLocation == 1) {
                LaunchPose = redFarLaunchPose;
                firstArtifacts = redBottomArtifacts;
            } else {
                LaunchPose = redNearLaunchPose;
                firstArtifacts = redTopArtifacts;

            }
            nearLaunchPose = redNearLaunchPose;
            allianceGoalPose = redGoalPose;
            allianceGoalZone = redGoalTargetPoint;
            notLaunchZone = notLaunchZoneRed;
            // Sets the middle artifact pose based on alliance
            secondArtifacts = redMiddleArtifacts;
        }
    }

    public void buildPaths() {
        /* This is our scorePreload path. We are using a BezierLine, which is a straight line. */
        scoreArtifact = new Path(new BezierLine(startPose, LaunchPose));
        scoreArtifact.setHeadingInterpolation(HeadingInterpolator.facingPoint(allianceGoalPose));

    /* Here is an example for Constant Interpolation
    scorePreload.setConstantInterpolation(startPose.getHeading()); */
            /* This is our grabPickup1 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        grabFirstArtifacts = follower.pathBuilder()
                .addPath(new BezierLine(LaunchPose, firstArtifacts))
                .setConstantHeadingInterpolation(firstArtifacts.getHeading())
                .build();
            /* This is our scorePickup1 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        scoreFirstArtifacts = follower.pathBuilder()
                .addPath(
                        new BezierLine(firstArtifacts, LaunchPose))
                .setHeadingInterpolation(HeadingInterpolator.facingPoint(allianceGoalPose))
                .build();
        if (TheKeepAuto.alliance == TheKeepAuto.Alliance.BLUE) {
            grabSecondArtifacts = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    LaunchPose,
                                    new Pose(76.500, 67.000),
                                    secondArtifacts
                            )
                    ).setConstantHeadingInterpolation(secondArtifacts.getHeading())

                    .build();
        } else {
            grabSecondArtifacts = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    LaunchPose,
                                    new Pose(67.500, 67.000),
                                    secondArtifacts
                            )
                    ).setConstantHeadingInterpolation(secondArtifacts.getHeading())

                    .build();
        }
            /* This is our scorePickup1 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        scoreSecondArtifacts = follower.pathBuilder()
                .addPath(new BezierLine(secondArtifacts, nearLaunchPose))
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