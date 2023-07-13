package uc.kircheplus;

import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.models.addon.annotation.AddonMain;
import uc.kircheplus.automaticactivity.ActivityGUI;
import uc.kircheplus.commands.VertragsInfo_Command;
import uc.kircheplus.commands.aEquip;
import uc.kircheplus.events.TabCompletionListener;
import uc.kircheplus.utils.Activity_User;
import uc.kircheplus.utils.FactionContract;
import uc.kircheplus.utils.GUIController;
import uc.kircheplus.utils.SpenderInfo;
import uc.kircheplus.utils.TabellenMethoden;
import uc.kircheplus.utils.UpdateCheck;
import uc.kircheplus.config.config;
import uc.kircheplus.core.generated.DefaultReferenceStorage;
import uc.kircheplus.events.Displayname;
import uc.kircheplus.events.PrefixHandler;
import uc.kircheplus.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@AddonMain
public class KirchePlus extends LabyAddon<config> {

    //TODO FOR LATER::
    //Utils Classes: RegistryHandler | SoundHandler

    public static KirchePlus main;
    public String VERSION = "3.2";
    public ArrayList<SpenderInfo> spender = new ArrayList<>();
    public HashMap<Activity_User, Integer> totalActivity = new HashMap<>();
    public ArrayList<FactionContract> FactionContracs = new ArrayList<>();
    public List<Command> commands = new ArrayList<>();
    /* Versioned Classes */
    public Displayname displaynameClass;
    public GUIController guiController;

    public Utils utils;
    public ActivityGUI activityGUI;
    @Override
    protected void enable() {
        main = this;
        registerSettingCategory();


        DefaultReferenceStorage referenceStorage = this.referenceStorageAccessor();
        displaynameClass = referenceStorage.displayname();
        guiController = referenceStorage.guiController();
        utils = referenceStorage.utils();
        activityGUI = referenceStorage.activityGUI();



        registerEvents();
        registerCommands();
        TabellenMethoden.init();
        UpdateCheck.updateCheck();

    }

    @Override
    protected Class<? extends config> configurationClass() {
        return config.class;
    }


    private void registerEvents() {
        this.registerListener(new PrefixHandler());
        this.registerListener(new UpdateCheck());
        this.registerListener(new aEquip());
        this.registerListener(new TabCompletionListener());
    }
    private void registerCommands() {
        register(new aEquip());
        register(new VertragsInfo_Command());
    }

    private void register(Command cmd){
        this.registerCommand(cmd);
        commands.add(cmd);
    }

}
