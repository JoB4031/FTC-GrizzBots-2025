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


    @Override
    public void init() {
        prompter = new Prompter(this);
        robot =  new hardware();
        //Sets up the prompter - Jason
        prompter.prompt("alliance", new OptionPrompt<>("Select Alliance", Alliance.RED, Alliance.BLUE))
                .prompt("startLocation", new OptionPrompt<>("Select Start Location", 1, 2))
                .prompt("Start Delay", new ValuePrompt("Start Delay", 0, 1))
                .prompt("robotCentric", new BooleanPrompt("Robot Centric", true))
                .onComplete(this::onPromptsComplete);

    } // This initializes all the motors and sensors

    // Function that runs when the prompter is done that stores the values and prints the results - Jason
    public void onPromptsComplete() {
        alliance = prompter.get("alliance");
        startLocation = prompter.get("startLocation");
        robotCentric = prompter.get("robotCentric");
        startDelay = prompter.get("Start Delay");
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
        robot.autonomousPath();
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
}