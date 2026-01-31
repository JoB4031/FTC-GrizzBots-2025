/*
The Keep Auto Version 4.1.0
Changelog:
Had to do a major overhaul of the auto which mainly included fixing bugs.
Also added a process to hold positions during auto.
*/

package org.firstinspires.ftc.teamcode.theKeep;

import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.skeletonarmy.marrow.prompts.BooleanPrompt;
import com.skeletonarmy.marrow.prompts.OptionPrompt;
import com.skeletonarmy.marrow.prompts.Prompter;
import org.firstinspires.ftc.teamcode.hardware.Motors;
import org.firstinspires.ftc.teamcode.hardware.PedroPathing;
import org.firstinspires.ftc.teamcode.hardware.Sensors;
import org.firstinspires.ftc.teamcode.hardware.Vision;

@Autonomous(name="The Keep Auto",group="The Keep")
public class TheKeepAuto extends OpMode {

    private Timer opmodeTimer;
    private Motors motors;
    private Vision vision;
    private PedroPathing pathing;
    private Sensors sensors;
    private boolean startUpdate = false;
    private double shotDelay = 0.75;
    private double defaultPower = 6.9;

    // These next lines setup the variables used to store prompter values, which define the autonomous is used - Jason
    public enum Alliance {
        RED,
        BLUE
    }

    private Prompter prompter;
    public static Alliance alliance;
    public static int startLocation;
    public static boolean robotCentric;


    @Override
    public void init() {
        opmodeTimer = new Timer();
        vision = new Vision();
        pathing = new PedroPathing();
        motors = new Motors();
        sensors = new Sensors();
        prompter = new Prompter(this);

        // Call their init methods
        vision.initAprilTag(hardwareMap);
        motors.initMotors(hardwareMap);
        sensors.initSensors(hardwareMap);
        //Sets up the prompter - Jason
        prompter.prompt("alliance", new OptionPrompt<>("Select Alliance", Alliance.RED, Alliance.BLUE))
                .prompt("startLocation", new OptionPrompt<>("Select Start Location", 1, 2))
                .prompt("robotCentric", new BooleanPrompt("Robot Centric", true))
                .onComplete(this::onPromptsComplete);

    } // This initializes all the motors and sensors

    // Function that runs when the prompter is done that stores the values and prints the results - Jason
    public void onPromptsComplete() {
        alliance = prompter.get("alliance");
        startLocation = prompter.get("startLocation");
        robotCentric = prompter.get("robotCentric");
        pathing.setStartPose(true);
        pathing.setArtifact();
        pathing.initFollower(hardwareMap);
        pathing.buildPaths();
        startUpdate = true;
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
        if (startUpdate) {
            pathing.update();
            telemetry.addData("Start Position", pathing.follower.getPose());
        }
    } // A loop that runs from when the init button is pressed to when the start button is hit

    @Override
    public void start() {
        telemetry.clear();
        opmodeTimer.resetTimer();
        if (startLocation == 1) {
            shotDelay = 1.5;
            defaultPower =  6.8;
        }
        motors.flywheelPower = defaultPower;
    }

