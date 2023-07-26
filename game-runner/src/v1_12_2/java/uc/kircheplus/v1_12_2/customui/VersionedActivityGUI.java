package uc.kircheplus.v1_12_2.customui;

import javax.inject.Inject;
import javax.inject.Singleton;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import uc.kircheplus.customui.ActivityGUI;

@Singleton
@Implements(ActivityGUI.class)
public class VersionedActivityGUI extends ActivityGUI {

    @Inject
    public VersionedActivityGUI() {

    }

    @Override
    public void init(boolean event, boolean money, boolean bless, boolean marry) {
        customGUI_Activity gui = new customGUI_Activity();
        gui.eventPage = event;
        gui.moneyPage = money;
        gui.blessPage = bless;
        gui.marryPage = marry;
        Minecraft.getMinecraft().displayGuiScreen(gui);
    }


}

