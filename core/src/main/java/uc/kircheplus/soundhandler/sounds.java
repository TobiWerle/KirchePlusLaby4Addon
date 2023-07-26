package uc.kircheplus.soundhandler;

import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.client.sound.SoundType;

public class sounds {

    public static SoundType drink;
    public static SoundType dryout;
    public static void registerSounds(){
        System.out.println("Register Sounds");
        drink = SoundType.create("entity.drink.master", ResourceLocation.create("kircheplusaddon", "assets/kircheplusaddon/themes/vanilla/sounds/drink.ogg"));
        //dryout = SoundType.create("entity.dryout.master", ResourceLocation.create("kircheplusaddon", "/sounds/entity.dryout.master"));
    }

}
