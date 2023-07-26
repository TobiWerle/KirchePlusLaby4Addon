package uc.kircheplus.v1_12_2.customui;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.automaticactivity.Handler;
import uc.kircheplus.automaticactivity.SheetHandler;
import uc.kircheplus.utils.Utils;

public class customGUI_Activity extends GuiScreen {

    public boolean eventPage = false;
    public boolean moneyPage = false;
    public boolean blessPage = false;
    public boolean marryPage = false;

    public boolean topicPage = false;
    public boolean bibelPage = false;
    public boolean donationPage = false;
    public boolean taufePage = false;

    private GuiTextField textField;
    private GuiTextField textFieldAmount;

    private boolean textFieldVisible = false;
    private boolean textFieldAmountVisible = false;

    public customGUI_Activity() {
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        textField = new GuiTextField(100, fontRenderer, width / 2 - 50, height / 2 - 66, 108, 20);
        textField.setVisible(false);
        textField.setText("");
        textField.setFocused(true);
        textField.setEnabled(true);
        textField.setCanLoseFocus(true);

        textFieldAmount = new GuiTextField(101, fontRenderer, width / 2 - 50, height / 2 - 33, 108,
            20);
        textFieldAmount.setVisible(false);
        textFieldAmount.setText("");
        textFieldAmount.setFocused(false);
        textFieldAmount.setEnabled(true);
        textFieldAmount.setCanLoseFocus(true);
        textFieldAmount.setMaxStringLength(2);

        addButtons();
        super.initGui();
    }

