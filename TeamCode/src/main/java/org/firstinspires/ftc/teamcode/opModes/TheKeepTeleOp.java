/*
The Keep Version 3.0.0
Changelog:
Testing NextFTC Library
*/
package org.firstinspires.ftc.teamcode.opModes;

import static org.firstinspires.ftc.teamcode.hardware.centralHub.CommandHub.COMMAND_HUB;
import static org.firstinspires.ftc.teamcode.hardware.pedroPathing.LaunchTracker.LAUNCH_TRACKER;
import static org.firstinspires.ftc.teamcode.hardware.pedroPathing.PoseLibrary.POSE_LIBRARY;
import static org.firstinspires.ftc.teamcode.hardware.sensors.ArtifactSensor.ARTIFACT_SENSOR;
import static org.firstinspires.ftc.teamcode.hardware.sensors.LEDIndicator.LED_INDICATOR;
import static org.firstinspires.ftc.teamcode.hardware.subsystems.Drive.DRIVE;
import static org.firstinspires.ftc.teamcode.hardware.subsystems.Ejector.EJECTOR;
import static org.firstinspires.ftc.teamcode.hardware.subsystems.FidgetTech.FIDGET_TECH;
import static org.firstinspires.ftc.teamcode.hardware.subsystems.Flywheel.FLYWHEEL;
import static org.firstinspires.ftc.teamcode.hardware.subsystems.Intake.INTAKE;
import static org.firstinspires.ftc.teamcode.hardware.subsystems.Vision.VISION;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.hardware.pedroPathing.PoseLibrary;
import org.firstinspires.ftc.teamcode.hardware.subsystems.FidgetTech;
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
                        LED_INDICATOR,
                        POSE_LIBRARY,
                        VISION,
                        LAUNCH_TRACKER,
                        DRIVE,
                        INTAKE,
                        FLYWHEEL,
                        FIDGET_TECH,
                        ARTIFACT_SENSOR,
                        EJECTOR,
                        COMMAND_HUB
                )
        );
    }
    @Override
    public void onStartButtonPressed() {
        PedroComponent.follower().setPose(PoseLibrary.startPose);
        DRIVE.normalTeleOpDrive().schedule();

        Gamepads.gamepad1().cross().toggleOnBecomesTrue()
                .whenBecomesTrue(DRIVE.facePointDrive(POSE_LIBRARY.goal)
                        .and(FLYWHEEL.setVelocity()))
                .whenBecomesFalse(DRIVE.normalTeleOpDrive()
                        .and(FLYWHEEL.stopPower()))
        ;

        Gamepads.gamepad1().rightTrigger().greaterThan(0.1).and(FLYWHEEL.weaponPrimed)
                .whenBecomesTrue(EJECTOR.fire())
        ;

        Gamepads.gamepad1().leftBumper()
                        .whenBecomesTrue(COMMAND_HUB.stopIntakeArtifacts().then(FIDGET_TECH.goToArtifact(FidgetTech.artifactColor.PURPLE)))
        ;

        Gamepads.gamepad1().rightBumper()
                        .whenBecomesTrue(COMMAND_HUB.stopIntakeArtifacts().then(FIDGET_TECH.goToArtifact(FidgetTech.artifactColor.GREEN)))
        ;

        Gamepads.gamepad1().circle().toggleOnBecomesTrue()
                .whenBecomesTrue(COMMAND_HUB.intakeArtifacts())
                .whenBecomesFalse(COMMAND_HUB.stopIntakeArtifacts())
        ;

        Gamepads.gamepad1().triangle()
                .whenBecomesTrue(COMMAND_HUB.stopIntakeArtifacts().then(INTAKE.setPower(-1,-1)))
                .whenBecomesFalse(INTAKE.setPower(0,0))
        ;

        Gamepads.gamepad1().square()
                .whenBecomesTrue(DRIVE.slowTeleOpDrive())
                .whenBecomesFalse(DRIVE.normalTeleOpDrive())
        ;

    }
}
