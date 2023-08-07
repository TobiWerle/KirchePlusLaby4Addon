package uc.kircheplus.commands;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.events.PrefixHandler;
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

}
