/*
The Keep Auto Version 4.0.0
Changelog:
Added two versions of the autonomous one to be tested and one that works
*/

package org.firstinspires.ftc.teamcode.theKeep;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.skeletonarmy.marrow.prompts.BooleanPrompt;
import com.skeletonarmy.marrow.prompts.OptionPrompt;
import com.skeletonarmy.marrow.prompts.Prompter;
import org.firstinspires.ftc.teamcode.hardware.Motors;
import org.firstinspires.ftc.teamcode.hardware.PedroPathing;
import org.firstinspires.ftc.teamcode.hardware.Vision;

@Autonomous(name="The Keep Auto",group="The Keep")
public class TheKeepAuto extends OpMode {

    private final Pose blueScorePose = new Pose(72, 72, Math.toRadians(135)); // Scoring Pose of our robot. It is facing the goal at a 135 degree angle.
    private final Pose redScorePose = new Pose(72, 72, Math.toRadians(45));
    private final Pose scorePose = new Pose(84, 95, Math.toRadians(45));
    private final Pose pickup1Pose = new Pose(18, 84, Math.toRadians(180)); // Highest (First Set) of Artifacts from the Spike Mark.
    private final Pose pickup2Pose = new Pose(18, 60, Math.toRadians(180)); // Middle (Second Set) of Artifacts from the Spike Mark.
    private final Pose pickup3Pose = new Pose(18, 35, Math.toRadians(180)); // Lowest (Third Set) of Artifacts from the Spike Mark.

    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;

    private Path scorePreload;
    private PathChain grabPickup1, scorePickup1, grabPickup2, scorePickup2, grabPickup3, scorePickup3;


    private boolean hasShot;
    private Motors motors;
    private Vision vision;
    private PedroPathing pathing;

    // These next lines setup the variables used to store prompter values, which define the autonomous is used - Jason
    public enum Alliance {
        RED,
        BLUE
    }

    private Prompter prompter = new Prompter(this);
    public static Alliance alliance;
    public static int startLocation;
    public static boolean robotCentric;


