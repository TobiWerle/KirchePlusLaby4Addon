package uc.kircheplus.soundhandler;

import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.client.sound.SoundType;

public class sounds {

    public static SoundType drink;
    public static SoundType dryout;
    public static void registerSounds(){
        ResourceLocation drinkLocation = ResourceLocation.create("kircheplusaddon", "entity.drink.master");
        drink = SoundType.create("entity.drink.master", drinkLocation);

        ResourceLocation dryoutLocation = ResourceLocation.create("kircheplusaddon", "entity.dryout.master");
        dryout = SoundType.create("entity.dryout.master", drinkLocation);
    }

}
