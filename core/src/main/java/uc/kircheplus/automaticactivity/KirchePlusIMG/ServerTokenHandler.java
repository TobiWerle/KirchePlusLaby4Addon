package uc.kircheplus.automaticactivity.KirchePlusIMG;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import uc.kircheplus.KirchePlus;

public class ServerTokenHandler {

    @Subscribe
    public void onChat(ChatReceiveEvent e) {
        String msg = e.chatMessage().getPlainText();
        if (msg.startsWith("Token:")) {
            String token = msg.replace("Token: ", "");
            KirchePlus.main.configuration().kircheplustoken().set(token);
            KirchePlus.main.saveConfiguration();
            e.setCancelled(true);
        }
    }
}
