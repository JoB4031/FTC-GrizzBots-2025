package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.skeletonarmy.marrow.prompts.*;
@Autonomous(name="Marrow",group="Examples")
public class Marrow extends OpMode {
    enum Alliance {
        RED,
        BLUE
    }
    private Prompter prompter = new Prompter(this);

    @Override
    public void init() {
        prompter.prompt("alliance", new OptionPrompt<>("Select Alliance", Alliance.RED, Alliance.BLUE))
                .prompt("delay", new ValuePrompt("Start Delay", 0.0, 10.0, 0.0, 1.0))
                .prompt("enablePark", new BooleanPrompt("Enable Park?", true))
                .prompt("parkLocation", () -> {
                    if (prompter.get("enablePark").equals(true)) {
                        return new OptionPrompt<>("Select Park Location", 1, 2);
                    }
                    return null; // Skip if the driver chose not to park
                })
                .onComplete(this::onPromptsComplete);
    }

    public void onPromptsComplete() {
        Alliance alliance = prompter.get("alliance");
        double delay = prompter.get("delay");
        boolean enablePark = prompter.get("enablePark");
        int parkLocation = prompter.getOrDefault("parkLocation", 0);

        telemetry.addData("Selected Alliance", alliance);
        telemetry.addData("Selected Delay", delay);
        telemetry.addData("Is Parking?", enablePark);
        telemetry.addData("Selected Park Location", parkLocation);
        telemetry.update();
    }

    @Override
    public void init_loop() {
        prompter.run();
    }

    @Override
    public void loop() {}
}