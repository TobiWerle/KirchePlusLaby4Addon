package uc.kircheplus.utils;


import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.net.ssl.SSLSocketFactory;
import net.labymod.api.client.component.Component;
import net.labymod.api.reference.annotation.Referenceable;
import net.labymod.api.util.I18n;

@Referenceable
public abstract class Utils {

    public void displayMessage(String text) {

    }

    public void displayMessageLater(String text, int seconds) {
    }

    public SSLSocketFactory socketFactory() {
        return null;
    }

    public boolean isOnline(String Playername) {
        return false;
    }

    public BufferedImage makeScreen() throws IOException {
        return null;
    }
    public static String translateAsString(String key, String... args){
        return I18n.translate(key, args);
    }
    public static Component translateAsComponent(String key, Component... components){
        return Component.translatable(key, components);
    }
}