    @Override
    public void onGuiClosed() {
        Handler.screenshotLink = "";
        textFieldAmount.setText("");
        textField.setText("");

        super.onGuiClosed();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(width / 2 - 58, height / 2 - 103, width / 2 + 58, height / 2 + 59, 0xa6c0c0c0);
        drawRect(width / 2 - 55, height / 2 - 100, width / 2 + 55, height / 2 + 56, 0x4ddcf527);
        fontRenderer.drawString(Utils.translateAsString("kircheplusaddon.activity.gui.title"), width / 2 - 25, height / 2 - 112, 0xd943ff64);

        if (textFieldVisible) {
            textField.drawTextBox();
        }
        if (textFieldAmountVisible) {
            textFieldAmount.drawTextBox();
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        textField.mouseClicked(mouseX, mouseY, mouseButton);
        textFieldAmount.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (SheetHandler.MemberSheet == null) {
            KirchePlus.main.utils.displayMessage(Utils.translateAsString("kircheplusaddon.activity.gui.sheetnotloaded"));
            return;
        }
        switch (button.id) {
            case 0:
                //Button für SHG
                try {
                    SheetHandler.saveActivity(SheetHandler.activityTypes.SHG);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Minecraft.getMinecraft().player.closeScreen();
                break;
            case 1:
                //Button für Tafel
                try {
                    SheetHandler.saveActivity(SheetHandler.activityTypes.TAFEL);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Minecraft.getMinecraft().player.closeScreen();
                break;
            case 2:
                //Button für Spendenevent
                try {
                    SheetHandler.saveActivity(SheetHandler.activityTypes.SPENDEEVENT);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Minecraft.getMinecraft().player.closeScreen();
                break;
            case 3:
                //Button für Beichtevent
                try {
                    SheetHandler.saveActivity(SheetHandler.activityTypes.BEICHTEVENT);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Minecraft.getMinecraft().player.closeScreen();
                break;
            case 4:
                //Button für JGA
                buttonList.clear();
                Handler.activityType = SheetHandler.activityTypes.JGA;
                eventPage = false;
                topicPage = true;
                addButtons();
                break;
            case 5:
                //Button für Kaffe und Kuchen
                buttonList.clear();
                Handler.activityType = SheetHandler.activityTypes.KAFFEKUCHEN;
                eventPage = false;
                topicPage = true;
                addButtons();
                break;
            case 6:
                //Button für Gottesdienst
                buttonList.clear();
                Handler.activityType = SheetHandler.activityTypes.GOTTESDIENST;
                eventPage = false;
                topicPage = true;
                addButtons();
                break;
            case 7:
                //Thema/Ort bestätigen Button
                if (textField.getText().length() <= 2) {
                    KirchePlus.main.utils.displayMessage(Utils.translateAsString("kircheplusaddon.activity.gui.error.shortthema"));
                    return;
                }
                Handler.topic = textField.getText();
                try {
                    SheetHandler.saveActivity(Handler.activityType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Minecraft.getMinecraft().player.closeScreen();
                break;
            case 8:
                //Thema/Ort Page zurück Button
                buttonList.clear();
                labelList.clear();
                textFieldVisible = false;
                //textField.setVisible(false);
                eventPage = true;
                topicPage = false;
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        addButtons();
                    }
                };
                thread.start();
                break;
            case 9:
                //GUI für Spenden
                Handler.activityType = SheetHandler.activityTypes.SPENDE;
                buttonList.clear();
                labelList.clear();
                moneyPage = false;
                donationPage = true;
                bibelPage = false;
                addButtons();
                break;
            case 10:
                //GUI für Bibeln
                Handler.activityType = SheetHandler.activityTypes.BIBEL;
                buttonList.clear();
                labelList.clear();
                moneyPage = false;
                donationPage = false;
                bibelPage = true;
                addButtons();
                break;
            case 11:
                //Spende bestätigen Button
                if (textField.getText().length() <= 2) {
                    KirchePlus.main.utils.displayMessage(Utils.translateAsString("kircheplusaddon.activity.gui.error.shortname"));
                    return;
                }
                if (Handler.amount == 0) {
                    KirchePlus.main.utils.displayMessage(Utils.translateAsString("kircheplusaddon.activity.gui.error.money"));
                    return;
                }
                Handler.topic = textField.getText();
                Handler.isDonation = true;
                try {
                    SheetHandler.saveActivity(Handler.activityType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Minecraft.getMinecraft().player.closeScreen();
                break;
            case 12:
                //Zurück Button von Spende GUI
                buttonList.clear();
                labelList.clear();
                textFieldVisible = true;
                textField.setVisible(false);
                donationPage = false;
                moneyPage = true;
                Thread thread2 = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        addButtons();
                    }
                };
                thread2.start();
                break;
            case 13:
                //Bibel bestätigen Button
                if (textField.getText().length() <= 2) {
                    KirchePlus.main.utils.displayMessage(Utils.translateAsString("kircheplusaddon.activity.gui.error.shortname"));
                    return;
                }
                if (textFieldAmount.getText().length() == 0
                    && Integer.parseInt(textFieldAmount.getText()) == 0) {
                    KirchePlus.main.utils.displayMessage(Utils.translateAsString("kircheplusaddon.activity.gui.error.zeroamount"));
                    return;
                }
                Handler.topic = textField.getText();
                Handler.amount = Integer.parseInt(textFieldAmount.getText());
                try {
                    SheetHandler.saveActivity(Handler.activityType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Minecraft.getMinecraft().player.closeScreen();
                break;
            case 14:
                //Zurück Button von Bibel GUI
                buttonList.clear();
                labelList.clear();
                textFieldVisible = false;
                textFieldAmountVisible = false;
                bibelPage = false;
                moneyPage = true;
                Thread thread3 = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        addButtons();
                    }
                };
                thread3.start();
                break;
            case 15:
                //Segen bestätigen
                Handler.activityType = SheetHandler.activityTypes.SEGEN;
                Handler.topic = textField.getText();
                try {
                    SheetHandler.saveActivity(Handler.activityType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Minecraft.getMinecraft().player.closeScreen();
                break;
            case 16:
                //GUI für Taufe
                buttonList.clear();
                labelList.clear();
                textFieldVisible = false;
                blessPage = false;
                taufePage = true;
                Thread thread5 = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        addButtons();
                    }
                };
                thread5.start();
                break;
            case 17:
                //Taufe bestätigen
                if (textField.getText().length() <= 2) {
                    KirchePlus.main.utils.displayMessage(Utils.translateAsString("kircheplusaddon.activity.gui.error.shortname"));
                    return;
                }
                Handler.activityType = SheetHandler.activityTypes.TAUFE;
                Handler.topic = textField.getText();
                try {
                    SheetHandler.saveActivity(Handler.activityType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Minecraft.getMinecraft().player.closeScreen();
                break;
            case 18:
                //Zurück Button von Taufe GUI
                buttonList.clear();
                labelList.clear();
                textFieldVisible = false;
                textFieldAmountVisible = false;
                taufePage = false;
                blessPage = true;
                addButtons();
                break;
            case 19:
                //Hochzeit bestätigen
                Handler.activityType = SheetHandler.activityTypes.HOCHZEIT;
                try {
                    SheetHandler.saveActivity(Handler.activityType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Minecraft.getMinecraft().player.closeScreen();
                break;
            case 20:
                //Befehl /marry bestätigen
                Handler.activityType = SheetHandler.activityTypes.CMDMARRY;
                try {
                    SheetHandler.saveActivity(Handler.activityType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Minecraft.getMinecraft().player.closeScreen();
                break;
        }
    }

    @Override
    public void updateScreen() {
        textField.updateCursorCounter();
        textFieldAmount.updateCursorCounter();

        super.updateScreen();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (textFieldVisible) {
            textField.textboxKeyTyped(typedChar, keyCode);
        }
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(typedChar + "");
        while (matcher.find()) {
            if (textFieldAmountVisible) {
                textFieldAmount.textboxKeyTyped(typedChar, keyCode);
            }

        }
        if (keyCode == 14) {
            if (textFieldAmountVisible) {
                textFieldAmount.textboxKeyTyped(typedChar, keyCode);
            }
        }
        super.keyTyped(typedChar, keyCode);
    }

    private void addButtons() {
        if (eventPage) {
            buttonList.add(new GuiButton(0, width / 2 - 50, height / 2 - 98, 100, 20, "SHG"));
            buttonList.add(new GuiButton(1, width / 2 - 50, height / 2 - 76, 100, 20, "Tafel"));
            buttonList.add(new GuiButton(2, width / 2 - 50, height / 2 - 54, 100, 20, "Spendenevent"));
            buttonList.add(new GuiButton(3, width / 2 - 50, height / 2 - 32, 100, 20, "Beichtevent"));

            buttonList.add(new GuiButton(4, width / 2 - 50, height / 2 - 10, 100, 20, "JGA"));
            buttonList.add(new GuiButton(5, width / 2 - 50, height / 2 + 12, 100, 20, "KK"));
            buttonList.add(new GuiButton(6, width / 2 - 50, height / 2 + 34, 100, 20, "Gottesdienst"));


        } else if (topicPage) {
            GuiLabel label = new GuiLabel(fontRenderer, 999, width / 2 - 50, height / 2 - 90, 50,
                20, 0xd943ff64);
            label.addLine(Utils.translateAsString("kircheplusaddon.activity.gui.addbuttons.entertopic"));
            labelList.add(label);
            textFieldVisible = true;

            buttonList.add(new GuiButton(7, width / 2 - 50, height / 2 - 43, 100, 20, Utils.translateAsString("kircheplusaddon.activity.gui.addbuttons.confirmtopic")));
            buttonList.add(new GuiButton(8, width / 2 - 50, height / 2 + 34, 100, 20, Utils.translateAsString("kircheplusaddon.activity.gui.addbuttons.back")));
        }
        if (moneyPage) {
            buttonList.add(new GuiButton(9, width / 2 - 50, height / 2 - 43, 100, 20, Utils.translateAsString("kircheplusaddon.activity.gui.addbuttons.donation")));
            buttonList.add(new GuiButton(10, width / 2 - 50, height / 2 - 20, 100, 20, Utils.translateAsString("kircheplusaddon.activity.gui.addbuttons.bible")));
        }

        if (donationPage) {
            GuiLabel label = new GuiLabel(fontRenderer, 888, width / 2 - 50, height / 2 - 90, 50,
                20, 0xd943ff64);
            label.addLine(Utils.translateAsString("kircheplusaddon.activity.gui.addbuttons.playername"));
            labelList.add(label);
            textFieldVisible = true;

            buttonList.add(
                new GuiButton(11, width / 2 - 50, height / 2 - 43, 100, 20, Utils.translateAsString("kircheplusaddon.activity.gui.addbuttons.confirm")));
            buttonList.add(new GuiButton(12, width / 2 - 50, height / 2 + 34, 100, 20, Utils.translateAsString("kircheplusaddon.activity.gui.addbuttons.back")));
        }
        if (bibelPage) {
            GuiLabel label = new GuiLabel(fontRenderer, 888, width / 2 - 50, height / 2 - 85, 50,
                20, 0xd943ff64);
            label.addLine(Utils.translateAsString("kircheplusaddon.activity.gui.addbuttons.playername"));
            labelList.add(label);

            textFieldVisible = true;
            GuiLabel label2 = new GuiLabel(fontRenderer, 889, width / 2 - 50, height / 2 - 48, 50,
                20, 0xd943ff64);
            label2.addLine(Utils.translateAsString("kircheplusaddon.activity.gui.addbuttons.amount"));
            labelList.add(label2);
            textFieldAmountVisible = true;

            buttonList.add(
                new GuiButton(13, width / 2 - 50, height / 2 + 11, 100, 20, Utils.translateAsString("kircheplusaddon.activity.gui.addbuttons.confirm")));
            buttonList.add(new GuiButton(14, width / 2 - 50, height / 2 + 34, 100, 20, Utils.translateAsString("kircheplusaddon.activity.gui.addbuttons.back")));
        }

        if (blessPage) {
            buttonList.add(new GuiButton(15, width / 2 - 50, height / 2 - 43, 100, 20, Utils.translateAsString("kircheplusaddon.activity.gui.addbuttons.blessing")));
            buttonList.add(new GuiButton(16, width / 2 - 50, height / 2 - 20, 100, 20, Utils.translateAsString("kircheplusaddon.activity.gui.addbuttons.baptism")));
        }

        if (taufePage) {
            GuiLabel label = new GuiLabel(fontRenderer, 890, width / 2 - 50, height / 2 - 85, 50,
                20, 0xd943ff64);
            label.addLine(Utils.translateAsString("kircheplusaddon.activity.gui.addbuttons.baptized"));
            labelList.add(label);
            textFieldVisible = true;

            buttonList.add(new GuiButton(17, width / 2 - 50, height / 2 + 11, 100, 20, Utils.translateAsString("kircheplusaddon.activity.gui.addbuttons.confirm")));
            buttonList.add(new GuiButton(18, width / 2 - 50, height / 2 + 34, 100, 20, Utils.translateAsString("kircheplusaddon.activity.gui.addbuttons.back")));
        }
        if (marryPage) {
            buttonList.add(new GuiButton(19, width / 2 - 50, height / 2 - 43, 100, 20, Utils.translateAsString("kircheplusaddon.activity.gui.addbuttons.marriage")));
            buttonList.add(new GuiButton(20, width / 2 - 50, height / 2 - 20, 100, 20, Utils.translateAsString("kircheplusaddon.activity.gui.addbuttons.commandmarry")));
        }
    }
}
