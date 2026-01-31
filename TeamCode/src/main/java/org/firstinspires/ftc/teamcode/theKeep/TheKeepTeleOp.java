/*
The Keep Version 2.4.0
Changelog:
Fixed the issue where the shoot all balls function would
not allow the bot to move once the function started.
Also fixed a few bugs that surfaced during the 1/17/2026
scrimmage.
*/
package org.firstinspires.ftc.teamcode.theKeep;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.hardware.Motors;
import org.firstinspires.ftc.teamcode.hardware.PedroPathing;
import org.firstinspires.ftc.teamcode.hardware.Sensors;
import org.firstinspires.ftc.teamcode.hardware.Vision;
@TeleOp(name="The Keep TeleOp", group="The Keep")
public class TheKeepTeleOp extends OpMode {

    // creates new variables to store our new instances of the hardware classes - Jason
    private Vision vision;
    private PedroPathing pathing;
    private Motors motors;
    private Sensors sensors;
    private double additionalLaunchPower = 0;

    private double movementMultiplier;

    @Override
    public void init() {
        // Creates a new instance of the hardware classes - Jason
        vision = new Vision();
        pathing = new PedroPathing();
        motors = new Motors();
        sensors =  new Sensors();
        pathing.setArtifact();
        // Call the hardware init methods - Jason
        vision.initAprilTag(hardwareMap);
        pathing.initFollower(hardwareMap);
        motors.initMotors(hardwareMap);
        sensors.initSensors(hardwareMap);
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
        // Turns Pedro Path into TeleOp drive mode - Jason
        pathing.follower.startTeleopDrive();
    } // This runs once when the play button is pressed
    @Override
    public void loop() {

        // Updates all the hardware - Jason
        pathing.update();
        motors.update();
        vision.update();
        sensors.update();

        // This tells the follower to activate manual drive mode if it is not following a path -Jason
        if (!pathing.automatedDrive) {
            if (TheKeepAuto.alliance == TheKeepAuto.Alliance.BLUE && !TheKeepAuto.robotCentric) {
                if (motors.intake.getPower() > 0 || motors.velocityController.getSetPoint() > 0) {
                    pathing.follower.setTeleOpDrive(
                            -gamepad1.left_stick_y*movementMultiplier,
                            -gamepad1.left_stick_x*movementMultiplier,
                            (gamepad1.right_stick_x*0.5)*movementMultiplier,
                            TheKeepAuto.robotCentric// Robot Centric
                    );
                } else {
                    pathing.follower.setTeleOpDrive(
                            -gamepad1.left_stick_y*movementMultiplier,
                            -gamepad1.left_stick_x*movementMultiplier,
                            gamepad1.right_stick_x*movementMultiplier,
                            TheKeepAuto.robotCentric // Robot Centric
                    );
                }
            } else {
                if (motors.intake.getPower() > 0 || motors.velocityController.getSetPoint() > 0) {
                    pathing.follower.setTeleOpDrive(
                            -gamepad1.left_stick_y*movementMultiplier,
                            -gamepad1.left_stick_x*movementMultiplier,
                            -(gamepad1.right_stick_x*0.5)*movementMultiplier,
                            TheKeepAuto.robotCentric// Robot Centric
                    );
                } else {
                    pathing.follower.setTeleOpDrive(
                            -gamepad1.left_stick_y*movementMultiplier,
                            -gamepad1.left_stick_x*movementMultiplier,
                            -gamepad1.right_stick_x*movementMultiplier,
                            TheKeepAuto.robotCentric // Robot Centric
                    );
                }
            }
        }

        // Turns the bots heading to face the alliance goal - Jason
        if (gamepad1.crossWasPressed() && pathing.robotInRange) {
            if (pathing.farLaunch) {
                motors.flywheelPower = 6.8 + additionalLaunchPower;
                motors.setIntake(0);
                motors.spinPosition = 12;
                motors.doNotSpin = true;
                pathing.follower.followPath(pathing.turnToGoal.get(),true);
                motors.setFlywheelVelocity(pathing.shootDistance);
                pathing.automatedDrive = true;
                while (pathing.follower.isBusy()) {
                    pathing.update();
                    motors.update();
                    vision.update();
                    motors.setFlywheelVelocity(pathing.shootDistance);
                }
                motors.time.reset();
                while (motors.time.seconds() < 1 ) {
                    pathing.update();
                    motors.update();
                    vision.update();
                    motors.setFlywheelVelocity(pathing.shootDistance);
                }
                int shootAll = 0;
                motors.spinPosition = 11;
                motors.doNotSpin = false;
                while (shootAll < 3) {
                    vision.update();
                    motors.setFlywheelVelocity(pathing.shootDistance);
                    motors.time.reset();
                    while (!motors.ableToShoot || motors.time.seconds() < 1.5) {
                        //just chill
                        motors.update();
                        pathing.update();
                    }
                    motors.ballEjector.setPosition(0.3);
                    motors.time.reset();
                    while (motors.time.seconds() < 0.25) {
                        //you get to chill again
                        motors.update();
                        pathing.update();
                    }
                    motors.ballEjector.setPosition(0);
                    motors.spinPosition += 2;
                    shootAll += 1;
                }
            } else {
                motors.flywheelPower = 6.9 + additionalLaunchPower;
                motors.setIntake(0);
                motors.spinPosition = 12;
                motors.doNotSpin = true;
                pathing.follower.followPath(pathing.turnToGoal.get(), true);
                motors.setFlywheelVelocity(pathing.shootDistance);
                pathing.automatedDrive = true;
                while (pathing.follower.isBusy()) {
                    pathing.update();
                    motors.update();
                    vision.update();
                    motors.setFlywheelVelocity(pathing.shootDistance);
                }
                motors.time.reset();
                while (motors.time.seconds() < 1) {
                    pathing.update();
                    motors.update();
                    vision.update();
                    motors.setFlywheelVelocity(pathing.shootDistance);
                }
                int shootAll = 0;
                motors.spinPosition = 11;
                motors.doNotSpin = false;
                while (shootAll < 3) {
                    vision.update();
                    motors.setFlywheelVelocity(pathing.shootDistance);
                    motors.time.reset();
                    while (!motors.ableToShoot || motors.time.seconds() < .75) {
                        //just chill
                        motors.update();
                        pathing.update();
                    }
                    motors.ballEjector.setPosition(0.3);
                    motors.time.reset();
                    while (motors.time.seconds() < 0.25) {
                        //you get to chill again
                        motors.update();
                        pathing.update();
                    }
                    motors.ballEjector.setPosition(0);
                    motors.spinPosition += 2;
                    shootAll += 1;
                }
            }

        }

        // The dpad down button is used as an emergency stop - Jason
        if (pathing.automatedDrive && (gamepad1.dpadDownWasPressed() || !pathing.follower.isBusy())) {
            pathing.follower.startTeleopDrive();
            pathing.automatedDrive = false;
        } // Switches to TeleOp drive if the follower is done - Jason

        // These lines set the flywheel to the required speed depending on the distance if the circle button is pressed and 0% if its not
        if ((gamepad1.left_trigger > 0 || gamepad1.cross)) {
            motors.setFlywheelVelocity(pathing.shootDistance);
            motors.setIntake(0);
        } else {
            motors.setFlywheelVelocity(0);
        }

        /* These lines check to see if the fidget tech is in the way of the ball ejector if it's not, when
        you press the triangle it will swing knocking out the ball - Jason */
        if ((gamepad1.right_trigger > 0 && motors.spinPosition % 2 == 1 && motors.ableToShoot && pathing.robotInRange) || (gamepad1.right_trigger > 0 && gamepad1.dpad_up)) {
                motors.ballEjector.setPosition(.3);
        } else {
            motors.ballEjector.setPosition(0);
        }

        // This if statement turns the intake on when the circle is pressed and off when the square is pressed - Jason
        if (gamepad1.circleWasPressed()) {
            if (motors.intake.getPower() == 0) {
                motors.setIntake(1);
                motors.spinPosition = 12;
            } else motors.setIntake(0);
        }

        if (motors.intake.getPower() > 0) {
            if (sensors.nothingDetected) {
                motors.leftIntake.setPosition(0);
                motors.rightIntake.setPosition(1);
            } else {
                motors.leftIntake.setPosition(0.5);
                motors.rightIntake.setPosition(0.5);
            }
        }
        if (motors.intake.getPower() > 0 && sensors.artifactLoaded) {
            sensors.update();
            if (motors.spinPosition == 12 ) {
                motors.spinPosition = 14;
                sensors.update();
            } else if(motors.spinPosition == 14) {
                motors.spinPosition = 16;
                sensors.update();
            } else if(motors.spinPosition == 16) {
                motors.spinPosition = 12;
                sensors.update();
            } else {
                motors.spinPosition = 12;
                sensors.update();
            }
        }

        // This if loop makes the robot shoot all the artifacts - Nikola
        if (gamepad1.triangleWasPressed() && pathing.robotInRange) {
            int shootAll = 0;
            motors.setIntake(0);
            motors.spinPosition = 11;
            while (shootAll < 3) {
                motors.setFlywheelVelocity(pathing.shootDistance);
                motors.time.reset();
                while (!motors.ableToShoot || motors.time.seconds() < .75) {
                    //just chill
                    motors.update();
                    pathing.update();
                }
                motors.ballEjector.setPosition(0.3);
                motors.time.reset();
                while (motors.time.seconds() < 0.25) {
                    //you get to chill again
                    motors.update();
                    pathing.update();
                }
                motors.ballEjector.setPosition(0);
                motors.spinPosition += 2;
                shootAll += 1;
            }
        }

        if (gamepad1.squareWasPressed()) {
            if (movementMultiplier == 1 || movementMultiplier ==-1) {
                movementMultiplier = movementMultiplier*.5;
            } else {
                movementMultiplier = movementMultiplier*2;
            }
        }

        // Moves the Fidget Tech forward or backward one step depending on which trigger was pressed
        if (gamepad1.leftBumperWasPressed() && motors.spinPosition >= 2) motors.spinPosition -= 2;
        if (gamepad1.rightBumperWasPressed() && motors.spinPosition <= 25) motors.spinPosition += 2;

        if (gamepad2.leftBumperWasPressed()) additionalLaunchPower -= 0.1;
        if (gamepad2.rightBumperWasPressed()) additionalLaunchPower += 0.1;
        // These lines write any april tag data to the telemetry - Jason
        if (vision.allianceBase != null) {
           telemetry.addData("Alliance Base Range", vision.allianceBase.ftcPose.range);
        }
        if (Vision.pattern != null) {
           telemetry.addData("Pattern Is", Vision.pattern);
        } else {
           telemetry.addData("Pattern Is", "unknown");
        }

        // These lines add the fidget tech's position and the bot's position to the telemetry - Jason
        telemetry.addData("Flywheel Error", motors.velocityController.getVelocityError());
        telemetry.addData("Flywheel Speed", motors.rightFlywheel.getVelocity());
        telemetry.addData("Fidget Tech Position", motors.spinPositions[motors.spinPosition]);
        telemetry.addData("Bot Position", pathing.follower.getPose());
        telemetry.addData("Artifact Distance", sensors.artifactedIntakeDetector.getDistance(DistanceUnit.INCH));
        telemetry.addData("Distance to Goal", pathing.shootDistance);
        telemetry.addData("Goal in range", pathing.robotInRange);
        telemetry.addData("Flywheel Strength", motors.flywheelPower);
        telemetry.addData("Additional Power", additionalLaunchPower);
        telemetry.update();

    } // This section holds all the controls used during TeleOp

    @Override
    public void stop() {
        // Sets the bots starting position to our end position - Jason
        pathing.setStartPose(false);
    }
}
