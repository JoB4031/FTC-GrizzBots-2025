/*
The Keep Auto Version 4.1.0
Changelog:
Had to do a major overhaul of the farAuto which mainly included fixing bugs.
Also added a process to hold positions during farAuto.
*/

package org.firstinspires.ftc.teamcode.theKeep;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.hardware.centralHub.AutoProgram;
import org.firstinspires.ftc.teamcode.hardware.centralHub.CommandHub;
import org.firstinspires.ftc.teamcode.hardware.pedroPathing.LaunchZoneTracker;
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
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@Autonomous(name="The Keep Auto",group="The Keep")
public class TheKeepAuto extends NextFTCOpMode {

    public TheKeepAuto() {
        addComponents(
                new SubsystemComponent(
                        LEDIndicator.INSTANCE,
                        PoseLibrary.INSTANCE,
                        Vision.INSTANCE,
                        LaunchZoneTracker.INSTANCE,
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
        AutoProgram.INSTANCE.farAuto.schedule();
    }
}


