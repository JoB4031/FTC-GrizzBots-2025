/*
The Keep Auto Version 4.1.0
Changelog:
Had to do a major overhaul of the auto which mainly included fixing bugs.
Also added a process to hold positions during auto.
*/

package org.firstinspires.ftc.teamcode.theKeep;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


import org.firstinspires.ftc.teamcode.hardware.centralHub.AutoProgram;
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

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@Autonomous(name="The Keep Auto",group="The Keep")
public class TheKeepAuto extends NextFTCOpMode {

    public TheKeepAuto() {
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
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onStartButtonPressed() {
        AutoProgram.INSTANCE.auto.schedule();
    }
}


