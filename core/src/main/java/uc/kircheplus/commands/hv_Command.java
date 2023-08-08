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
import org.checkerframework.checker.units.qual.Prefix;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.automaticactivity.Handler;
import uc.kircheplus.events.Displayname;
import uc.kircheplus.events.PrefixHandler;
import uc.kircheplus.events.tabcompletion;
import uc.kircheplus.utils.HV_ADD;
import uc.kircheplus.utils.HV_User;
import uc.kircheplus.utils.PlayerCheck;
import uc.kircheplus.utils.TabellenMethoden;
import uc.kircheplus.utils.Utils;

public class hv_Command extends Command {

    public hv_Command() {
        super("hv");
    }

    @Override
    public boolean execute(String prefix, String[] args) {
        if(!CommandBypass.bypass)return true;
        CommandBypass.bypass = false;
        if (args.length == 0) {
            displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.sync.pending"));
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        TabellenMethoden.getHVList();
                    } catch (IOException | GeneralSecurityException e1) {
                    }
                    KirchePlus.main.displayname.refreshAll();
                    displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.sync.complete"));
                }
            };
            thread.start();
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.help.main"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.help.list"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.help.namecheck"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.help.info"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.help.add"));

            } else if (args[0].equalsIgnoreCase("list")) {
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.title"));
                ArrayList<String> online = new ArrayList<String>();
                for (String name : PrefixHandler.HVs.keySet()) {
                    if (!KirchePlus.main.utils.isOnline(name)) {
                        if (!TabellenMethoden.isDayOver(PrefixHandler.HVs.get(name).getUntilDate())) {
                            displayMessage(" §8- " + name);
                        } else {
                            displayMessage(" §8- §c" + name);
                        }
                    } else {
                        online.add(name);
                    }
                }
                for (String name : online) {
                    if (!TabellenMethoden.isDayOver(PrefixHandler.HVs.get(name).getUntilDate())) {
                        displayMessage(" §a- " + name);
                    } else {
                        displayMessage(" §a-§c " + name);
                    }
                }
                return true;
            } else if (args[0].equalsIgnoreCase("namecheck")) {
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.namecheck.sync"));
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        ArrayList<String> nameError = new ArrayList<>();
                        for (String name : PrefixHandler.HVs.keySet()) {
                            if (!KirchePlus.main.utils.isOnline(name)) {
                                if (!PlayerCheck.checkName(name)) {
                                    nameError.add(name);
                                }
                            }
                        }
                        if (nameError.size() == 0) {
                            displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.namecheck.nochanges"));
                        } else {
                            displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.namecheck.changesfound"));
                        }
                        for (String player : nameError) {
                            displayMessage(" §8-§c " + player);
                        }
                    }
                };
                thread.start();
            } else if (args[0].equalsIgnoreCase("info")) {
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.help.info"));
                return true;
            } else {
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.help.main"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.help.list"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.help.namecheck"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.help.info"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.help.add"));
            }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("info")) {
                for (HV_User users : PrefixHandler.HVs.values()) {
                    if (args[1].equalsIgnoreCase(users.getName())) {
                        displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.title"));
                        displayMessage("");
                        displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.info.who", users.getName()));
                        displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.info.by", users.getFromMember()));
                        displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.info.reason", users.getReason()));
                        displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.info.when", users.getFromDate()));
                        displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.info.until", users.getUntilDate()));

                        String str = "";
                        if (!users.getWeeks().equals("Permanent")) {
                            str =  Utils.translateAsString("kircheplusaddon.commands.hv.info.weeks");
                        }
                        displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.info.duration", users.getWeeks(), str));
                        displayMessage("");
                        return true;
                    }
                }
            }

            if (args[0].equalsIgnoreCase("add")) {
                if (args[1].length() > 16) {
                    displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.add.error"));
                    return true;
                }
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy");
                LocalDateTime now = LocalDateTime.now();
                HV_ADD.temp_fromMember = Laby.labyAPI().getProfile().getUsername();
                HV_ADD.temp_fromDate = dateFormatter.format(now);
                HV_ADD.temp_Who = args[1];
                Handler.openHVGUI = true;
            }
        }

        return true;
    }

    @Override
    public List<String> complete(String[] arguments) {
        List<String> tabCompletions = new ArrayList<>();
        if (arguments.length == 0) {
            tabCompletions.add("list");
            tabCompletions.add("namecheck");
            tabCompletions.add("info");
            tabCompletions.add("add");
            return tabCompletions;
        }

        if(arguments.length == 1){
            if(arguments[0].equalsIgnoreCase("info")) {
                for(String names : PrefixHandler.HVs.keySet()) {
                    tabCompletions.add(names);
                }
                return tabCompletions;
            }

            if(arguments[0].equalsIgnoreCase("add")) {
                for (String playerName : KirchePlus.main.utils.getAllOnlinePlayers()) {
                        tabCompletions.add(playerName);
                }
                return tabCompletions;
            }

            String list = "list";
            String namecheck = "namecheck";
            String info = "info";
            String add = "add";
            if(list.startsWith(arguments[0].toLowerCase())){
                if(tabcompletion.spaces > 1) return Collections.emptyList();
                tabCompletions.add("list");
            }
            if(namecheck.startsWith(arguments[0].toLowerCase())){
                if(tabcompletion.spaces > 1) return Collections.emptyList();
                tabCompletions.add("namecheck");
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
                if(tabcompletion.spaces > 2) return Collections.emptyList();
                String start = arguments[1].toLowerCase();
                for(String names : PrefixHandler.HVs.keySet()) {
                    if(names.toLowerCase().startsWith(start)) {
                        tabCompletions.add(names);
                    }
                }
                return tabCompletions;
            }

            if(arguments[0].equalsIgnoreCase("add")) {
                if(tabcompletion.spaces > 2) return Collections.emptyList();
                for (String playerName : KirchePlus.main.utils.getAllOnlinePlayers()) {
                    if (playerName.toLowerCase().startsWith(arguments[1].toLowerCase())) {
                        tabCompletions.add(playerName);
                    }
                }
                return tabCompletions;
            }
        }
        return Collections.emptyList();
    }

}
