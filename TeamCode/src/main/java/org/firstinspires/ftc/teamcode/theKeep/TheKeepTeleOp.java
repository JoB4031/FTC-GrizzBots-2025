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

import org.firstinspires.ftc.teamcode.hardware.motors.EjectorSubsystem;
import org.firstinspires.ftc.teamcode.hardware.motors.FlywheelSubsystem;
import org.firstinspires.ftc.teamcode.hardware.motors.IndexerSubsystem;
import org.firstinspires.ftc.teamcode.hardware.motors.IntakeSubsystem;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@TeleOp(name="The Keep TeleOp", group="The Keep")
public class TheKeepTeleOp extends NextFTCOpMode {
    public void TeleOpProgram() {
        addComponents(
                new SubsystemComponent(
                        EjectorSubsystem.INSTANCE,
                        FlywheelSubsystem.INSTANCE,
                        IndexerSubsystem.INSTANCE,
                        IntakeSubsystem.INSTANCE
                ),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }
    @Override
    public void onStartButtonPressed() {

    }
}
