/*
The Keep Version 2.4.0
Changelog:
Fixed the issue where the shoot all balls function would
not allow the bot to move once the function started.
Also fixed a few bugs that surfaced during the 1/17/2026
scrimmage.
*/
package org.firstinspires.ftc.teamcode.theKeep;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.hardware.centralHub.CommandHub;
import org.firstinspires.ftc.teamcode.hardware.pedroPathing.LaunchZoneTracker;
import org.firstinspires.ftc.teamcode.hardware.pedroPathing.PoseLibrary;
import org.firstinspires.ftc.teamcode.hardware.sensors.ArtifactSensor;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Drive;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Ejector;
import org.firstinspires.ftc.teamcode.hardware.subsystems.FidgetTech;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Intake;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Vision;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@TeleOp(name="The Keep TeleOp", group="The Keep")
public class TheKeepTeleOp extends NextFTCOpMode {
    public TheKeepTeleOp() {
        addComponents(
                new SubsystemComponent(
                        Drive.INSTANCE,
                        Ejector.INSTANCE,
                        FidgetTech.INSTANCE,
                        Flywheel.INSTANCE,
                        Intake.INSTANCE,
                        Vision.INSTANCE,
                        ArtifactSensor.INSTANCE,
                        LaunchZoneTracker.INSTANCE,
                        PoseLibrary.INSTANCE,
                        CommandHub.INSTANCE
                ),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE,
                new PedroComponent(Constants::createFollower)
        );
    }
    @Override
    public void onStartButtonPressed() {

        // Immediately starts teleOp driving:
        Drive.INSTANCE.normalTeleOpDrive().schedule();

        // What happens when the right trigger is pressed:
        Gamepads.gamepad1().rightTrigger()
                .greaterThan(0)
                .whenBecomesTrue(Ejector.INSTANCE.fire)
                .whenBecomesFalse(Ejector.INSTANCE.reset)
        ;

        // What happens when the left trigger is pressed:
        Gamepads.gamepad1().leftTrigger()
                .greaterThan(0)
                .whenBecomesTrue(Flywheel.INSTANCE.setVelocity(3500))
                .whenBecomesFalse(Flywheel.INSTANCE.stopPower())
        ;

        // What happens when the right bumper is pressed:
        Gamepads.gamepad1().rightBumper()
                .whenBecomesTrue(FidgetTech.INSTANCE.next)
        ;

        // What happens when the left bumper is pressed:
        Gamepads.gamepad1().leftBumper()
                .whenBecomesTrue(FidgetTech.INSTANCE.previous)
        ;

        // What happens when the circle button is pressed:
        Gamepads.gamepad1().circle()
                .toggleOnBecomesTrue()
                .whenBecomesTrue(Intake.INSTANCE.externalPower(1))
                .whenBecomesFalse(Intake.INSTANCE.externalPower(0))
        ;

        // What happens when the triangle button is pressed:
        Gamepads.gamepad1().triangle()
                .whenTrue(Intake.INSTANCE.setPower(-1,-1))
                .whenBecomesFalse(Intake.INSTANCE.setPower(0,0))
        ;

        // What happens when the square button is pressed:
        Gamepads.gamepad1().square()
                .toggleOnBecomesTrue()
                .whenBecomesTrue(Drive.INSTANCE.normalTeleOpDrive())
                .whenBecomesFalse(Drive.INSTANCE.slowTeleOpDrive())
        ;

        // What happens when the cross button is pressed:
        Gamepads.gamepad1().cross()
                .whenBecomesTrue(Drive.INSTANCE.facePointDrive(new Pose(PoseLibrary.targetPoint.getX(), PoseLibrary.targetPoint.getY())))
                .whenBecomesFalse(Drive.INSTANCE.resumeTeleOpDrive())
        ;

        // What happens when the up dpad button is pressed:
        Gamepads.gamepad1().dpadUp()
                .whenBecomesTrue(CommandHub.INSTANCE.firePattern())
        ;

        // What happens when the left dpad button is pressed:
        Gamepads.gamepad1().dpadLeft()
                .whenBecomesTrue(CommandHub.INSTANCE.fireColor(FidgetTech.artifactColor.PURPLE))
        ;

        // What happens when the right dpad button is pressed:
        Gamepads.gamepad1().dpadRight()
                .whenBecomesTrue(CommandHub.INSTANCE.fireColor(FidgetTech.artifactColor.GREEN))
        ;

    }
}
