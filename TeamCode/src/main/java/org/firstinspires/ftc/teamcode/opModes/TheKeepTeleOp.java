/*
The Keep Version 3.0.0
Changelog:
Testing NextFTC Library
*/
package org.firstinspires.ftc.teamcode.opModes;

import static org.firstinspires.ftc.teamcode.hardware.subsystems.Drive.DRIVE;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.hardware.pedroPathing.PoseLibrary;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
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
                        DRIVE
                )
        );
    }
    @Override
    public void onStartButtonPressed() {
        PedroComponent.follower().setPose(PoseLibrary.startPose);
        DRIVE.normalTeleOpDrive().schedule();
    }
}
