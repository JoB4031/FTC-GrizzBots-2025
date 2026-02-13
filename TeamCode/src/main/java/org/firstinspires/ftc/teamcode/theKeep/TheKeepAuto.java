/*
The Keep Auto Version 4.1.0
Changelog:
Had to do a major overhaul of the auto which mainly included fixing bugs.
Also added a process to hold positions during auto.
*/

package org.firstinspires.ftc.teamcode.theKeep;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.skeletonarmy.marrow.prompts.BooleanPrompt;
import com.skeletonarmy.marrow.prompts.OptionPrompt;
import com.skeletonarmy.marrow.prompts.Prompter;
import com.skeletonarmy.marrow.prompts.ValuePrompt;

import org.firstinspires.ftc.teamcode.hardware.centralHub.hardware;
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
    public static boolean robotCentric;
    private int startDelay;
    public int artifactsToCollect;


    @Override
    public void init() {
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
        robot.initHardware(hardwareMap, true);
        robot.startPosition(true);
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
    } // A loop that runs from when the init button is pressed to when the start button is hit

    @Override
    public void start() {
        telemetry.clear();
        robot.autoTime.reset();
        robot.timer.reset();
        while (robot.timer.seconds() < startDelay) robot.update();
    }

    @Override
    public void loop() {
        // Updates the hardware - Jason
        robot.update();
        autoPathing();
        // These lines grab the april tag data then write any tags data to the telemetry - Jason
        if (Vision.pattern != null) {
            telemetry.addData("Pattern Is", Vision.pattern);
        } else {
            telemetry.addData("Pattern Is", "unknown");
        }

        // These lines add the fidget tech's position and the bots position to the telemetry - Jason
        telemetry.addData("Fidget Tech Position", robot.fidgetTech.getSnapPoint());
        telemetry.addData("Bot Position", robot.pathFollower.getPosition());
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
                robot.followPath(robot.pathBuilder.scoreArtifact());
                robot.setFlywheelToShootDistance();
                robot.pathBuilder.setPathState(1);
                break;
            case 1:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!robot.pathingIsBusy()) {
                    robot.doNotSpin = false;
                    robot.shootAllBalls();
                    robot.pathBuilder.setPathState(artifactsToCollect == 0 ? 8 : 2);
                }
                break;
            case 2:
                if (!robot.pathingIsBusy()) {
                    robot.flywheel.off();
                    robot.intake.on();
                    robot.fidgetTech.setSnapPoint(12);
                    robot.update();
                    robot.pathFollower.follower.followPath(robot.pathBuilder.grabFirstArtifacts(), 0.5, true);
                    robot.pathBuilder.setPathState(3);
                }
                break;
            case 3:

                robot.automaticPickup();
                if (!robot.pathingIsBusy()) {
                    robot.timer.reset();
                    while (robot.timer.seconds() < 2) {
                        robot.update();
                        robot.automaticPickup();
                    }
                    robot.intake.setPower(0, -1);
                    robot.doNotSpin = true;
                    robot.setFlywheelToShootDistance();
                    robot.followPath(robot.pathBuilder.scoreFirstArtifacts());
                    robot.intake.setPower(-1,-1);
                    robot.pathBuilder.setPathState(4);
                }
                break;
            case 4:
                if (!robot.pathingIsBusy()) {
                    robot.doNotSpin = false;
                    robot.shootAllBalls();
                    robot.pathBuilder.setPathState(artifactsToCollect == 1 ? 8 : 5);
                }
                break;
            case 5:
                if (!robot.pathingIsBusy()) {
                    robot.flywheel.off();
                    robot.intake.on();
                    robot.fidgetTech.setSnapPoint(12);
                    robot.update();
                    robot.pathFollower.follower.followPath(robot.pathBuilder.grabSecondArtifacts(), 0.5, true);
                    robot.pathBuilder.setPathState(6);
                }
                break;
            case 6:

                robot.automaticPickup();
                if (!robot.pathingIsBusy()) {
                    robot.timer.reset();
                    while (robot.timer.seconds() < 2) {
                        robot.update();
                        robot.automaticPickup();
                    }
                    robot.doNotSpin = true;
                    robot.setFlywheelToShootDistance();
                    robot.followPath(robot.pathBuilder.scoreSecondArtifacts());
                    robot.intake.setPower(-1,-1);
                    robot.pathBuilder.setPathState(7);
                }
                break;
            case 7:
                if (!robot.pathingIsBusy()) {
                    robot.doNotSpin = false;
                    robot.shootAllBalls();
                    robot.pathBuilder.setPathState(8);
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


