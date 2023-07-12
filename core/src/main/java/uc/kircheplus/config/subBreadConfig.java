package uc.kircheplus.config;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class subBreadConfig extends Config {

    @SwitchSetting
    private final ConfigProperty<Boolean> breadenable = new ConfigProperty<>(true);

    @TextFieldSetting
    private final ConfigProperty<String> breadprefix = new ConfigProperty<>("&8[&2✔&8]");

    public ConfigProperty<Boolean> breadenable() {
        return breadenable;
    }

    public ConfigProperty<String> breadprefix() {
        return breadprefix;
    }

}
