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
import org.firstinspires.ftc.teamcode.hardware.subsystems.DriveChainSubsystem;
import org.firstinspires.ftc.teamcode.hardware.subsystems.EjectorSubsystem;
import org.firstinspires.ftc.teamcode.hardware.subsystems.FlywheelSubsystem;
import org.firstinspires.ftc.teamcode.hardware.subsystems.FidgetTechSubsystem;
import org.firstinspires.ftc.teamcode.hardware.subsystems.IntakeSubsystem;
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
                        EjectorSubsystem.INSTANCE,
                        FlywheelSubsystem.INSTANCE,
                        FidgetTechSubsystem.INSTANCE,
                        IntakeSubsystem.INSTANCE,
                        DriveChainSubsystem.INSTANCE,
                        LaunchZoneTracker.INSTANCE
                ),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE,
                new PedroComponent(Constants::createFollower)
        );
    }
    @Override
    public void onStartButtonPressed() {
        DriveChainSubsystem.INSTANCE.driverControlled.schedule();

        Gamepads.gamepad1().dpadUp()
                .whenBecomesTrue(CommandHub.INSTANCE.firePattern());

        Gamepads.gamepad1().dpadLeft()
                .whenBecomesTrue(CommandHub.INSTANCE.fireColor(FidgetTechSubsystem.artifactColor.PURPLE));

        Gamepads.gamepad1().dpadRight()
                .whenBecomesTrue(CommandHub.INSTANCE.fireColor(FidgetTechSubsystem.artifactColor.GREEN));

        Gamepads.gamepad1().leftBumper()
                .whenBecomesTrue(DriveChainSubsystem.INSTANCE.goTo(PoseLibrary.INSTANCE.nearLaunchPose).and(FlywheelSubsystem.INSTANCE.setVelocity(4000)))
                .whenBecomesFalse(DriveChainSubsystem.INSTANCE.driverControlled);

    }
}
