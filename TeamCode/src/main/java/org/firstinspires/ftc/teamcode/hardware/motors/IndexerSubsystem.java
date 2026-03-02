package org.firstinspires.ftc.teamcode.hardware.motors;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class IndexerSubsystem implements Subsystem {

    public static final IndexerSubsystem INSTANCE = new IndexerSubsystem();
    private IndexerSubsystem() {}

    private ServoEx fidgetTech = new ServoEx("fidgetTech", 0.001);
    private NormalizedColorSensor artifactColorDetector;

    private int snapPoint = 0;
    private enum artifactColor {PURPLE, GREEN, NONE}

    private static artifactColor[] artifactsHeld = {artifactColor.NONE, artifactColor.NONE, artifactColor.NONE};

    private final double[] positions = {
            0.0, 0.03, 0.07, 0.105, 0.145, 0.18, 0.22, 0.26,
            0.295, 0.325, 0.365, 0.405, 0.445, 0.485, 0.525, 0.565,
            0.6, 0.64, 0.68, 0.715, 0.755, 0.785, 0.825, 0.855,
            0.89, 0.925, 0.96, 0.995
    };

    // ----------------------------------------------------------
    // GEOMETRY HELPERS
    // ----------------------------------------------------------

    /** True if this snapPoint is an intake position. */
    public boolean isIntakePosition(int p) {
        return (p % 4 == 0);
    }

    /** True if this snapPoint is a shoot position. */
    public boolean isShootPosition(int p) {
        return (p % 4 == 3);
    }

    /** Returns the slot number (0,1,2) for any snapPoint. */
    public int getSlot(int p) {
        return ((p % 28) / 4) % 3;
    }

    /** Returns the current slot. */
    public int getCurrentSlot() {
        return getSlot(snapPoint);
    }

    /** Returns the intake positions for a given slot. */
    public int[] intakePositionsForSlot(int slot) {
        switch (slot) {
            case 0: return new int[]{0, 12, 24};
            case 1: return new int[]{4, 16};
            case 2: return new int[]{8, 20};
        }
        return new int[]{};
    }

    /** Returns the shoot positions for a given slot. */
    public int[] shootPositionsForSlot(int slot) {
        switch (slot) {
            case 0: return new int[]{3, 15, 27};
            case 1: return new int[]{7, 19};
            case 2: return new int[]{11, 23};
        }
        return new int[]{};
    }

    /** Finds the closest snapPoint from a list of targets. */
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

    /** Computes shortest rotation direction to a target snapPoint. */
    private int shortestRotationTarget(int target) {
        int total = positions.length; // 28
        int forward = (target - snapPoint + total) % total;
        int backward = (snapPoint - target + total) % total;

        return (forward <= backward) ? target : target;
    }

    // ----------------------------------------------------------
    // COMMANDS
    // ----------------------------------------------------------

    /** Moves to the next intake/shoot pair (manual). */
    public Command next = new Command() {
        @Override
        public void start() {
            snapPoint = (snapPoint + 1) % 28;
            new SetPosition(fidgetTech, positions[snapPoint]);
        }
        @Override
        public boolean isDone() { return true; }
    };

    /** Moves to the previous intake/shoot pair (manual). */
    public Command previous = new Command() {
        @Override
        public void start() {
            snapPoint = (snapPoint - 1 + 28) % 28;
            new SetPosition(fidgetTech, positions[snapPoint]);
        }
        @Override
        public boolean isDone() { return true; }
    };

    /** Sets snapPoint directly. */
    public Command setSnapPoint(int point) {
        return new Command() {
            @Override
            public void start() {
                snapPoint = point % 28;
                new SetPosition(fidgetTech, positions[snapPoint]);
            }
            @Override
            public boolean isDone() { return true; }
        };
    }

    /** Goes to intake for a specific slot. */
    public Command goToIntake(int slot) {
        int target = closestOf(intakePositionsForSlot(slot));
        target = shortestRotationTarget(target);

        return setSnapPoint(target);
    }

    /** Goes to shoot for a specific slot. */
    public Command goToShoot(int slot) {
        int target = closestOf(shootPositionsForSlot(slot));
        target = shortestRotationTarget(target);

        return setSnapPoint(target);
    }

    /** Goes to intake for the current slot. */
    public Command goToCurrentIntake() {
        return goToIntake(getCurrentSlot());
    }

    /** Goes to shoot for the current slot. */
    public Command goToCurrentShoot() {
        return goToShoot(getCurrentSlot());
    }

    /** Goes to the slot 0 intake closest to the center (snapPoint 14). */
    public Command reset() {
        return new Command() {
            @Override
            public void start() {
                snapPoint = 12;
                new SetPosition(fidgetTech, positions[snapPoint]);
            }

            @Override
            public boolean isDone() { return true; }
        };
    }

    public int getSnapPoint() {
        return snapPoint;
    }
}
