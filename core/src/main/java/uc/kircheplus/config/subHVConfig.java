package uc.kircheplus.config;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class subHVConfig extends Config {

    @SwitchSetting
    private final ConfigProperty<Boolean> hvenable = new ConfigProperty<>(true);

    @TextFieldSetting
    private final ConfigProperty<String> hvprefix = new ConfigProperty<>("&8[&cHV&8]");

    public ConfigProperty<Boolean> hvenable() {
        return hvenable;
    }

    public ConfigProperty<String> hvprefix() {
        return hvprefix;
    }

}
