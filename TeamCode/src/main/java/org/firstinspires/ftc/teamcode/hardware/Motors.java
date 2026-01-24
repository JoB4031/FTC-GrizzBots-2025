package org.firstinspires.ftc.teamcode.hardware;

import com.arcrobotics.ftclib.hardware.motors.CRServo;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@Configurable
public class Motors{

    public ElapsedTime time = new ElapsedTime();
    public DcMotor intake = null;
    public Servo leftIntake = null;
    public Servo rightIntake = null;
    public boolean doNotSpin = false;
    public DcMotorEx rightFlywheel = null;
    public DcMotorEx leftFlywheel = null;
    public Servo ballEjector = null;
    public Servo fidgetTech = null;
    public PIDFController velocityController;
    //PID controller constants
    public static double kP = 0.0003;
    public static double kI = 0.0;
    public static double kD = 0.0;
    public static double kF = 0.00025;
    public int spinPosition;
    public final double[] spinPositions = {
            0,0,0.04,0.08,0.12,0.15,0.19,0.22,0.26,0.3,0.33,0.37,0.41,
            0.45,0.48,0.52,0.56,0.61,0.64,0.68,0.72,0.75,0.79,0.83,
            0.86,0.9,0.93,0.97
    };
    public boolean ableToShoot;

    public void initMotors(HardwareMap hardwareMap) {
        // maps the motors and servos using the hardware map when called - Jason
        intake = hardwareMap.get(DcMotor.class, "intake");
        leftIntake = hardwareMap.get(Servo.class, "leftIntake");
        rightIntake = hardwareMap.get(Servo.class, "rightIntake");


        ballEjector = hardwareMap.get(Servo.class, "ballEjector");
        fidgetTech = hardwareMap.get(Servo.class, "spinIndexer");

        spinPosition = 15;

        rightFlywheel = hardwareMap.get(DcMotorEx.class, "rightFlywheel");
        rightFlywheel.setDirection(DcMotorEx.Direction.FORWARD);
        rightFlywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        leftFlywheel = hardwareMap.get(DcMotorEx.class, "leftFlywheel");
        leftFlywheel.setDirection(DcMotorEx.Direction.REVERSE);
        leftFlywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        velocityController = new PIDFController(kP, kI, kD, kF);
        velocityController.setSetPoint(0);

    }
    // velocity is in RPM
    public void setFlywheelVelocity(double distance){
        velocityController.setSetPoint((6.3*Math.sqrt(398210*distance)-900*Math.sqrt(distance)+(Math.ceil(distance/5)*400))*(28.0/60.0));
    }
    public void update() {
        // Updates the Flywheel Velocity
        double currentVelocity = rightFlywheel.getVelocity();
        double power = velocityController.calculate(currentVelocity); // Get power from PIDF

        if(velocityController.getSetPoint() <= 0) {
            rightFlywheel.setPower(0);
            leftFlywheel.setPower(0);
        } else {
            rightFlywheel.setPower(power);
            leftFlywheel.setPower(power);
        }

        // Sets the fidget tech position to pick-up or fire based on whether the intake is on or off
        if(rightFlywheel.getPower() <= 0 || doNotSpin) {
            if (spinPosition % 2 == 1) {
                if (spinPosition >= 15) {
                    spinPosition -= 1;
                } else spinPosition += 1;
            }
        } else {
            if(spinPosition % 2 == 0){
                if (spinPosition >= 15) {
                    spinPosition -= 1;
                } else spinPosition += 1;
            }
        }
        ableToShoot = (velocityController.getVelocityError() < 100 && velocityController.getVelocityError() > -100);
        // moves the Fidget Tech to the set position
        fidgetTech.setPosition(spinPositions[spinPosition]);
    }

    //This public void adds a function to shoot all three artifacts - Nikola
     public void shootAllBalls (double distance){
        // Code to shoot all three artifacts when △ is pressed
        // Sets up a variable used for the loop to shoot three artifacts - Nikola
        int shootAll = 0;
        intake.setPower(0);
        spinPosition = 9;
        while(shootAll < 3){
            setFlywheelVelocity(distance);
            time.reset();
            while(!ableToShoot){
                //just chill
                update();
            }
            ballEjector.setPosition(0.3);
            time.reset();
            while (time.seconds() < 0.5) {
                //you get to chill again
                update();
            }
            ballEjector.setPosition(0);
            spinPosition += 2;
            shootAll += 1;
        }


    }
    public void setIntake(double power) {
        intake.setPower(power);
        if (power <= 0) {
            leftIntake.setPosition(.5);
            rightIntake.setPosition(.5);
        } else {
            leftIntake.setPosition(0);
            rightIntake.setPosition(1);
        }

    }
}