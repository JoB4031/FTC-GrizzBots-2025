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

import org.firstinspires.ftc.teamcode.hardware.hardware;
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


    @Override
    public void init() {
        prompter = new Prompter(this);
        robot =  new hardware();
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
    }

    @Override
    public void loop() {
        // Updates the hardware - Jason
        robot.update();
        autonomousPathUpdate();
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

    public void autonomousPathUpdate() {
        switch (robot.pathBuilder.pathState) {
            case 0:
                robot.followPath(robot.pathBuilder.scoreArtifact());
                robot.setFlywheelToShootDistance();
                robot.pathBuilder.setPathState(1);
                break;
            case 1:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!robot.pathingIsBusy()) {
                    robot.shootAllBalls();
                    robot.pathBuilder.setPathState(2);
                }
                break;
            case 2:
                if (!robot.pathingIsBusy()) {
                    robot.flywheel.off();
                    robot.intake.on();
                    robot.fidgetTech.setSnapPoint(12);
                    robot.update();
                    robot.followPath(robot.pathBuilder.grabFirstArtifacts());
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
                    robot.intake.setPower(0,-1);
                    robot.setFlywheelToShootDistance();
                    robot.followPath(robot.pathBuilder.scoreFirstArtifacts());
                    robot.pathBuilder.setPathState(4);
                }
                break;
            case 4:
                if (!robot.pathingIsBusy()) {
                    robot.shootAllBalls();
                    robot.pathBuilder.setPathState(5);
                }
                break;
            case 5:
                if (!robot.pathingIsBusy()) {
                    robot.flywheel.off();
                    robot.intake.on();
                    robot.fidgetTech.setSnapPoint(12);
                    robot.update();
                    robot.followPath(robot.pathBuilder.grabSecondArtifacts());
                    robot.pathBuilder.setPathState(3);
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
                    robot.setFlywheelToShootDistance();
                    robot.followPath(robot.pathBuilder.scoreSecondArtifacts());
                    robot.pathBuilder.setPathState(7);
                }
                break;
            case 7:
                if (!robot.pathingIsBusy()) {
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