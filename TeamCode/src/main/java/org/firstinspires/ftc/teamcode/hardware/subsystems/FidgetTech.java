package org.firstinspires.ftc.teamcode.hardware.subsystems;

import com.qualcomm.robotcore.util.ElapsedTime;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.ServoEx;

public class FidgetTech implements Subsystem {

    public static final FidgetTech FIDGET_TECH = new FidgetTech();
    private FidgetTech() {}

    public enum artifactColor { PURPLE, GREEN, UNKNOWN, NONE }
    public static artifactColor[] artifactsHeld =
            { artifactColor.NONE, artifactColor.NONE, artifactColor.NONE };
    public boolean fidgetTechFull = false;

    private final double[] positions = {
            0.0, 0.03, 0.07, 0.105, 0.145, 0.18, 0.22, 0.26,
            0.295, 0.325, 0.365, 0.405, 0.445, 0.485, 0.525, 0.565,
            0.6, 0.64, 0.68, 0.715, 0.755, 0.785, 0.825, 0.855,
            0.89, 0.925, 0.96, 0.995
    };
    private static int snapPosition = 14;
    private static final ElapsedTime spinTime = new ElapsedTime();

    private final ServoEx fidgetTech = new ServoEx("fidgetTech", 0.001);

    public Command nextSlot() {
        return new Command() {
            @Override
            public void start() {
                if (snapPosition != 27) snapPosition -= 1;
            }
            @Override
            public boolean isDone() {
                return true;
            }
        }.requires(this);
    }
    public Command previousSlot() {
        return new Command() {
            @Override
            public void start() {
                if (snapPosition != 1) snapPosition += 1;
            }
            @Override
            public boolean isDone() {
                return true;
            }
        }.requires(this);
    }

    @Override
    public void periodic() {
        fidgetTech.setPosition(positions[snapPosition]);
    }

}