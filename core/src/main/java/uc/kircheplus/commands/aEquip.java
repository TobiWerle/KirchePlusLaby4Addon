package uc.kircheplus.commands;

import net.labymod.api.client.chat.command.Command;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.events.PrefixHandler;
import uc.kircheplus.events.tabcompletion;
import uc.kircheplus.utils.FactionContract;
import uc.kircheplus.utils.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class aEquip extends Command {

    public int slot = 0;
    public boolean enabled = false;
    public int amount = 0;

    public boolean factionEnabled = false;
    public int factionAmountBread = 0;
    public int factionAmountWater = 0;

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
            } else{
                KirchePlus.main.displayMessage(Utils.translateAsString("kircheplusaddon.commands.aequip.error.usage"));
                return true;
            }
        }
        if (args.length == 2) {
            try {
                if(args[0].equalsIgnoreCase("give")){
                    if(VertragsInfo_Command.getFactions().stream().anyMatch(name -> name.equalsIgnoreCase(args[1]))){
                        int[] factionConditions = getFactionCondition(args[1]);
                        factionAmountBread = factionConditions[0] / 16;
                        factionAmountWater = factionConditions[1];
                        System.out.println(factionAmountBread);
                        System.out.println(factionAmountWater);
                        enabled = true;
                        factionEnabled = true;
                        KirchePlus.main.utils.sendChatMessage("/equip");
                        equip();
                        return true;
                    }else{
                        System.out.println("Nix Fraktion gefunden....");
                        //TODO ERROR MESSAGE : NO FACTION FOUND
                    }
                    return true;
                }
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
        List<String> tabCompletions = new ArrayList<>();
        if (arguments.length == 0) {
            tabCompletions.add("brot");
            tabCompletions.add("wasser");
            tabCompletions.add("suppe");
            tabCompletions.add("give");
            return tabCompletions;
        }
        if(arguments.length == 1){

            if(arguments[0].equalsIgnoreCase("give")) {
                if(tabcompletion.spaces != 2) return Collections.emptyList();
                for(FactionContract contract : KirchePlus.main.factionContracts) {
                    tabCompletions.add(contract.getFaction());
                }
                return tabCompletions;
            }

            if(tabcompletion.spaces != 1) return Collections.emptyList();
            String brot = "brot";
            String wasser = "wasser";
            String suppe = "suppe";
            String give = "give";
            if(brot.startsWith(arguments[0].toLowerCase())){
                tabCompletions.add("brot");
            }
            if(wasser.startsWith(arguments[0].toLowerCase())){
                tabCompletions.add("wasser");
            }
            if(suppe.startsWith(arguments[0].toLowerCase())){
                tabCompletions.add("suppe");
            }
            if(give.startsWith(arguments[0].toLowerCase())){
                tabCompletions.add("give");
            }
            return tabCompletions;
        }
        if(arguments.length == 2) {
            if (arguments[0].equalsIgnoreCase("give")) {
                if (tabcompletion.spaces != 2) return Collections.emptyList();
                String start = arguments[1].toLowerCase();
                for (FactionContract contracts : KirchePlus.main.factionContracts) {
                    if (contracts.getFaction().toLowerCase().startsWith(start)) {
                        tabCompletions.add(contracts.getFaction());
                    }
                }
                return tabCompletions;
            }
        }
        //TODO ADD FACTION TO /aEquip give Command

        return Collections.emptyList();
    }

    @Subscribe
    public void onChatReceived(ChatReceiveEvent e) {
        if (!enabled) {
            return;
        }
        String msg = e.chatMessage().getPlainText();
        if (msg.contains("Du bist nicht am Equip-Punkt deiner Fraktion.")) {
            enabled = false;
        }
    }

    public void equip() {
        if(!enabled)return;
        Thread thread = new Thread(() -> {
            try {
                if(factionEnabled){
                    if(factionAmountBread != 0){
                        Thread.sleep(100);
                        KirchePlus.main.guiController.clickSlot(0);
                        System.out.println("Nehme Brot");
                        factionAmountBread--;
                        Thread.sleep(750);
                        KirchePlus.main.utils.sendChatMessage("/equip");
                        equip();
                        return;
                    }
                    if(factionAmountWater != 0){
                        Thread.sleep(100);
                        KirchePlus.main.guiController.clickSlot(1);
                        System.out.println("Nehme Wasser");
                        factionAmountWater--;
                        Thread.sleep(750);
                        if(factionAmountWater == 0){
                            enabled = false;
                            factionEnabled = false;
                            return;
                        }
                        KirchePlus.main.utils.sendChatMessage("/equip");
                        equip();
                        return;
                    }
                    enabled = false;
                    factionEnabled = false;
                    return; //TODO TESTEN!!!!
                }


                Thread.sleep(100);
                KirchePlus.main.guiController.clickSlot(slot);
                Thread.sleep(750);
                amount--;
                if (amount > 0) {
                    if(!enabled)return;
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

    public static int[] getFactionCondition(String faction){
        int bread = 0;
        int water = 0;
        for(FactionContract contract : KirchePlus.main.factionContracts){
            if(contract.getFaction().equalsIgnoreCase(faction)){
               bread = Integer.parseInt(contract.getConditions()[0].replaceAll("\\D", ""));
               water = Integer.parseInt(contract.getConditions()[1].replaceAll("\\D", ""));
            }
        }
        int[] conditions = {bread, water};
        return conditions;
    }
}
