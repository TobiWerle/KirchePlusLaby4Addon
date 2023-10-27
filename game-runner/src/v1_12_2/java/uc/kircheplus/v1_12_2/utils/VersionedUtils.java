package uc.kircheplus.v1_12_2.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.text.TextComponentString;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.utils.Utils;

@Singleton
@Implements(Utils.class)
public class VersionedUtils extends Utils {

    @Inject
    public VersionedUtils() {
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
    public void sendChatMessage(String message){
        Minecraft.getMinecraft().player.sendChatMessage(message);
    }
    @Override
    public void addMessageToChatHistory(String message){
        Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(message);
    }

    @Override
    public void printChatMessageWithOptionalDeletion(String message){
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(message), 1);
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
        if (Minecraft.getMinecraft().getConnection().getPlayerInfo(Playername) != null) {
            return true;
        }
        return false;
    }

    public ArrayList<String> getAllOnlinePlayers(){
        ArrayList<String> playerList = new ArrayList<>();
        Collection<NetworkPlayerInfo> playersC = Minecraft.getMinecraft().getConnection().getPlayerInfoMap();
        playersC.forEach((loadedPlayer) -> {
            String name = loadedPlayer.getGameProfile().getName();
            playerList.add(name);
        });
        return playerList;
    }

    public BufferedImage makeScreen() throws IOException {
        File file1 = new File(Minecraft.getMinecraft().gameDir, "Kirche+");
        file1.mkdir();
        BufferedImage bufferedimage = ScreenShotHelper.createScreenshot(
            Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight,
            Minecraft.getMinecraft().getFramebuffer());
        File file2 = new File(file1, "lastActivity.jpg");
        file2 = file2.getCanonicalFile();
        ImageIO.write(bufferedimage, "jpg", file2);
        return bufferedimage;
    }

    public File getGameDir(){
        return Minecraft.getMinecraft().gameDir;
    }
}
