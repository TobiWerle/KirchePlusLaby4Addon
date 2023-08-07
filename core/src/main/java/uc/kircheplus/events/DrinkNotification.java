package uc.kircheplus.events;


import net.labymod.api.Laby;
import net.labymod.api.client.chat.Title;
import net.labymod.api.client.component.Component;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.soundhandler.sounds;
import uc.kircheplus.utils.Utils;

public class DrinkNotification {
static boolean cooldown = false;

private static void cooldown() {

    Thread thread = new Thread(() -> {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        cooldown = false;

    });
    thread.start();
}

    @Subscribe
    public void onChatReceived(ChatReceiveEvent e) {
        if (cooldown)
            return;

        String msg = e.chatMessage().getPlainText();

        if (msg.startsWith("Du bist durstig.")) {
            if (KirchePlus.main.configuration().drink.drinkmessageenable().get()) {
                Component drinktitle = Utils.translateAsComponent("kircheplusaddon.drink.title");
                Component drinksubtitle = Utils.translateAsComponent("kircheplusaddon.drink.subtitle");
                Title tile = new Title(drinktitle, drinksubtitle, 1, 3 * 20, 4);
                Laby.labyAPI().minecraft().showTitle(tile);
            }
            if (KirchePlus.main.configuration().drink.drinksoundenable().get()) {
                Laby.references().soundService().play(sounds.drink);
            }
            cooldown = true;
            cooldown();
        }
        if (msg.startsWith("Du verdurstest...")) {
            if (KirchePlus.main.configuration().drink.dryoutmessageenable().get()) {
                Component drinktitle = Utils.translateAsComponent("kircheplusaddon.dryout.title");
                Component drinksubtitle = Utils.translateAsComponent("kircheplusaddon.dryout.subtitle");
                Title tile = new Title(drinktitle, drinksubtitle, 1, 3 * 20, 4);
                Laby.labyAPI().minecraft().showTitle(tile);
            }
            if (KirchePlus.main.configuration().drink.dryoutsoundenable().get()) {
                Laby.references().soundService().play(sounds.dryout);
            }
            cooldown = true;
            cooldown();
        }
    }
}
