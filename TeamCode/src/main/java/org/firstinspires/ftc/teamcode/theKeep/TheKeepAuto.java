/*
The Keep Auto Version 4.1.0
Changelog:
Had to do a major overhaul of the auto which mainly included fixing bugs.
Also added a process to hold positions during auto.
*/

package org.firstinspires.ftc.teamcode.theKeep;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


import org.firstinspires.ftc.teamcode.hardware.centralHub.AutoProgram;
import org.firstinspires.ftc.teamcode.hardware.pedroPathing.LaunchZoneTracker;
import org.firstinspires.ftc.teamcode.hardware.subsystems.DriveChainSubsystem;
import org.firstinspires.ftc.teamcode.hardware.subsystems.EjectorSubsystem;
import org.firstinspires.ftc.teamcode.hardware.subsystems.FidgetTechSubsystem;
import org.firstinspires.ftc.teamcode.hardware.subsystems.FlywheelSubsystem;
import org.firstinspires.ftc.teamcode.hardware.subsystems.IntakeSubsystem;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@Autonomous(name="The Keep Auto",group="The Keep")
public class TheKeepAuto extends NextFTCOpMode {

    public void AutonomousProgram() {
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
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onStartButtonPressed() {
        AutoProgram.INSTANCE.auto.schedule();
    }
}


