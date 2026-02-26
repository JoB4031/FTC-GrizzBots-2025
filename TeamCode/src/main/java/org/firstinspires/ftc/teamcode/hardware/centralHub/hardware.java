package org.firstinspires.ftc.teamcode.hardware.centralHub;

import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.hardware.motors.Ejector;
import org.firstinspires.ftc.teamcode.hardware.motors.FidgetTech;
import org.firstinspires.ftc.teamcode.hardware.motors.Flywheel;
import org.firstinspires.ftc.teamcode.hardware.motors.Intake;
import org.firstinspires.ftc.teamcode.hardware.pedroPathing.LaunchZoneTracker;
import org.firstinspires.ftc.teamcode.hardware.pedroPathing.PathBuilder;
import org.firstinspires.ftc.teamcode.hardware.pedroPathing.PathFollower;
import org.firstinspires.ftc.teamcode.hardware.pedroPathing.PoseLibrary;
import org.firstinspires.ftc.teamcode.hardware.sensors.IntakeSensor;
import org.firstinspires.ftc.teamcode.hardware.sensors.LED;
import org.firstinspires.ftc.teamcode.hardware.vision.Vision;

import java.util.ArrayList;
import java.util.List;

public class hardware {
    private double runTime;
    public final ElapsedTime autoTime = new ElapsedTime();
    private final ElapsedTime fidgetReady = new ElapsedTime();
    public boolean resetFidgetReady = true;
    private boolean spinReady = false;
    // Motor Wrappers
    public Flywheel flywheel;
    public Intake intake;
    public FidgetTech fidgetTech;
    public Ejector ejector;

    // Sensor Wrappers
    public IntakeSensor intakeSensor;
    public LED led;

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
    public int[] spinPattern = {0, 0, 0};

    private TelemetryManager telemetryM;


    public void initHardware(HardwareMap hw, boolean auto, TelemetryManager telemetryManager) {
        // Sets up how long the program should run for before turning off
        if (auto) {
            runTime = 30;
        } else runTime = 999;
        autoTime.reset();
        // Initializes the flywheel
        flywheel = new Flywheel();
        flywheel.initFlywheel(hw);
        // Initializes the intake
        intake = new Intake();
        intake.initIntake(hw);
        // Initializes the Fidget Tech
        fidgetTech = new FidgetTech();
        fidgetTech.initFidgetTech(hw);
        // Initializes the ejector
        ejector = new Ejector();
        ejector.initEjector(hw);

        // Initializes the intake sensor
        intakeSensor = new IntakeSensor();
        intakeSensor.initIntakeSensor(hw);

        led = new LED();
        led.initLED(hw);

        // Initializes the pedro pathing positions based on alliance and start position
        poseLib = new PoseLibrary();
        poseLib.configureAlliancePaths();
        if (auto) {
            startPosition(true);
        }
        // Initializes the pedro pathing follower
        pathFollower = new PathFollower();
        pathFollower.init(hw, poseLib, launchZoneTracker);
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

        telemetryM = telemetryManager;
    }

    public void update() {
        // Checks to make sure the program has not exceeded the run time, then if it has not updates all the wrappers
        if (autoTime.seconds() < runTime || infiniteRun) {
            flywheel.update();
            fidgetTech.update(flywheel.isPowered(), doNotSpin);
            pathFollower.update();
            launchZoneTracker.update(pathFollower.getPosition(), poseLib.targetPoint);
            vision.update();
            sortAllBalls();
            telemetryM.addData("Flywheel RPM", flywheel.getRPM());
            telemetryM.addData("Ejector Position", ejector.getPosition());
            telemetryM.addData("Launch Distance", launchZoneTracker.shootDistance);
            telemetryM.update();
        } else stopRequested = true;
    }

