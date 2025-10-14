package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@TeleOp(name= "Move to Angle",group= "Jason's Tests")
public class Gyro_Test extends LinearOpMode {

    private DcMotor left_drive = null;
    private DcMotor right_drive = null;
    private IMU imu = null;
    private double Yaw = 0;
    private double Roll = 0;
    private double Pitch = 0;
    private int Angle = 0;
    @Override
    public void runOpMode() throws InterruptedException {
        left_drive = hardwareMap.get(DcMotor.class, "left_drive");
        right_drive = hardwareMap.get(DcMotor.class, "right_drive");
        left_drive.setDirection(DcMotor.Direction.REVERSE);
        right_drive.setDirection(DcMotor.Direction.FORWARD);
        left_drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.RIGHT;
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);
        // Now initialize the IMU with this mounting orientation
        // This sample expects the IMU to be in a REV Hub and named "imu".
        imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(orientationOnRobot));
        waitForStart();
        imu.resetYaw();
        while (opModeIsActive()) {
            if (gamepad1.dpadUpWasPressed()) Angle = Angle + 10;
            if (gamepad1.dpadDownWasPressed()) Angle = Angle - 10;
            if (gamepad1.dpadLeftWasPressed()) Angle = Angle - 1;
            if (gamepad1.dpadRightWasPressed()) Angle = Angle + 1;
            getAngles();
            if (gamepad1.circleWasPressed()) {
                turnAngle(Angle);
            }
            if (gamepad1.triangle) {
                getAngles();
                left_drive.setPower(1 + (Yaw / 180));
                right_drive.setPower(1 - (Yaw / 180));

            }
            left_drive.setPower(gamepad1.left_stick_y);
            right_drive.setPower(gamepad1.right_stick_y);
            telemetry.addData("Yaw", Yaw);
            telemetry.addData("Pitch", Pitch);
            telemetry.addData("Roll", Roll);
            telemetry.addData("Wanted Angle", Angle);
            telemetry.update();

        }
    }
    private void getAngles() {
        YawPitchRollAngles robotOrientation;
        robotOrientation = imu.getRobotYawPitchRollAngles();
        Yaw = robotOrientation.getYaw(AngleUnit.DEGREES);
        Pitch = robotOrientation.getPitch(AngleUnit.DEGREES);
        Roll = robotOrientation.getRoll(AngleUnit.DEGREES);
    }
    public void turnAngle(int angleToTurn) {
        if (Math.round(Yaw) > angleToTurn) {
            while (Math.round(Yaw) > angleToTurn) {
                left_drive.setPower(-Math.abs((Yaw-angleToTurn)/180));
                right_drive.setPower(Math.abs((Yaw-angleToTurn)/180));
                getAngles();
            }
            left_drive.setPower(0);
            right_drive.setPower(0);
        } else if (Math.round(Yaw) < angleToTurn) {
            while (Math.round(Yaw) < angleToTurn) {
                left_drive.setPower(Math.abs((Yaw-angleToTurn)/180));
                right_drive.setPower(-Math.abs((Yaw-angleToTurn)/180));
                getAngles();
            }
            left_drive.setPower(0);
            right_drive.setPower(0);
        }
    }
}

