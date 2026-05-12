package org.firstinspires.ftc.teamcode.hardware.pedroPathing;

import com.skeletonarmy.marrow.zones.Point;
import com.skeletonarmy.marrow.zones.PolygonZone;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.ActiveOpMode;

public class LaunchTracker implements Subsystem {

    public static final LaunchTracker INSTANCE = new LaunchTracker();
    private LaunchTracker() {}

    private final PolygonZone closeLaunchArea =
            new PolygonZone(new Point(144, 144), new Point(72, 72), new Point(0, 144));
    private final PolygonZone farLaunchArea =
            new PolygonZone(new Point(48, 0), new Point(72, 24), new Point(96, 0));

    private final PolygonZone robotToGoalZone = new PolygonZone(1, 1);
    public final PolygonZone robotLaunchZone = new PolygonZone(17, 17.5);

    public boolean robotInRange;
    public boolean farLaunch;
    public double shootDistance;

    @Override
    public void periodic() {
        robotToGoalZone.setPosition(PedroComponent.follower().getPose().getX(), PedroComponent.follower().getPose().getY());
        robotToGoalZone.setRotation(PedroComponent.follower().getHeading());

        robotLaunchZone.setPosition(PedroComponent.follower().getPose().getX(), PedroComponent.follower().getPose().getY());
        robotLaunchZone.setRotation(PedroComponent.follower().getHeading());

        shootDistance = ((robotToGoalZone.distanceTo(new Point(PoseLibrary.INSTANCE.goal.getX(), PoseLibrary.INSTANCE.goal.getY())) * 0.0254) - 0.2);
        robotInRange = shootDistance >= 0.9;

        farLaunch = robotLaunchZone.isInside(farLaunchArea);

        ActiveOpMode.telemetry().addData("Shoot Distance", shootDistance);
    }

}

