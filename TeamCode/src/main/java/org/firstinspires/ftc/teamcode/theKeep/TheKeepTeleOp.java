/*
The Keep Version 2.4.0
Changelog:
Fixed the issue where the shoot all balls function would
not allow the bot to move once the function started.
Also fixed a few bugs that surfaced during the 1/17/2026
scrimmage.
*/
package org.firstinspires.ftc.teamcode.theKeep;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.hardware.centralHub.hardware;
import org.firstinspires.ftc.teamcode.hardware.motors.FidgetTech;
import org.firstinspires.ftc.teamcode.hardware.motors.Flywheel;
import org.firstinspires.ftc.teamcode.hardware.sensors.IntakeSensor;
import org.firstinspires.ftc.teamcode.hardware.vision.Vision;

@TeleOp(name="The Keep TeleOp", group="The Keep")
public class TheKeepTeleOp extends OpMode {

    // creates new variables to store our new instances of the hardware classes - Jason
    private hardware robot;
    private double movementMultiplier;
    private boolean drive = false;
    private TelemetryManager telemetryM;

    @Override
    public void init() {
        // Creates a new instance of the hardware classes - Jason
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        robot = new hardware();
        robot.initHardware(hardwareMap, false, telemetryM);
        // Call the hardware init methods - Jason
        if (TheKeepAuto.alliance == TheKeepAuto.Alliance.BLUE && !TheKeepAuto.robotCentric) {
            movementMultiplier = -1;
        } else movementMultiplier =  1;
        // Reports the status - Jason
        telemetry.addData("Status", "Initialized");
        telemetry.update();

    }

