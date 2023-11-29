package uc.kircheplus.utils;


import java.time.Duration;
import java.time.LocalDateTime;
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
    public Duration getDuration(){
        if(getUntilDate().equalsIgnoreCase("Nie")) return null;
        String[] s = getUntilDate().split("\\.");
        int year = Integer.parseInt(s[2]);
        int month = Integer.parseInt(s[1]);
        int day = Integer.parseInt(s[0]);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime until = LocalDateTime.of(year, month, day, 0, 0);

        Duration diff = Duration.between(now, until);
        return diff;
    }


}
