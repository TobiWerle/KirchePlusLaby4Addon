package uc.kircheplus.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import org.jetbrains.annotations.NotNull;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.utils.Utils;
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
            KirchePlus.main.displayMessage(Utils.translateAsString("kircheplusaddon.commands.aevent.usage"));
            return true;
        }
        String event = args[0];
        String time = args[1];
        if(!isEvent(event)){
            KirchePlus.main.displayMessage(Utils.translateAsString("kircheplusaddon.commands.aevent.error.nonevent"));
            return true;
        }
        if(!isTimeValid(time)){
            KirchePlus.main.displayMessage(Utils.translateAsString("kircheplusaddon.commands.aevent.error.invalidtime"));
            return true;
        }


        String token = KirchePlus.main.configuration().kircheplustoken().get();
        String member = Laby.labyAPI().minecraft().getClientPlayer().getName();


        Thread thread = new Thread(() -> {
            String response = sendEventNotifier(event, time, token, member);
            if(response.equals("complete")){
                KirchePlus.main.displayMessage(Utils.translateAsString("kircheplusaddon.commands.aevent.sended", event));
            }else if(response.equals("auth error")){
                KirchePlus.main.displayMessage(Utils.translateAsString("kircheplusaddon.commands.aevent.apiresponse.auth"));
            }else if(response.equals("event error")){
                KirchePlus.main.displayMessage(Utils.translateAsString("kircheplusaddon.commands.aevent.apiresponse.event"));
            }else if(response.equals("Error")){
                KirchePlus.main.displayMessage(Utils.translateAsString("kircheplusaddon.commands.aevent.apiresponse.error"));
            }else if(response.equals("cooldown")){
                KirchePlus.main.displayMessage(Utils.translateAsString("kircheplusaddon.commands.aevent.apiresponse.cooldown"));
            }else if(response.equals("not a member")){
                KirchePlus.main.displayMessage(Utils.translateAsString("kircheplusaddon.commands.aevent.apiresponse.notamember"));
            }
        });
        thread.start();
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
        if(time.length != 2 && time[0].length() != 2 && time[1].length() != 2){
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

    protected String sendEventNotifier(String event, String time, String token, String member){
        try {
            String url = String.format("http://152.89.107.212:8082/serverapi?token=%s&event=%s&member=%s&time=%s",
                token,
                event,
                member,
                time);

            System.out.println(url);
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


    public Component setCommand(Component message, String activityCommand) {
        Component activityMessage = message.copy();
        activityMessage.clickEvent(ClickEvent.suggestCommand(""));//hier den fertigen Befehl einfügen!
            // Wenn er Chat nicht öffnet, einfach anders überlegen. Beispiel: ClickChat Event oder so

        String hovertext = Utils.translateAsString("kircheplusaddon.activity.chat.hovertext");//Andere Nachricht!
        activityMessage.hoverEvent(HoverEvent.showText(Component.text(hovertext)));
        activityMessage.append(Component.text("§9 {⬆}"));
        return activityMessage;
    }
}
