package uc.kircheplus.v1_12_2.automaticactivity;

import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import uc.kircheplus.automaticactivity.ActivityGUI;

import javax.inject.Inject;
import javax.inject.Singleton;
@Singleton
@Implements(ActivityGUI.class)
public class VersionedActivityGUI extends ActivityGUI {

    @Inject
    public VersionedActivityGUI() {

    }

    @Override
    public void init(boolean event, boolean money, boolean bless, boolean marry){
        VersionedGUI gui = new VersionedGUI();
        gui.eventPage = event;
        gui.moneyPage = money;
        gui.blessPage = bless;
        gui.marryPage = marry;
        Minecraft.getMinecraft().displayGuiScreen(gui);
    }


}

