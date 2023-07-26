package uc.kircheplus.v1_12_2.customui;

import javax.inject.Inject;
import javax.inject.Singleton;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import uc.kircheplus.customui.GD_GUI;

@Singleton
@Implements(GD_GUI.class)
public class VersionedGD_GUI extends GD_GUI {

    @Inject
    public VersionedGD_GUI() {

    }

    @Override
    public void init() {
        customGUI_GD gui = new customGUI_GD();
        Minecraft.getMinecraft().displayGuiScreen(gui);
    }

}
