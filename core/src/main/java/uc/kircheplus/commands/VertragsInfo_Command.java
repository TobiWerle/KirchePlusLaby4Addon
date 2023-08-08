package uc.kircheplus.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import net.labymod.api.client.chat.command.Command;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.events.tabcompletion;
import uc.kircheplus.utils.FactionContract;
import uc.kircheplus.utils.Utils;

public class VertragsInfo_Command extends Command {

    public VertragsInfo_Command() {
        super("vertragsinfo", "vinfo");
    }

    @Override
    public boolean execute(String prefix, String[] args) {
        if(!CommandBypass.bypass)return true;
        CommandBypass.bypass = false;
        if (args.length < 1) {
            displayMessage(Utils.translateAsString("kircheplusaddon.commands.contractinfo.help.info"));
            displayMessage(Utils.translateAsString("kircheplusaddon.commands.contractinfo.help.faction"));
            displayMessage(Utils.translateAsString("kircheplusaddon.commands.contractinfo.help.main"));
            loadFactionInfoJSON();
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("list")) {

                displayMessage(Utils.translateAsString("kircheplusaddon.commands.contractinfo.title"));
                for (FactionContract factionContract : KirchePlus.main.factionContracts) {
                    String name = factionContract.getFaction();
                    String contract = "§4✖";
                    if (factionContract.isContract()) {
                        contract = "§a✔";
                    }
                    String[] conditions = factionContract.getConditions();

                    displayMessage(" " + name);
                    displayMessage(Utils.translateAsString("kircheplusaddon.commands.contractinfo.info.contract") + contract);
                }
                displayMessage(Utils.translateAsString("kircheplusaddon.commands.contractinfo.info.placeholder"));
                return true;
            }

            for (FactionContract factionContract : KirchePlus.main.factionContracts) {
                String name = factionContract.getFaction();
                if (name.toLowerCase().startsWith(args[0].toLowerCase())) {
                    String contract = "§4✖";
                    if (factionContract.isContract()) {
                        contract = "§a✔";
                    }
                    String[] conditions = factionContract.getConditions();

                    displayMessage(Utils.translateAsString("kircheplusaddon.commands.contractinfo.title"));
                    displayMessage(" " + name);
                    displayMessage(Utils.translateAsString("kircheplusaddon.commands.contractinfo.info.contracts") + " " + contract);
                    if (conditions == null) {
                        displayMessage(Utils.translateAsString("kircheplusaddon.commands.contractinfo.info.noconditions"));
                    }
                    if (conditions != null) {
                        for (String conditionsString : conditions) {
                            displayMessage("§f    " + conditionsString);
                        }
                    }
                    displayMessage("§3 ====================================");
                    return true;
                }
            }

            displayMessage(Utils.translateAsString("kircheplusaddon.commands.contractinfo.info.factionnotexists"));

        }

        return true;
    }

    @Override
    public List<String> complete(String[] arguments) {
        List<String> tabCompletions = new ArrayList<>();
        if (arguments.length == 0) {
            tabCompletions.add("info");
            tabCompletions.add("list");
            for(String faction : getFactions()){
                tabCompletions.add(faction);
            }
            return tabCompletions;
        }

        if(arguments.length == 1){
            if(tabcompletion.spaces > 1) return Collections.emptyList();
            for(String faction : getFactions()){
                if(faction.toLowerCase().startsWith(arguments[0].toLowerCase())){
                    tabCompletions.add(faction);
                }
            }
        }
        return tabCompletions;
    }

    public static void loadFactionInfoJSON() {
        try {
            KirchePlus.main.factionContracts.clear();

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(getJson());
            JsonObject json = element.getAsJsonObject();
            JsonArray faction = json.getAsJsonArray("factions");

            for (int i = 0; i < faction.size(); i++) {
                JsonObject factionjson = (JsonObject) faction.get(i);
                String name = factionjson.get("Name").getAsString();
                boolean contract = factionjson.get("contract").getAsBoolean();
                JsonArray conditionsArray = factionjson.get("conditions").getAsJsonArray();
                String[] conditions = null;

                if (conditionsArray.size() != 0) {
                    conditions = new String[conditionsArray.size()];
                    for (int s = 0; s < conditionsArray.size(); s++) {
                        conditions[s] = conditionsArray.get(s).getAsString();
                    }
                }
                System.out.println(name + contract);
                new FactionContract(name, contract, conditions);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getJson() {
        String jsonUrl = "https://kircheplus-mod.de/api/factioncontract.json";

        try {
            SSLSocketFactory socketFactory = KirchePlus.main.utils.socketFactory();
            URL url = new URL(jsonUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(socketFactory);

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            conn.disconnect();
            String json = response.toString();
            return json;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getFactions() {
        ArrayList<String> list = new ArrayList<>();
        for (FactionContract faction : KirchePlus.main.factionContracts) {
            list.add(faction.getFaction());
        }
        return list;
    }
}
