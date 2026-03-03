package org.firstinspires.ftc.teamcode.hardware.subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

public class IntakeSubsystem implements Subsystem {
    public static final IntakeSubsystem INSTANCE = new IntakeSubsystem();
    private IntakeSubsystem() {}

    private MotorEx externalIntake = new MotorEx("externalIntake").floatMode();
    private MotorEx internalIntake = new MotorEx("internalIntake").floatMode();

    public Command setPower(double exteriorPower, double interiorPower) {
        return new Command() {

            @Override
            public void start() {
                externalIntake.setPower(exteriorPower);
                internalIntake.setPower(interiorPower);
            }

            @Override
            public boolean isDone() {
                return true;
            }
        }.requires(externalIntake, internalIntake);
    }

    public Command externalPower(double power)  {
        return new SetPower(externalIntake, power).requires(externalIntake);
    }

    public Command internalPower(double power) {
        return new SetPower(internalIntake, power).requires(internalIntake);
    }

    public boolean isPowered() {
        return (externalIntake.getPower() > 0);
    }

}

