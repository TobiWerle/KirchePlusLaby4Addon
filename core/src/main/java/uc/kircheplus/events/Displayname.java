package uc.kircheplus.events;

import net.labymod.api.reference.annotation.Referenceable;

@Referenceable
public abstract class Displayname {

    public void addTeam(String playername) {

    }

    public void checkHide() {
        System.out.println("Standart Hide");
    }

    public void refreshAll() {

    }

    public boolean isMasked(Object o) {
        return false;
    }
}