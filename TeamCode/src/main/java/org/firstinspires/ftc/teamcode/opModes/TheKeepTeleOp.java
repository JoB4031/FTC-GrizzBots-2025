/*
The Keep Version 3.0.0
Changelog:
Testing NextFTC Library
*/
package org.firstinspires.ftc.teamcode.opModes;

import static org.firstinspires.ftc.teamcode.hardware.pedroPathing.LaunchTracker.LAUNCH_TRACKER;
import static org.firstinspires.ftc.teamcode.hardware.pedroPathing.PoseLibrary.POSE_LIBRARY;
import static org.firstinspires.ftc.teamcode.hardware.subsystems.Drive.DRIVE;
import static org.firstinspires.ftc.teamcode.hardware.subsystems.Ejector.EJECTOR;
import static org.firstinspires.ftc.teamcode.hardware.subsystems.FidgetTech.FIDGET_TECH;
import static org.firstinspires.ftc.teamcode.hardware.subsystems.Flywheel.FLYWHEEL;
import static org.firstinspires.ftc.teamcode.hardware.subsystems.Intake.INTAKE;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.hardware.pedroPathing.PoseLibrary;
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
                new PedroComponent(Constants::createFollower),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE,
                new SubsystemComponent(
                        POSE_LIBRARY,
                        LAUNCH_TRACKER,
                        DRIVE,
                        EJECTOR,
                        FLYWHEEL,
                        INTAKE,
                        FIDGET_TECH
                )
        );
    }
    @Override
    public void onStartButtonPressed() {
        PedroComponent.follower().setPose(PoseLibrary.startPose);
        DRIVE.normalTeleOpDrive().schedule();
        Gamepads.gamepad1().cross()
                .whenBecomesTrue(DRIVE.slowTeleOpDrive())
                .whenBecomesFalse(DRIVE.normalTeleOpDrive())
        ;
        Gamepads.gamepad1().rightTrigger().greaterThan(0.1)
                .whenBecomesTrue(EJECTOR.fire())
        ;
        Gamepads.gamepad1().leftTrigger().greaterThan(0.1)
                .whenBecomesTrue(FLYWHEEL.setVelocity())
                .whenBecomesFalse(FLYWHEEL.stopPower())
        ;
        Gamepads.gamepad1().circle().toggleOnBecomesTrue()
                .whenBecomesTrue(INTAKE.setPower(1,1))
                .whenBecomesFalse(INTAKE.setPower(0,0))
        ;
        Gamepads.gamepad1().triangle()
                .whenBecomesTrue(INTAKE.setPower(-1,-1))
                .whenBecomesFalse(INTAKE.setPower(0,0))
        ;
        Gamepads.gamepad1().leftBumper()
                .whenBecomesTrue(FIDGET_TECH.previousSlot())

        ;
        Gamepads.gamepad1().rightBumper()
                .whenBecomesTrue(FIDGET_TECH.nextSlot())
        ;
    }
}
