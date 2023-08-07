package uc.kircheplus.commands;

import net.labymod.api.client.chat.command.Command;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class aEquip extends Command {

    public static int slot = 0;
    public static boolean enabled = false;
    public static int amount = 0;

    public aEquip() {
        super("aequip");
    }

    @Override
    public boolean execute(String prefix, String[] args) {
        if(!CommandBypass.bypass)return true;
        CommandBypass.bypass = false;
        if (args.length < 1) {
            KirchePlus.main.displayMessage(Utils.translateAsString("kircheplusaddon.commands.aequip.usage"));
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("wasser")) {
                slot = 1;
                enabled = true;
                amount = 1;
                KirchePlus.main.utils.sendChatMessage("/equip");
                equip();
                return true;
            }else
            if (args[0].equalsIgnoreCase("brot")) {
                slot = 0;
                enabled = true;
                amount = 1;
                KirchePlus.main.utils.sendChatMessage("/equip");
                equip();
                return true;
            }else
            if (args[0].equalsIgnoreCase("suppe")) {
                slot = 2;
                enabled = true;
                amount = 1;
                KirchePlus.main.utils.sendChatMessage("/equip");
                equip();
                return true;
            }else{
                KirchePlus.main.displayMessage(Utils.translateAsString("kircheplusaddon.commands.aequip.error.usage"));
                return true;
            }
        }
        if (args.length == 2) {
            try {
                amount = Integer.parseInt(args[1]);
                if(amount == 0){
                    KirchePlus.main.displayMessage(Utils.translateAsString("kircheplusaddon.commands.aequip.error.number"));
                    return true;
                }
                if (amount <= 5) {
                    if (args[0].equalsIgnoreCase("brot")) {
                        slot = 0;
                        enabled = true;
                        KirchePlus.main.utils.sendChatMessage("/equip");
                        equip();
                        return true;
                    }else
                    if (args[0].equalsIgnoreCase("wasser")) {
                        slot = 1;
                        enabled = true;
                        KirchePlus.main.utils.sendChatMessage("/equip");
                        equip();
                        return true;
                    }else
                    if (args[0].equalsIgnoreCase("suppe")) {
                        slot = 2;
                        enabled = true;
                        KirchePlus.main.utils.sendChatMessage("/equip");
                        equip();
                        return true;
                    }else{
                        KirchePlus.main.displayMessage(Utils.translateAsString("kircheplusaddon.commands.aequip.error.usage"));
                        return true;
                    }
                } else {
                    KirchePlus.main.displayMessage(Utils.translateAsString("kircheplusaddon.commands.aequip.error.number"));
                    KirchePlus.main.displayMessage(Utils.translateAsString("kircheplusaddon.commands.aequip.error.usage"));
                    return true;
                }
            } catch (Exception e) {
                KirchePlus.main.displayMessage(Utils.translateAsString("kircheplusaddon.commands.aequip.error.number"));
                KirchePlus.main.displayMessage(Utils.translateAsString("kircheplusaddon.commands.aequip.error.usage"));
                return true;
            }
        }
        return true;
    }

    @Override
    public List<String> complete(String[] arguments) {
        if (arguments.length == 0) {
            List<String> tabCompletions = new ArrayList<>();
            tabCompletions.add("brot");
            tabCompletions.add("wasser");
            tabCompletions.add("suppe");
            return tabCompletions;
        }
        if(arguments.length == 1){
            List<String> tabCompletions = new ArrayList<>();
            String brot = "brot";
            String wasser = "wasser";
            String suppe = "suppe";
            if(brot.startsWith(arguments[0].toLowerCase())){
                tabCompletions.add("brot");
            }
            if(wasser.startsWith(arguments[0].toLowerCase())){
                tabCompletions.add("wasser");
            }
            if(suppe.startsWith(arguments[0].toLowerCase())){
                tabCompletions.add("suppe");
            }
            return tabCompletions;
        }

        return Collections.emptyList();
    }

    @Subscribe
    public void onChatReceived(ChatReceiveEvent e) {
        if (!enabled) {
            return;
        }
        String msg = e.message().toString();
        if (msg.contains("Du bist nicht am Equip-Punkt deiner Fraktion.")) {
            enabled = false;
        }
    }

    public static void equip() {
        if(!enabled)return;
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(100);
                KirchePlus.main.guiController.clickSlot(slot);
                Thread.sleep(750);
                amount--;
                if (amount > 0) {
                    KirchePlus.main.utils.sendChatMessage("/equip");
                    equip();
                } else {
                    enabled = false;
                }
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }
}
