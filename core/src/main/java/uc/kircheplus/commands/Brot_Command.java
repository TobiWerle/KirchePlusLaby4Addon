package uc.kircheplus.commands;

import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.entity.player.Player;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.events.PrefixHandler;
import uc.kircheplus.utils.Bread_ADD;
import uc.kircheplus.utils.Brot_User;
import uc.kircheplus.utils.TabellenMethoden;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Brot_Command extends Command {

    public Brot_Command() {
        super("brot", "bread", "breadlist", "brotlist", "durchfütterung", "fütterungszeit");
    }

    @Override
    public boolean execute(String prefix, String[] args) {
        if(args.length == 0) {
        displayMessage(" §7Die Brotliste wird synchronisiert! Dies könnte paar Sekunden dauern!");
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    TabellenMethoden.getBrotList();
                } catch (IOException | GeneralSecurityException e1) {}
                KirchePlus.main.displaynameClass.refreshAll();
                displayMessage("§bDie Brotliste wurde synchronisiert!");
            }
        };
        thread.start();
    }

        if(args.length == 1) {
        if(args[0].equalsIgnoreCase("help")) {
            displayMessage("§8 - §b/brot §8-> §7synchronisiere die Brotliste mit dem Client.");
            displayMessage("§8 - §b/brot list §8-> §7Gibt eine Liste mit allen Spielern aus, die Brot bekommen haben.");
            displayMessage("§8 - §b/brot info <User> §8-> Zeigt die Brot-Infos zum Spieler.");
            displayMessage("§8 - §b/brot add <User> §8-> Füge einen Spieler zur Brotliste hinzu.");
        }else
        if(args[0].equalsIgnoreCase("list")) {
            displayMessage("§1[]§a========§7[§6Brotliste§7]§a========§1[]");

            ArrayList<String> online = new ArrayList<String>();

            for(String name : PrefixHandler.BrotUser.keySet()) {
                if(!KirchePlus.main.utils.isOnline(name)) {
                    Brot_User user = PrefixHandler.BrotUser.get(name);
                    String color = " §a";
                    if(!TabellenMethoden.isSameDay(user.getDatum())) color = " §c";
                    displayMessage("§8 - "+name + color + user.getDatum());
                }else online.add(name);
            }
            for(String name : online) {
                Brot_User user = PrefixHandler.BrotUser.get(name);
                String color = " §a";
                if(!TabellenMethoden.isSameDay(user.getDatum())) color = " §c";
                displayMessage(color+ " - "+name + color + user.getDatum());
            }
        }
    }
        if(args.length == 2) {
        if(args[0].equalsIgnoreCase("info")) {
            for(Brot_User users : PrefixHandler.BrotUser.values()) {
                if(args[1].equalsIgnoreCase(users.getEmpfänger())) {
                    displayMessage("§1[]§a========§7[§6Brotliste§7]§a========§1[]");
                    displayMessage("");
                    displayMessage("§3 -§6 Empfänger: §a" +  users.getEmpfänger());
                    displayMessage("§3 -§6 Member: §a" +  users.getMember());
                    String color = "§a";
                    if(!TabellenMethoden.isSameDay(users.getDatum())) color = " §c";
                    displayMessage("§e -§6 Datum: " + color + users.getDatum());
                    displayMessage("");
                    return false;
                }
            }
        }

        if(args[0].equalsIgnoreCase("add")){
            if(args[1].length() > 16){
                displayMessage("§cDer Name darf nicht länger als 16 Zeichen lang sein!");
                return false;
            }
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy");
            LocalDateTime now = LocalDateTime.now();
            Bread_ADD.Member = Laby.labyAPI().minecraft().getClientPlayer().getName();
            Bread_ADD.Date = dateFormatter.format(now);
            Bread_ADD.Who = args[1];
            Bread_ADD.writeBread();
        }
    }
        return false;
    }

}
