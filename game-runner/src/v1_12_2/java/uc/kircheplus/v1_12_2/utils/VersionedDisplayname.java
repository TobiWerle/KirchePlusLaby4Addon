package uc.kircheplus.v1_12_2.utils;

import java.util.HashMap;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.labymod.api.Laby;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import org.apache.logging.log4j.core.layout.PatternLayout;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.events.Displayname;
import uc.kircheplus.events.PrefixHandler;
import uc.kircheplus.utils.TabellenMethoden;

@Singleton
@Implements(Displayname.class)
public class VersionedDisplayname extends Displayname {

    @Inject
    public VersionedDisplayname() {

    }

    public static HashMap<String, ScorePlayerTeam> teams = new HashMap<>();

    static int time = 20 * 30;
    static int reloadTime = 60 * 20 * 2 * 5;
    static boolean hide = false;

    @Override
    public void addTeam(String playername) {
        EntityPlayer p = Minecraft.getMinecraft().world.getPlayerEntityByName(playername);
        if (Minecraft.getMinecraft().gameSettings.hideGUI) {
            return;
        }
        try {
            String teamName = p.getName();
            //System.out.println(teamName + " wird registriert!");
            if(Minecraft.getMinecraft().player.getWorldScoreboard().getObjectiveInDisplaySlot(1) == null) {
                boolean b = p.getWorldScoreboard().getTeam("nopush").getMembershipCollection()
                    .stream().anyMatch(name -> name.equalsIgnoreCase(playername));
                if (b) {
                    //System.out.println(playername + " Ist im NoPushTeam :(");
                    return;
                }
            }

            //removeFromOtherTeam(playername);
            if (!PrefixHandler.HVs.containsKey(p.getName()) && !PrefixHandler.BrotUser.containsKey(p.getName())) {
                if (Minecraft.getMinecraft().player.getWorldScoreboard().getTeams().stream().anyMatch(team -> team.getName().equals(teamName))) {
                    Minecraft.getMinecraft().player.getWorldScoreboard().removeTeam(teams.get(p.getName()));
                    //System.out.println(playername + " team entfernt!");
                }
                return;
            }

            if (Minecraft.getMinecraft().player.getWorldScoreboard().getTeams().stream().noneMatch(team -> team.getName().equals(teamName))) {
                ScorePlayerTeam team = Minecraft.getMinecraft().player.getWorldScoreboard().createTeam(teamName);
                teams.put(p.getName(), team);
                //System.out.println(playername + "Team erstellt");
            }

            if (PrefixHandler.HVs.containsKey(p.getName())) {
                if (KirchePlus.main.configuration().hv.hvenable().get()) {
                    if (!TabellenMethoden.isDayOver(PrefixHandler.HVs.get(p.getName()).getUntilDate())) {
                        Minecraft.getMinecraft().player.getWorldScoreboard().getTeam(teamName).setPrefix(KirchePlus.main.configuration().hv.hvprefix().get().replace("&", "§") + "§f ");
                    }
                }
            } else {
                Minecraft.getMinecraft().player.getWorldScoreboard().getTeam(teamName).setPrefix("");
            }

            if (PrefixHandler.BrotUser.containsKey(p.getName())) {
                if (KirchePlus.main.configuration().bread.breadenable().get()) {
                    if (TabellenMethoden.isSameDay(PrefixHandler.BrotUser.get(p.getName()).getDatum())) {
                        Minecraft.getMinecraft().player.getWorldScoreboard().getTeam(teamName).setSuffix(KirchePlus.main.configuration().bread.breadprefix().get().replace("&", "§") + "§f ");
                    }
                }
            } else {
                Minecraft.getMinecraft().player.getWorldScoreboard().getTeam(teamName).setSuffix("");
            }

            if (KirchePlus.main.configuration().bread.breadenable().get() || KirchePlus.main.configuration().hv.hvenable().get()) {
                if (!isMasked(p)) {
                    //System.out.println(playername + "Team gegeben");
                    Minecraft.getMinecraft().player.getWorldScoreboard().addPlayerToTeam(p.getName(), teamName);
                    //System.out.println(playername + "Team gegeben2");
                }else{
                    //System.out.println(playername + " ist maskiert");
                }
            }

        } catch (Exception e2) {

        }
    }

    @Override
    public void checkHide() {
        if (!Laby.labyAPI().minecraft().isIngame()) {
            return;
        }
        if (reloadTime != 0) {
            reloadTime--;
        } else {
            TabellenMethoden.reloadLists();
            refreshAll();
            reloadTime = 60 * 20 * 2 * 5;
        }

        if (Minecraft.getMinecraft().gameSettings.hideGUI) {
            for (Entity e1 : Minecraft.getMinecraft().world.loadedEntityList) {
                if (e1 instanceof EntityPlayer) {
                    if (Minecraft.getMinecraft().player.getWorldScoreboard().getTeams().stream().anyMatch(team -> team.getName().equals(e1.getName()))) {
                        Minecraft.getMinecraft().player.getWorldScoreboard().removeTeam(teams.get(e1.getName()));
                        teams.remove(e1.getName());
                    }
                }
            }
            hide = true;
            return;
        }
        if (hide) {
            refreshAll();
            hide = false;
        }

        if (time != 0) {
            time--;
            return;
        }
        try {
            refreshAll();
        } catch (Exception s) {
            s.printStackTrace();
        }
        time = 20 * 10 *2;
    }

    @Override
    public void removeFromOtherTeam(String playername) {
        if (!Laby.labyAPI().minecraft().isIngame()) {
            return;
        }
        for(EntityPlayer player : Minecraft.getMinecraft().world.playerEntities){
            if(player.getTeam().getName() != null) {
                //System.out.println(player.getName() + " - " + player.getTeam().getName());
            }
        }
    }

    @Override
    public void refreshAll() {
        for(ScorePlayerTeam team : teams.values()){
            try {
                //Minecraft.getMinecraft().player.getWorldScoreboard().removeTeam(team);
            }catch (Exception ignored){

            }
        }
        try{
            for (Entity e1 : Minecraft.getMinecraft().world.playerEntities) {
                if(Minecraft.getMinecraft().player.getWorldScoreboard().getObjectiveInDisplaySlot(1) == null){
                    if(Minecraft.getMinecraft().player.getWorldScoreboard().getTeam("nopush").getMembershipCollection().stream().anyMatch(name -> name.equalsIgnoreCase(e1.getName()))){
                        //System.out.println(e1.getName() + " ist no push");
                    }else addTeam(e1.getName());
                }else{
                    addTeam(e1.getName());
                }
            }
        }catch (Exception ignored){

        }
    }

    @Override
    public boolean isMasked(Object o) {
        EntityPlayer p = (EntityPlayer) o;
        if(Minecraft.getMinecraft().player.getWorldScoreboard().getObjectiveInDisplaySlot(1) == null) {
            if(Minecraft.getMinecraft().player.getWorldScoreboard().getTeam("masked").getMembershipCollection().stream().anyMatch(name -> name.equalsIgnoreCase(p.getName()))){
                return true;
            }
        }
        return false;
    }

}