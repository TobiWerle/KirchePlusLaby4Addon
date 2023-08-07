package uc.kircheplus.commands;

import java.awt.image.BufferedImage;
import java.io.IOException;
import net.labymod.api.client.chat.command.Command;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.automaticactivity.Handler;
import uc.kircheplus.automaticactivity.KirchePlusIMG.KirchePlusIMG_API;
import uc.kircheplus.automaticactivity.SheetHandler;
import uc.kircheplus.config.uploadTypes;
import uc.kircheplus.utils.Utils;

public class SaveActivity_Command extends Command {

    public static BufferedImage image = null;

    public SaveActivity_Command() {
        super("saveactivity");
    }

    @Override
    public boolean execute(String prefix, String[] args) {
        if(!CommandBypass.bypass)return true;
        CommandBypass.bypass = false;
        if (!KirchePlus.main.configuration().owngmailenabled().get()) {
            displayMessage(Utils.translateAsString("kircheplusaddon.commands.saveactivty.error.owngmail"));
            return true;
        }
        if (KirchePlus.main.configuration().uploadtype().get() == uploadTypes.KIRCHEPLUSIMG) {
            if (KirchePlus.main.configuration().kircheplustoken().get().equals("")) {
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.saveactivty.error.needtoken"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.saveactivty.error.createtoken"));
                return true;
            }
            if (!KirchePlusIMG_API.checkConnection()) {
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.saveactivty.error.serverdown"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.saveactivty.error.fix"));
                return true;
            }
            if (!KirchePlusIMG_API.isTokenValid()) {
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.saveactivty.error.tokeninvalid"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.saveactivty.error.tokenfix"));
                return true;
            }
        }

        if (args.length != 1) {
            return true;
        }
        Handler handler = new Handler();
        try {
            image = KirchePlus.main.utils.makeScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    if (SheetHandler.MemberSheet == null) {
                        SheetHandler.getMemberOwnSheet();
                    }
                    if (args[0].equalsIgnoreCase("ablass")) {
                        displayMessage(Utils.translateAsString("kircheplusaddon.commands.saveactivty.screenupload"));
                        SheetHandler.saveActivity(SheetHandler.activityTypes.ABLASSBRIEF);
                        return;
                    }

                    Thread.sleep(50);
                } catch (IOException e) {
                    displayMessage(Utils.translateAsString("kircheplusaddon.commands.saveactivty.unexpected"));
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
        if (args[0].equalsIgnoreCase("ablass")) {
            return true;
        }
        if (args[0].equalsIgnoreCase("event")) {
            Handler.eventPage = true;
        }
        if (args[0].equalsIgnoreCase("money")) {
            Handler.moneyPage = true;
        }
        if (args[0].equalsIgnoreCase("segen")) {
            Handler.blessPage = true;
        }
        if (args[0].equalsIgnoreCase("marry")) {
            Handler.topic = args[1] + "&" + args[2];
            Handler.marryPage = true;
        }
        Handler.openGUI = true;
        return true;
    }
}
