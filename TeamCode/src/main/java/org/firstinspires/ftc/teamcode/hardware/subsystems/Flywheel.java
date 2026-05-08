package org.firstinspires.ftc.teamcode.hardware.subsystems;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

public class Flywheel implements Subsystem {
    public static final Flywheel INSTANCE = new Flywheel();
    private Flywheel() { }
    private double goal = 0;
    private final MotorEx leftFlywheel = new MotorEx("leftFlywheel").reversed().floatMode();
    private final MotorEx rightFlywheel = new MotorEx("rightFlywheel").floatMode();
    private final MotorGroup flywheel = new MotorGroup(rightFlywheel, leftFlywheel);

    private final ControlSystem velocityController = ControlSystem.builder()
            .velPid(0.006, 0.0, 0.0)
            .basicFF(0.003, 0.08, 0.0)
            .build();

    public Command setVelocity(double velocity) {
        this.goal = velocity;
        return new RunToVelocity(velocityController, velocity).requires(this);
    }

    public Command stopPower() {
        this.goal = 0;
        return new SetPower(flywheel, 0).requires(this);
    }

    public boolean atSpeed() {
        double current = flywheel.getState().getVelocity();
        return Math.abs(current - goal) < 50;
    }

    @Override
    public void periodic() {
        if (goal == 0) {
            flywheel.setPower(0);
            return;
        }
        flywheel.setPower(velocityController.calculate(flywheel.getState()));
    }

}


