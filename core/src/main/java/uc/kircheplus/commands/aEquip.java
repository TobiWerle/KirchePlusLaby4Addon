package uc.kircheplus.commands;

import java.util.ArrayList;
import java.util.List;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.utils.Utils;

public class aEquip extends Command {

    static int slot = 0;
    static boolean enabled = false;
    static int amount = 0;

    public aEquip() {
        super("aequip");

    }

    @Override
    public boolean execute(String prefix, String[] args) {
        if (args.length < 1) {
            displayMessage(Utils.translateAsString("kircheplusaddon.commands.aequip.usage"));
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("wasser")) {
                slot = 1;
                enabled = true;
                amount = 1;
                Laby.references().chatExecutor().chat("/equip");
                equip();
                return true;
            }
            if (args[0].equalsIgnoreCase("brot")) {
                slot = 0;
                enabled = true;
                amount = 1;
                Laby.references().chatExecutor().chat("/equip");
                equip();
                return true;
            }
        }
        if (args.length == 2) {
            try {
                amount = Integer.parseInt(args[1]);
                if(amount == 0){
                    displayMessage(Utils.translateAsString("kircheplusaddon.commands.aequip.error.number"));
                    return true;
                }
                if (amount <= 5) {
                    if (args[0].equalsIgnoreCase("brot")) {
                        slot = 0;
                        enabled = true;
                        Laby.references().chatExecutor().chat("/equip");
                        equip();
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("wasser")) {
                        slot = 1;
                        enabled = true;
                        Laby.references().chatExecutor().chat("/equip");
                        equip();
                        return true;
                    }
                } else {
                    displayMessage(Utils.translateAsString("kircheplusaddon.commands.aequip.error.number"));
                    displayMessage(Utils.translateAsString("kircheplusaddon.commands.aequip.error.usage"));
                }
            } catch (Exception e) {
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.aequip.error.number"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.aequip.error.usage"));
            }
        }
        return true;
    }

    @Override
    public List<String> complete(String[] args) {
        List<String> tabs = new ArrayList<String>();
        if (args.length == 1) {
            if (args[0].isEmpty()) {
                tabs.add("wasser");
                tabs.add("brot");
                return tabs;
            }
            String water = "wasser";
            String bread = "brot";
            if (bread.toLowerCase().startsWith(args[0].toLowerCase())) {
                tabs.add("brot");
            }
            if (water.toLowerCase().startsWith(args[0].toLowerCase())) {
                tabs.add("wasser");
            }
        }
        return tabs;
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

    private static void equip() {
        if(!enabled)return;
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(100);
                KirchePlus.main.guiController.clickSlot(slot);
                Thread.sleep(750);
                amount--;
                if (amount > 0) {
                    Laby.references().chatExecutor().chat("/equip");
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