    public void automaticPickup() {
        doNotSpin = true;
        if (resetFidgetReady) {
            fidgetReady.reset();
            resetFidgetReady = false;
            spinReady = false;
        }
        if (fidgetTechIsFull()) {
            update();
            resetFidgetReady = true;
        } else if (FidgetTech.artifactsLoaded[0] == IntakeSensor.detectedColor.NONE) {
            fidgetTech.setSnapPoint(12);
            if (intakeSensor.artifactedIntakeDetector.getDistance(DistanceUnit.INCH) > 2.5 || fidgetReady.seconds() > 1) {
                spinReady = true;
            }
            if (spinReady && ((intakeSensor.artifactedIntakeDetector.getDistance(DistanceUnit.INCH) < 2) && intakeSensor.getDetectedColor() != IntakeSensor.detectedColor.NONE)) {
                FidgetTech.artifactsLoaded[0] = intakeSensor.getDetectedColor();
                resetFidgetReady = true;
            }
        } else if (FidgetTech.artifactsLoaded[1] == IntakeSensor.detectedColor.NONE) {
            fidgetTech.setSnapPoint(14);
            if (intakeSensor.artifactedIntakeDetector.getDistance(DistanceUnit.INCH) > 2.5 || fidgetReady.seconds() > 1) {
                spinReady = true;
            }
            if (spinReady && ((intakeSensor.artifactedIntakeDetector.getDistance(DistanceUnit.INCH) < 2) && intakeSensor.getDetectedColor() != IntakeSensor.detectedColor.NONE)) {
                FidgetTech.artifactsLoaded[1] = intakeSensor.getDetectedColor();
                resetFidgetReady = true;
            }
        } else if (FidgetTech.artifactsLoaded[2] == IntakeSensor.detectedColor.NONE) {
            fidgetTech.setSnapPoint(16);
            if (intakeSensor.artifactedIntakeDetector.getDistance(DistanceUnit.INCH) > 2.5 || fidgetReady.seconds() > 1) {
                spinReady = true;
            }
            if (spinReady && ((intakeSensor.artifactedIntakeDetector.getDistance(DistanceUnit.INCH) < 2) && intakeSensor.getDetectedColor() != IntakeSensor.detectedColor.NONE)) {
                FidgetTech.artifactsLoaded[2] = intakeSensor.getDetectedColor();
                resetFidgetReady = true;
            }
        }
        if (!intakeSensor.isNothingDetected()) {
            if (fidgetTechIsFull()) {
                intake.off();
            } else intake.setPower(1, -0.1);
        } else intake.setPower(1, 1);
    }

