package org.firstinspires.ftc.teamcode.hardware.subsystems;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;

public class Flywheel implements Subsystem {
    public static final Flywheel INSTANCE = new Flywheel();
    private Flywheel() { }
    private final MotorEx leftFlywheel = new MotorEx("leftFlywheel")
            .reversed()
            .floatMode();
    private final MotorEx rightFlywheel = new MotorEx("rightFlywheel")
            .floatMode();
    private final MotorGroup flywheel = new MotorGroup(rightFlywheel, leftFlywheel);
    private final ControlSystem velocityController = ControlSystem.builder()
            .velPid(0.006, 0.0, 0.0)
            .build();

    public Command setVelocity(double velocity) {
        return new RunToVelocity(velocityController, velocity).requires(this);
    }
    public Command stopPower() {
        return new RunToVelocity(velocityController, 0).requires(this);
    }

    @Override
    public void periodic() {
        if (velocityController.getGoal().getVelocity() > 0) {
            flywheel.setPower(velocityController.calculate(flywheel.getState()));
        } else {
            flywheel.setPower(0);
        }
        ActiveOpMode.telemetry().addData("Goal", velocityController.getGoal().getVelocity());
    }

}


