package uc.kircheplus.v1_12_2.utils;

import javax.inject.Inject;
import javax.inject.Singleton;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import uc.kircheplus.utils.GUIController;

@Singleton
@Implements(GUIController.class)
public class VersionedGUIController extends GUIController {

    @Inject
    public VersionedGUIController() {

    }

    @Override
    public void clickSlot(int slot) {
        Container container = Minecraft.getMinecraft().player.openContainer;
        Minecraft.getMinecraft().playerController.windowClick(container.windowId, slot, 0,
            ClickType.THROW, Minecraft.getMinecraft().player);
        container.detectAndSendChanges();
        Minecraft.getMinecraft().player.openContainer.detectAndSendChanges();
    }
}
