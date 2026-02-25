/*
The Keep Auto Version 4.1.0
Changelog:
Had to do a major overhaul of the auto which mainly included fixing bugs.
Also added a process to hold positions during auto.
*/

package org.firstinspires.ftc.teamcode.theKeep;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.skeletonarmy.marrow.prompts.BooleanPrompt;
import com.skeletonarmy.marrow.prompts.OptionPrompt;
import com.skeletonarmy.marrow.prompts.Prompter;
import com.skeletonarmy.marrow.prompts.ValuePrompt;

import org.firstinspires.ftc.teamcode.hardware.centralHub.hardware;
import org.firstinspires.ftc.teamcode.hardware.motors.FidgetTech;
import org.firstinspires.ftc.teamcode.hardware.vision.Vision;

@Autonomous(name="The Keep Auto",group="The Keep")
public class TheKeepAuto extends OpMode {

    private hardware robot;
    // These next lines setup the variables used to store prompter values, which define the autonomous is used - Jason
    public enum Alliance {
        RED,
        BLUE
    }

    private Prompter prompter;
    public static Alliance alliance;
    public static int startLocation;
    public static int artifactsToCollect;
    public static boolean robotCentric;
    private int startDelay;
    private boolean prompterDone = false;
    private boolean visionMove = false;

    private TelemetryManager telemetryM;
    private boolean emergencyStop = false;

    @Override
    public void init() {
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        prompter = new Prompter(this);
        robot =  new hardware();
        //Sets up the prompter - Jason
        prompter.prompt("alliance", new OptionPrompt<>("Select Alliance", Alliance.RED, Alliance.BLUE))
                .prompt("startLocation", new OptionPrompt<>("Select Start Location", 1, 2))
                .prompt("Start Delay", new ValuePrompt("Start Delay", 0, 1))
                .prompt("robotCentric", new BooleanPrompt("Robot Centric", true))
                .prompt("artifactsToCollect", new ValuePrompt("How many artifact sets to collect?", 0,2, 2,1))
                .onComplete(this::onPromptsComplete);

    } // This initializes all the motors and sensors

