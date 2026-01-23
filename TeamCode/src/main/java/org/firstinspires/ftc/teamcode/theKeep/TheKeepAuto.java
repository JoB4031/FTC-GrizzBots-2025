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
import org.firstinspires.ftc.teamcode.hardware.Vision;

@Autonomous(name="The Keep Auto",group="The Keep")
public class TheKeepAuto extends OpMode {

    private Timer opmodeTimer;
    private Motors motors;
    private Vision vision;
    private PedroPathing pathing;

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
        prompter = new Prompter(this);

        // Call their init methods
        vision.initAprilTag(hardwareMap);
        motors.initMotors(hardwareMap);
        motors.setFlywheelVelocity(0);
        //Sets up the prompter - Jason
        prompter.prompt("alliance", new OptionPrompt<>("Select Alliance", Alliance.RED, Alliance.BLUE))
                .prompt("startLocation", new OptionPrompt<>("Select Start Location", 1, 2))
                .prompt("robotCentric", new BooleanPrompt("Robot Centric",true))
                .onComplete(this::onPromptsComplete);

    } // This initializes all the motors and sensors

    // Function that runs when the prompter is done that stores the values and prints the results - Jason
    public void onPromptsComplete() {
        alliance = prompter.get("alliance");
        startLocation = prompter.get("startLocation");
        robotCentric = prompter.get("robotCentric");
        pathing.setStartPose(true);
        pathing.initFollower(hardwareMap);
        pathing.setArtifact();
        pathing.buildPaths();
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
    } // A loop that runs from when the init button is pressed to when the start button is hit

    @Override
    public void start() {
        telemetry.clear();
        opmodeTimer.resetTimer();
    }

    @Override
    public void loop() {
        // Updates the hardware - Jason
        pathing.update();
        motors.intake.setPower(.1);
        motors.update();
        vision.update();
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
                pathing.follower.followPath(pathing.scoreArtifact,true);
                pathing.setPathState(1);
                break;
            case 1:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!pathing.follower.isBusy()) {
                    int shootAll = 0;
                    motors.intake.setPower(0);
                    motors.spinPosition = 9;
                    while (shootAll < 3) {
                        motors.setFlywheelVelocity(vision.allianceBase.ftcPose.range);
                        motors.time.reset();
                        while (!motors.ableToShoot) {
                            //just chill
                            motors.update();
                            pathing.update();
                        }
                        motors.ballEjector.setPosition(0.3);
                        motors.time.reset();
                        while (motors.time.seconds() < 0.5) {
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
                    pathing.follower.followPath(pathing.leaveLaunchZone,true);
                    pathing.setPathState(-1);
                }
                break;
        }
    }
}
