package org.firstinspires.ftc.teamcode.hardware.subsystems;

import com.qualcomm.robotcore.util.ElapsedTime;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;

public class FidgetTech implements Subsystem {

    public static final FidgetTech INSTANCE = new FidgetTech();
    private FidgetTech() {}

    public enum artifactColor { PURPLE, GREEN, NONE }
    public static artifactColor[] artifactsHeld =
            { artifactColor.NONE, artifactColor.NONE, artifactColor.NONE };
    private final double[] positions = {
            0.0, 0.03, 0.07, 0.105, 0.145, 0.18, 0.22, 0.26,
            0.295, 0.325, 0.365, 0.405, 0.445, 0.485, 0.525, 0.565,
            0.6, 0.64, 0.68, 0.715, 0.755, 0.785, 0.825, 0.855,
            0.89, 0.925, 0.96, 0.995
    };
    private static int snapPosition = 14;
    public static int currentSlot = 1;
    public static boolean inIntakePosition = true;

    private static final ElapsedTime spinTime = new ElapsedTime();
    private static final double TIME_PER_SNAP = 0.25;
    private int snapSpaces = 0;
    public boolean spinComplete = false;

    private void setSnapPosition(int targetSnap) {
        spinTime.reset();
        snapSpaces = Math.abs(snapPosition - targetSnap);
        snapPosition = targetSnap;
    }
    private void goToShootSlot(int slot) {
        int snapPointsToMove = -2;
        if (slot == 0) {
            while (currentSlot != slot) {
                if (((snapPosition + snapPointsToMove)-3) % 6 == 0) {
                    setSnapPosition(snapPosition+snapPointsToMove);
                } else snapPointsToMove ++;
                periodic();
            }
        }
        if (slot == 1) {
            while (currentSlot != slot) {
                if (((snapPosition + snapPointsToMove)-5) % 6 == 0) {
                    setSnapPosition(snapPosition+snapPointsToMove);
                } else snapPointsToMove ++;
                periodic();
            }
        }
        if (slot == 2) {
            while (currentSlot != slot) {
                if (((snapPosition + snapPointsToMove)-7) % 6 == 0) {
                    setSnapPosition(snapPosition+snapPointsToMove);
                } else snapPointsToMove ++;
                periodic();
            }
        }
    }
    private void goToIntakeSlot(int slot) {
        int snapPointsToMove = -2;
        if (slot == 0) {
            while (currentSlot != slot) {
                if ((snapPosition + snapPointsToMove) % 6 == 0) {
                    setSnapPosition(snapPosition+snapPointsToMove);
                } else snapPointsToMove ++;
                periodic();
            }
        }
        if (slot == 1) {
            while (currentSlot != slot) {
                if (((snapPosition + snapPointsToMove)-2) % 6 == 0) {
                    setSnapPosition(snapPosition+snapPointsToMove);
                } else snapPointsToMove ++;
                periodic();
            }
        }
        if (slot == 2) {
            while (currentSlot != slot) {
                if (((snapPosition + snapPointsToMove)-4) % 6 == 0) {
                    setSnapPosition(snapPosition+snapPointsToMove);
                } else snapPointsToMove ++;
                periodic();
            }
        }
    }

    private final ServoEx fidgetTech = new ServoEx("fidgetTech", 0.001);

    public Command next = new Command(){

        @Override
        public void start () {
            setSnapPosition(snapPosition+2);
        }

        @Override
        public boolean isDone () {
            return spinComplete;
        }

    };
    public Command previous = new Command(){
        @Override
        public void start () {
            setSnapPosition(snapPosition-2);
        }

        @Override
        public boolean isDone () {
            return spinComplete;
        }

    };
    public Command setSnapPoint(int snapPoint) {
        return new Command() {

            @Override
            public void start() {
                setSnapPosition(snapPoint);
            }

            @Override
            public boolean isDone() {
                return spinComplete;
            }

        };
    }
    public Command goEmptyIntakeSlot = new Command() {
        @Override
        public void start() {
            if (artifactsHeld[0] == artifactColor.NONE) {
                goToIntakeSlot(0);
            } else if (artifactsHeld[1] == artifactColor.NONE) {
                goToIntakeSlot(1);
            } else if (artifactsHeld[2] == artifactColor.NONE) {
                goToIntakeSlot(2);
            }
        }
        @Override
        public boolean isDone() {
            return spinComplete;
        }
    };
    public Command goToArtifact(artifactColor artifact) {
        return new Command() {

            @Override
            public void start() {
                if (artifact == artifactColor.PURPLE) {
                    if (artifactsHeld[0] == artifactColor.PURPLE) {
                        goToShootSlot(0);
                    } else if (artifactsHeld[1] == artifactColor.PURPLE) {
                        goToShootSlot(1);
                    } else if (artifactsHeld[2] == artifactColor.PURPLE) {
                        goToShootSlot(2);
                    }
                } else {
                    if (artifactsHeld[0] == artifactColor.GREEN) {
                        goToShootSlot(0);
                    } else if (artifactsHeld[1] == artifactColor.GREEN) {
                        goToShootSlot(1);
                    } else if (artifactsHeld[2] == artifactColor.GREEN) {
                        goToShootSlot(2);
                    }
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
        spinComplete = spinTime.seconds() > snapSpaces * TIME_PER_SNAP;
        fidgetTech.setPosition(positions[snapPosition]);
        inIntakePosition = snapPosition % 2 == 0;
        if (inIntakePosition) {
            if (snapPosition % 6 == 0) {
                currentSlot = 0;
            } else if ((snapPosition - 2) % 6 == 0) {
                currentSlot = 1;
            } else if ((snapPosition - 4) % 6 == 0) {
                currentSlot = 2;
            } else currentSlot = 0;
        } else {
            if ((snapPosition-3) % 6 == 0) {
                currentSlot = 0;
            } else if ((snapPosition - 5) % 6 == 0) {
                currentSlot = 1;
            } else if ((snapPosition - 7) % 6 == 0) {
                currentSlot = 2;
            } else currentSlot = 0;
        }
    }

}