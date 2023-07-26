package uc.kircheplus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.models.addon.annotation.AddonMain;
import uc.kircheplus.automaticactivity.Handler;
import uc.kircheplus.automaticactivity.KirchePlusIMG.ServerTokenHandler;
import uc.kircheplus.commands.Brot_Command;
import uc.kircheplus.commands.GDEinteilung_Command;
import uc.kircheplus.commands.SaveActivity_Command;
import uc.kircheplus.commands.VertragsInfo_Command;
import uc.kircheplus.commands.aEquip;
import uc.kircheplus.commands.checkDonation_Command;
import uc.kircheplus.commands.hv_Command;
import uc.kircheplus.commands.topActivity_Command;
import uc.kircheplus.config.config;
import uc.kircheplus.core.generated.DefaultReferenceStorage;
import uc.kircheplus.customui.ActivityGUI;
import uc.kircheplus.customui.GD_GUI;
import uc.kircheplus.customui.HVADD_GUI;
import uc.kircheplus.events.Displayname;
import uc.kircheplus.events.PrefixHandler;
import uc.kircheplus.soundhandler.sounds;
import uc.kircheplus.utils.Activity_User;
import uc.kircheplus.utils.FactionContract;
import uc.kircheplus.utils.GUIController;
import uc.kircheplus.utils.SpenderInfo;
import uc.kircheplus.utils.TabellenMethoden;
import uc.kircheplus.utils.UpdateCheck;
import uc.kircheplus.utils.Utils;

@AddonMain
public class KirchePlus extends LabyAddon<config> {

    //TODO FOR LATER::
    // aEquip: add Suppe!!!!
    // Feature: See own Frakmembers
    //Utils Classes: RegistryHandler | SoundHandler

    public static KirchePlus main;
    public String VERSION = "3.2";
    public ArrayList<SpenderInfo> spender = new ArrayList<>();
    public HashMap<Activity_User, Integer> totalActivity = new HashMap<>();
    public ArrayList<FactionContract> FactionContracs = new ArrayList<>();
    public List<Command> commands = new ArrayList<>();

    /* Versioned Classes */
    public Displayname displayname;
    public GUIController guiController;

    public Utils utils;
    public ActivityGUI activityGUI;
    public GD_GUI gd_gui;
    public HVADD_GUI hv_gui;

    @Override
    protected void enable() {
        main = this;
        registerSettingCategory();
        DefaultReferenceStorage referenceStorage = this.referenceStorageAccessor();
        displayname = referenceStorage.displayname();
        guiController = referenceStorage.guiController();
        utils = referenceStorage.utils();
        activityGUI = referenceStorage.activityGUI();
        gd_gui = referenceStorage.gD_GUI();
        hv_gui = referenceStorage.hvadD_GUI();

        registerEvents();
        registerCommands();
        sounds.registerSounds();
        TabellenMethoden.init();
        UpdateCheck.updateCheck();

        Laby.references().soundService().play(sounds.drink);
    }

    @Override
    protected Class<? extends config> configurationClass() {
        return config.class;
    }


    private void registerEvents() {
        this.registerListener(new PrefixHandler());
        this.registerListener(new UpdateCheck());
        this.registerListener(new aEquip());
        this.registerListener(new ServerTokenHandler());
        this.registerListener(new Handler());
    }

    private void registerCommands() {
        register(new aEquip());
        register(new VertragsInfo_Command());
        register(new SaveActivity_Command());
        register(new GDEinteilung_Command());
        register(new checkDonation_Command());
        register(new hv_Command());
        register(new Brot_Command());
        register(new topActivity_Command());
    }

    private void register(Command cmd) {
        this.registerCommand(cmd);
        commands.add(cmd);
    }

}
