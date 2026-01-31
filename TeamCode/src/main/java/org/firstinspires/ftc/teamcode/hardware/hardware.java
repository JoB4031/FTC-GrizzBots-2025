package org.firstinspires.ftc.teamcode.hardware;

import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.motors.ejector;
import org.firstinspires.ftc.teamcode.hardware.motors.fidgetTech;
import org.firstinspires.ftc.teamcode.hardware.motors.flywheel;
import org.firstinspires.ftc.teamcode.hardware.motors.intake;
import org.firstinspires.ftc.teamcode.hardware.pedroPathing.launchZoneTracker;
import org.firstinspires.ftc.teamcode.hardware.pedroPathing.pathBuilder;
import org.firstinspires.ftc.teamcode.hardware.pedroPathing.pathFollower;
import org.firstinspires.ftc.teamcode.hardware.pedroPathing.poseLibrary;
import org.firstinspires.ftc.teamcode.hardware.sensors.intakeSensor;

public class hardware {

    // Motor Wrappers
    public flywheel flywheel;
    public intake intake;
    public fidgetTech fidgetTech;
    public ejector ejector;

    // Sensor Wrappers
    public intakeSensor intakeSensor;

    // Pedro Pathing Wrappers
    public pathFollower pathFollower;
    public launchZoneTracker launchZoneTracker;
    public pathBuilder pathBuilder;
    public poseLibrary poseLib;
    public final ElapsedTime timer = new ElapsedTime();


    public void initHardware(HardwareMap hw) {

        flywheel = new flywheel();
        intake   = new intake();
        fidgetTech = new fidgetTech();
        ejector  = new ejector();


        flywheel.initFlywheel(hw);
        intake.initIntake(hw);
        fidgetTech.initFidgetTech(hw);
        ejector.initEjector(hw);


        intakeSensor = new intakeSensor();
        intakeSensor.initIntakeSensor(hw);

        pathFollower = new pathFollower();
        launchZoneTracker = new launchZoneTracker();
        pathBuilder = new pathBuilder();
        poseLib = new poseLibrary();
        poseLib.configureAlliancePaths();
        pathFollower.init(hw, poseLib);
        pathBuilder.initPathBuilder(pathFollower.follower, poseLib);

    }

    public void update() {
        flywheel.update();
        fidgetTech.update(flywheel.isAtSpeed(),false);
        pathFollower.update();
        launchZoneTracker.update(pathFollower.getPosition(), poseLib.targetPoint);
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
    }

    public void shootAllBalls() {
        intake.setPower(0);
        fidgetTech.setPosition(11);

        for (int i = 0; i < 3; i++) {

            setFlywheelToShootDistance();
            timer.reset();
            while (!flywheel.isAtSpeed() || timer.seconds() < .75) {
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
