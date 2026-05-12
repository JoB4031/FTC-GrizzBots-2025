/*
The Keep Version 2.4.0
Changelog:
Fixed the issue where the shoot all balls function would
not allow the bot to move once the function started.
Also fixed a few bugs that surfaced during the 1/17/2026
scrimmage.
*/
package org.firstinspires.ftc.teamcode.theKeep;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.hardware.centralHub.CommandHub;
import org.firstinspires.ftc.teamcode.hardware.pedroPathing.LaunchTracker;
import org.firstinspires.ftc.teamcode.hardware.pedroPathing.PoseLibrary;
import org.firstinspires.ftc.teamcode.hardware.sensors.ArtifactSensor;
import org.firstinspires.ftc.teamcode.hardware.sensors.LEDIndicator;
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
                        LEDIndicator.INSTANCE,
                        PoseLibrary.INSTANCE,
                        Vision.INSTANCE,
                        LaunchTracker.INSTANCE,
                        Drive.INSTANCE,
                        Intake.INSTANCE,
                        Flywheel.INSTANCE,
                        FidgetTech.INSTANCE,
                        ArtifactSensor.INSTANCE,
                        Ejector.INSTANCE,
                        CommandHub.INSTANCE
                ),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE,
                new PedroComponent(Constants::createFollower)
        );
    }
    @Override
    public void onStartButtonPressed() {
        PedroComponent.follower().setPose(PoseLibrary.startPose);
        Drive.INSTANCE.normalTeleOpDrive().schedule();

        Gamepads.gamepad1().leftTrigger().greaterThan(0.1)
                .whenBecomesTrue(Drive.INSTANCE.facePointDrive(PoseLibrary.INSTANCE.goal).and(Flywheel.INSTANCE.setVelocity(Flywheel.INSTANCE.getRequiredVelocity(LaunchTracker.INSTANCE.shootDistance, LaunchTracker.INSTANCE.farLaunch))))
                .whenBecomesFalse(Drive.INSTANCE.normalTeleOpDrive().and(Flywheel.INSTANCE.stopPower()))
        ;

        Gamepads.gamepad1().rightTrigger().greaterThan(0.1)
                .whenBecomesTrue(Ejector.INSTANCE.fire())
        ;

        Gamepads.gamepad1().circle().toggleOnBecomesTrue()
                .whenBecomesTrue(CommandHub.INSTANCE.intakeArtifacts().then(Intake.INSTANCE.setPower(0,0)))
                .whenBecomesFalse(CommandHub.INSTANCE.stopIntakeArtifacts().and(Intake.INSTANCE.setPower(0,0)))
        ;

        Gamepads.gamepad1().triangle()
                .whenBecomesTrue(Intake.INSTANCE.setPower(-1,-1))
                .whenBecomesFalse(Intake.INSTANCE.setPower(0,0))
        ;
    }
}
