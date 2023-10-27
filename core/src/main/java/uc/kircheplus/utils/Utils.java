package uc.kircheplus.utils;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.net.ssl.SSLSocketFactory;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.Style;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.reference.annotation.Referenceable;
import net.labymod.api.util.I18n;
import org.jetbrains.annotations.Unmodifiable;

@Referenceable
public abstract class Utils {

    public void displayMessage(String text) {

    }

    public void displayMessageLater(String text, int seconds) {
    }

    public void sendChatMessage(String message){

    }

    public void addMessageToChatHistory(String message){

    }

    public void printChatMessageWithOptionalDeletion(String message){

    }

    public SSLSocketFactory socketFactory() {
        return null;
    }

    public boolean isOnline(String Playername) {
        return false;
    }

    public ArrayList<String> getAllOnlinePlayers(){
        return new ArrayList<>();
    }

    public BufferedImage makeScreen() throws IOException {
        return null;
    }
    public File getGameDir(){
        return null;
    }
    public static String translateAsString(String key, String... args){
        return I18n.translate(key, args);
    }
    public static Component translateAsComponent(String key, Component... components){
        return Component.text(I18n.translate(key, components));
    }

    public static String buildString(String[] strings){
        String resultString = "";
        if(strings.length == 0){
            return "";
        }
        for (int i = 0; i < strings.length; i++) {
            resultString += strings[i]+" ";
        }
        return resultString;
    }

    public static String[] removeStringFromArray(String[] array, String toRemove){
        String[] resultArray = new String[array.length - 1];
        int resultIndex = 0;
        for (String element : array) {
            if (!element.equals(toRemove)) {
                resultArray[resultIndex] = element;
                resultIndex++;
            }
        }
        return resultArray;
    }
    public static String[] addStringToArray(String[] array, String toAdd){
        String[] resultArray = new String[array.length + 1];

        for (int i = 0; i < array.length; i++) {
            resultArray[i] = array[i];
        }
        resultArray[resultArray.length - 1] = toAdd;

        return resultArray;
    }

}
