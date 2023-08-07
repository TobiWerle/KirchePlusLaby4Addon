package uc.kircheplus.config;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class subDrinkConfig extends Config {

    @SwitchSetting
    private final ConfigProperty<Boolean> drinkmessageenable = new ConfigProperty<>(true);

    @SwitchSetting
    private final ConfigProperty<Boolean> drinksoundenable = new ConfigProperty<>(true);

    @SwitchSetting
    private final ConfigProperty<Boolean> dryoutmessageenable = new ConfigProperty<>(true);

    @SwitchSetting
    private final ConfigProperty<Boolean> dryoutsoundenable = new ConfigProperty<>(true);

    public ConfigProperty<Boolean> drinkmessageenable() {
        return drinkmessageenable;
    }

    public ConfigProperty<Boolean> drinksoundenable() {
        return drinksoundenable;
    }

    public ConfigProperty<Boolean> dryoutmessageenable() {
        return dryoutmessageenable;
    }
    public ConfigProperty<Boolean> dryoutsoundenable() {
        return dryoutsoundenable;
    }
}