    // Function that runs when the prompter is done that stores the values and prints the results - Jason
    public void onPromptsComplete() {
        alliance = prompter.get("alliance");
        startLocation = prompter.get("startLocation");
        robotCentric = prompter.get("robotCentric");
        startDelay = prompter.get("Start Delay");
        artifactsToCollect = prompter.get("artifactsToCollect");
        robot.initHardware(hardwareMap, true, telemetryM);
        robot.startPosition(true);
        prompterDone = true;
        telemetry.addData("Selected Alliance", alliance);
        telemetry.addData("Selected Start Location", startLocation);
        telemetry.addData("Selected Start Position", robot.pathFollower.getPosition());
        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void init_loop() {
        // Runs the prompter - Jason
        prompter.run();
        if (prompterDone) {
            telemetry.addData("Artifact 1", FidgetTech.artifactsLoaded[0]);
            telemetry.addData("Artifact 2", FidgetTech.artifactsLoaded[1]);
            telemetry.addData("Artifact 3", FidgetTech.artifactsLoaded[2]);
            robot.update();
            if(gamepad1.circle) {
                robot.intake.on();
            } else robot.intake.off();
            if(robot.intake.isPowered()) {
                robot.automaticPickup();
            }
        }
    } // A loop that runs from when the init button is pressed to when the start button is hit

    @Override
    public void start() {
        Vision.pattern = null;
        telemetry.clear();
        robot.autoTime.reset();
        robot.timer.reset();
        while (robot.timer.seconds() < startDelay) robot.update();
        robot.fidgetTech.setSnapPoint(robot.spinPattern[0]);
    }

    @Override
    public void loop() {
        // Updates the hardware - Jason
        if(Vision.pattern != null) {
            if(TheKeepAuto.alliance == Alliance.BLUE) {
                robot.led.blue();
            } else robot.led.red();
        } else robot.led.white();
        robot.update();
        autoPathing();
        if(robot.autoTime.seconds() > 27 && !emergencyStop) {
            robot.pathBuilder.setPathState(8);
            emergencyStop = true;
        }
        // These lines grab the april tag data then write any tags data to the telemetry - Jason
        if (Vision.pattern != null) {
            telemetry.addData("Pattern Is", Vision.pattern);
        } else {
            telemetry.addData("Pattern Is", "unknown");
        }

        // These lines add the fidget tech's position and the bots position to the telemetry - Jason
        telemetry.addData("Fidget Tech Position", robot.fidgetTech.getSnapPoint());
        telemetry.addData("Bot Position", robot.pathFollower.getPosition());
        telemetry.addData("Artifact One", FidgetTech.artifactsLoaded[0]);
        telemetry.addData("Artifact Two", FidgetTech.artifactsLoaded[1]);
        telemetry.addData("Artifact Three", FidgetTech.artifactsLoaded[2]);
        telemetry.update();

    }

    @Override
    public void stop() {
        robot.startPosition(false);
    }

    public void autoPathing() {
        switch (robot.pathBuilder.pathState) {
            case 0:
                robot.doNotSpin = true;
                if(!visionMove) {
                    robot.followPath(robot.pathBuilder.getAprilTag());
                    visionMove = true;
                }
                robot.setFlywheelToShootDistance();
                if(Vision.pattern != null) {
                    robot.followPath(robot.pathBuilder.scoreArtifact());
                    robot.pathBuilder.setPathState(1);
                }
                break;
            case 1:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!robot.pathingIsBusy()) {
                    robot.intake.off();
                    robot.fidgetTech.setSnapPoint(robot.spinPattern[0]);
                    robot.doNotSpin = false;
                    if(robot.autoTime.seconds() > 25) {
                        robot.pathBuilder.setPathState(8);
                        break;
                    }
                    robot.shootAllBalls(true);
                    robot.pathBuilder.setPathState(artifactsToCollect == 0 ? 8 : 2);
                }
                break;
            case 2:
                if (!robot.pathingIsBusy()) {
                    robot.flywheel.off();
                    robot.update();
                    robot.pathFollower.follower.followPath(robot.pathBuilder.grabFirstArtifacts(), 0.75, true);
                    robot.pathBuilder.setPathState(3);
                }
                break;
            case 3:
                robot.intake.on();
                robot.automaticPickup();
                if (!robot.pathingIsBusy()) {
                    robot.setFlywheelToShootDistance();
                    robot.followPath(robot.pathBuilder.scoreFirstArtifacts());
                    robot.pathBuilder.setPathState(4);
                }
                break;
            case 4:
                robot.automaticPickup();
                if (!robot.pathingIsBusy()) {
                    robot.timer.reset();
                    if(!robot.fidgetTechIsFull()) {
                        robot.automaticPickup();
                        robot.update();
                    } else {
                        robot.intake.off();
                        robot.doNotSpin = false;
                        robot.fidgetTech.setSnapPoint(robot.spinPattern[0]);
                        robot.timer.reset();
                        while (robot.timer.seconds() < 1) {
                            robot.setFlywheelToShootDistance();
                            robot.update();
                        }
                        if(robot.autoTime.seconds() > 25) {
                            robot.pathBuilder.setPathState(8);
                            break;
                        }
                        robot.shootAllBalls(true);
                        robot.pathBuilder.setPathState(artifactsToCollect == 1 ? 8 : 5);
                    }
                }
                break;
            case 5:
                if (!robot.pathingIsBusy()) {
                    robot.flywheel.off();
                    robot.update();
                    robot.pathFollower.follower.followPath(robot.pathBuilder.grabSecondArtifacts(), 0.75,true);
                    robot.pathBuilder.setPathState(6);
                }
                break;
            case 6:
                robot.intake.on();
                robot.automaticPickup();
                if (!robot.pathingIsBusy()) {
                    robot.setFlywheelToShootDistance();
                    robot.followPath(robot.pathBuilder.scoreSecondArtifacts());
                    robot.pathBuilder.setPathState(7);
                }
                break;
            case 7:
                robot.automaticPickup();
                if (!robot.pathingIsBusy()) {
                    robot.timer.reset();
                    if(!robot.fidgetTechIsFull()) {
                        robot.automaticPickup();
                        robot.update();
                    } else {
                        robot.intake.off();
                        robot.doNotSpin = false;
                        robot.fidgetTech.setSnapPoint(robot.spinPattern[0]);
                        robot.timer.reset();
                        while (robot.timer.seconds() < 1) {
                            robot.setFlywheelToShootDistance();
                            robot.update();
                        }
                        if(robot.autoTime.seconds() > 25) {
                            robot.pathBuilder.setPathState(8);
                            break;
                        }
                        robot.shootAllBalls(true);
                        robot.pathBuilder.setPathState(8);
                    }
                }
                break;
            case 8:
                if (!robot.pathingIsBusy()) {
                    robot.flywheel.off();
                    robot.intake.off();
                    robot.update();
                    robot.followPath(robot.pathBuilder.leaveLaunchZone());
                    robot.pathBuilder.setPathState(-1);
                }
                break;
            }
    }
}


