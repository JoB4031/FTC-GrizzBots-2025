package org.firstinspires.ftc.teamcode.hardware.sensors;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;

public class LEDIndicator implements Subsystem {
    public static final LEDIndicator INSTANCE = new LEDIndicator();
    private LEDIndicator() {}
    private final ServoEx led = new ServoEx("LED");

    public Command red() {
        return new Command() {
            @Override
            public void start() {led.setPosition(0.277);}
            @Override
            public boolean isDone() {return true;}
        }.requires(this);
    }
    public Command orange() {
        return new Command() {
            @Override
            public void start() {led.setPosition(0.333);}
            @Override
            public boolean isDone() {return true;}
        }.requires(this);
    }
    public Command yellow() {
        return new Command() {
            @Override
            public void start() {led.setPosition(0.388);}
            @Override
            public boolean isDone() {return true;}
        }.requires(this);
    }
    public Command green() {
        return new Command() {
            @Override
            public void start() {led.setPosition(0.500);}
            @Override
            public boolean isDone() {return true;}
        }.requires(this);
    }
    public Command blue() {
        return new Command() {
            @Override
            public void start() {led.setPosition(0.611);}
            @Override
            public boolean isDone() {return true;}
        }.requires(this);
    }
    public Command purple() {
        return new Command() {
            @Override
            public void start() {led.setPosition(0.722);}
            @Override
            public boolean isDone() {return true;}
        }.requires(this);
    }
    public Command white() {
        return new Command() {
            @Override
            public void start() {led.setPosition(1);}
            @Override
            public boolean isDone() {return true;}
        }.requires(this);
    }
    public Command off() {
        return new Command() {
            @Override
            public void start() {led.setPosition(0);}
            @Override
            public boolean isDone() {return true;}
        }.requires(this);
    }
}