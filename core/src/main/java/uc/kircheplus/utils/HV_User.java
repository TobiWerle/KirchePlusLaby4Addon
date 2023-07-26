package uc.kircheplus.utils;


import uc.kircheplus.events.PrefixHandler;

public class HV_User {

    String name;
    String fromMember;
    String reason;
    String fromDate;
    String utilDate;
    String weeks;


    public HV_User(String s, String s2, String s3, String s4, String s5, String s6) {
        name = s;
        fromMember = s2;
        reason = s3;
        fromDate = s4;
        utilDate = s5;
        weeks = s6;
        PrefixHandler.HVs.put(s, this);
    }


    public String getName() {
        return name;
    }


    public String getFromMember() {
        return fromMember;
    }


    public String getReason() {
        return reason;
    }


    public String getFromDate() {
        return fromDate;
    }


    public String getUntilDate() {
        return utilDate;
    }


    public String getWeeks() {
        return weeks;
    }


}
