package org.firstinspires.ftc.teamcode.hardware.centralHub;

import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.motors.Ejector;
import org.firstinspires.ftc.teamcode.hardware.motors.FidgetTech;
import org.firstinspires.ftc.teamcode.hardware.motors.Flywheel;
import org.firstinspires.ftc.teamcode.hardware.motors.Intake;
import org.firstinspires.ftc.teamcode.hardware.pedroPathing.LaunchZoneTracker;
import org.firstinspires.ftc.teamcode.hardware.pedroPathing.PathBuilder;
import org.firstinspires.ftc.teamcode.hardware.pedroPathing.PathFollower;
import org.firstinspires.ftc.teamcode.hardware.pedroPathing.PoseLibrary;
import org.firstinspires.ftc.teamcode.hardware.sensors.IntakeSensor;
import org.firstinspires.ftc.teamcode.hardware.vision.Vision;

public class hardware {
    private double runTime;
    public final ElapsedTime autoTime = new ElapsedTime();
    // Motor Wrappers
    public Flywheel flywheel;
    public Intake intake;
    public FidgetTech fidgetTech;
    public Ejector ejector;

    // Sensor Wrappers
    public IntakeSensor intakeSensor;

    // Pedro Pathing Wrappers
    public PathFollower pathFollower;
    public LaunchZoneTracker launchZoneTracker;
    public PathBuilder pathBuilder;
    public PoseLibrary poseLib;

    private AutonomousPath autonomousPath;

    public final ElapsedTime timer = new ElapsedTime();

    public Vision vision;
    public boolean infiniteRun = false;

    public boolean automatedDrive = false;
    public boolean doNotSpin = false;
    private boolean stopRequested = false;


    public void initHardware(HardwareMap hw, boolean auto) {
        // Sets up how long the program should run for before turning off
        if (auto) {
            runTime = 30;
        } else runTime = 120;
        // Initializes the flywheel
        flywheel = new Flywheel();
        flywheel.initFlywheel(hw);
        // Initializes the intake
        intake   = new Intake();
        intake.initIntake(hw);
        // Initializes the Fidget Tech
        fidgetTech = new FidgetTech();
        fidgetTech.initFidgetTech(hw);
        // Initializes the ejector
        ejector  = new Ejector();
        ejector.initEjector(hw);

        // Initializes the intake sensor
        intakeSensor = new IntakeSensor();
        intakeSensor.initIntakeSensor(hw);

        // Initializes the pedro pathing positions based on alliance and start position
        poseLib = new PoseLibrary();
        poseLib.configureAlliancePaths();
        if (auto) {
            startPosition(true);
        }
        // Initializes the pedro pathing follower
        pathFollower = new PathFollower();
        pathFollower.init(hw, poseLib);
        // Initializes the launch zone tracker
        launchZoneTracker = new LaunchZoneTracker();
        // Initializes the path builder
        pathBuilder = new PathBuilder();
        pathBuilder.initPathBuilder(pathFollower.follower, poseLib);
        // Initializes the autonomous path updater
        autonomousPath = new AutonomousPath();
        // Initializes the vision
        vision = new Vision();
        vision.initVision(hw);
    }

    public void update() {
        // Checks to make sure the program has not exceeded the run time, then if it has not updates all the wrappers
        if (autoTime.seconds() < runTime || infiniteRun) {
            flywheel.update();
            fidgetTech.update(flywheel.isPowered(), doNotSpin);
            pathFollower.update();
            launchZoneTracker.update(pathFollower.getPosition(), poseLib.targetPoint);
            vision.update();
        } else {
            stopRequested = true;
            flywheel.off();
            flywheel.update();
            intake.off();
        }
    }
    public void automaticPickup() {
        // Checks to see if a ball is loaded and the intake is on if so it loads the ball into the sorter
        if (intake.isPowered() && intakeSensor.isArtifactLoaded()) {
            update();
            if (fidgetTech.getSnapPoint() == 12 ) {
                fidgetTech.next();
                update();
            } else if(fidgetTech.getSnapPoint() == 14) {
                fidgetTech.next();
                update();
            } else if(fidgetTech.getSnapPoint() == 16) {
                fidgetTech.next();
                update();
            } else {
                fidgetTech.setSnapPoint(12);
                update();
            }
        }
        // Checks to see if a ball is in the sorter's intake if it is it spins the intake backward to prevent jamming
        if (!intakeSensor.isNothingDetected()) {
            if (fidgetTech.getSnapPoint() == 12 ) {
                FidgetTech.artifactsLoaded[0] = IntakeSensor.detectedColor.UNKNOWN;
            } else if(fidgetTech.getSnapPoint() == 14) {
                FidgetTech.artifactsLoaded[1] = IntakeSensor.detectedColor.UNKNOWN;
            } else if(fidgetTech.getSnapPoint() == 16) {
                FidgetTech.artifactsLoaded[2] = IntakeSensor.detectedColor.UNKNOWN;
            }
            intake.setPower(1,-0.25);
        } else intake.setPower(1,1);
    }

    public void shootAllBalls() {
        // Runs a chain of commands that shoots all the balls in the Fidget Tech
        intake.off();
        fidgetTech.setSnapPoint(11);
        timer.reset();
        while (timer.seconds() < 0.1) {
            update();
            if (stopRequested) break;
        }
        for (int i = 0; i < 3; i++) {
            if (stopRequested) break;
            setFlywheelToShootDistance();
            timer.reset();
            while (!flywheel.isAtSpeed()) {
                update();
                if (stopRequested) break;
            }
            ejector.fire();
            timer.reset();
            while (timer.seconds() < 0.2) {
                update();
                if (stopRequested) break;
            }
            ejector.reset();
            fidgetTech.next();
            timer.reset();
            while (timer.seconds() < 0.4) {
                update();
                if (stopRequested) break;
            }
        }
        FidgetTech.artifactsLoaded[0] = IntakeSensor.detectedColor.NONE;
        FidgetTech.artifactsLoaded[1] = IntakeSensor.detectedColor.NONE;
        FidgetTech.artifactsLoaded[2] = IntakeSensor.detectedColor.NONE;
        flywheel.off();
        fidgetTech.setSnapPoint(12);
    }
    public void startPosition(boolean atPromptEnd) {
        // Sets the start position for the bot
        if (atPromptEnd) {
            poseLib.assignStartPose();
        } else poseLib.saveStartPose(pathFollower);
    }
    public void followPath(PathChain path) {
        // Simplifies the path following command and locks hold end on true
        pathFollower.follower.followPath(path,true);
    }
    public boolean pathingIsBusy() {
        // Checks to see if the follower is busy
        return pathFollower.follower.isBusy();
    }

    public void autonomousPath() {
        // Runs the autonomous program
        autonomousPath.autonomousPathUpdate(this);
    }
    public void setFlywheelToShootDistance() {
        // Sets the flywheel to the desired speed based on the distance to the goal
        flywheel.setFlywheelFireDistance(launchZoneTracker.shootDistance);
    }
}
