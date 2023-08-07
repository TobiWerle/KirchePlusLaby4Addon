package uc.kircheplus.commands;

import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatMessageSendEvent;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.utils.Utils;
import java.util.Arrays;

public class CommandBypass {

    public static boolean bypass = false;

    @Subscribe
    public void onSend(ChatMessageSendEvent e){
        if(e.isMessageCommand()){
            for(Command cmd : KirchePlus.main.commands){
                String prefix = e.getMessage().replace("/","").split(" ")[0];
                if(cmd.getPrefix().equalsIgnoreCase(prefix) || Arrays.stream(cmd.getAliases()).anyMatch(name -> name.equalsIgnoreCase(prefix))){
                    e.setCancelled(true);
                    String[] arguments = Arrays.copyOfRange(e.getMessage().split(" "), 1, e.getMessage().split(" ").length);
                    bypass = true;
                    KirchePlus.main.labyAPI().commandService().fireCommand(cmd, prefix, arguments);
                    KirchePlus.main.utils.addMessageToChatHistory("/" + prefix + Utils.buildString(arguments));
                    return;
                }
            }
        }
    }

}