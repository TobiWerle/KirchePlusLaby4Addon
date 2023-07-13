package uc.kircheplus.v1_12_2.utils;

import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.utils.Utils;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Timer;
import java.util.TimerTask;

@Singleton
@Implements(Utils.class)
public class VersionedUtils extends Utils {

    @Inject
    public VersionedUtils(){

    }

    @Override
    public void displayMessage(String text) {
        KirchePlus.main.displayMessage(text);
    }
    @Override
    public void displayMessageLater(String text, int seconds) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                KirchePlus.main.displayMessage(text);
            }
        }, seconds * 1000L);
    }
    @Override
    public SSLSocketFactory socketFactory() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory result = sslContext.getSocketFactory();

            return result;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Failed to create a SSL socket factory", e);
        }
    }
    @Override
    public boolean isOnline(String Playername) {
        if(Minecraft.getMinecraft().getConnection().getPlayerInfo(Playername) != null) {
            return true;
        }
        return false;
    }

}
