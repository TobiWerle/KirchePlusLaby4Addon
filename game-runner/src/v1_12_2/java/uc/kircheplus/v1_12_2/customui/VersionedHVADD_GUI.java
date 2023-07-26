package uc.kircheplus.v1_12_2.customui;

import javax.inject.Singleton;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import uc.kircheplus.customui.HVADD_GUI;

@Singleton
@Implements(HVADD_GUI.class)
public class VersionedHVADD_GUI extends HVADD_GUI {

    @Override
    public void init() {
        customGUI_HV gui = new customGUI_HV();
        Minecraft.getMinecraft().displayGuiScreen(gui);
    }

}
