package org.firstinspires.ftc.teamcode.tuners;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.skeletonarmy.marrow.prompts.BooleanPrompt;
import com.skeletonarmy.marrow.prompts.OptionPrompt;
import com.skeletonarmy.marrow.prompts.ValuePrompt;
import com.skeletonarmy.marrow.settings.SettingsOpMode;

@TeleOp(name="Settings")
public class SettingsSetter extends SettingsOpMode {
    public enum alliance {RED, BLUE}
    public enum startLocations {NEAR, FAR}
    @Override
    public void defineSettings() {
        add("debug_mode", "Debug Mode", new BooleanPrompt("Enable debug mode?", false));
        add("alliance", "Select Alliance", new OptionPrompt<>("Select alliance", alliance.RED, alliance.BLUE));
        add("start position", "Select Start", new OptionPrompt<>("select start", startLocations.NEAR, startLocations.FAR));
        add("artifact sets", "Artifact Sets", new ValuePrompt("How Many", 0, 3, 2, 1));

    }
}