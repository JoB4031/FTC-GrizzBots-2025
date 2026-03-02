package org.firstinspires.ftc.teamcode.hardware.motors;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

public class IntakeSubsystem implements Subsystem {
    public static final IntakeSubsystem INSTANCE = new IntakeSubsystem();
    private IntakeSubsystem() {}

    private
    private MotorEx externalIntake = new MotorEx("externalIntake").floatMode();
    private MotorEx internalIntake = new MotorEx("internalIntake").floatMode();

    public void setPower(double exteriorPower, double interiorPower) {
        externalIntake.setPower(exteriorPower);
        internalIntake.setPower(interiorPower);
    }

    public Command externalOn(double power)  {
        return new SetPower(externalIntake, power).requires(externalIntake);
    }

    public Command internalPower(double power) {
        return new SetPower(internalIntake, power).requires(internalIntake);
    }

    public boolean isPowered() {
        return (externalIntake.getPower() > 0);
    }
}

