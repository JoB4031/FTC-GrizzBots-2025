package org.firstinspires.ftc.teamcode.hardware.subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

public class Intake implements Subsystem {
    public static final Intake INSTANCE = new Intake();
    private Intake() {}

    private final MotorEx externalIntake = new MotorEx("intake").floatMode();
    private final MotorEx internalIntake = new MotorEx("internalIntake").reversed().floatMode();

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
    public Command externalPower(double power) {
        return new SetPower(externalIntake, power).requires(externalIntake);
    }
    public Command internalPower(double power) {
        return new SetPower(internalIntake, power).requires(internalIntake);
    }

}

