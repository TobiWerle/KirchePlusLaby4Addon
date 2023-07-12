package uc.kircheplus.commands;

import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.event.Event;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.screen.ScreenDisplayEvent;
import net.labymod.api.event.client.gui.screen.ScreenOpenEvent;
import org.jetbrains.annotations.NotNull;
import uc.kircheplus.KirchePlus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class aEquip extends Command {
    static int slot = 0;
    static boolean enabled = false;
    static int amount = 0;
    public aEquip() {
        super("aequip");

    }

    @Override
    public boolean execute(String prefix, String[] args) {
        if(args.length < 1){
            displayMessage("§8 - §b /aequip <Wasser/Brot> {Anzahl} §8 -> §7Equipe dir automatisch Brot oder Wasser");
            return false;
        }
        if(args.length == 1){
            if(args[0].equalsIgnoreCase("wasser")){
                slot = 1;
                enabled = true;
                amount = 1;
                Laby.references().chatExecutor().chat("/equip");
                equip();
                return false;
            }
            if(args[0].equalsIgnoreCase("brot")){
                slot = 0;
                enabled = true;
                amount = 1;
                Laby.references().chatExecutor().chat("/equip");
                equip();
                return false;
            }
        }
        if(args.length == 2){
            try {
                amount = Integer.parseInt(args[1]);
                if(amount <= 5){
                    if(args[0].equalsIgnoreCase("brot")){
                        slot = 0;
                        enabled = true;
                        Laby.references().chatExecutor().chat("/equip");
                        equip();
                        return false;
                    }
                    if(args[0].equalsIgnoreCase("wasser")){
                        slot = 1;
                        enabled = true;
                        Laby.references().chatExecutor().chat("/equip");
                        equip();
                    }
                }else{
                    displayMessage("§cBitte gib eine Zahl an, die nicht über 5 ist.");
                    displayMessage("§8 - §b /aequip <Wasser/Brot> {Anzahl}");
                }
            }catch (Exception e){
                displayMessage("§cBitte gib eine Zahl an, die nicht über 5 ist.");
                displayMessage("§8 - §b /aequip <Wasser/Brot> {Anzahl}");
            }
        }
        return false;
    }

    @Override
    public List<String> complete(String[] args) {
        List<String> tabs = new ArrayList<String>();
        if(args.length == 1) {
            if(args[0].isEmpty()) {
                tabs.add("wasser");
                tabs.add("brot");
                return tabs;
            }
            String water = "wasser";
            String bread = "brot";
            if(bread.toLowerCase().startsWith(args[0].toLowerCase())){
                tabs.add("brot");
            }
            if(water.toLowerCase().startsWith(args[0].toLowerCase())){
                tabs.add("wasser");
            }
        }
        System.out.println("Return normales");
        return tabs;
    }


    /*@Subscribe
    public void onGuiOpenEvent(ScreenDisplayEvent e){
        if(!enabled)return;
        System.out.println("Inventar geöffnet!");
        if(amount != 0){
            equip();
            amount--;
        }
    }*/

    private static void equip(){
        Thread thread = new Thread(() -> {
            try {
                System.out.println("Debug1");
                Thread.sleep(500);
                System.out.println("Debug2");
                KirchePlus.main.guiController.clickSlot(slot);
                System.out.println("Debug3");
                Thread.sleep(700);
                amount--;
                if(amount != 0){
                    Laby.references().chatExecutor().chat("/equip");
                    equip();
                }else {
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
