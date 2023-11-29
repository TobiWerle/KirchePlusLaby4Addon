package uc.kircheplus.utils;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import javax.net.ssl.SSLSocketFactory;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.reference.annotation.Referenceable;
import net.labymod.api.util.I18n;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.commands.aEquip;

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

    public void sendCommandUsage(){
        for(Command command : KirchePlus.main.commands) {
            if (command.getPrefix().equals("aequip")) {
                displayMessage("§3 ============="+ command.getPrefix() +"=============");
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.aequip.help.1"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.aequip.help.2"));
            }else if (command.getPrefix().equals("aevent")) {
                displayMessage("§3 ============="+ command.getPrefix() +"=============");
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.aevent.usage"));
            }else if (command.getPrefix().equals("brot")) {
                displayMessage("§3 ============="+ command.getPrefix() +"=============");
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.brot.help.main"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.brot.help.list"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.brot.help.info"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.brot.help.add"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.brot.help.help"));
            }else if (command.getPrefix().equals("checkdonation")) {
                displayMessage("§3 ============="+ command.getPrefix() +"=============");
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.checkdonation.help.1"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.checkdonation.help.2"));
            }else if (command.getPrefix().equals("gdeinteilung")) {
                displayMessage("§3 ============="+ command.getPrefix() +"=============");
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.gdeinteilung.help"));
            }else if (command.getPrefix().equals("hv")) {
                displayMessage("§3 ============="+ command.getPrefix() +"=============");
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.help.main"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.help.list"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.help.namecheck"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.help.info"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.hv.help.add"));
            }else if (command.getPrefix().equals("topactivity")) {
                displayMessage("§3 ============="+ command.getPrefix() +"=============");
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.topactivity.help"));
            }else if (command.getPrefix().equals("vertragsinfo")) {
                displayMessage("§3 ============="+ command.getPrefix() +"=============");
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.contractinfo.help.info"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.contractinfo.help.faction"));
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.contractinfo.help.main"));
            }else if (command.getPrefix().equals("kircheplus")) {

            }else if (command.getPrefix().equals("saveactivity")) {

            }else{
                displayMessage("Es wurde keine Hilfeseite für den Befehl " + command.getPrefix() + " gefunden!");
            }
        }
    }

}
