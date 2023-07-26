package uc.kircheplus.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.net.ssl.HttpsURLConnection;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.world.WorldEnterEvent;
import uc.kircheplus.KirchePlus;

public class UpdateCheck {

    private static boolean sended = false;
    private static boolean updateStatus = true;

    @Subscribe
    public void onWorldJoin(WorldEnterEvent e) {
        if (!sended && !updateStatus) {
            sended = true;
            KirchePlus.main.utils.displayMessageLater(Utils.translateAsString("kircheplusaddon.update"), 5);
        }
    }

    public static void updateCheck() {
        try {
            if (!KirchePlus.main.VERSION.equals(getVersion())) {
                updateStatus = false;
            }
        } catch (Exception ignored) {
        }
    }

    private static String getVersion() throws IOException {
        URL url = new URL("https://kircheplus-mod.de/api/version.txt");
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setSSLSocketFactory(KirchePlus.main.utils.socketFactory());

        BufferedReader bufferedReader = new BufferedReader(
            new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String input;
        StringBuilder builder = new StringBuilder();
        while ((input = bufferedReader.readLine()) != null) {
            builder.append(input);
        }
        bufferedReader.close();
        return builder.toString();
    }

}