    @Override
    public void init() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();
        vision = new Vision();
        pathing = new PedroPathing();
        motors = new Motors();
        // Call their init methods
        vision.initAprilTag(hardwareMap);
        motors.initMotors(hardwareMap);
        motors.setFlywheelVelocity(0,0);
        //Sets up the prompter - Jason
        prompter.prompt("alliance", new OptionPrompt<>("Select Alliance", Alliance.RED, Alliance.BLUE))
                .prompt("startLocation", new OptionPrompt<>("Select Start Location", 1, 2))
                .prompt("robotCentric", new BooleanPrompt("Robot Centric",true))
                .onComplete(this::onPromptsComplete);

    } // This initializes all the motors and sensors

    // Function that runs when the prompter is done that stores the values and prints the results - Jason
    public void onPromptsComplete() {
        alliance = prompter.get("alliance");
        startLocation = prompter.get("startLocation");
        robotCentric = prompter.get("robotCentric");
        pathing.setStartPose(true);
        pathing.initFollower(hardwareMap);
        pathing.setArtifact();
        pathing.buildPaths();
        buildPaths();
        telemetry.addData("Selected Alliance", alliance);
        telemetry.addData("Selected Start Location", startLocation);
        telemetry.addData("Selected Start Position", pathing.follower.getPose());
        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void init_loop() {
        // Runs the prompter - Jason
        prompter.run();
    } // A loop that runs from when the init button is pressed to when the start button is hit

    @Override
    public void start() {
        telemetry.clear();
    }

    @Override
    public void loop() {
        // Updates the hardware - Jason
        pathing.update();
        motors.intake.setPower(.1);
        motors.update();
        vision.update();
        autonomousPathUpdate();
        // These lines grab the april tag data then write any tags data to the telemetry - Jason
        if (Vision.pattern != null) {
            telemetry.addData("Pattern Is", Vision.pattern);
        } else {
            telemetry.addData("Pattern Is", "unknown");
        }

        // These lines add the fidget tech's position and the bots position to the telemetry - Jason
        if (vision.allianceBase != null) {
            telemetry.addData("Alliance Goal", vision.allianceBase.ftcPose.range);
        }
        telemetry.addData("Fidget Tech Position", motors.spinPositions[motors.spinPosition]);
        telemetry.addData("Bot Position", pathing.follower.getPose());
        telemetry.update();

    }
    @Override
    public void stop() {
        pathing.setStartPose(false);
    }

    public void buildPaths() {
        if (alliance == Alliance.BLUE) {
            /* This is our scorePreload path. We are using a BezierLine, which is a straight line. */
            scorePreload = new Path(new BezierLine(PedroPathing.startPose, blueScorePose));
            scorePreload.setHeadingInterpolation(HeadingInterpolator.facingPoint(0,144));

        } else {
            /* This is our scorePreload path. We are using a BezierLine, which is a straight line. */
            scorePreload = new Path(new BezierLine(PedroPathing.startPose, redScorePose));
            scorePreload.setHeadingInterpolation(HeadingInterpolator.facingPoint(144,144));

        }
        grabPickup1 = pathing.follower.pathBuilder()
                .addPath(new BezierLine(scorePose, pickup1Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading())
                .build();
        scorePickup1 = pathing.follower.pathBuilder()
                .addPath(new BezierLine(pickup1Pose, scorePose))
                .setLinearHeadingInterpolation(pickup1Pose.getHeading(), scorePose.getHeading())
                .build();
        grabPickup2 = pathing.follower.pathBuilder()
                .addPath(new BezierLine(scorePose, pickup2Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup2Pose.getHeading())
                .build();
        scorePickup2 = pathing.follower.pathBuilder()
                .addPath(new BezierLine(pickup2Pose, scorePose))
                .setLinearHeadingInterpolation(pickup2Pose.getHeading(), scorePose.getHeading())
                .build();
        grabPickup3 = pathing.follower.pathBuilder()
                .addPath(new BezierLine(scorePose, pickup3Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup3Pose.getHeading())
                .build();
        scorePickup3 = pathing.follower.pathBuilder()
                .addPath(new BezierLine(pickup3Pose, scorePose))
                .setLinearHeadingInterpolation(pickup3Pose.getHeading(), scorePose.getHeading())
                .build();
    }


    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:

                pathing.follower.followPath(scorePreload);
                setPathState(1);
                break;
            case 1:
                vision.update();
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!pathing.follower.isBusy()) {
                    motors.shootAllBalls(1.5);
                    setPathState(-1);
                }
                break;
            case 2:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
                if (!pathing.follower.isBusy()) {
                    /* Grab Sample */
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    pathing.follower.followPath(scorePickup1, true);
                    setPathState(3);
                }
                break;
            case 3:
                vision.update();/* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!pathing.follower.isBusy()) {
                    motors.shootAllBalls(1.5);
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    pathing.follower.followPath(grabPickup2, true);
                    setPathState(4);
                }
                break;
            case 4:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup2Pose's position */
                if (!pathing.follower.isBusy()) {
                    /* Grab Sample */
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    pathing.follower.followPath(scorePickup2, true);
                    setPathState(5);
                }
                break;
            case 5:
                vision.update();/* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!pathing.follower.isBusy()) {
                    motors.shootAllBalls(1.5);
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    pathing.follower.followPath(grabPickup3, true);
                    setPathState(6);
                }
                break;
            case 6:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup3Pose's position */
                if (!pathing.follower.isBusy()) {
                    /* Grab Sample */
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    pathing.follower.followPath(scorePickup3, true);
                    setPathState(7);
                }
                break;
            case 7:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!pathing.follower.isBusy()) {
                    /* Set the state to a Case we won't use or define, so it just stops running an new paths */
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

    public void autonomousPathUpdate2() {
        switch (pathState) {
            case 0:
                pathing.follower.followPath(scorePreload);
                setPathState(1);
                break;
            case 1:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!pathing.follower.isBusy()) {
                    motors.shootAllBalls(vision.allianceBase.ftcPose.range);
                    motors.intake.setPower(1);
                    pathing.follower.followPath(pathing.grabTFArtifact,true);
                    setPathState(2);
                }
                break;
            case 2:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
                if (!pathing.follower.isBusy()) {
                    motors.spinPosition -= 2;
                    /* Grab Sample */
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    pathing.follower.followPath(pathing.grabTMArtifact, true);
                    setPathState(3);
                }
                break;
            case 3:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!pathing.follower.isBusy()) {
                    motors.spinPosition -= 2;
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    pathing.follower.followPath(pathing.grabTBArtifact, true);
                    setPathState(4);
                }
                break;
            case 4:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup2Pose's position */
                if (!pathing.follower.isBusy()) {
                    motors.spinPosition -= 2;
                    motors.intake.setPower(0.1);
                    /* Grab Sample */
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    pathing.follower.followPath(pathing.scoreTopArtifacts, true);
                    setPathState(5);
                }
                break;
            case 5:
                ;/* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!pathing.follower.isBusy()) {

                    motors.shootAllBalls(vision.allianceBase.ftcPose.range);
                    motors.intake.setPower(1);
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    pathing.follower.followPath(pathing.grabMFArtifact, true);
                    setPathState(6);
                }
                break;
            case 6:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup3Pose's position */
                if (!pathing.follower.isBusy()) {
                    /* Grab Sample */
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    motors.spinPosition -= 2;
                    pathing.follower.followPath(pathing.grabMMArtifact, true);
                    setPathState(7);
                }
                break;
            case 7:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!pathing.follower.isBusy()) {

                    motors.spinPosition -= 2;
                    pathing.follower.followPath(pathing.grabMBArtifact, true);
                    setPathState(8);
                }
                break;
            case 8:
                if (!pathing.follower.isBusy()) {
                    motors.spinPosition -= 2;
                    motors.intake.setPower(0.1);
                    pathing.follower.followPath(pathing.scoreMiddleArtifacts, true);
                    setPathState(8);
                }
                break;
            case 9:
                if (!pathing.follower.isBusy()) {
                    motors.intake.setPower(0);
                    motors.setFlywheelVelocity(0,0);
                    setPathState(-1);
                }
                break;
        }
    }

}
