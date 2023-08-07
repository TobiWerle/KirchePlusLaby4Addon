package uc.kircheplus.commands;

import java.io.IOException;
import java.util.ArrayList;
import net.labymod.api.client.chat.command.Command;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.utils.Activity_User;
import uc.kircheplus.utils.TabellenMethoden;
import uc.kircheplus.utils.Utils;

public class topActivity_Command extends Command {

    public topActivity_Command() {
        super("topactivity");
    }

    @Override
    public boolean execute(String prefix, String[] args) {
        if(!CommandBypass.bypass)return true;
        CommandBypass.bypass = false;
        if (args.length == 0) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        TabellenMethoden.getActivitys();
                    } catch (IOException ignored) {
                    }
                    Activity_User self = Activity_User.getSelf();
                    String arrow = " ➜ ";

                    displayMessage(Utils.translateAsString("kircheplusaddon.commands.topactivity.title"));
                    if (getTotalPlace(true) == null) {
                        displayMessage(Utils.translateAsString("kircheplusaddon.commands.topactivity.noactivitys"));
                        displayMessage(Utils.translateAsString("kircheplusaddon.commands.topactivity.title"));
                        return;
                    }
                    if (getTotalPlace(true) != null) {
                        displayMessage(Utils.translateAsString("kircheplusaddon.commands.topactivity.places.1", getTotalPlace(false)));
                    }
                    if (getTotalPlace(true) != null) {
                        displayMessage(Utils.translateAsString("kircheplusaddon.commands.topactivity.places.2", getTotalPlace(false)));
                    }
                    if (getTotalPlace(true) != null) {
                        displayMessage(Utils.translateAsString("kircheplusaddon.commands.topactivity.places.3", getTotalPlace(false)));
                    }
                    if (getTotalPlace(true) != null) {
                        displayMessage(Utils.translateAsString("kircheplusaddon.commands.topactivity.places.4", getTotalPlace(false)));
                    }
                    if (getTotalPlace(true) != null) {
                        displayMessage(Utils.translateAsString("kircheplusaddon.commands.topactivity.places.5", getTotalPlace(false)));
                    }
                    displayMessage(Utils.translateAsString("kircheplusaddon.commands.topactivity.ownactivity.youractivity"));
                    displayMessage(Utils.translateAsString("kircheplusaddon.commands.topactivity.ownactivity.name", self.getName()));
                    displayMessage(Utils.translateAsString("kircheplusaddon.commands.topactivity.ownactivity.total", self.getTotalActivity()));
                    displayMessage(Utils.translateAsString("kircheplusaddon.commands.topactivity.ownactivity.roleplay", self.getRpActivity()));
                    displayMessage(Utils.translateAsString("kircheplusaddon.commands.topactivity.ownactivity.donations", self.getDonationActivity()));
                    displayMessage(Utils.translateAsString("kircheplusaddon.commands.topactivity.ownactivity.placeholder"));
                }
            });
            thread.start();
        }
        return true;
    }


    public static String getTotalPlace(boolean b) {
        ArrayList<Activity_User> totalActivity = Activity_User.getTotalActivityUsers(0);
        String place = "";
        int size = totalActivity.size();
        for (Activity_User user : totalActivity) {
            if (user.getTotalActivity().equals("0")) {
                return null;
            }
            if (b) {
                return "";
            }
            if (size == 1) {
                place = place + user.getName() + " | " + user.getTotalActivity();
            } else {
                place = place + user.getName() + " / ";
            }
            KirchePlus.main.totalActivity.remove(user);
            size--;
        }
        return place;
    }
}