    @Override
    public void loop() {
        // Updates the hardware - Jason
        pathing.update();
        motors.update();
        vision.update();
        sensors.update();


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

    public void autonomousPathUpdate() {
        switch (pathing.pathState) {
            case 0:
                pathing.follower.followPath(pathing.scoreArtifact, true);
                motors.doNotSpin = true;
                motors.setFlywheelVelocity(pathing.shootDistance);
                pathing.setPathState(1);
                break;
            case 1:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!pathing.follower.isBusy()) {
                    motors.time.reset();
                    motors.setIntake(0);
                    while (motors.time.seconds() < 1) {
                        motors.update();
                        pathing.update();
                        motors.setFlywheelVelocity(pathing.shootDistance);
                    }
                    motors.doNotSpin = false;
                    int shootAll = 0;
                    motors.spinPosition = 11;
                    while (shootAll < 3) {
                        motors.setFlywheelVelocity(pathing.shootDistance);
                        motors.time.reset();
                        while (!motors.ableToShoot || motors.time.seconds() < shotDelay) {
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
                    pathing.setPathState(2);
                }
                break;
            case 2:
                if (!pathing.follower.isBusy()) {
                    motors.setFlywheelVelocity(0);
                    motors.setIntake(1);
                    motors.spinPosition = 12;
                    motors.update();
                    pathing.follower.followPath(pathing.grabFirstArtifacts, 0.75, true);
                    pathing.setPathState(3);
                }
                break;
            case 3:

                if (motors.intake.getPower() > 0 && sensors.artifactLoaded) {
                    sensors.update();
                    if (motors.spinPosition == 12) {
                        motors.spinPosition = 14;
                        sensors.update();
                    } else if (motors.spinPosition == 14) {
                        motors.spinPosition = 16;
                        sensors.update();
                    } else if (motors.spinPosition == 16) {
                        motors.spinPosition = 12;
                        sensors.update();
                    } else {
                        motors.spinPosition = 12;
                        sensors.update();
                    }
                }
                if (!pathing.follower.isBusy()) {
                    motors.time.reset();
                    while (motors.time.seconds() < 2) {
                        motors.update();
                        pathing.update();

                        if (motors.intake.getPower() > 0 && sensors.artifactLoaded) {
                            sensors.update();
                            if (motors.spinPosition == 12) {
                                motors.spinPosition = 14;
                                sensors.update();
                            } else if (motors.spinPosition == 14) {
                                motors.spinPosition = 16;
                                sensors.update();
                            } else if (motors.spinPosition == 16) {
                                motors.spinPosition = 12;
                                sensors.update();
                            } else {
                                motors.spinPosition = 12;
                                sensors.update();
                            }
                        }
                    }
                    motors.doNotSpin = true;
                    motors.setFlywheelVelocity(pathing.shootDistance);
                    pathing.follower.followPath(pathing.scoreFirstArtifacts, true);
                    pathing.setPathState(4);
                }
                break;
            case 4:
                if (!pathing.follower.isBusy()) {
                    motors.time.reset();
                    motors.setIntake(0);
                    while (motors.time.seconds() < 1) {
                        motors.update();
                        pathing.update();
                        motors.setFlywheelVelocity(pathing.shootDistance);
                    }
                    motors.doNotSpin = false;
                    int shootAll = 0;
                    motors.spinPosition = 11;
                    while (shootAll < 3) {
                        motors.setFlywheelVelocity(pathing.shootDistance);
                        motors.time.reset();
                        while (!motors.ableToShoot || motors.time.seconds() < shotDelay) {
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
                    pathing.setPathState(5);
                }
                break;
            case 5:
                if (!pathing.follower.isBusy()) {
                    motors.setFlywheelVelocity(0);
                    motors.setIntake(1);
                    motors.spinPosition = 12;
                    motors.update();
                    pathing.follower.followPath(pathing.grabSecondArtifacts, 0.75, true);
                    pathing.setPathState(6);
                }
                break;
            case 6:

                if (motors.intake.getPower() > 0 && sensors.artifactLoaded) {
                    sensors.update();
                    if (motors.spinPosition == 12) {
                        motors.spinPosition = 14;
                        sensors.update();
                    } else if (motors.spinPosition == 14) {
                        motors.spinPosition = 16;
                        sensors.update();
                    } else if (motors.spinPosition == 16) {
                        motors.spinPosition = 12;
                        sensors.update();
                    } else {
                        motors.spinPosition = 12;
                        sensors.update();
                    }
                }
                if (!pathing.follower.isBusy()) {
                    motors.time.reset();
                    while (motors.time.seconds() < 2) {
                        motors.update();
                        pathing.update();

                        if (motors.intake.getPower() > 0 && sensors.artifactLoaded) {
                            sensors.update();
                            if (motors.spinPosition == 12) {
                                motors.spinPosition = 14;
                                sensors.update();
                            } else if (motors.spinPosition == 14) {
                                motors.spinPosition = 16;
                                sensors.update();
                            } else if (motors.spinPosition == 16) {
                                motors.spinPosition = 12;
                                sensors.update();
                            } else {
                                motors.spinPosition = 12;
                                sensors.update();
                            }
                        }
                    }
                    motors.doNotSpin = true;
                    motors.setFlywheelVelocity(pathing.shootDistance);
                    pathing.follower.followPath(pathing.scoreSecondArtifacts, true);
                    pathing.setPathState(7);
                }
                break;
            case 7:
                if (!pathing.follower.isBusy()) {
                    motors.flywheelPower = 6.3;
                    motors.time.reset();
                    motors.setIntake(0);
                    while (motors.time.seconds() < 1) {
                        motors.update();
                        pathing.update();
                        motors.setFlywheelVelocity(pathing.shootDistance);
                    }
                    motors.doNotSpin = false;
                    int shootAll = 0;
                    motors.spinPosition = 11;
                    while (shootAll < 3) {
                        motors.setFlywheelVelocity(pathing.shootDistance);
                        motors.time.reset();
                        while (!motors.ableToShoot || motors.time.seconds() < 0.75) {
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
                    pathing.setPathState(8);
                }
                break;
            case 8:
                if (!pathing.follower.isBusy()) {
                    motors.setFlywheelVelocity(0);
                    pathing.follower.followPath(pathing.leaveLaunchZone, true);
                    motors.update();
                    pathing.setPathState(-1);
                }
        }
    }
}