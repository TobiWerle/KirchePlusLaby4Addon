package uc.kircheplus.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import net.labymod.api.client.chat.command.Command;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.utils.FactionContract;
import uc.kircheplus.utils.Utils;
public class VertragsInfo_Command extends Command {

    public VertragsInfo_Command() {
        super("vertragsinfo", "vinfo");
    }

    @Override
    public boolean execute(String prefix, String[] args) {
        if(args.length < 1){
            displayMessage("§8 - §b/vertraginfo info §8-> §7Zeigt dir an, mit wem die Kirche ein Vertrag hat.");
            displayMessage("§8 - §b/vertraginfo <Fraktion> §8-> §7Zeigt dir genaue Infos zum jeweiligen Vertrag an.");
            displayMessage("§8 - §b/vertraginfo §8-> §7Zeigt dir diese Liste an und aktualisiert die Liste.");
            loadFactionInfoJSON();

            return false;
        }
        if(args.length == 1){
            if(args[0].equalsIgnoreCase("info")){

                Utils.displayMessage("§3 =============Verträge=============");
                for(FactionContract factionContract : KirchePlus.main.FactionContracs){
                    String name = factionContract.getFaction();
                    String contract = "§4✖";
                    if(factionContract.isContract()) contract = "§a✔";
                    String[] conditions = factionContract.getConditions();

                    displayMessage(" "+ name);
                    displayMessage("§5   Vertrag: "+contract);
                }
                displayMessage("§3 ====================================");
                return false;
            }

            for(FactionContract factionContract : KirchePlus.main.FactionContracs){
                String name = factionContract.getFaction();
                if(name.toLowerCase().startsWith(args[0].toLowerCase())){
                    String contract = "§4✖";
                    if(factionContract.isContract()) contract = "§a✔";
                    String[] conditions = factionContract.getConditions();

                    Utils.displayMessage("§3 =============Verträge=============");
                    displayMessage(" "+ name);
                    displayMessage("§5   Vertrag: "+contract);
                    if(conditions == null ) displayMessage("§f   Konditionen: §cKeine");
                    if(conditions != null ) for(String conditionsString : conditions) displayMessage("§f    "+conditionsString);
                    Utils.displayMessage("§3 ====================================");
                    return false;
                }
            }

            displayMessage("§cEs wurde keine Fraktion mit diesem Namen gefunden!");

        }

        return false;
    }

    public static void loadFactionInfoJSON() {
        try {
            KirchePlus.main.FactionContracs.clear();

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(getJson());
            JsonObject json = element.getAsJsonObject();
            JsonArray faction = json.getAsJsonArray("factions");

            for(int i = 0; i < faction.size(); i++){
                JsonObject factionjson = (JsonObject) faction.get(i);
                String name = factionjson.get("Name").getAsString();
                boolean contract = factionjson.get("contract").getAsBoolean();
                JsonArray conditionsArray = factionjson.get("conditions").getAsJsonArray();
                String[] conditions = null;

                if(conditionsArray.size() != 0){
                    conditions = new String[conditionsArray.size()];
                    for (int s = 0; s < conditionsArray.size(); s++) {
                        conditions[s] = conditionsArray.get(s).getAsString();
                    }
                }
                System.out.println(name + contract);
                new FactionContract(name, contract, conditions);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getJson() {
        String jsonUrl = "https://kircheplus-mod.de/api/factioncontract.json"; // Die URL der JSON-Datei hier eintragen

        try {
            SSLSocketFactory socketFactory = Utils.socketFactory();
            URL url = new URL(jsonUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(socketFactory);

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
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
    public ArrayList<String> getFactions(){
        ArrayList<String> list = new ArrayList<>();
        for(FactionContract faction : KirchePlus.main.FactionContracs){
            list.add(faction.getFaction());
        }
        return list;
    }
}
