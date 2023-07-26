package uc.kircheplus.events;

import java.util.ArrayList;
import java.util.HashMap;
import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;
import net.labymod.api.event.client.lifecycle.GameTickEvent;
import net.labymod.api.event.client.render.PlayerNameTagRenderEvent;
import net.labymod.api.event.client.render.RenderEvent;
import net.labymod.api.event.client.render.entity.EntityRenderEvent;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.utils.Brot_User;
import uc.kircheplus.utils.HV_User;
import uc.kircheplus.utils.TabellenMethoden;


public class PrefixHandler {

    public static HashMap<String, HV_User> HVs = new HashMap<>();
    public static HashMap<String, Brot_User> BrotUser = new HashMap<>();

    private ArrayList<String> rendered = new ArrayList<>();

    @Subscribe
    public void onNameTag(EntityRenderEvent e) {
        if (e.entity() instanceof Player) {
            Player player = (Player) e.entity();
            if (!rendered.contains(player.getName())) {
                KirchePlus.main.displayname.addTeam(player.getName());
                rendered.add(player.getName());
            }
        }
    }

    @Subscribe
    public void onGameTick(GameTickEvent e) {
        KirchePlus.main.displayname.checkHide();
    }
}
