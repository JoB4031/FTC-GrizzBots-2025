package org.firstinspires.ftc.teamcode.hardware.pedroPathing;
import com.pedropathing.geometry.Pose;
import com.skeletonarmy.marrow.zones.Point;
import com.skeletonarmy.marrow.zones.PolygonZone;

public class LaunchZoneTracker {

    private final PolygonZone closeLaunchArea =
            new PolygonZone(new Point(144, 144), new Point(72, 72), new Point(0, 144));

    private final PolygonZone farLaunchArea =
            new PolygonZone(new Point(48, 0), new Point(72, 24), new Point(96, 0));

    private final PolygonZone robotToGoalZone = new PolygonZone(1, 1);
    public final PolygonZone robotLaunchZone = new PolygonZone(17, 17.5);

    public boolean robotInRange;
    public boolean farLaunch;
    public double shootDistance;

    public void update(Pose pose, Point targetPoint) {
        robotToGoalZone.setPosition(pose.getX(), pose.getY());
        robotToGoalZone.setRotation(pose.getHeading());

        robotLaunchZone.setPosition(pose.getX(), pose.getY());
        robotLaunchZone.setRotation(pose.getHeading());

        shootDistance = ((robotToGoalZone.distanceTo(targetPoint) * 0.0254) - 0.2);

        robotInRange =
                (shootDistance >= 0.9);

        farLaunch = robotLaunchZone.isInside(farLaunchArea);
    }
    public Pose closestPoseToZone(PolygonZone zone, Pose... poses) {
        if (poses == null || poses.length == 0) {
            throw new IllegalArgumentException("At least one pose must be provided");
        }

        Pose closest = poses[0];
        double closestDistance = zone.distanceTo(new Point(closest.getX(), closest.getY()));

        for (int i = 1; i < poses.length; i++) {
            Pose current = poses[i];
            double currentDistance = zone.distanceTo(new Point(current.getX(), current.getY()));

            if (currentDistance < closestDistance) {
                closest = current;
                closestDistance = currentDistance;
            }
        }

        return closest;
    }

}

