package org.firstinspires.ftc.teamcode.hardware.subsystems;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.hardware.sensors.ArtifactSensor;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class FidgetTech implements Subsystem {

    public static final FidgetTech INSTANCE = new FidgetTech();
    private FidgetTech() {}

    private static final ElapsedTime spinTime = new ElapsedTime();
    private static final double TIME_PER_SNAP = 0.25;

    private final ServoEx fidgetTech = new ServoEx("fidgetTech", 0.001);

    private int snapPoint = 0;
    private int snapPointsMoved = 0;
    public boolean spinComplete = false;

    public enum artifactColor { PURPLE, GREEN, NONE }

    public static artifactColor[] artifactsHeld =
            { artifactColor.NONE, artifactColor.NONE, artifactColor.NONE };

    // 28 servo positions
    private final double[] positions = {
            0.0, 0.03, 0.07, 0.105, 0.145, 0.18, 0.22, 0.26,
            0.295, 0.325, 0.365, 0.405, 0.445, 0.485, 0.525, 0.565,
            0.6, 0.64, 0.68, 0.715, 0.755, 0.785, 0.825, 0.855,
            0.89, 0.925, 0.96, 0.995
    };

    // ----------------------------------------------------------
    // GEOMETRY (CORRECTED FOR 14 PHYSICAL SLOTS)
    // ----------------------------------------------------------

    /** True if this snapPoint is an intake position (even numbers). */
    public boolean isIntakePosition(int p) {
        return (p % 2 == 0);
    }

    /** True if this snapPoint is a shoot position (odd numbers). */
    public boolean isShootPosition(int p) {
        return (p % 2 == 1);
    }

    /** Physical slot index 0–13. */
    public int getPhysicalSlot(int p) {
        return (p / 2);  // each slot uses 2 snap points
    }

    /** Logical slot index 0–2. */
    public int getSlot(int p) {
        return getPhysicalSlot(p) % 3;
    }

    public int getCurrentSlot() {
        return getSlot(snapPoint);
    }

    /** Intake positions for each logical slot. */
    public int[] intakePositionsForSlot(int slot) {
        switch (slot) {
            case 0: return new int[]{0, 6, 12, 18, 24};
            case 1: return new int[]{2, 8, 14, 20, 26};
            case 2: return new int[]{4, 10, 16, 22};
        }
        return new int[]{};
    }

    /** Shoot positions for each logical slot. */
    public int[] shootPositionsForSlot(int slot) {
        switch (slot) {
            case 0: return new int[]{3, 9, 15, 21, 27};
            case 1: return new int[]{5, 11, 17, 23, 1};
            case 2: return new int[]{7, 13, 19, 25};
        }
        return new int[]{};
    }

    /** Finds closest target snapPoint. */
    private int closestOf(int[] targets) {
        int best = targets[0];
        int bestDist = Math.abs(snapPoint - best);

        for (int t : targets) {
            int d = Math.abs(snapPoint - t);
            if (d < bestDist) {
                best = t;
                bestDist = d;
            }
        }
        return best;
    }

    /** Computes shortest rotation direction. */
    private int shortestRotationTarget(int target) {
        int total = 28;
        int forward = (target - snapPoint + total) % total;
        int backward = (snapPoint - target + total) % total;
        return (forward <= backward) ? target : target;
    }

    // ----------------------------------------------------------
    // COMMANDS
    // ----------------------------------------------------------

    /** Manual next. */
    public Command next = new Command() {
        @Override
        public void start() {
            snapPoint = (snapPoint + 1) % 28;
            snapPointsMoved = 2;
            spinTime.reset();
            new SetPosition(fidgetTech, positions[snapPoint]);
        }
        @Override
        public boolean isDone() { return true; }
    }.requires(spinComplete);

    /** Manual previous. */
    public Command previous = new Command() {
        @Override
        public void start() {
            snapPoint = (snapPoint - 1 + 28) % 28;
            snapPointsMoved = 2;
            spinTime.reset();
            new SetPosition(fidgetTech, positions[snapPoint]);
        }
        @Override
        public boolean isDone() { return true; }
    }.requires(spinComplete);

    /** Move to a specific snapPoint. */
    public Command setSnapPoint(int point) {
        return new Command() {
            @Override
            public void start() {
                int newPoint = (point + 28) % 28;

                int total = 28;
                int forward = (newPoint - snapPoint + total) % total;
                int backward = (snapPoint - newPoint + total) % total;
                snapPointsMoved = Math.min(forward, backward);
                spinTime.reset();
                snapPoint = newPoint;
                new SetPosition(fidgetTech, positions[snapPoint]);
            }

            @Override
            public boolean isDone() { return spinComplete; }
        }.requires(this);
    }

    /** Go to intake for a logical slot. */
    public Command goToIntake(int slot) {
        int target = closestOf(intakePositionsForSlot(slot));
        return setSnapPoint(shortestRotationTarget(target));
    }

    /** Go to shoot for a logical slot. */
    public Command goToShoot(int slot) {
        int target = closestOf(shootPositionsForSlot(slot));
        return setSnapPoint(shortestRotationTarget(target));
    }

    public Command goToCurrentIntake() {
        return goToIntake(getCurrentSlot());
    }

    public Command goToCurrentShoot() {
        return goToShoot(getCurrentSlot());
    }

    /** Go to shoot position for the first slot containing the given color. */
    public Command goToColorArtifact(artifactColor color) {
        for (int i = 0; i < artifactsHeld.length; i++) {
            if (artifactsHeld[i] == color) {
                return goToShoot(i);
            }
        }
        return goToCurrentShoot();
    }

    /** Reset to slot 0 intake (snapPoint 0). */
    public Command reset() {
        return setSnapPoint(0);
    }

    public int getSnapPoint() {
        return snapPoint;
    }

    public boolean isFull() {
        for (artifactColor a : artifactsHeld) {
            if (a == artifactColor.NONE) return false;
        }
        return true;
    }

    public int findEmptySlot() {
        for (int i = 0; i < artifactsHeld.length; i++) {
            if (artifactsHeld[i] == artifactColor.NONE) return i;
        }
        return 0;
    }

    @Override
    public void periodic() {
        spinComplete = spinTime.seconds() >= snapPointsMoved * TIME_PER_SNAP;

        if (spinComplete && isIntakePosition(snapPoint)) {
            artifactColor detected = ArtifactSensor.INSTANCE.getArtifact();
            if (detected != artifactColor.NONE) {
                artifactsHeld[getCurrentSlot()] = detected;
            }
        }
    }
}