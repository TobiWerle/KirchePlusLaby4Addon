package uc.kircheplus.config;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class subDrinkConfig {

    @SwitchSetting
    private final ConfigProperty<Boolean> drinkmessageenable = new ConfigProperty<>(true);

    @SwitchSetting
    private final ConfigProperty<Boolean> drinksoundenable = new ConfigProperty<>(true);

    public ConfigProperty<Boolean> drinkmessageenable() {
        return drinkmessageenable;
    }

    public ConfigProperty<Boolean> drinksoundenable() {
        return drinksoundenable;
    }

}
