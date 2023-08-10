package uc.kircheplus.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import org.jetbrains.annotations.NotNull;
import uc.kircheplus.KirchePlus;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class aEvent_Command extends Command {

    public aEvent_Command() {
        super("aEvent", "discordevent", "churchevent");
    }

    @Override
    public boolean execute(String prefix, String[] args) {
        if(!CommandBypass.bypass)return true;
        CommandBypass.bypass = false;
        if(args.length != 2){
            //send help
            //usage : /aEvent <event> <time(00:00)>
            return true;
        }
        String event = args[0];
        String time = args[1];
        if(!isEvent(event)){
            //send notevent
        }
        if(!isTimeValid(time)){
            //send invalidTIme
        }
        System.out.println(sendEventNotifier(event,time));

        return true;
    }

    @Override
    public List<String> complete(String[] arguments) {
        List<String> tabCompletions = new ArrayList<>();
        if (arguments.length == 0) {
            tabCompletions.add("SHG");
            tabCompletions.add("KK");
            tabCompletions.add("JGA");
            tabCompletions.add("Tafel");
            tabCompletions.add("Spendenevent");
            tabCompletions.add("Beichtevent");
        }

        return tabCompletions;
    }

    private boolean isEvent(String s){
        String[] events = {"SHG", "KK", "JGA", "Tafel","Spendenevent","Beichtevent"};

        for(String event : events){
            if(s.equalsIgnoreCase(event)){
                return true;
            }
        }
        return false;
    }
    
    private boolean isTimeValid(String s){
        String[] time = s.split(":");
        if(time.length != 2){
            return false;
        }
        try {
            int hour = Integer.parseInt(time[0]);
            int minute = Integer.parseInt(time[1]);
            if(hour > 24 || hour < 0 || minute > 60 || minute < 0)return false;
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    protected String sendEventNotifier(String event, String time){
        try {
            String url = "http://152.89.107.212:8082/serverapi?";

            url = url + "token=" + KirchePlus.main.configuration().kircheplustoken().get();
            url = url + "&event=" + event;
            url = url + "&member=" + Laby.labyAPI().minecraft().getClientPlayer().getName();
            url = url + "&time=" + time;

            StringBuilder result = new StringBuilder();
            URL URL = new URL(url); //url
            HttpURLConnection conn = (HttpURLConnection) URL.openConnection();
            conn.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    result.append(line);
                }
            }

            return result.toString();
        } catch (Exception ignored) {
            return null;
        }
    }
}
