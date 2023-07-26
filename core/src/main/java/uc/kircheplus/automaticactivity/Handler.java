package uc.kircheplus.automaticactivity;


import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.event.client.lifecycle.GameTickEvent;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.automaticactivity.Imgur.Imgur;
import uc.kircheplus.automaticactivity.KirchePlusIMG.KirchePlusIMG_API;
import uc.kircheplus.config.uploadTypes;
import uc.kircheplus.utils.Utils;

public class Handler {

    public static String screenshotLink = "";
    public static String topic = "";
    public static int amount = 0;
    public static boolean isDonation = false;
    static boolean churchdonation = false;
    public static SheetHandler.activityTypes activityType;

    static String[] ranks = {
        "Theologe", "Theologin",
        "Diakon", "Diakonin",
        "Priester", "Priesterin",
        "Dekan", "Dekanin",
        "Bischof", "Bischöfin",
        "Kardinal", "Kardinälin",
        "Papst", "Päpstin"
    };


    @Subscribe
    public void onChatReceiveEvent(ChatReceiveEvent e) {
        if (!KirchePlus.main.configuration().owngmailenabled().get()) {
            return;
        }

        Component message = e.message();
        String unformattedText = e.chatMessage().getPlainText();
        String[] arr = unformattedText.replace(":", "").split(" ");

        for (String rank : ranks) {
            if (arr[0].equals(rank)) {
                if (arr[1].equals(Laby.labyAPI().getProfile().getUsername())) {
                    if (arr[2].equals(".")
                        || unformattedText.contains("Begrüßung:") && unformattedText.contains(
                        "Texte:") && unformattedText.contains("GBK:")) {
                        Component newMessage = createActivityMessage(message,
                            "/saveactivity event");
                        e.setMessage(newMessage);
                        return;
                    }
                } else {
                    if (unformattedText.contains("Begrüßung:") && unformattedText.contains("Texte:")
                        && unformattedText.contains("GBK:")) {
                        Component newMessage = createActivityMessage(message,
                            "/saveactivity event");
                        e.setMessage(newMessage);
                        return;
                    }
                }
            }
        }

        if (unformattedText.contains("[F-Bank]") && unformattedText.contains("in die Fraktionsbank")
            && unformattedText.contains(Laby.labyAPI().getProfile().getUsername())) {
            Pattern pattern = Pattern.compile("\\w+\\$");
            Matcher matcher = pattern.matcher(unformattedText);
            while (matcher.find()) {
                amount = Integer.parseInt(matcher.group().replace("$", ""));
            }
            Component newMessage = createActivityMessage(message, "/saveactivity money");
            e.setMessage(newMessage);
            return;
        }

        if (unformattedText.startsWith("[Ablass]") && unformattedText.contains(
            "hat einen Ablassbrief gekauft.")) {
            Component newMessage = createActivityMessage(message, "/saveactivity ablass");
            e.setMessage(newMessage);
            topic = unformattedText.split(" ")[1];
            return;
        }
        if (unformattedText.startsWith("[Segen]") && unformattedText.contains("Du hast")) {
            Component newMessage = createActivityMessage(message, "/saveactivity segen");
            e.setMessage(newMessage);
            topic = unformattedText.split(" ")[3];
            return;
        }
        if (unformattedText.startsWith("News") && unformattedText.contains(
            "sind nun verheiratet!")) {
            Component newMessage = createActivityMessage(message, "/saveactivity marry");
            e.setMessage(newMessage);
            topic = arr[1] + " & " + arr[3];
            return;
        }
        if (unformattedText.startsWith("Danke") && unformattedText.contains("die Spende!")) {
            churchdonation = true;
            return;
        }
        if (churchdonation) {
            if (unformattedText.contains("$") && unformattedText.contains("-")) {
                int donation = Integer.parseInt(
                    unformattedText.replace("  -", "").replace("$", ""));
                amount = donation;
                churchdonation = false;
                Component newMessage = createActivityMessage(message, "/saveactivity money");
                e.setMessage(newMessage);
                return;
            }
        }

    }

    public Component createActivityMessage(Component message, String activityCommand) {
        Component activityMessage = message.copy();
        activityMessage.clickEvent(ClickEvent.runCommand(activityCommand));
        String hovertext = Utils.translateAsString("kircheplusaddon.activity.chat.hovertext");
        activityMessage.hoverEvent(HoverEvent.showText(Component.text(hovertext)));
        activityMessage.append(Component.text("§9 {⬆}"));
        return activityMessage;
    }

    public static String screenshot(BufferedImage image) throws IOException {
        File file = new File(System.getenv("APPDATA") + "/.minecraft/Kirche+/lastActivity.jpg");
        ImageIO.write(addTextWatermark(image), "jpg", file);

        if (KirchePlus.main.configuration().uploadtype().get() == uploadTypes.KIRCHEPLUSIMG) {
            return KirchePlusIMG_API.uploadIMG(file);
        }

        return Imgur.uploadToLink(file);
    }


    static BufferedImage addTextWatermark(BufferedImage image) {
        DateTimeFormatter date = DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm");
        LocalDateTime now = LocalDateTime.now();
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
        g2d.setComposite(alphaChannel);
        g2d.setColor(Color.MAGENTA);
        g2d.setFont(new Font("Arial", Font.BOLD, 32));
        FontMetrics fontMetrics = g2d.getFontMetrics();
        Rectangle2D rect = fontMetrics.getStringBounds(date.format(now), g2d);

        int centerX = (image.getWidth() - (int) rect.getWidth()) / 2;
        int centerY = image.getHeight() / 2;

        g2d.drawString(date.format(now), centerX, centerY - 100);
        g2d.drawString(Laby.labyAPI().minecraft().getClientPlayer().getName(), centerX,
            centerY - 50);
        if (isDonation) {
            g2d.drawString(+amount + "$", centerX, centerY);
        }
        g2d.dispose();
        return image;
        //thanks to codejava.net
    }

    public static boolean openGUI = false;
    public static boolean eventPage = false;
    public static boolean moneyPage = false;
    public static boolean blessPage = false;
    public static boolean marryPage = false;
    static int ticks = 3;
    public static boolean openHVGUI = false;
    public static boolean GDGUI = false;

    @Subscribe
    public void onTick(GameTickEvent e) {
        if (openGUI) {
            openGUI = false;
            KirchePlus.main.activityGUI.init(eventPage, moneyPage, blessPage, marryPage);
            eventPage = false;
            moneyPage = false;
            blessPage = false;
            marryPage = false;
            isDonation = false;
        }

        if (openHVGUI) {
            KirchePlus.main.hv_gui.init();
            openHVGUI = false;
        }
        if (GDGUI) {
            KirchePlus.main.gd_gui.init();
            GDGUI = false;
        }
    }
}