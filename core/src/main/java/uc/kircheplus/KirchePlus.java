package uc.kircheplus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.models.addon.annotation.AddonMain;
import uc.kircheplus.automaticactivity.Handler;
import uc.kircheplus.automaticactivity.KirchePlusIMG.ServerTokenHandler;
import uc.kircheplus.commands.Brot_Command;
import uc.kircheplus.commands.CommandBypass;
import uc.kircheplus.commands.GDEinteilung_Command;
import uc.kircheplus.commands.SaveActivity_Command;
import uc.kircheplus.commands.VertragsInfo_Command;
import uc.kircheplus.commands.aEquip;
import uc.kircheplus.commands.aEvent_Command;
import uc.kircheplus.commands.checkDonation_Command;
import uc.kircheplus.commands.hv_Command;
import uc.kircheplus.commands.kircheplus_command;
import uc.kircheplus.commands.topActivity_Command;
import uc.kircheplus.config.config;
import uc.kircheplus.core.generated.DefaultReferenceStorage;
import uc.kircheplus.customui.ActivityGUI;
import uc.kircheplus.customui.GD_GUI;
import uc.kircheplus.customui.HVADD_GUI;
import uc.kircheplus.events.Displayname;
import uc.kircheplus.events.DrinkNotification;
import uc.kircheplus.events.PrefixHandler;
import uc.kircheplus.events.configChangeEvent;
import uc.kircheplus.events.tabcompletion;
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

    //TODO TEST IT

    //todo done
    // add /kircheplus -> Show all command usages
    // Duration at /hv info
    // add /aequip give <Vertragspartner>
    // remove own at disable owngmail and init own gmail at enable
    // add /hv remove <Name>
    // Remove englisch translation
    // MAC Compatibility

    //TODO PORT TO FORGE
    // sort list at /brot list

    public static KirchePlus main;
    public String VERSION = "3.4";
    public ArrayList<SpenderInfo> spender = new ArrayList<>();
    public HashMap<Activity_User, Integer> totalActivity = new HashMap<>();
    public ArrayList<FactionContract> factionContracts = new ArrayList<>();
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
        loadData();
        sounds.registerSounds();
    }
    @Override
    protected Class<? extends config> configurationClass() {
        return config.class;
    }


    private void registerEvents() {
        this.registerListener(new aEquip());
        this.registerListener(new PrefixHandler());
        this.registerListener(new UpdateCheck());
        this.registerListener(new ServerTokenHandler());
        this.registerListener(new Handler());
        this.registerListener(new DrinkNotification());
        this.registerListener(new CommandBypass());
        this.registerListener(new tabcompletion());
        this.registerListener(new configChangeEvent());
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
        register(new aEvent_Command());
        register(new kircheplus_command());
    }

    private void register(Command cmd) {
        this.registerCommand(cmd);
        commands.add(cmd);
    }
    private void loadData(){
        TabellenMethoden.init();
        UpdateCheck.updateCheck();
        VertragsInfo_Command.loadFactionInfoJSON();
    }
}
