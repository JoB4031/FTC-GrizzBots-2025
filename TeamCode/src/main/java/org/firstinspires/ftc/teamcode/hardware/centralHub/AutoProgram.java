package org.firstinspires.ftc.teamcode.hardware.centralHub;

import org.firstinspires.ftc.teamcode.hardware.pedroPathing.PoseLibrary;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Drive;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Ejector;
import org.firstinspires.ftc.teamcode.hardware.subsystems.FidgetTech;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.hardware.subsystems.Intake;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.SequentialGroup;

public class AutoProgram {
    public static final AutoProgram INSTANCE = new AutoProgram();
    AutoProgram() {}



    public SequentialGroup farAuto = new SequentialGroup(
            Drive.INSTANCE.goTo(PoseLibrary.INSTANCE.farLaunchPose, PoseLibrary.INSTANCE.goal)
                    .and(Flywheel.INSTANCE.setVelocity(3500)),
            FidgetTech.INSTANCE.goToArtifact(FidgetTech.artifactColor.PURPLE).then(Ejector.INSTANCE.fire()),
            FidgetTech.INSTANCE.goToArtifact(FidgetTech.artifactColor.PURPLE).then(Ejector.INSTANCE.fire()),
            FidgetTech.INSTANCE.goToArtifact(FidgetTech.artifactColor.GREEN).then(Ejector.INSTANCE.fire())

    );
}