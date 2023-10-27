package uc.kircheplus.events;

import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.labymod.config.SettingUpdateEvent;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.utils.TabellenMethoden;

public class configChangeEvent {

    @Subscribe
    public void on(SettingUpdateEvent e){
        if(e.phase() == Phase.POST){
            if(e.setting().getId().equals("owngmailenabled")){
                if(!KirchePlus.main.configuration().owngmailenabled().get()){
                    TabellenMethoden.deleteOwn();
                    TabellenMethoden.init();
                }else{
                    TabellenMethoden.init();
                }
            }
        }
    }
}
