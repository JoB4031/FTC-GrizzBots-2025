package org.firstinspires.ftc.teamcode.hardware;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotorEx;


import com.qualcomm.robotcore.util.ElapsedTime;

@Configurable
public class Motors{

    ElapsedTime time = new ElapsedTime();

    public DcMotor intake = null;
    public DcMotorEx flywheel = null;
    public DcMotor shooter = null;
    public Servo ballEjector = null;
    public Servo fidgetTech = null;
    PIDFController velocityController;
    //PID controller constants
    public static double kP = 0.0003;
    public static double kI = 0.0;
    public static double kD = 0.0;
    public static double kF = 0.00025;
    public double wantedVelocity;

    public int spinPosition;
    public final double[] spinPositions = {
            0.03,0.06,0.1,0.14,0.17,0.21,0.25,0.28,0.32,0.36,
            0.4,0.43,0.47,0.51,0.55,0.58,0.62,0.66,0.7,0.73,
            0.77,0.8,0.83,0.87,0.91,0.94,0.98
    };

    public void initMotors(HardwareMap hardwareMap) {
        // maps the motors and servos using the hardware map when called - Jason
        shooter = hardwareMap.get(DcMotor.class, "Shooter");
        intake = hardwareMap.get(DcMotor.class, "intake");
        ballEjector = hardwareMap.get(Servo.class, "ballEjector");
        fidgetTech = hardwareMap.get(Servo.class, "spinIndexer");

        spinPosition = 13;
        flywheel = hardwareMap.get(DcMotorEx.class, "Shooter");
        velocityController = new PIDFController(kP, kI, kD, kF);
        velocityController.setSetPoint(0);

    }
    // velocity is in RPM
    public void setFlywheelVelocity(double distance, double multiplier){
        velocityController.setSetPoint(multiplier*((6*Math.sqrt(398210*distance)-900*Math.sqrt(distance)+(Math.ceil(distance/5)*400))*(28.0/60.0)));
    }
    public void update() {
        // Updates the Flywheel Velocity
        double currentVelocity = flywheel.getVelocity();
        double power = velocityController.calculate(currentVelocity); // Get power from PIDF
        flywheel.setPower(power);
        // Sets the fidget tech position to pick-up or fire based on whether the intake is on or off
        if(intake.getPower() > 0) {
            if (spinPosition % 2 == 1) {
                if (spinPosition >= 13) {
                    spinPosition -= 1;
                } else spinPosition += 1;
            }
        } else {
            if(spinPosition % 2 == 0){
                if (spinPosition >= 13) {
                    spinPosition -= 1;
                } else spinPosition += 1;
            }
        }

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
            setFlywheelVelocity(distance,1);
            time.reset();
            while(time.seconds() < 3 ){
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
}