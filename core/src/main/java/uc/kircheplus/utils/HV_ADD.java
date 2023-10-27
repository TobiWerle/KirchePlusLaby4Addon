package uc.kircheplus.utils;

import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.events.PrefixHandler;

public class HV_ADD {


    public static String temp_fromDate = null;
    public static String temp_fromMember = null;
    public static String temp_Who = null;

    public static String temp_untilDate = null;
    public static String temp_reason = null;
    public static String temp_weeks = null;


    static String HV_prefix = "§7[§cHV§7]";


    public static void writeHouseBan() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ValueRange valueRange = getSpace(getRanges());
                    if (valueRange == null) {
                        KirchePlus.main.displayMessage(HV_prefix + " "+ Utils.translateAsString("kircheplusaddon.commands.hv.add.nospace"));
                        return;
                    }
                    List<List<Object>> values = Arrays.asList(
                        Arrays.asList(temp_Who, temp_fromMember, temp_reason, temp_weeks,
                            temp_fromDate, temp_untilDate));

                    String range = valueRange.getRange();
                    ValueRange body = new ValueRange().setValues(values);

                    UpdateValuesResponse result = TabellenMethoden.sheetsService.spreadsheets()
                        .values().update(TabellenMethoden.SPREADSHEET_ID, range, body)
                        .setValueInputOption("USER_ENTERED").execute();
                    KirchePlus.main.displayMessage(HV_prefix + " "+ Utils.translateAsString("kircheplusaddon.commands.hv.add.success"));
                    HV_User user = new HV_User(temp_Who, temp_fromMember, temp_reason,
                        temp_fromDate, temp_untilDate, temp_weeks);
                    temp_fromDate = null;
                    temp_untilDate = null;
                    temp_fromMember = null;
                    temp_Who = null;
                    temp_reason = null;
                    temp_weeks = null;
                    KirchePlus.main.displayname.refreshAll();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (e.getMessage().contains("400 Bad Request")) {
                        KirchePlus.main.displayMessage(HV_prefix + " "+ Utils.translateAsString("kircheplusaddon.commands.hv.add.nopermissions"));
                    }
                }
            }
        });
        thread.start();
    }
    public static void deleteHouseBan(String playername) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String range = "Hausverbote!B19:G92";
                    int count = 0;
                    ValueRange response = TabellenMethoden.sheetsService.spreadsheets().values().get(TabellenMethoden.SPREADSHEET_ID, range)
                        .execute();
                    List<List<Object>> values = response.getValues();

                    for (List row : values) {
                        if (row.get(0).equals(playername)) {
                            List<Object> newvalues = Arrays.asList("", "", "", "", "", "");
                            values.set(count, newvalues);
                            ValueRange body = new ValueRange().setValues(values);
                            UpdateValuesResponse result =
                                TabellenMethoden.sheetsService.spreadsheets().values()
                                    .update(TabellenMethoden.SPREADSHEET_ID, range, body)
                                    .setValueInputOption("RAW").execute();
                            PrefixHandler.HVs.remove(playername);
                            KirchePlus.main.displayname.refreshAll();
                            KirchePlus.main.displayMessage(HV_prefix + " "+ Utils.translateAsString("kircheplusaddon.commands.hv.remove.success"));
                            return;
                        }
                        count++;
                        if(values.size() == count){
                            KirchePlus.main.displayMessage(HV_prefix + " "+ Utils.translateAsString("kircheplusaddon.commands.hv.remove.playernotfound"));
                        }
                    }
                } catch (Exception e) {
                    if (e.getMessage().contains("400 Bad Request")) {
                        KirchePlus.main.displayMessage(HV_prefix + " "+ Utils.translateAsString("kircheplusaddon.commands.hv.remove.nopermissions"));
                    }
                }

            }
        });
        thread.start();
    }

    private static List<String> getRanges() {
        return Arrays.asList("B19:G19", "B20:G20", "B21:G21", "B22:G22", "B23:G23", "B24:G24",
            "B25:G25", "B26:G26", "B27:G27", "B28:G28", "B29:G29", "B30:G30", "B31:G31",
            "B32:G32", "B33:G33", "B34:G34", "B35:G35", "B36:G36", "B37:G37", "B38:G38", "B39:G39",
            "B40:G40", "B41:G41", "B42:G42", "B43:G43", "B44:G44", "B45:G45",
            "B46:G46", "B47:G47", "B48:G48", "B49:G49", "B50:G50", "B51:G51", "B52:G52", "B53:G53",
            "B54:G54", "B55:G55", "B56:G56", "B57:G57", "B58:G58", "B59:G59",
            "B60:G60", "B61:G61", "B62:G62", "B63:G63", "B64:G64", "B65:G65", "B66:G66", "B67:G67",
            "B68:G68", "B69:G69", "B70:G70", "B71:G71", "B72:G72", "B73:G73",
            "B74:G74", "B75:G75", "B76:G76", "B77:G77", "B78:G78", "B79:G79", "B80:G80", "B81:G81",
            "B82:G82", "B83:G83", "B84:G84", "B85:G85", "B86:G86", "B87:G87",
            "B88:G88", "B89:G89", "B90:G90", "B91:G91", "B92:G92");
    }

    private static ValueRange getSpace(List<String> ranges) throws IOException {
        List<String> range = new ArrayList<String>();
        for (String s : ranges) {
            range.add("Hausverbote!" + s);
        }

        BatchGetValuesResponse result = TabellenMethoden.sheetsService.spreadsheets().values()
            .batchGet(TabellenMethoden.SPREADSHEET_ID)
            .setRanges(range).execute();
        for (ValueRange valueRanges : result.getValueRanges()) {
            if (valueRanges.getValues() == null || valueRanges.getValues().isEmpty()) {
                return valueRanges;
            }
        }
        return null;
    }


}