    public void sortAllBalls() {
        if (Vision.pattern == Vision.BallPattern.PPG) {
            if ((FidgetTech.artifactsLoaded[0] == IntakeSensor.detectedColor.PURPLE) && (FidgetTech.artifactsLoaded[1] == IntakeSensor.detectedColor.PURPLE) && (FidgetTech.artifactsLoaded[2] == IntakeSensor.detectedColor.GREEN)) {
                spinPattern[0] = 9;
                spinPattern[1] = 11;
                spinPattern[2] = 13;
            } else if ((FidgetTech.artifactsLoaded[0] == IntakeSensor.detectedColor.PURPLE) && (FidgetTech.artifactsLoaded[1] == IntakeSensor.detectedColor.GREEN) && (FidgetTech.artifactsLoaded[2] == IntakeSensor.detectedColor.PURPLE)) {
                spinPattern[0] = 13;
                spinPattern[1] = 15;
                spinPattern[2] = 17;
            } else if ((FidgetTech.artifactsLoaded[0] == IntakeSensor.detectedColor.GREEN) && (FidgetTech.artifactsLoaded[1] == IntakeSensor.detectedColor.PURPLE) && (FidgetTech.artifactsLoaded[2] == IntakeSensor.detectedColor.PURPLE)) {
                spinPattern[0] = 11;
                spinPattern[1] = 13;
                spinPattern[2] = 15;
            } else {
                spinPattern[0] = 9;
                spinPattern[1] = 11;
                spinPattern[2] = 13;
            }
        } else if (Vision.pattern == Vision.BallPattern.PGP) {
            if ((FidgetTech.artifactsLoaded[0] == IntakeSensor.detectedColor.PURPLE) && (FidgetTech.artifactsLoaded[1] == IntakeSensor.detectedColor.PURPLE) && (FidgetTech.artifactsLoaded[2] == IntakeSensor.detectedColor.GREEN)) {
                spinPattern[0] = 11;
                spinPattern[1] = 13;
                spinPattern[2] = 15;
            } else if ((FidgetTech.artifactsLoaded[0] == IntakeSensor.detectedColor.PURPLE) && (FidgetTech.artifactsLoaded[1] == IntakeSensor.detectedColor.GREEN) && (FidgetTech.artifactsLoaded[2] == IntakeSensor.detectedColor.PURPLE)) {
                spinPattern[0] = 9;
                spinPattern[1] = 11;
                spinPattern[2] = 13;
            } else if ((FidgetTech.artifactsLoaded[0] == IntakeSensor.detectedColor.GREEN) && (FidgetTech.artifactsLoaded[1] == IntakeSensor.detectedColor.PURPLE) && (FidgetTech.artifactsLoaded[2] == IntakeSensor.detectedColor.PURPLE)) {
                spinPattern[0] = 13;
                spinPattern[1] = 15;
                spinPattern[2] = 17;
            } else {
                spinPattern[0] = 11;
                spinPattern[1] = 13;
                spinPattern[2] = 15;
            }
        } else if (Vision.pattern == Vision.BallPattern.GPP) {
            if ((FidgetTech.artifactsLoaded[0] == IntakeSensor.detectedColor.PURPLE) && (FidgetTech.artifactsLoaded[1] == IntakeSensor.detectedColor.PURPLE) && (FidgetTech.artifactsLoaded[2] == IntakeSensor.detectedColor.GREEN)) {
                spinPattern[0] = 13;
                spinPattern[1] = 15;
                spinPattern[2] = 17;
            } else if ((FidgetTech.artifactsLoaded[0] == IntakeSensor.detectedColor.PURPLE) && (FidgetTech.artifactsLoaded[1] == IntakeSensor.detectedColor.GREEN) && (FidgetTech.artifactsLoaded[2] == IntakeSensor.detectedColor.PURPLE)) {
                spinPattern[0] = 11;
                spinPattern[1] = 13;
                spinPattern[2] = 15;
            } else if ((FidgetTech.artifactsLoaded[0] == IntakeSensor.detectedColor.GREEN) && (FidgetTech.artifactsLoaded[1] == IntakeSensor.detectedColor.PURPLE) && (FidgetTech.artifactsLoaded[2] == IntakeSensor.detectedColor.PURPLE)) {
                spinPattern[0] = 9;
                spinPattern[1] = 11;
                spinPattern[2] = 13;
            } else {
                spinPattern[0] = 11;
                spinPattern[1] = 13;
                spinPattern[2] = 15;
            }
        } else if (Vision.pattern == null) {
            spinPattern[0] = 11;
            spinPattern[1] = 13;
            spinPattern[2] = 15;
        }
    }
    public void shootColorArtifact(IntakeSensor.detectedColor color){
        int ballFired = 0;
        if(color == IntakeSensor.detectedColor.PURPLE) {
            if (FidgetTech.artifactsLoaded[0] == IntakeSensor.detectedColor.PURPLE) {
                fidgetTech.setSnapPoint(9);
            } else if (FidgetTech.artifactsLoaded[1] == IntakeSensor.detectedColor.PURPLE) {
                fidgetTech.setSnapPoint(11);
                ballFired = 1;
            } else if (FidgetTech.artifactsLoaded[2] == IntakeSensor.detectedColor.PURPLE) {
                fidgetTech.setSnapPoint(13);
                ballFired = 2;

            } else {
                fidgetTech.setSnapPoint(11);
                ballFired = 1;
            }
        } else {
            if(FidgetTech.artifactsLoaded[0] == IntakeSensor.detectedColor.GREEN) {
                fidgetTech.setSnapPoint(9);
            } else if(FidgetTech.artifactsLoaded[1] == IntakeSensor.detectedColor.GREEN) {
                fidgetTech.setSnapPoint(11);
                ballFired = 1;
            } else if(FidgetTech.artifactsLoaded[2] == IntakeSensor.detectedColor.GREEN) {
                fidgetTech.setSnapPoint(13);
                ballFired = 2;
            } else {
                fidgetTech.setSnapPoint(11);
                ballFired = 1;
            }
        }
        FidgetTech.artifactsLoaded[ballFired] = IntakeSensor.detectedColor.NONE;

    }
    public void shootAllBalls(boolean sort) {
        if (sort) {
            // Runs a chain of commands that shoots all the balls in the Fidget Tech
            intake.off();
            fidgetTech.setSnapPoint(spinPattern[0]);
            setFlywheelToShootDistance();
            for (int i = 0; i < 3; i++) {
                if (stopRequested) break;
                setFlywheelToShootDistance();
                timer.reset();
                flywheel.flywheelStable.reset();
                while (!flywheel.isAtSpeed()) {
                    update();
                    if (timer.seconds() > 3) {
                        break;
                    }
                    if (stopRequested) break;
                }
                ejector.fire();
                timer.reset();
                while (timer.seconds() < 0.2) {
                    setFlywheelToShootDistance();
                    update();
                    if (stopRequested) break;
                }
                ejector.reset();
                if(i != 2) {
                    fidgetTech.setSnapPoint(spinPattern[i + 1]);
                }
                timer.reset();
                while (timer.seconds() < 0.4) {
                    setFlywheelToShootDistance();
                    update();
                    if (stopRequested) break;
                }
            }
            flywheel.off();
            FidgetTech.artifactsLoaded[0] = IntakeSensor.detectedColor.NONE;
            FidgetTech.artifactsLoaded[1] = IntakeSensor.detectedColor.NONE;
            FidgetTech.artifactsLoaded[2] = IntakeSensor.detectedColor.NONE;
            fidgetTech.setSnapPoint(12);
        } else {
            // Runs a chain of commands that shoots all the balls in the Fidget Tech
            intake.off();
            fidgetTech.setSnapPoint(11);
            setFlywheelToShootDistance();
            timer.reset();
            while (timer.seconds() < 0.5) {
                update();
                if (stopRequested) break;
            }
            for (int i = 0; i < 3; i++) {
                if (stopRequested) break;
                setFlywheelToShootDistance();
                timer.reset();
                flywheel.flywheelStable.reset();
                while (!flywheel.isAtSpeed()) {
                    update();
                    if (timer.seconds() > 3) {
                        break;
                    }
                    if (stopRequested) break;
                }
                ejector.fire();
                timer.reset();
                while (timer.seconds() < 0.4) {
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
            flywheel.off();
            FidgetTech.artifactsLoaded[0] = IntakeSensor.detectedColor.NONE;
            FidgetTech.artifactsLoaded[1] = IntakeSensor.detectedColor.NONE;
            FidgetTech.artifactsLoaded[2] = IntakeSensor.detectedColor.NONE;
            fidgetTech.setSnapPoint(12);
        }
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
        if(launchZoneTracker.shootDistance > 2.5) {
            flywheel.setFlywheelFarFire(launchZoneTracker.shootDistance);
        } else {
            flywheel.setFlywheelNearFire(launchZoneTracker.shootDistance);
        }
    }
    public boolean fidgetTechIsFull() {
        return (FidgetTech.artifactsLoaded[0] != IntakeSensor.detectedColor.NONE) && (FidgetTech.artifactsLoaded[1] != IntakeSensor.detectedColor.NONE) && (FidgetTech.artifactsLoaded[2] != IntakeSensor.detectedColor.NONE);
    }
}
