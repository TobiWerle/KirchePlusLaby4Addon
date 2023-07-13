package uc.kircheplus.utils;


import net.labymod.api.reference.annotation.Referenceable;
import uc.kircheplus.KirchePlus;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Timer;
import java.util.TimerTask;

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
}
