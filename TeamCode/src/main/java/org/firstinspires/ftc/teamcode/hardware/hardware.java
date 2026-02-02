package org.firstinspires.ftc.teamcode.hardware;

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
    public final ElapsedTime timer = new ElapsedTime();

    public Vision vision;

    public boolean automatedDrive = false;
    public boolean doNotSpin = false;


    public void initHardware(HardwareMap hw) {

        flywheel = new Flywheel();
        flywheel.initFlywheel(hw);

        intake   = new Intake();
        intake.initIntake(hw);

        fidgetTech = new FidgetTech();
        fidgetTech.initFidgetTech(hw);

        ejector  = new Ejector();
        ejector.initEjector(hw);


        intakeSensor = new IntakeSensor();
        intakeSensor.initIntakeSensor(hw);

        poseLib = new PoseLibrary();
        poseLib.configureAlliancePaths();

        pathFollower = new PathFollower();
        pathFollower.init(hw, poseLib);

        launchZoneTracker = new LaunchZoneTracker();

        pathBuilder = new PathBuilder();
        pathBuilder.initPathBuilder(pathFollower.follower, poseLib);

        vision = new Vision();
        vision.initVision(hw);
    }

    public void update() {
        flywheel.update();
        fidgetTech.update(flywheel.isPowered(), doNotSpin);
        pathFollower.update();
        launchZoneTracker.update(pathFollower.getPosition(), poseLib.targetPoint);
        vision.update();

    }
    public void automaticPickup() {
        if (intake.isPowered() && intakeSensor.isArtifactLoaded()) {
            update();
            if (fidgetTech.getPosition() == 12 ) {
                fidgetTech.advance();
                update();
            } else if(fidgetTech.getPosition() == 14) {
                fidgetTech.advance();
                update();
            } else if(fidgetTech.getPosition() == 16) {
                fidgetTech.advance();
                update();
            } else {
                fidgetTech.setPosition(12);
                update();
            }
        }
        if (!intakeSensor.isNothingDetected()) {
            intake.setPower(1,0);
        }
    }

    public void shootAllBalls() {
        intake.off();
        fidgetTech.setPosition(11);

        for (int i = 0; i < 3; i++) {

            setFlywheelToShootDistance();
            timer.reset();
            while (!flywheel.isAtSpeed()) {
                update();
            }
            ejector.fire();
            timer.reset();
            while (timer.seconds() < 0.5) update();

            ejector.reset();
            fidgetTech.advance();
        }
    }
    public void startPosition(boolean atPromptEnd) {
        if (atPromptEnd) {
            poseLib.assignStartPose();
        } else poseLib.saveStartPose(pathFollower);
    }
    public void followPath(PathChain path) {
        pathFollower.follower.followPath(path,true);
    }
    public boolean pathingIsBusy() {
        return pathFollower.follower.isBusy();
    }
    public void setFlywheelToShootDistance() {
        flywheel.setFlywheelFireDistance(launchZoneTracker.shootDistance);
    }
}
