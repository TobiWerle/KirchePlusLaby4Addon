package uc.kircheplus.v1_12_2.customui;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.automaticactivity.SheetHandler;
import uc.kircheplus.utils.HV_ADD;
import uc.kircheplus.utils.Utils;

public class customGUI_HV extends GuiScreen {

    private GuiTextField weeks;
    private GuiTextField reason;

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }


    @Override
    public void initGui() {
        GuiLabel label = new GuiLabel(fontRenderer, 1, width / 2 - 50 + 30, height / 2 - 90, 50, 20,
            0xd943ff64);
        label.addLine(Utils.translateAsString("kircheplusaddon.guihouseban.reason"));
        labelList.add(label);

        reason = new GuiTextField(2, fontRenderer, width / 2 - 50, height / 2 - 66, 108, 20);
        reason.setText("");
        reason.setMaxStringLength(500);
        reason.setFocused(true);
        reason.setEnabled(true);
        reason.setCanLoseFocus(true);
        reason.setVisible(true);

        GuiLabel label2 = new GuiLabel(fontRenderer, 3, width / 2 - 50, height / 2 - 36, 90, 20,
            0xd943ff64);
        label2.addLine(Utils.translateAsString("kircheplusaddon.guihouseban.duration"));
        labelList.add(label2);

        weeks = new GuiTextField(4, fontRenderer, width / 2 - 50, height / 2 - 16, 108, 20);
        weeks.setText("");
        weeks.setFocused(false);
        weeks.setEnabled(true);
        weeks.setCanLoseFocus(true);
        weeks.setVisible(true);
        weeks.setMaxStringLength(1);

        buttonList.add(new GuiButton(0, width / 2 - 50, height / 2 + 10, 108, 20, Utils.translateAsString("kircheplusaddon.guihouseban.confirm")));

        super.initGui();
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(width / 2 - 58, height / 2 - 103, width / 2 + 58, height / 2 + 59, 0xa6c0c0c0);
        drawRect(width / 2 - 55, height / 2 - 100, width / 2 + 55, height / 2 + 56, 0x4ddcf527);
        fontRenderer.drawString(Utils.translateAsString("kircheplusaddon.guihouseban.title"), width / 2 - 25, height / 2 - 112, 0xd943ff64);
        reason.drawTextBox();
        weeks.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        weeks.mouseClicked(mouseX, mouseY, mouseButton);
        reason.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (reason.getText().length() <= 3) {
                            KirchePlus.main.utils.displayMessage(Utils.translateAsString("kircheplusaddon.guihouseban.error.reasonshort"));
                            return;
                        }
                        try {
                            Integer.parseInt(weeks.getText());
                        } catch (NumberFormatException e) {
                            KirchePlus.main.utils.displayMessage(Utils.translateAsString("kircheplusaddon.guihouseban.error.weekstoshort"));
                            return;
                        }
                        if (Integer.parseInt(weeks.getText()) <= 0) {
                            KirchePlus.main.utils.displayMessage(Utils.translateAsString("kircheplusaddon.guihouseban.error.weekstoshort"));
                            return;
                        }
                        int rang = 0;
                        try {
                            rang = SheetHandler.getRang();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (Integer.parseInt(weeks.getText()) > 3 && rang <= 3) {
                            KirchePlus.main.utils.displayMessage(Utils.translateAsString("kircheplusaddon.guihouseban.error.weekstolongrank3"));
                            return;
                        }
                        if (Integer.parseInt(weeks.getText()) > 4 && rang <= 4) {
                            KirchePlus.main.utils.displayMessage(Utils.translateAsString("kircheplusaddon.guihouseban.error.weekstolongrank4"));
                            return;
                        }

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date());
                        calendar.add(Calendar.DATE, 7 * Integer.parseInt(weeks.getText()));
                        DateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");

                        HV_ADD.temp_untilDate = dateFormatter.format(calendar.getTime());
                        HV_ADD.temp_weeks = weeks.getText();
                        HV_ADD.temp_reason = reason.getText();
                        HV_ADD.writeHouseBan();
                    }
                });
                Minecraft.getMinecraft().player.closeScreen();
                thread.start();
                break;
        }
    }

    @Override
    public void updateScreen() {
        weeks.updateCursorCounter();
        reason.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(typedChar + "");
        while (matcher.find()) {
            weeks.textboxKeyTyped(typedChar, keyCode);
        }
        if (keyCode == 14) {
            weeks.textboxKeyTyped(typedChar, keyCode);
        }
        reason.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

}
