package org.firstinspires.ftc.teamcode.hardware;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.BezierPoint;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.theKeep.TheKeepAuto;
import java.util.function.Supplier;

public class PedroPathing {

    public boolean automatedDrive;
    public Follower follower;
    public Supplier<PathChain> turnToGoal;

    // Sets up a bunch of positions that the bot uses to create the start position - Jason
    public static Pose startPose;
    private final Pose redAlliance1 = new Pose(81,9.2,Math.toRadians(90));
    private final Pose redAlliance2 = new Pose(63,135,Math.toRadians(-90));
    private final Pose blueAlliance1 = new Pose(64,9.2,Math.toRadians(90));
    private final Pose blueAlliance2 = new Pose(81,135,Math.toRadians(-90));
    private final Pose redGoalPose = new Pose(144, 144);
    private final Pose blueGoalPose = new Pose(0,144);


    // Score Positions
    private final Pose blueNearLaunchPose = new Pose(72, 72, Math.toRadians(135)); // Scoring Pose of our robot. It is facing the goal at a 135 degree angle.
    private final Pose redNearLaunchPose = new Pose(72, 72, Math.toRadians(45));
    private final Pose blueFarLaunchPose = new Pose(60,21, Math.toRadians(113));
    private final Pose redFarLaunchPose = new Pose(84,21,Math.toRadians(70));
    private Pose LaunchPose;


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


    // Current alliance's top artifact positions
    private Pose firstFrontArtifact;
    private Pose firstMiddleArtifact;
    private Pose firstBackArtifact;

    // Current alliance's middle artifact positions
    private Pose secondFrontArtifact;
    private Pose secondMiddleArtifact;
    private  Pose secondBackArtifact;

    // Current alliance's bottom artifact positions
    private Pose bottomFrontArtifact;
    private Pose bottomMiddleArtifact;
    private Pose bottomBackArtifact;

    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;
    private Path scoreArtifact;

    public PathChain grabTFArtifact, grabTMArtifact, grabTBArtifact, scoreTopArtifacts;
    public PathChain grabMFArtifact, grabMMArtifact, grabMBArtifact, scoreMiddleArtifacts;

    // A method that sets up the follower for pedro pathing - Jason
    public void initFollower(HardwareMap hardwareMap) {

        // Sets up the follower based on the given position and tuned values from the constants class - Jason
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        follower.update();

        // Decides which goal the PathChain turnToGoal should face based on the chosen alliance - Jason
        if (TheKeepAuto.alliance == TheKeepAuto.Alliance.RED) {
            turnToGoal = () -> follower.pathBuilder()
                    .addPath(new BezierPoint(follower::getPose))
                    .setHeadingInterpolation(HeadingInterpolator.facingPoint(redGoalPose))
                    .build();
        } else {
            turnToGoal = () -> follower.pathBuilder()
                    .addPath(new BezierPoint(follower::getPose))
                    .setHeadingInterpolation(HeadingInterpolator.facingPoint(blueGoalPose))
                    .build();
        }
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

            // Sets the middle artifact pose based on alliance
            secondFrontArtifact = RMFArtifact;
            secondMiddleArtifact = RMMArtifact;
            secondBackArtifact = RMBArtifact;
        }
    }

    public void buildPaths() {
        /* This is our scorePreload path. We are using a BezierLine, which is a straight line. */
        scoreArtifact = new Path(new BezierLine(startPose, LaunchPose));
        scoreArtifact.setHeadingInterpolation(HeadingInterpolator.facingPoint(blueGoalPose));
    /* Here is an example for Constant Interpolation
    scorePreload.setConstantInterpolation(startPose.getHeading()); */
            /* This is our grabPickup1 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        grabTFArtifact = follower.pathBuilder()
                .addPath(new BezierLine(LaunchPose, firstFrontArtifact))
                .setLinearHeadingInterpolation(LaunchPose.getHeading(), firstFrontArtifact.getHeading())
                .build();
        grabTMArtifact = follower.pathBuilder()
                .addPath(new BezierLine(firstFrontArtifact, firstMiddleArtifact))
                .setTangentHeadingInterpolation()
                .build();
        grabTBArtifact = follower.pathBuilder()
                .addPath(new BezierLine(firstMiddleArtifact, firstBackArtifact))
                .setTangentHeadingInterpolation()
                .build();
            /* This is our scorePickup1 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        scoreTopArtifacts = follower.pathBuilder()
                .addPath(new BezierLine(firstBackArtifact, LaunchPose))
                .setHeadingInterpolation(HeadingInterpolator.facingPoint(blueGoalPose))
                .build();


        grabMFArtifact = follower.pathBuilder()
                .addPath(new BezierLine(LaunchPose, secondFrontArtifact))
                .setLinearHeadingInterpolation(LaunchPose.getHeading(), secondFrontArtifact.getHeading())
                .build();
        grabMMArtifact = follower.pathBuilder()
                .addPath(new BezierLine(secondFrontArtifact, secondMiddleArtifact))
                .setTangentHeadingInterpolation()
                .build();
        grabMBArtifact = follower.pathBuilder()
                .addPath(new BezierLine(secondMiddleArtifact, secondBackArtifact))
                .setTangentHeadingInterpolation()
                .build();
            /* This is our scorePickup1 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        scoreMiddleArtifacts = follower.pathBuilder()
                .addPath(new BezierLine(secondBackArtifact, LaunchPose))
                .setHeadingInterpolation(HeadingInterpolator.facingPoint(blueGoalPose))
                .build();
    }


    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(scoreArtifact);
                setPathState(1);
                break;
            case 1:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!follower.isBusy()) {
                    follower.followPath(grabTFArtifact,true);
                    setPathState(2);
                }
                break;
            case 2:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
                if (!follower.isBusy()) {
                    /* Grab Sample */
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(grabTMArtifact, true);
                    setPathState(3);
                }
                break;
            case 3:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!follower.isBusy()) {
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower.followPath(grabTBArtifact, true);
                    setPathState(4);
                }
                break;
            case 4:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup2Pose's position */
                if (!follower.isBusy()) {
                    /* Grab Sample */
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(scoreTopArtifacts, true);
                    setPathState(5);
                }
                break;
            case 5:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!follower.isBusy()) {


                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower.followPath(grabMFArtifact, true);
                    setPathState(6);
                }
                break;
            case 6:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup3Pose's position */
                if (!follower.isBusy()) {
                    /* Grab Sample */
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(grabMMArtifact, true);
                    setPathState(7);
                }
                break;
            case 7:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!follower.isBusy()) {

                    follower.followPath(grabMBArtifact, true);
                    setPathState(8);
                }
                break;
            case 8:
                if (!follower.isBusy()) {

                    follower.followPath(scoreMiddleArtifacts, true);
                    setPathState(8);
                }
                break;
            case 9:
                if (!follower.isBusy()) {

                    setPathState(-1);
                }
                break;
        }
    }

    /**
     * These change the states of the paths and actions. It will also reset the timers of the individual switches
     **/
    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }


}