    @Override
    public void start() {
        telemetry.clear();
        robot.autoTime.reset();
        // Turns Pedro Path into TeleOp drive mode - Jason
        robot.pathFollower.follower.startTeleopDrive();

    } // This runs once when the play button is pressed
    @Override
    public void loop() {
        robot.update();
        // Updates all the hardware - Jason

        // This tells the follower to activate manual drive mode if it is not following a path -Jason
        if (!robot.automatedDrive) {
            if (TheKeepAuto.alliance == TheKeepAuto.Alliance.BLUE && !TheKeepAuto.robotCentric) {
                if (robot.intake.isPowered() || robot.flywheel.isPowered()) {
                    robot.pathFollower.follower.setTeleOpDrive(
                            -gamepad1.left_stick_y*movementMultiplier,
                            -gamepad1.left_stick_x*movementMultiplier,
                            (gamepad1.right_stick_x*0.5)*movementMultiplier,
                            TheKeepAuto.robotCentric// Robot Centric
                    );
                } else {
                    robot.pathFollower.follower.setTeleOpDrive(
                            -gamepad1.left_stick_y*movementMultiplier,
                            -gamepad1.left_stick_x*movementMultiplier,
                            gamepad1.right_stick_x*movementMultiplier,
                            TheKeepAuto.robotCentric // Robot Centric
                    );
                }
            } else {
                if (robot.intake.isPowered() || robot.flywheel.isPowered()) {
                    robot.pathFollower.follower.setTeleOpDrive(
                            -gamepad1.left_stick_y*movementMultiplier,
                            -gamepad1.left_stick_x*movementMultiplier,
                            -(gamepad1.right_stick_x*0.5)*movementMultiplier,
                            TheKeepAuto.robotCentric// Robot Centric
                    );
                } else {
                    robot.pathFollower.follower.setTeleOpDrive(
                            -gamepad1.left_stick_y*movementMultiplier,
                            -gamepad1.left_stick_x*movementMultiplier,
                            -gamepad1.right_stick_x*movementMultiplier,
                            TheKeepAuto.robotCentric // Robot Centric
                    );
                }
            }
        }

        // Turns the bots heading to face the alliance goal - Jason
        if (gamepad1.dpadLeftWasPressed()) {
            robot.doNotSpin = true;
            robot.setFlywheelToShootDistance();
            robot.followPath(robot.pathFollower.turnToGoal.get());
            while (robot.pathingIsBusy()) {
                robot.update();
            }
            robot.doNotSpin = false;
            robot.shootColorArtifact(IntakeSensor.detectedColor.PURPLE);
            robot.pathFollower.follower.startTeleopDrive();
            robot.automatedDrive = false;
        }

        if (gamepad1.dpadRightWasPressed()) {
            robot.doNotSpin = true;
            robot.setFlywheelToShootDistance();
            robot.followPath(robot.pathFollower.turnToGoal.get());
            while (robot.pathingIsBusy()) {
                robot.update();
            }
            robot.doNotSpin = false;
            robot.shootColorArtifact(IntakeSensor.detectedColor.GREEN);
            robot.pathFollower.follower.startTeleopDrive();
            robot.automatedDrive = false;
        }

        if (gamepad1.dpadUpWasPressed()) {
            robot.doNotSpin = true;
            robot.fidgetTech.setSnapPoint(robot.spinPattern[0]);
            robot.setFlywheelToShootDistance();
            robot.followPath(robot.pathFollower.turnToGoal.get());
            robot.timer.reset();
            while (robot.pathingIsBusy() || robot.timer.seconds() < 2) {
                robot.update();
            }
            robot.doNotSpin = false;
            robot.shootAllBalls(true);
            robot.pathFollower.follower.startTeleopDrive();
            robot.automatedDrive = false;
        }

        if (gamepad1.dpadDownWasPressed()) {
            robot.doNotSpin = true;
            robot.fidgetTech.setSnapPoint(11);
            robot.setFlywheelToShootDistance();
            robot.followPath(robot.pathFollower.turnToGoal.get());
            robot.timer.reset();
            while (robot.pathingIsBusy() || robot.timer.seconds() < 2) {
                robot.update();
            }
            robot.doNotSpin = false;
            robot.shootAllBalls(false);
            robot.pathFollower.follower.startTeleopDrive();
            robot.automatedDrive = false;
        }

        if (gamepad1.cross) {
            drive = true;
            robot.followPath(robot.pathFollower.returnToBase.get());
            robot.update();
        } else {
            if (drive) {
                robot.pathFollower.follower.startTeleopDrive();
                robot.automatedDrive = false;
                drive = false;
            }
        }


        // These lines set the flywheel to the required speed depending on the distance if the circle button is pressed and 0% if its not
        if ((gamepad1.left_trigger > 0)) {
            robot.setFlywheelToShootDistance();
            robot.intake.off();
        } else {
            robot.flywheel.off();
        }

        /* These lines check to see if the fidget tech is in the way of the ball ejector if it's not, when
        you press the triangle it will swing knocking out the ball - Jason */
        if ((gamepad1.right_trigger > 0 && robot.fidgetTech.getSnapPoint() % 2 == 1 && robot.flywheel.isAtSpeed() && robot.launchZoneTracker.robotInRange) || (gamepad1.right_trigger > 0 && gamepad1.right_stick_button)) {
                robot.ejector.fire();
        } else {
            robot.ejector.reset();
        }

        // This if statement turns the intake on when the circle is pressed and off when the square is pressed - Jason
        if (gamepad1.circleWasPressed()) {

            if (!robot.intake.isPowered()) {
                robot.intake.on();
                robot.resetFidgetReady = true;
                if (robot.fidgetTechIsFull()) {
                    FidgetTech.artifactsLoaded[0] = IntakeSensor.detectedColor.NONE;
                    FidgetTech.artifactsLoaded[1] = IntakeSensor.detectedColor.NONE;
                    FidgetTech.artifactsLoaded[2] = IntakeSensor.detectedColor.NONE;
                }
            } else robot.intake.off();
        }

        if (robot.intake.isPowered()) {
            robot.automaticPickup();
        } else robot.doNotSpin = false;

        // This if loop makes the robot shoot all the artifacts - Nikola
        if (gamepad1.triangleWasPressed()) {
            robot.intake.setPower(-1,-1);
        }

        if (gamepad1.square) {
            if (Math.abs(movementMultiplier) == 1) {
                movementMultiplier = movementMultiplier*.25;
            }
        } else if (Math.abs(movementMultiplier) == 0.25){
            movementMultiplier = movementMultiplier*4;
        }

        // Moves the Fidget Tech forward or backward one step depending on which trigger was pressed
        if (gamepad1.leftBumperWasPressed()) robot.fidgetTech.previous();
        if (gamepad1.rightBumperWasPressed()) robot.fidgetTech.next();

        if (gamepad2.leftBumperWasPressed()) Flywheel.shotMultiplier -= 0.1;
        if (gamepad2.rightBumperWasPressed()) Flywheel.shotMultiplier += 0.1;
        // These lines write any april tag data to the telemetry - Jason
        if (Vision.pattern != null) {
           telemetry.addData("Pattern Is", Vision.pattern);
        } else {
           telemetry.addData("Pattern Is", "unknown");
        }

        if(robot.fidgetTechIsFull()) {
            if(robot.launchZoneTracker.robotInRange) {
                if(TheKeepAuto.alliance == TheKeepAuto.Alliance.BLUE) {
                    robot.led.blue();
                } else robot.led.red();
            } else robot.led.white();
        } else robot.led.white();

        // These lines add the fidget tech's position and the bot's position to the telemetry - Jason
        telemetry.addData("Flywheel Speed In RPM",robot.flywheel.getRPM());
        telemetry.addData("Fidget Tech Position", robot.fidgetTech.getSnapPoint());
        telemetry.addData("Bot Position", robot.pathFollower.getPosition());
        telemetry.addData("Distance to Goal", robot.launchZoneTracker.shootDistance);
        telemetry.addData("Goal in range", robot.launchZoneTracker.robotInRange);
        telemetry.addData("Additional Power", Flywheel.shotMultiplier);
        telemetry.addData("Artifacts Held", FidgetTech.artifactsLoaded[0]);
        telemetry.addData("Artifacts Held", FidgetTech.artifactsLoaded[1]);
        telemetry.addData("Artifacts Held", FidgetTech.artifactsLoaded[2]);
        telemetry.addData("Artifact Spots" , robot.spinPattern[0]);
        telemetry.addData("Artifact Spots" , robot.spinPattern[1]);
        telemetry.addData("Artifact Spots" , robot.spinPattern[2]);
        telemetry.addData("Infinite Run", robot.infiniteRun);
        telemetry.addData("AprilTag Data", robot.vision.getLocation());
        telemetry.update();

        telemetryM.addData("Flywheel RPM", robot.flywheel.getRPM());
        telemetryM.addData("Ejector Position", robot.ejector.getPosition());
        telemetryM.addData("Launch Distance", robot.launchZoneTracker.shootDistance);
        telemetryM.update();

    } // This section holds all the controls used during TeleOp

    @Override
    public void stop() {
        // Sets the bots starting position to our end position - Jason
        robot.startPosition(false);
    }
}
