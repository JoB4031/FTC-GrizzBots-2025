/*
The Keep Auto Version 4.1.0
Changelog:
Had to do a major overhaul of the farAuto which mainly included fixing bugs.
Also added a process to hold positions during farAuto.
*/

package org.firstinspires.ftc.teamcode.theKeep;

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
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.hardware.centralHub.AutoProgram;
import org.firstinspires.ftc.teamcode.hardware.pedroPathing.PoseLibrary;
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
                ),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE,
                new PedroComponent(Constants::createFollower)
        );
    }

    @Override
    public void onStartButtonPressed() {
        PedroComponent.follower().setPose(PoseLibrary.startPose);
        AutoProgram.AUTO_PROGRAM.farAuto.schedule();
    }
}


