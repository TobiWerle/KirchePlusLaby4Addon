package uc.kircheplus.config;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.annotation.SettingListener;
import net.labymod.api.configuration.settings.annotation.SettingSection;

@ConfigName("settings")
public class config extends AddonConfig {

    @SwitchSetting
    private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

    @SettingSection("addonsettings")
    public final subHVConfig hv = new subHVConfig();

    public final subBreadConfig bread = new subBreadConfig();

    public final subDrinkConfig drink = new subDrinkConfig();

    @DropdownSetting
    private final ConfigProperty<uploadTypes> uploadtype = new ConfigProperty<>(uploadTypes.KIRCHEPLUSIMG);

    @SwitchSetting
    private final ConfigProperty<Boolean> owngmailenabled = new ConfigProperty<>(true);
    @TextFieldSetting
    private final ConfigProperty<String> kircheplustoken = new ConfigProperty<>("");

    @Override
    public ConfigProperty<Boolean> enabled() {
        return enabled;
    }

    public ConfigProperty<uploadTypes> uploadtype() {
        return uploadtype;
    }

    public ConfigProperty<Boolean> owngmailenabled() {
        return owngmailenabled;
    }

    public ConfigProperty<String> kircheplustoken() {
        return kircheplustoken;
    }

}
