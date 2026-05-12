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
    public static int currentSlot = 0;
    public static boolean inIntakePosition = true;

    private static final ElapsedTime spinTime = new ElapsedTime();
    private static final double TIME_PER_SNAP = 0.25;
    private int snapSpaces = 0;
    public boolean spinComplete = false;
    private final ServoEx fidgetTech = new ServoEx("fidgetTech", 0.001);

    private void getCurrentSlot() {
        if (inIntakePosition) {
            if (snapPosition == 2 || snapPosition == 8 || snapPosition == 14 || snapPosition == 20 || snapPosition == 26) currentSlot = 0;
            if (snapPosition == 4 || snapPosition == 10 || snapPosition == 16 || snapPosition == 22 || snapPosition == 28) currentSlot = 1;
            if (snapPosition == 0 || snapPosition == 6 || snapPosition == 12 || snapPosition == 18 || snapPosition == 24) currentSlot = 2;
        } else {
            if (snapPosition == 1 || snapPosition == 7 || snapPosition == 13 || snapPosition == 19 || snapPosition == 25) currentSlot = 1;
            if (snapPosition == 3 || snapPosition == 9 || snapPosition == 15 || snapPosition == 21 || snapPosition == 27) currentSlot = 2;
            if (snapPosition == 5 || snapPosition == 11 || snapPosition == 17 || snapPosition == 23) currentSlot = 0;
        }
    }
    private void setSnapPosition(int targetSnap) {
        spinTime.reset();
        snapSpaces = Math.abs(snapPosition - targetSnap);
        snapPosition = targetSnap;
    }
    public void goToSlot(int slot, boolean intakePosition) {
        if (intakePosition == inIntakePosition) {
            if (snapPosition > 14) {
                setSnapPosition(snapPosition+((currentSlot-slot)*-2));
            } else {
                setSnapPosition(snapPosition-((currentSlot-slot)*-2));
            }
        } else {
            if (snapPosition > 14) {
                setSnapPosition(snapPosition+((((currentSlot-slot)*2)+3)*-1));
            } else {
                setSnapPosition(snapPosition-((((currentSlot-slot)*2)+3)*-1));
            }
        }
    }

    public Command goToArtifact(artifactColor artifact) {
        return new Command() {
            @Override
            public void start() {
                int slotFound = 0;
                for (artifactColor artifactWanted : artifactsHeld) {
                    if (artifactWanted == artifact) return;
                    slotFound ++;
                }
                if (slotFound > 3) {
                    goToSlot(slotFound, artifact == artifactColor.NONE);
                }
            }
            @Override
            public boolean isDone() {
                return spinComplete;
            }
        };
    }

    @Override
    public void periodic() {
        fidgetTech.setPosition(positions[snapPosition]);
        inIntakePosition = snapPosition % 2 == 0;
        getCurrentSlot();
        spinComplete = spinTime.seconds() > snapSpaces * TIME_PER_SNAP;
        fidgetTechFull = (artifactsHeld[0] != artifactColor.NONE) && (artifactsHeld[1] != artifactColor.NONE) && (artifactsHeld[2] != artifactColor.NONE);

        ActiveOpMode.telemetry().addData("Current Slot", currentSlot);
        ActiveOpMode.telemetry().addData("In Intake Position", inIntakePosition);
        ActiveOpMode.telemetry().addData("Artifacts Held", artifactsHeld);
    }

}