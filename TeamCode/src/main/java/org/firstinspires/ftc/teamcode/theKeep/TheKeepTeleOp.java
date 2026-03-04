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
import org.firstinspires.ftc.teamcode.hardware.pedroPathing.LaunchZoneTracker;
import org.firstinspires.ftc.teamcode.hardware.pedroPathing.PoseLibrary;
import org.firstinspires.ftc.teamcode.hardware.sensors.ArtifactSensor;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Drive;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Ejector;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.hardware.subsystems.FidgetTech;
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
    public void TeleOpProgram() {
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
                        PoseLibrary.INSTANCE
                ),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE,
                new PedroComponent(Constants::createFollower)
        );
    }
    @Override
    public void onStartButtonPressed() {
        Drive.INSTANCE.driverControlled.schedule();

        Gamepads.gamepad1().rightBumper()
                        .whenBecomesTrue(FidgetTech.INSTANCE.next);

        Gamepads.gamepad1().leftBumper()
                .whenBecomesTrue(FidgetTech.INSTANCE.previous);

        Gamepads.gamepad1().circle()
                        .toggleOnBecomesTrue()
                                .whenBecomesTrue(Intake.INSTANCE.setPower(1,1))
                                .whenBecomesFalse(Intake.INSTANCE.setPower(0,0));

        Gamepads.gamepad1().dpadUp()
                .whenBecomesTrue(CommandHub.INSTANCE.firePattern());

        Gamepads.gamepad1().dpadLeft()
                .whenBecomesTrue(CommandHub.INSTANCE.fireColor(FidgetTech.artifactColor.PURPLE));

        Gamepads.gamepad1().dpadRight()
                .whenBecomesTrue(CommandHub.INSTANCE.fireColor(FidgetTech.artifactColor.GREEN));

        Gamepads.gamepad1().cross()
                .whenBecomesTrue(CommandHub.INSTANCE.followPath(PoseLibrary.INSTANCE.nearLaunchPose));

    }
}
