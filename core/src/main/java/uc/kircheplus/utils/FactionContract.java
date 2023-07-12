package uc.kircheplus.utils;

import uc.kircheplus.KirchePlus;

public class FactionContract {

    String faction;
    boolean contract;
    String[] conditions;

    public boolean isContract() {
        return contract;
    }

    public String[] getConditions() {
        return conditions;
    }

    public String getFaction() {
        return faction;
    }

    public FactionContract(String faction, boolean contract, String[] conditions) {
        this.faction = faction;
        this.contract = contract;
        this.conditions = conditions;

        KirchePlus.main.FactionContracs.add(this);
    }
}
