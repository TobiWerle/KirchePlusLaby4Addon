package uc.kircheplus.v1_12_2.utils;

import net.labymod.api.Laby;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.utils.TabellenMethoden;
import uc.kircheplus.events.Displayname;
import uc.kircheplus.events.PrefixHandler;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;

@Singleton
@Implements(Displayname.class)
public class VersionedDisplayname extends Displayname {

  @Inject
  public VersionedDisplayname(){

  }
  public static HashMap<String, ScorePlayerTeam> teams = new HashMap<>();

  static int time = 20*30;
  static int reloadTime = 60*20*2*5;
  static boolean hide = false;
  @Override
  public void addTeam(String playername) {
    EntityPlayer p = Minecraft.getMinecraft().world.getPlayerEntityByName(playername);
      if(Minecraft.getMinecraft().gameSettings.hideGUI){
          return;
      }
      try {
          String teamName = p.getName();
          boolean b = p.getWorldScoreboard().getTeam("nopush").getMembershipCollection().stream().anyMatch(name -> name.equalsIgnoreCase(p.getName()));
          if(b)return;

          if (!PrefixHandler.HVs.containsKey(p.getName()) && !PrefixHandler.BrotUser.containsKey(p.getName())) {
              if (Minecraft.getMinecraft().player.getWorldScoreboard().getTeams().stream().anyMatch(team -> team.getName().equals(teamName))) {
                  Minecraft.getMinecraft().player.getWorldScoreboard().removeTeam(teams.get(p.getName()));
              }
              return;
          }

          if (Minecraft.getMinecraft().player.getWorldScoreboard().getTeams().stream().noneMatch(team -> team.getName().equals(teamName))) {
              ScorePlayerTeam team = Minecraft.getMinecraft().player.getWorldScoreboard().createTeam(teamName);
              teams.put(p.getName(), team);
          }

          if (PrefixHandler.HVs.containsKey(p.getName())) {
              if (KirchePlus.main.configuration().hv.hvenable().get()) {
                  if (!TabellenMethoden.isDayOver(PrefixHandler.HVs.get(p.getName()).getUtilDate())) {
                      Minecraft.getMinecraft().player.getWorldScoreboard().getTeam(teamName).setPrefix(KirchePlus.main.configuration().hv.hvprefix().get().replace("&", "§") + "§f ");
                  }
              }
          }else{
              Minecraft.getMinecraft().player.getWorldScoreboard().getTeam(teamName).setPrefix("");
          }

          if (PrefixHandler.BrotUser.containsKey(p.getName())) {
              if (KirchePlus.main.configuration().bread.breadenable().get()) {
                  if (TabellenMethoden.isSameDay(PrefixHandler.BrotUser.get(p.getName()).getDatum())) {
                      Minecraft.getMinecraft().player.getWorldScoreboard().getTeam(teamName).setSuffix(KirchePlus.main.configuration().bread.breadprefix().get().replace("&", "§") + "§f ");
                  }
              }
          }else{
              Minecraft.getMinecraft().player.getWorldScoreboard().getTeam(teamName).setSuffix("");
          }

          if (KirchePlus.main.configuration().bread.breadenable().get() || KirchePlus.main.configuration().hv.hvenable().get()) {
              if(!isMasked(p)) {
                  Minecraft.getMinecraft().player.getWorldScoreboard().addPlayerToTeam(p.getName(), teamName);
              }
          }

      } catch (Exception e2) {
          e2.printStackTrace();
      }
  }
  @Override
  public void checkHide(){
      if(!Laby.labyAPI().minecraft().isIngame()){
          return;
      }
    if(reloadTime != 0){
      reloadTime--;
    }else{
      TabellenMethoden.reloadLists();
      refreshAll();
      reloadTime = 60*20*2*5;
    }

    if(Minecraft.getMinecraft().gameSettings.hideGUI){
      for(Entity e1 : Minecraft.getMinecraft().world.loadedEntityList) {
        if(e1 instanceof EntityPlayer) {
          if (Minecraft.getMinecraft().player.getWorldScoreboard().getTeams().stream().anyMatch(team -> team.getName().equals(e1.getName()))) {
            Minecraft.getMinecraft().player.getWorldScoreboard().removeTeam(teams.get(e1.getName()));
          }
        }
      }
      hide = true;
      return;
    }
    if(hide){
      refreshAll();
      System.out.println("Refresh all after dingsens... hier F1 :D");
      hide = false;
    }

    if(time != 0) {
      time--;
      return;
    }
    try {
      refreshAll();
    } catch (Exception s) {
      s.printStackTrace();
    }
    System.out.println("jetzt");
    time = 20*30*2;
  }

  @Override
  public void refreshAll(){
    for(Entity e1 : Minecraft.getMinecraft().world.loadedEntityList) {
      if(e1 instanceof EntityPlayer) {
        if (((EntityPlayer) e1).getWorldScoreboard().getTeams().stream().anyMatch(team -> team.getName().equals("nopush"))) {
          return;
        }
        addTeam(e1.getName());
      }
    }
  }
  @Override
  public boolean isMasked(Object o){
    EntityPlayer p = (EntityPlayer) o;
    if(ScorePlayerTeam.formatPlayerName(p.getTeam(), p.getName()).contains("§k")){
      return true;
    }
    return false;
  }

}