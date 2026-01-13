/*
The Keep Version 2.3.0
Changelog:
Added velocity control for the flywheel and tuned it
Used an equation to be able to shoot from close and far range
Made sure all the hardware had an update function
Cleaned up some code and moved exes processes to the hardware methods
*/
package org.firstinspires.ftc.teamcode.theKeep;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
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

    @Override
    public void init() {

        // Creates a new instance of the hardware classes - Jason
        vision = new Vision();
        pathing = new PedroPathing();
        motors = new Motors();
        sensors = new Sensors();

        // Call the hardware init methods - Jason
        vision.initAprilTag(hardwareMap);
        pathing.initFollower(hardwareMap);
        motors.initMotors(hardwareMap);
        sensors.initSensors(hardwareMap);

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
            if (motors.intake.getPower() > 0 || motors.shooter.getPower() > 0) {
                pathing.follower.setTeleOpDrive(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x,
                        -(gamepad1.right_stick_x*0.5),
                        true// Robot Centric
                );
            } else {
                pathing.follower.setTeleOpDrive(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x,
                        -gamepad1.right_stick_x,
                        true // Robot Centric
                );
            }
        }

        // Turns the bots heading to face the alliance goal - Jason
        if (gamepad1.crossWasPressed()) {
            pathing.follower.followPath(pathing.turnToGoal.get());
            pathing.automatedDrive = true;
        }

        // The dpad down button is used as an emergency stop - Jason
        if (pathing.automatedDrive && (gamepad1.dpadDownWasPressed() || !pathing.follower.isBusy())) {
            pathing.follower.startTeleopDrive();
            pathing.automatedDrive = false;
        } // Switches to TeleOp drive if the follower is done - Jason

        // These lines set the flywheel to the required speed depending on the distance if the circle button is pressed and 0% if its not
        if (gamepad1.left_trigger > 0) {
            motors.setFlywheelVelocity(1,1);
            motors.intake.setPower(0);
        } else motors.setFlywheelVelocity(0,0);

        /* These lines check to see if the fidget tech is in the way of the ball ejector if it's not, when
        you press the triangle it will swing knocking out the ball - Jason */
        if (gamepad1.right_trigger > 0 && motors.spinPosition % 2 == 1) {
            motors.ballEjector.setPosition(.3);
        } else motors.ballEjector.setPosition(0);

        // This if statement turns the intake on when the circle is pressed and off when the square is pressed - Jason
        if (gamepad1.circleWasPressed()) {
            if (motors.intake.getPower() == 0) {
                motors.intake.setPower(1);
            } else motors.intake.setPower(0);
        }

        // This if loop makes the robot shoot all the artifacts - Nikola
        if (gamepad1.triangleWasPressed()){
            motors.shootAllBalls(1);
        }

        // Moves the Fidget Tech forward or backward one step depending on which trigger was pressed
        if (gamepad1.leftBumperWasPressed() && motors.spinPosition != 0) motors.spinPosition -= 2;
        if (gamepad1.rightBumperWasPressed() && motors.spinPosition != 26) motors.spinPosition += 2;

        // These lines write any april tag data to the telemetry - Jason
        if (vision.allianceBase != null) {
           telemetry.addData("Alliance Base Range", vision.allianceBase.ftcPose.range);
           telemetry.addData("Alliance Base Bearing", vision.allianceBase.ftcPose.bearing);
        }
        if (Vision.pattern != null) {
           telemetry.addData("Pattern Is", Vision.pattern);
        } else {
           telemetry.addData("Pattern Is", "unknown");
        }

        // These lines add the fidget tech's position and the bot's position to the telemetry - Jason
        telemetry.addData("Flywheel Speed", motors.flywheel.getVelocity());
        telemetry.addData("Fidget Tech Position", motors.spinPositions[motors.spinPosition]);
        telemetry.addData("Bot Position", pathing.follower.getPose());
        telemetry.addData("Artifact Detected", sensors.artifactDetected);
        telemetry.update();

    } // This section holds all the controls used during TeleOp

    @Override
    public void stop() {
        // Sets the bots starting position to our end position - Jason
        pathing.setStartPose(false);
    }
}
