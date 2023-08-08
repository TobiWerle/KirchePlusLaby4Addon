package uc.kircheplus.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.labymod.api.client.chat.command.Command;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.events.tabcompletion;
import uc.kircheplus.utils.PlayerCheck;
import uc.kircheplus.utils.SpenderInfo;
import uc.kircheplus.utils.SpenderUtils;
import uc.kircheplus.utils.TabellenMethoden;
import uc.kircheplus.utils.Utils;
import uc.kircheplus.utils.publicDonators;

public class checkDonation_Command extends Command {

    boolean inTask = false;

    public checkDonation_Command() {
        super("checkdonation");
    }

    @Override
    public boolean execute(String prefix, String[] args) {
        if(!CommandBypass.bypass)return true;
        CommandBypass.bypass = false;
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("updatenames")) {
                if (!KirchePlus.main.configuration().owngmailenabled().get()) {
                    displayMessage(Utils.translateAsString("kircheplusaddon.commands.checkdonation.updatenames.owngmailerror"));
                    return true;
                }
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.checkdonation.updatenames.sync.pending"));
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            TabellenMethoden.getDonations();
                            Map<String, String> wrongNames = new HashMap<>();
                            for (publicDonators info : SpenderUtils.publicDonations) {
                                String currentName = PlayerCheck.NameFromUUID(info.getUUID());
                                if (!info.getName().equals(currentName)) {
                                    wrongNames.put(currentName, info.getName());
                                    TabellenMethoden.updateName(currentName, info.getUUID());
                                }
                            }

                            for (String s : wrongNames.keySet()) {
                                displayMessage(Utils.translateAsString("kircheplusaddon.commands.checkdonation.updatenames.sync.replaced", wrongNames.get(s), s));
                            }
                            if (wrongNames.isEmpty()) {
                                displayMessage(Utils.translateAsString("kircheplusaddon.commands.checkdonation.updatenames.sync.nochanges"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        }
        if (args.length == 0) {
            if (inTask) {
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.checkdonation.intask"));
                return true;
            }
            inTask = true;
            displayMessage(Utils.translateAsString("kircheplusaddon.commands.checkdonation.sync"));
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        TabellenMethoden.memberSheets.clear();
                        TabellenMethoden.getAllMemberSheets();
                        TabellenMethoden.getDonations();
                        TabellenMethoden.checkDonations();
                        HashMap<String, Integer> inPublic = new HashMap<>();
                        HashMap<String, Integer> notPublic = new HashMap<>();

                        for (SpenderInfo spender : KirchePlus.main.spender) {
                            if (isInPublic(spender.getName())) {
                                inPublic.put(spender.getName(), spender.getAmount() + SpenderUtils.getAmountByName(spender.getName()));
                                int donation = spender.getAmount() + SpenderUtils.getAmountByName(spender.getName());
                                displayMessage(Utils.translateAsString("kircheplusaddon.commands.checkdonation.inpublic", spender.getName(), String.valueOf(donation), String.valueOf(spender.getAmount())));
                            }
                            if (spender.getAmount() >= 5000) {
                                if (!isInPublic(spender.getName())) {
                                    notPublic.put(spender.getName(), spender.getAmount());
                                    displayMessage(Utils.translateAsString("kircheplusaddon.commands.checkdonation.notpublic", spender.getName(), String.valueOf(spender.getAmount())));
                                }
                            }
                        }
                        if (inPublic.size() == 0 && notPublic.size() == 0) {
                            displayMessage(Utils.translateAsString("kircheplusaddon.commands.checkdonation.nodonation"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    inTask = false;
                }
            };
            thread.start();
        }
        return true;
    }

    private boolean isInPublic(String name) {
        for (publicDonators info : SpenderUtils.publicDonations) {
            if (info.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public List<String> complete(String[] arguments) {
        List<String> tabCompletions = new ArrayList<>();
        if (arguments.length == 0) {
            tabCompletions.add("updatenames");
            return tabCompletions;
        }
        if(arguments.length == 1){
            if(tabcompletion.spaces > 1) return Collections.emptyList();
            String updatenames = "updatenames";
            if(updatenames.startsWith(arguments[0].toLowerCase())){
                tabCompletions.add("updatenames");
            }
        }

        return Collections.emptyList();
    }

}
