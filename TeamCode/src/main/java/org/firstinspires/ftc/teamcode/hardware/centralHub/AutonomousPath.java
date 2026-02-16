package org.firstinspires.ftc.teamcode.hardware.centralHub;

import org.firstinspires.ftc.teamcode.theKeep.TheKeepAuto;

public class AutonomousPath {
    public void autonomousPathUpdate(hardware robot) {
        switch (robot.pathBuilder.pathState) {
            case 0:
                robot.doNotSpin = true;
                robot.followPath(robot.pathBuilder.scoreArtifact());
                robot.setFlywheelToShootDistance();
                robot.pathBuilder.setPathState(1);
                break;
            case 1:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!robot.pathingIsBusy()) {
                    robot.doNotSpin = false;
                    robot.shootAllBalls();
                    robot.pathBuilder.setPathState(TheKeepAuto.artifactsToCollect == 0 ? 8 : 2);
                }
                break;
            case 2:
                if (!robot.pathingIsBusy()) {
                    robot.flywheel.off();
                    robot.intake.on();
                    robot.fidgetTech.setSnapPoint(12);
                    robot.update();
                    robot.pathFollower.follower.followPath(robot.pathBuilder.grabFirstArtifacts(), 0.5, true);
                    robot.pathBuilder.setPathState(3);
                }
                break;
            case 3:

                robot.automaticPickup();
                if (!robot.pathingIsBusy()) {
                    robot.timer.reset();
                    while (robot.timer.seconds() < 2) {
                        robot.update();
                        robot.automaticPickup();
                    }
                    robot.intake.setPower(0, -1);
                    robot.doNotSpin = true;
                    robot.setFlywheelToShootDistance();
                    robot.followPath(robot.pathBuilder.scoreFirstArtifacts());
                    robot.intake.setPower(-1,-1);
                    robot.pathBuilder.setPathState(4);
                }
                break;
            case 4:
                if (!robot.pathingIsBusy()) {
                    robot.doNotSpin = false;
                    robot.shootAllBalls();
                    robot.pathBuilder.setPathState(TheKeepAuto.artifactsToCollect == 1 ? 8 : 5);
                }
                break;
            case 5:
                if (!robot.pathingIsBusy()) {
                    robot.flywheel.off();
                    robot.intake.on();
                    robot.fidgetTech.setSnapPoint(12);
                    robot.update();
                    robot.pathFollower.follower.followPath(robot.pathBuilder.grabSecondArtifacts(), 0.5, true);
                    robot.pathBuilder.setPathState(6);
                }
                break;
            case 6:

                robot.automaticPickup();
                if (!robot.pathingIsBusy()) {
                    robot.timer.reset();
                    while (robot.timer.seconds() < 2) {
                        robot.update();
                        robot.automaticPickup();
                    }
                    robot.doNotSpin = true;
                    robot.setFlywheelToShootDistance();
                    robot.followPath(robot.pathBuilder.scoreSecondArtifacts());
                    robot.intake.setPower(-1,-1);
                    robot.pathBuilder.setPathState(7);
                }
                break;
            case 7:
                if (!robot.pathingIsBusy()) {
                    robot.doNotSpin = false;
                    robot.shootAllBalls();
                    robot.pathBuilder.setPathState(8);
                }
                break;
            case 8:
                if (!robot.pathingIsBusy()) {
                    robot.flywheel.off();
                    robot.intake.off();
                    robot.update();
                    robot.followPath(robot.pathBuilder.leaveLaunchZone());
                    robot.pathBuilder.setPathState(-1);
                }
                break;
        }
    }
}
