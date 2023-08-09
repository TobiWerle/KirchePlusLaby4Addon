package uc.kircheplus.commands;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.events.PrefixHandler;
import uc.kircheplus.events.tabcompletion;
import uc.kircheplus.utils.Bread_ADD;
import uc.kircheplus.utils.Brot_User;
import uc.kircheplus.utils.TabellenMethoden;
import uc.kircheplus.utils.Utils;

public class Brot_Command extends Command {

    public Brot_Command() {
        super("brot", "bread", "breadlist", "brotlist", "durchfütterung", "fütterungszeit");
    }

    @Override
    public boolean execute(String prefix, String[] args) {
        if(!CommandBypass.bypass)return true;
        CommandBypass.bypass = false;
        if (args.length == 0) {
            displayMessage(Utils.translateAsString("kircheplusaddon.commands.brot.sync.pending"));
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        TabellenMethoden.getBrotList();
                    } catch (IOException | GeneralSecurityException e1) {
                    }
                    displayMessage(Utils.translateAsString("kircheplusaddon.commands.brot.sync.complete"));
                }
            };
            thread.start();
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.brot.help.main"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.brot.help.list"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.brot.help.info"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.brot.help.add"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.brot.help.help"));

            } else if (args[0].equalsIgnoreCase("list")) {
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.brot.title"));

                ArrayList<String> online = new ArrayList<String>();

                for (String name : PrefixHandler.BrotUser.keySet()) {
                    if (!KirchePlus.main.utils.isOnline(name)) {
                        Brot_User user = PrefixHandler.BrotUser.get(name);
                        String color = " §a";
                        if (!TabellenMethoden.isSameDay(user.getDatum())) {
                            color = " §c";
                        }
                        displayMessage("§8 - " + name + color + user.getDatum());
                    } else {
                        online.add(name);
                    }
                }
                for (String name : online) {
                    Brot_User user = PrefixHandler.BrotUser.get(name);
                    String color = " §a";
                    if (!TabellenMethoden.isSameDay(user.getDatum())) {
                        color = " §c";
                    }
                    displayMessage(color + " - " + name + color + user.getDatum());
                }
            }
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("info")) {
                for (Brot_User users : PrefixHandler.BrotUser.values()) {
                    if (args[1].equalsIgnoreCase(users.getEmpfänger())) {
                        displayMessage(Utils.translateAsString("kircheplusaddon.commands.brot.title"));
                        displayMessage("");
                        displayMessage(Utils.translateAsString("kircheplusaddon.commands.brot.info.receiver", users.getEmpfänger()));
                        displayMessage(Utils.translateAsString("kircheplusaddon.commands.brot.info.member", users.getMember()));
                        String color = "§a";
                        if (!TabellenMethoden.isSameDay(users.getDatum())) {
                            color = " §c";
                        }
                        displayMessage(Utils.translateAsString("kircheplusaddon.commands.brot.info.date") + " "+ color + users.getDatum());
                        displayMessage("");
                        return true;
                    }
                }
            }

            if (args[0].equalsIgnoreCase("add")) {
                if (args[1].length() > 16) {
                    displayMessage(Utils.translateAsString("kircheplusaddon.commands.brot.add.error.namelenght"));
                    return true;
                }
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy");
                LocalDateTime now = LocalDateTime.now();
                Bread_ADD.Member = Laby.labyAPI().minecraft().getClientPlayer().getName();
                Bread_ADD.Date = dateFormatter.format(now);
                Bread_ADD.Who = args[1];
                Bread_ADD.writeBread();
            }
        }
        return true;
    }

    @Override
    public List<String> complete(String[] arguments) {
        List<String> tabCompletions = new ArrayList<>();
        if (arguments.length == 0) {
            tabCompletions.add("help");
            tabCompletions.add("info");
            tabCompletions.add("list");
            tabCompletions.add("add");
            return tabCompletions;
        }

        if(arguments.length == 1){
            if(arguments[0].equalsIgnoreCase("info")) {
                if(tabcompletion.spaces != 2) return Collections.emptyList();
                for(String names : PrefixHandler.BrotUser.keySet()) {
                    tabCompletions.add(names);
                }
                return tabCompletions;
            }

            if(arguments[0].equalsIgnoreCase("add")) {
                if(tabcompletion.spaces != 2) return Collections.emptyList();
                for (String playerName : KirchePlus.main.utils.getAllOnlinePlayers()) {
                    tabCompletions.add(playerName);
                }
                return tabCompletions;
            }

            String help = "help";
            String list = "list";
            String info = "info";
            String add = "add";
            if(help.startsWith(arguments[0].toLowerCase())){
                if(tabcompletion.spaces > 1) return Collections.emptyList();
                tabCompletions.add("help");
            }
            if(list.startsWith(arguments[0].toLowerCase())){
                if(tabcompletion.spaces > 1) return Collections.emptyList();
                tabCompletions.add("list");
            }
            if(info.startsWith(arguments[0].toLowerCase())){
                tabCompletions.add("info");
            }
            if(add.startsWith(arguments[0].toLowerCase())){
                tabCompletions.add("add");
            }
            return tabCompletions;

        }

        if(arguments.length == 2){
            if(arguments[0].equalsIgnoreCase("info")) {
                if(tabcompletion.spaces != 2) return Collections.emptyList();
                String start = arguments[1].toLowerCase();
                for(String names : PrefixHandler.BrotUser.keySet()) {
                    if(names.toLowerCase().startsWith(start)) {
                        tabCompletions.add(names);
                    }
                }
                return tabCompletions;
            }

            if(arguments[0].equalsIgnoreCase("add")) {
                if(tabcompletion.spaces != 2) return Collections.emptyList();
                for (String playerName : KirchePlus.main.utils.getAllOnlinePlayers()) {
                    if (playerName.toLowerCase().startsWith(arguments[1].toLowerCase())) {
                        tabCompletions.add(playerName);
                    }
                }
                return tabCompletions;
            }
        }
        return tabCompletions;
    }

}
