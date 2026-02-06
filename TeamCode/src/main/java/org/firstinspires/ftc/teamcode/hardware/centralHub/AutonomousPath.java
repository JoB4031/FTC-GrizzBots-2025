package org.firstinspires.ftc.teamcode.hardware.centralHub;

public class AutonomousPath {
    public void autonomousPathUpdate(hardware robot) {
        if (robot.autoTime.seconds() > 25) {
            switch (robot.pathBuilder.pathState) {
                case 0:
                    robot.followPath(robot.pathBuilder.scoreArtifact());
                    robot.setFlywheelToShootDistance();
                    robot.pathBuilder.setPathState(1);
                    break;
                case 1:
                    /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                    if (!robot.pathingIsBusy()) {
                        robot.shootAllBalls();
                        robot.pathBuilder.setPathState(2);
                    }
                    break;
                case 2:
                    if (!robot.pathingIsBusy()) {
                        robot.flywheel.off();
                        robot.intake.on();
                        robot.fidgetTech.setSnapPoint(12);
                        robot.update();
                        robot.followPath(robot.pathBuilder.grabFirstArtifacts());
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
                        robot.setFlywheelToShootDistance();
                        robot.followPath(robot.pathBuilder.scoreFirstArtifacts());
                        robot.pathBuilder.setPathState(4);
                    }
                    break;
                case 4:
                    if (!robot.pathingIsBusy()) {
                        robot.shootAllBalls();
                        robot.pathBuilder.setPathState(5);
                    }
                    break;
                case 5:
                    if (!robot.pathingIsBusy()) {
                        robot.flywheel.off();
                        robot.intake.on();
                        robot.fidgetTech.setSnapPoint(12);
                        robot.update();
                        robot.followPath(robot.pathBuilder.grabSecondArtifacts());
                        robot.pathBuilder.setPathState(3);
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
                        robot.setFlywheelToShootDistance();
                        robot.followPath(robot.pathBuilder.scoreSecondArtifacts());
                        robot.pathBuilder.setPathState(7);
                    }
                    break;
                case 7:
                    if (!robot.pathingIsBusy()) {
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
        } else {
            robot.followPath(robot.pathBuilder.leaveLaunchZone());
            robot.intake.off();
            robot.flywheel.off();
        }
    }
}