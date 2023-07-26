package uc.kircheplus.commands;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.automaticactivity.Handler;
import uc.kircheplus.events.PrefixHandler;
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
}
